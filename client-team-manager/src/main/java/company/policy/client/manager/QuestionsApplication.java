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
    public static final ResourceBundle MESSAGES = ResourceBundle.getBundle("i18n/messages", Locale.getDefault());
    public static final ResourceBundle LOG_MESSAGES = ResourceBundle.getBundle("i18n/log_messages", Locale.getDefault());

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

                LOG.log(Level.INFO, LOG_MESSAGES.getString("log.info.sentQuestion"), questionsView.getQuestionToSend());

                out.writeObject(questionsView.getQuestionToSend());
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, LOG_MESSAGES.getString("log.error.communication"), ex);

                showPlatformExitDialog();
            }
        });

        // Set up stage close event handler
        stage.setOnCloseRequest(event -> {
            try {
                listeningThread.interrupt();
                listeningThread.join();
                LOG.log(Level.INFO, LOG_MESSAGES.getString("log.info.disconnected"));
            } catch (InterruptedException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        });

        stage.setTitle(format(MESSAGES.getString("title")));
        stage.setScene(scene);
        stage.show();

        listeningThread = new Thread(() -> {
            while (!Thread.interrupted()) {
                try (Socket socket = new Socket("localhost", 8080); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                    questionsView.setConnectedStatus(socket);

                    Object read = in.readObject();

                    LOG.log(Level.INFO, LOG_MESSAGES.getString("log.info.receivedMessage"), read);

                    if (read instanceof Question question && question.getGivenAnswer() > 0) {
                        questionsView.handle(question);
                    }
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, LOG_MESSAGES.getString("log.error.communication"), ex);
                    Platform.runLater(this::showPlatformExitDialog);
                    break;
                } catch (ClassNotFoundException ex) {
                    LOG.log(Level.SEVERE, LOG_MESSAGES.getString("log.error.deserialization"), ex);
                }
            }
        });

        listeningThread.start();

    }

    private void showPlatformExitDialog() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(format(MESSAGES.getString("alert.title")));
        alert.setHeaderText(format(MESSAGES.getString("alert.header")));
        alert.setContentText(format(MESSAGES.getString("alert.content")));
        alert.showAndWait();

        Platform.exit();
    }
}
