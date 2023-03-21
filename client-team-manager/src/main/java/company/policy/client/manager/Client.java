package company.policy.client.manager;

import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Client extends Application {

    private ObjectOutputStream out;

    @Override
    public void start(Stage primaryStage) throws Exception {
        TextArea messagesArea = new TextArea();
        TextField messageInput = new TextField();

        Button sendButton = new Button("Send");
        sendButton.setOnAction(event -> {
            try {
                String message = messageInput.getText();
                out.writeObject(message);
                out.flush();
                messageInput.clear();
            } catch (IOException e) {
                System.out.println("Error sending message to server");
            }
        });

        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(messagesArea, messageInput, sendButton);

        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        String serverAddress = "localhost";
        int serverPort = 8080;

        try {
            Socket socket = new Socket(serverAddress, serverPort);
            out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            new Thread(() -> {
                try {
                    while (true) {
                        Object message = in.readObject();
                        if (message != null) {
                            messagesArea.appendText(message.toString() + "\n");
                        } else {
                            break;
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Error reading message from server");
                }
            }).start();
        } catch (IOException e) {
            System.out.println("Error connecting to server");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
