package company.policy.client.staff;

import company.policy.client.core.Question;
import java.io.IOException;
import java.net.Socket;
import static java.text.MessageFormat.format;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AnswersView {

    private static final Logger LOG = Logger.getLogger(AnswersView.class.getName());
    public static final ResourceBundle LOG_MESSAGES = ResourceBundle.getBundle("i18n/log_messages", Locale.getDefault());
    private final ObjectProperty<Question> questionProperty;
    private Parent root;
    @FXML
    private ResourceBundle resources;
    @FXML
    private TextField answerTextField;
    @FXML
    private Label connectionStatusLabel;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField option1TextField;
    @FXML
    private TextField option2TextField;
    @FXML
    private TextField option3TextField;
    @FXML
    private TextField option4TextField;
    @FXML
    private TextField option5TextField;
    @FXML
    private TextField questionTextField;
    @FXML
    private Button submitButton;
    @FXML
    private TextField topicTextField;

    public AnswersView() {
        questionProperty = new SimpleObjectProperty<>();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("answers-view.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setResources(ResourceBundles.DEFAULT.getResourceBundle());
        try {
            root = (Parent) fxmlLoader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @FXML
    protected void initialize() {
        questionProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                nameTextField.setText(newValue.getStaffName());
                topicTextField.setText(newValue.getTopic());
                questionTextField.setText(newValue.getQuestion());
                option1TextField.setText(newValue.getOptionA());
                option2TextField.setText(newValue.getOptionB());
                option3TextField.setText(newValue.getOptionC());
                option4TextField.setText(newValue.getOptionD());
                option5TextField.setText(newValue.getOptionE());
                answerTextField.setText(null);
            }

            submitButton.setDisable(newValue != null);
        });

        nameTextField.skinProperty().addListener((observable) -> {
            nameTextField.requestFocus();
            nameTextField.selectAll();
        });

        answerTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            setSubmitButtonDisable(newValue);
        });

        setSubmitButtonDisable(answerTextField.getText());
    }

    Parent getRoot() {
        return root;
    }

    Button getSubmitButton() {
        return submitButton;
    }

    Question getQuestionToSend() {
        Question question = questionProperty.getValue();

        if (question != null) {
            String staffName = nameTextField.getText();
            int answer = Integer.parseInt(answerTextField.getText());

            question = question.from(staffName, answer);
        }

        return question;
    }

    void setConnectedStatus(Socket socket) {
        Platform.runLater(() -> connectionStatusLabel.setText(format(resources.getString("label.connection.status.connected"), socket)));
    }

    void handle(Object object) {
        if (object instanceof Question question) {
            questionProperty.setValue(question);
        }
    }

    private void setSubmitButtonDisable(String newValue) {
        submitButton.setDisable(true);

        if (newValue != null) {
            int answer = 0;

            try {
                answer = Integer.parseInt(newValue);
            } catch (NumberFormatException ex) {
                LOG.log(Level.WARNING, LOG_MESSAGES.getString("log.warning.couldnt.read"), ex.getMessage());
            }

            String name = nameTextField.getText();

            submitButton.setDisable(name == null || name.isBlank() || answer <= 0 || answer > 5);
        }
    }

}
