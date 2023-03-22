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
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AnswersApplication extends Application {

    private static final Logger LOG = Logger.getLogger(AnswersApplication.class.getName());
    public static final ResourceBundle RB = ResourceBundle.getBundle("i18n/messages", Locale.ITALY);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        AnswersView answersView = new AnswersView();
        Scene scene = new Scene(answersView.getRoot());

        stage.setTitle(format(RB.getString("policy_question")));
        stage.setScene(scene);
        stage.show();

        answersView.getSubmitButton().setOnAction(event -> {
            try (Socket socket = new Socket("localhost", 8080); ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
                out.writeObject(answersView.getQuestionToSend());
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        });

        new Thread(() -> {

            while (true) {
                try (Socket socket = new Socket("localhost", 8080); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                    answersView.setConnectedStatus(socket);

                    Object read = in.readObject();

                    LOG.log(Level.INFO, "Received: {0}", read);

                    if (read instanceof Question question && question.getGivenAnswer() == -1) {
                        answersView.handle(read);
                    }
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, "Error communicating with server: {0}", ex);
                } catch (ClassNotFoundException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
            }

        }).start();

    }

}
