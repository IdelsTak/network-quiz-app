package company.policy.client.manager;

import company.policy.client.core.Question;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import static java.text.MessageFormat.format;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class QuestionsApplication extends Application {

    private static final Logger LOG = Logger.getLogger(QuestionsApplication.class.getName());
    public static final ResourceBundle RB = ResourceBundle.getBundle("i18n/messages", Locale.getDefault());
    private Thread listeningThread;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        QuestionsView questionsView = new QuestionsView(stage);
        Scene scene = new Scene(questionsView.getRoot());

        questionsView.getSendButton().setOnAction(event -> {
            try (Socket socket = new Socket("localhost", 8080); ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
                questionsView.setConnectedStatus(socket);

                LOG.log(Level.INFO, "Sending Question: {0}", questionsView.getQuestionToSend());

                out.writeObject(questionsView.getQuestionToSend());
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Error communicating with server", ex);

                showPlatformExitDialog();
            }
        });

        // Set up stage close event handler
        stage.setOnCloseRequest(event -> {
            try {
                listeningThread.interrupt();
                listeningThread.join();
                LOG.log(Level.INFO, "Disconnected from server");
            } catch (InterruptedException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        });

        stage.setTitle(format(RB.getString("title")));
        stage.setScene(scene);
        stage.show();

        listeningThread = new Thread(() -> {
            while (!Thread.interrupted()) {
                try (Socket socket = new Socket("localhost", 8080); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                    questionsView.setConnectedStatus(socket);

                    Object read = in.readObject();

                    LOG.log(Level.INFO, "Received: {0}", read);

                    if (read instanceof Question question && question.getGivenAnswer() > 0) {
                        questionsView.handle(question);
                    }
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, "Error communicating with server", ex);
                    Platform.runLater(this::showPlatformExitDialog);
                    break;
                } catch (ClassNotFoundException ex) {
                    LOG.log(Level.SEVERE, "Error deserializing object", ex);
                }
            }
        });

        listeningThread.start();

    }

    private void showPlatformExitDialog() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Connection closed");
        alert.setContentText("The connection to the server was terminated");
        alert.showAndWait();

        Platform.exit();
    }
}
