package company.policy.client.staff;

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
import javafx.stage.Stage;

public class AnswersApplication extends Application {

    private static final Logger LOG = Logger.getLogger(AnswersApplication.class.getName());
    public static final ResourceBundle MESSAGES = ResourceBundle.getBundle("i18n/messages", Locale.getDefault());
    public static final ResourceBundle LOG_MESSAGES = ResourceBundle.getBundle("i18n/log_messages", Locale.getDefault());
    private Thread listeningThread;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        AnswersView answersView = new AnswersView();
        Scene scene = new Scene(answersView.getRoot());

        answersView.getSubmitButton().setOnAction(event -> {
            try (Socket socket = new Socket("localhost", 8080); ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
                answersView.setConnectedStatus(socket);

                LOG.log(Level.INFO, LOG_MESSAGES.getString("log.info.sentAnswer"), answersView.getQuestionToSend());

                out.writeObject(answersView.getQuestionToSend());
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
                try (Socket clientSocket = new Socket("localhost", 8080); ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {
                    answersView.setConnectedStatus(clientSocket);

                    Object read = in.readObject();

                    LOG.log(Level.INFO, LOG_MESSAGES.getString("log.info.receivedMessage"), read);

                    if (read instanceof Question question && question.getGivenAnswer() == -1) {
                        answersView.handle(read);
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
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(format(MESSAGES.getString("alert.title")));
        alert.setHeaderText(format(MESSAGES.getString("alert.header")));
        alert.setContentText(format(MESSAGES.getString("alert.content")));
        alert.showAndWait();

        Platform.exit();
    }

}
