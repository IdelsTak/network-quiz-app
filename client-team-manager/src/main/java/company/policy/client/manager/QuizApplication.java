package company.policy.client.manager;

import static java.text.MessageFormat.format;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class QuizApplication extends Application {

    public static final ResourceBundle RB = ResourceBundle.getBundle("i18n/messages", Locale.ITALY);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("quiz-view.fxml"));
        fxmlLoader.setResources(RB);
        Scene scene = new Scene(fxmlLoader.load());

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle(format(RB.getString("title")));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}
