package com.sunkenpotato.homebase.controller;

import com.sunkenpotato.homebase.MainApplication;
import com.sunkenpotato.homebase.web.RequestFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerHandler {

    public final static RequestFactory REQUEST_FACTORY = RequestFactory.getInstance();

    public static void changeScene(String fxml, Stage stage, int width, int height, String... stylesheets) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxml));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        for (String stylesheet : stylesheets) {
            scene.getStylesheets().add(MainApplication.class.getResource(stylesheet).toExternalForm());
        }
        scene.getStylesheets().add(MainApplication.class.getResource("css/global.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void setTitle(Stage stage, String title) {
        stage.setTitle(title);  
    }

    public static void setTitle(Node node, String title) {
        node.sceneProperty().addListener((obsScene, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obsWindow, oldWindow, newWindow) -> {
                    if (newWindow instanceof Stage) {
                        ((Stage) newWindow).setTitle(title);
                    }
                });
            }
        });
    }


}
