package com.example.ttt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HelloApplication extends Application {
    String file = null;

    @Override
    public void start(Stage stage) {
        BorderPane border = new BorderPane();

        Button choose = new Button("Choose File");
        border.setTop(choose);
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        border.setCenter(textArea);

        choose.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            fileChooser.setTitle("Open Resource File");
            File chosenFile = fileChooser.showOpenDialog(stage);

            if (chosenFile != null) {
                file = chosenFile.getPath();
                try {
                    String input = new String(Files.readAllBytes(Paths.get(file)));

                    Lexer lexer = new Lexer(input);
                    Parser parser = new Parser(lexer);

                    try { // try to parse the input
                        parser.parse();
                        textArea.setText("Parsing completed successfully.");
                    } catch (Exception e) {
                        showAlert(Alert.AlertType.ERROR, "Parsing Error", "Parsing failed: " + e.getMessage());
                    }
                } catch (IOException e) {
                    showAlert(Alert.AlertType.ERROR, "File Error", "Failed to read file: " + e.getMessage());
                }
            }
        });

        Scene scene = new Scene(border, 600, 400); // create a scene with specified width and height
        stage.setTitle("Compiler");
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}
