package company.policy.client.manager;

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
import javafx.scene.Scene;
import javafx.stage.Stage;

public class QuestionsApplication extends Application {

    private static final Logger LOG = Logger.getLogger(QuestionsApplication.class.getName());
    public static final ResourceBundle RB = ResourceBundle.getBundle("i18n/messages", Locale.getDefault());
    private ObjectOutputStream out;

    public static void main(String[] args) {
        launch(args);
    }
    private boolean asked;

    @Override
    public void start(Stage stage) throws Exception {
        QuestionsView questionsView = new QuestionsView(stage);
        Scene scene = new Scene(questionsView.getRoot());

        questionsView.getSendButton().setOnAction(event -> {
            try (Socket socket = new Socket("localhost", 8080); ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
                LOG.log(Level.INFO, "Sending Question..");
                out.writeObject(questionsView.getQuestionToSend());

            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Error communicating with server: {0}", ex);
            }
        });

        stage.setTitle(format(RB.getString("title")));
        stage.setScene(scene);
        stage.show();

        new Thread(() -> {
            while (true) {
                try (Socket socket = new Socket("localhost", 8080); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                    questionsView.setConnectedStatus(socket);
                    
                    Object readObject = in.readObject();

                    LOG.log(Level.INFO, "Received: {0}", readObject);
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, "Error communicating with server: {0}", ex);
                } catch (ClassNotFoundException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
            }

        }).start();
    }
}
