package me.webhead1104.menuDesigner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class MenuDesignerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        stage.setTitle("Menu Designer");
        stage.setScene(scene);
        // Ensure the application process terminates when the window is closed
        stage.setOnCloseRequest((WindowEvent e) -> {
            Platform.exit();
        });
        stage.show();
        stage.centerOnScreen();
    }
}
