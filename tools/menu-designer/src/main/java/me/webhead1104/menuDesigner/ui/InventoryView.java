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

import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import me.webhead1104.menuDesigner.MiniMessageFx;
import me.webhead1104.menuDesigner.TextureService;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class InventoryView extends Pane {
    public static final int SLOT_SIZE = 18;
    public static final int LEFT = 8;
    public static final int CHEST_TOP = 18;
    public static final int PLAYER_TOP = 140;

    private final TextureService textures;
    private final ImageView backgroundView = new ImageView();
    private final List<SlotView> chestSlots = new ArrayList<>();
    private final List<SlotView> playerMainSlots = new ArrayList<>();
    private final List<SlotView> hotbarSlots = new ArrayList<>();
    @Getter
    private SlotView selected;
    @Setter
    private Consumer<SlotView> onSelected;

    public InventoryView(TextureService textures) {
        this.textures = textures;
        getChildren().add(backgroundView);
        setPickOnBounds(false);
    }

    private static @NotNull Tooltip getTooltip(VBox box) {
        Tooltip tip = new Tooltip();
        tip.setText(null);
        tip.setGraphic(new MinecraftTooltipSkin(box));
        tip.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-background-insets: 0; -fx-background-radius: 0;");
        tip.setShowDelay(Duration.ZERO);
        tip.setHideDelay(Duration.ZERO);
        tip.setShowDuration(Duration.seconds(10));
        return tip;
    }

    public void load() throws IOException {
        Image bg = textures.loadInventoryBackgroundGeneric54();
        backgroundView.setImage(bg);
        double bgWidth = bg.getWidth();
        double bgHeight = bg.getHeight();
        setPrefSize(bgWidth, bgHeight);
        setMinSize(bgWidth, bgHeight);
        setMaxSize(bgWidth, bgHeight);

        chestSlots.clear();
        getChildren().removeIf(n -> n instanceof SlotView);
        for (int row = 0; row < 6; row++) {
            doChestSlot(row, CHEST_TOP, chestSlots);
        }
        for (int row = 0; row < 3; row++) {
            doChestSlot(row, PLAYER_TOP, playerMainSlots);
        }
        for (int col = 0; col < 9; col++) {
            int x = LEFT + col * SLOT_SIZE;
            int y = PLAYER_TOP + 58;
            SlotView s = new SlotView();
            positionSlot(s, x, y);
            hotbarSlots.add(s);
        }
    }

    private void doChestSlot(int row, int playerTop, List<SlotView> playerMainSlots) {
        for (int col = 0; col < 9; col++) {
            int x = LEFT + col * SLOT_SIZE;
            int y = playerTop + row * SLOT_SIZE;
            SlotView s = new SlotView();
            positionSlot(s, x, y);
            playerMainSlots.add(s);
        }
    }

    public void setScaleFactor(double factor) {
        if (factor <= 0) factor = 1.0;
        setScaleX(factor);
        setScaleY(factor);
    }

    private void positionSlot(SlotView s, int x, int y) {
        s.setLayoutX(x);
        s.setLayoutY(y);
        s.setOnMouseClicked(ev -> select(s));
        getChildren().add(s);
    }

    private void select(SlotView s) {
        if (selected != null) selected.setSelected(false);
        selected = s;
        if (selected != null) selected.setSelected(true);
        if (onSelected != null) onSelected.accept(selected);
    }

    public List<SlotView> getAllSlots() {
        List<SlotView> all = new ArrayList<>();
        all.addAll(chestSlots);
        all.addAll(playerMainSlots);
        all.addAll(hotbarSlots);
        return all;
    }

    public void setSelectedItemTexture(Image image) {
        if (selected != null) {
            selected.setItemImage(image);
        }
    }

    public void setSelectedItemName(String miniMessage) {
        if (selected != null) {
            selected.setDisplayName(miniMessage);
        }
    }

    public void setSelectedItemLore(List<String> loreLines) {
        if (selected != null) {
            selected.setLore(loreLines);
        }
    }

    public void selectFirstSlot() {
        List<SlotView> all = getAllSlots();
        if (!all.isEmpty()) {
            select(all.getFirst());
        }
    }

    public static class SlotView extends StackPane {
        private final ImageView itemView = new ImageView();
        @Getter
        private String displayName;
        private List<String> lore = new ArrayList<>();

        public SlotView() {
            setPrefSize(SLOT_SIZE, SLOT_SIZE);
            setMaxSize(SLOT_SIZE, SLOT_SIZE);
            setMinSize(SLOT_SIZE, SLOT_SIZE);
            setAlignment(Pos.CENTER);
            itemView.setPreserveRatio(true);
            itemView.setFitWidth(16);
            itemView.setFitHeight(16);
            getChildren().add(itemView);
            getStyleClass().add("slot-view");
            updateBorder(false);
        }

        public void setItemImage(Image img) {
            itemView.setImage(img);
        }

        public void setSelected(boolean sel) {
            updateBorder(sel);
        }

        private void updateBorder(boolean sel) {
            if (sel) {
                setStyle("-fx-border-color: #ffff00; -fx-border-width: 2; -fx-border-insets: -1;");
            } else {
                setStyle("-fx-border-color: transparent; -fx-border-width: 2;");
            }
        }

        public void setDisplayName(String miniMessage) {
            this.displayName = miniMessage;
            updateTooltip();
        }

        public void setLore(List<String> loreLines) {
            this.lore = loreLines == null ? new ArrayList<>() : new ArrayList<>(loreLines);
            updateTooltip();
        }

        private void updateTooltip() {
            Tooltip previous = (Tooltip) getProperties().get("tooltip");
            if (previous != null) {
                Tooltip.uninstall(this, previous);
                getProperties().remove("tooltip");
            }

            boolean hasName = displayName != null && !displayName.isBlank();
            boolean hasLore = lore != null && !lore.isEmpty();
            if (!hasName && !hasLore) {
                return;
            }

            VBox box = new VBox(2.0);
            box.setAlignment(Pos.CENTER);
            if (hasName) {
                TextFlow nameFlow = MiniMessageFx.render(displayName);
                box.getChildren().add(nameFlow);
            }
            if (hasLore) {
                for (String line : lore) {
                    TextFlow tf = MiniMessageFx.render(line == null ? "" : line);
                    box.getChildren().add(tf);
                }
            }

            Tooltip tip = getTooltip(box);

            Tooltip.install(this, tip);
            getProperties().put("tooltip", tip);
        }
    }
}
