package com.example.ai_test2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AI_Test2 extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Create a FXMLLoader to load the user interface layout from an FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(AI_Test2.class.getResource("GUI.fxml"));
        // Create a Scene using the loaded FXML layout, with dimensions 600x400
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Test 2");
        // Display the application window
        stage.setScene(scene);
        stage.show();
    }
    /**
     * the main method that launches the JavaFX application
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }
}