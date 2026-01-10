/*
 * MIT License
 *
 * Copyright (c) 2026 Webhead1104
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.webhead1104.menuDesigner.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.stage.WindowEvent;
import lombok.extern.slf4j.Slf4j;
import me.webhead1104.menuDesigner.AssetCache;
import me.webhead1104.menuDesigner.MiniMessageFx;
import me.webhead1104.menuDesigner.TextureService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class MenuDesignerController {
    private final ExecutorService ioPool = Executors.newFixedThreadPool(4);
    @FXML
    private BorderPane root;
    @FXML
    private AnchorPane inventoryPane;
    @FXML
    private ListView<String> materialList;
    @FXML
    private TextField materialFilter;
    @FXML
    private TextField nameField;
    @FXML
    private TextFlow namePreview;
    @FXML
    private TextArea loreField;
    @FXML
    private VBox lorePreview;
    @FXML
    private ToggleButton darkToggle;
    private InventoryView inventoryView;
    private TextureService textures;
    private FilteredList<String> filteredMaterials;

    @FXML
    public void initialize() {
        Path cacheBase = locateCacheDir();
        AssetCache cache = new AssetCache(cacheBase);
        textures = new TextureService(cache);

        inventoryView = new InventoryView(textures);
        inventoryView.setScaleFactor(2.0);

        StackPane centerWrap = new StackPane(inventoryView);
        centerWrap.setAlignment(Pos.CENTER);
        AnchorPane.setTopAnchor(centerWrap, 0.0);
        AnchorPane.setRightAnchor(centerWrap, 0.0);
        AnchorPane.setBottomAnchor(centerWrap, 0.0);
        AnchorPane.setLeftAnchor(centerWrap, 0.0);
        inventoryPane.getChildren().add(centerWrap);

        nameField.setDisable(true);
        nameField.setPromptText("Select a slot, then type a name (e.g. <yellow>My Item)");
        if (loreField != null) loreField.setDisable(true);

        Platform.runLater(() -> {
            try {
                inventoryView.load();
                inventoryView.selectFirstSlot();
                nameField.setDisable(false);
                if (loreField != null) loreField.setDisable(false);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        });

        // Ensure background threads are stopped when the window is closed
        root.sceneProperty().addListener((obsScene, oldScene, scene) -> {
            if (scene != null) {
                scene.windowProperty().addListener((obsWin, oldWin, win) -> {
                    if (win != null) {
                        win.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, evt -> {
                            try {
                                ioPool.shutdownNow();
                            } catch (Exception ignored) {
                            }
                        });
                    }
                });
            }
        });

        if (darkToggle != null) {
            darkToggle.selectedProperty().addListener((obs, old, on) -> applyDarkMode(on));
        }

        CompletableFuture.supplyAsync(() -> {
            try {
                return textures.listAllItemTextureNames();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                return List.<String>of();
            }
        }, ioPool).thenAccept(list -> Platform.runLater(() -> setupMaterialList(list)));

        inventoryView.setOnSelected(slot -> {
            if (slot != null) {
                nameField.setDisable(false);
                if (loreField != null) loreField.setDisable(false);
                applyNameToSelected();
                applyLoreToSelected();
            } else {
                nameField.setDisable(true);
                if (loreField != null) loreField.setDisable(true);
            }
        });

        nameField.textProperty().addListener((obs, old, val) -> {
            renderPreview(val);
            inventoryView.setSelectedItemName(val);
        });

        if (loreField != null) {
            loreField.textProperty().addListener((obs, old, val) -> {
                renderLorePreview(val);
                inventoryView.setSelectedItemLore(splitLore(val));
            });
        }
    }

    private void setupMaterialList(List<String> materials) {
        filteredMaterials = new FilteredList<>(FXCollections.observableArrayList(materials), s -> true);
        materialList.setItems(filteredMaterials);
        materialFilter.textProperty().addListener((obs, old, val) -> {
            String q = val == null ? "" : val.trim().toLowerCase();
            filteredMaterials.setPredicate(s -> q.isEmpty() || s.toLowerCase().contains(q));
        });
        materialList.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> {
            if (val != null) {
                loadAndApplyItem(val);
            }
        });
    }

    private void loadAndApplyItem(String name) {
        CompletableFuture.supplyAsync(() -> {
            try {
                return textures.loadItem(name);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                return null;
            }
        }, ioPool).thenAccept(img -> Platform.runLater(() -> {
            if (img != null) inventoryView.setSelectedItemTexture(img);
        }));
    }

    private void applyNameToSelected() {
        String mm = nameField.getText();
        inventoryView.setSelectedItemName(mm);
        renderPreview(mm);
    }

    private void renderPreview(String mm) {
        TextFlow rendered = MiniMessageFx.render(mm);
        namePreview.getChildren().setAll(rendered.getChildren());
    }

    private void applyLoreToSelected() {
        if (loreField == null) return;
        List<String> lines = splitLore(loreField.getText());
        inventoryView.setSelectedItemLore(lines);
        renderLorePreview(loreField.getText());
    }

    private List<String> splitLore(String text) {
        if (text == null || text.isBlank()) return List.of();
        String[] arr = text.replace("\r", "").split("\n");
        return java.util.Arrays.stream(arr).map(String::trim).toList();
    }

    private void renderLorePreview(String text) {
        if (lorePreview == null) return;
        lorePreview.getChildren().clear();
        List<String> lines = splitLore(text);
        for (String line : lines) {
            TextFlow tf = MiniMessageFx.render(line);
            lorePreview.getChildren().add(tf);
        }
    }

    private Path locateCacheDir() {
        try {
            Path moduleDir = Path.of("").toAbsolutePath();
            Path candidate = moduleDir.resolve(".cache");
            Files.createDirectories(candidate);
            return candidate;
        } catch (Exception e) {
            Path home = Path.of(System.getProperty("user.home"), ".towncraft", "menu-designer-cache");
            try {
                Files.createDirectories(home);
            } catch (IOException ignored) {
            }
            return home;
        }
    }

    private void applyDarkMode(boolean enable) {
        if (root == null) {
            return;
        }
        String url = Objects.requireNonNull(getClass().getResource("/dark.css")).toExternalForm();
        if (url == null) {
            return;
        }
        if (enable) {
            if (!root.getStylesheets().contains(url)) {
                root.getStylesheets().add(url);
            }
        } else {
            root.getStylesheets().remove(url);
        }
    }
}
