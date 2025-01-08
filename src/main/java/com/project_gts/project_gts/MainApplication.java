package com.project_gts.project_gts;

import com.project_gts.project_gts.controllers.data.TableViewUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("GTS Management");
        setPrimaryStage(stage);
        switchToScene("templates/EntryPage.fxml", stage);
        stage.show();
        stage.setResizable(false);
    }

    private void setPrimaryStage(Stage stage) {
        MainApplication.primaryStage = stage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void switchToScene(String path, Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(path));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        stage.setScene(scene);
    }

    public static void main(String[] args) {
        launch();
    }
}