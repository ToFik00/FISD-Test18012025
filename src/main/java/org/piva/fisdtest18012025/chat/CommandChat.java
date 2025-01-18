package org.piva.fisdtest18012025.chat;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class CommandChat extends Application {
    CommandManager commandManager;

    private TextArea chatArea;
    private TextField inputField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        commandManager = new CommandManager();

        BorderPane root = new BorderPane();

        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        root.setCenter(chatArea);

        inputField = new TextField();
        inputField.setPromptText("Enter your command here...");

        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> processInput());

        inputField.setOnAction(e -> sendButton.fire());

        HBox inputBox = new HBox(10, inputField, sendButton);
        inputBox.setPadding(new Insets(10));
        root.setBottom(inputBox);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Chat Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void processInput() {
        String userInput = inputField.getText().trim();

        if (userInput.isEmpty()) {
            return;
        }

        chatArea.appendText("Client: " + userInput + "\n");
        inputField.clear();

        String response = commandManager.generateResponse(userInput);
        chatArea.appendText("Program: " + response + "\n");
    }
}
