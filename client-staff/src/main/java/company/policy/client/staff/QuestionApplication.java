package company.policy.client.staff;

import static java.text.MessageFormat.format;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class QuestionApplication extends Application {

    public static final ResourceBundle RB = ResourceBundle.getBundle("i18n/messages", Locale.getDefault());

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("question-view.fxml"));
        fxmlLoader.setResources(RB);
        Scene scene = new Scene(fxmlLoader.load());

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle(format(RB.getString("policy_question")));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}
