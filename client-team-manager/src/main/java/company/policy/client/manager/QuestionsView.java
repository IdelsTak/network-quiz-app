package company.policy.client.manager;

import company.policy.client.core.Question;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class QuestionsView {

    private static final Logger LOG = Logger.getLogger(QuestionsView.class.getName());
    private final Parent root;
    private final ObservableList<Question> questions;
    private final Stage stage;
    @FXML
    private ResourceBundle resources;
    @FXML
    private Label connectionStatusLabel;
    @FXML
    private TableView<Question> questionsTable;
    @FXML
    private TableColumn<Question, Integer> questionNumberColumn;
    @FXML
    private TableColumn<Question, String> subTopicColumn;
    @FXML
    private TableColumn<Question, String> topicColumn;
    @FXML
    private TextField topicTextField;
    @FXML
    private TextField subTopicTextField;
    @FXML
    private TextField questionTextField;
    @FXML
    private TextField optionATextField;
    @FXML
    private TextField optionBTextField;
    @FXML
    private TextField optionCTextField;
    @FXML
    private TextField optionDTextField;
    @FXML
    private TextField optionETextField;
    @FXML
    private TextField answerTextField;
    @FXML
    private Button sendButton;

    public QuestionsView(Stage stage) {
        this.stage = stage;

        questions = FXCollections.observableArrayList();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("questions-pane.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setResources(ResourceBundles.ITALIAN.getResourceBundle());
        try {
            root = (Parent) fxmlLoader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Parent getRoot() {
        return root;
    }

    @FXML
    protected void initialize() {
        questionsTable.setItems(questions);
        questionsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        questionNumberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        topicColumn.setCellValueFactory(new PropertyValueFactory<>("topic"));
        subTopicColumn.setCellValueFactory(new PropertyValueFactory<>("subTopic"));

        questionsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                topicTextField.setText(newValue.getTopic());
                subTopicTextField.setText(newValue.getSubTopic());
                questionTextField.setText(newValue.getQuestion());
                optionATextField.setText(newValue.getOptionA());
                optionBTextField.setText(newValue.getOptionB());
                optionCTextField.setText(newValue.getOptionC());
                optionDTextField.setText(newValue.getOptionD());
                optionETextField.setText(newValue.getOptionE());
                answerTextField.setText(Integer.toString(newValue.getCorrectAnswer()));
            }

            setSendButtonDisable(answerTextField.getText());
        });

        answerTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            setSendButtonDisable(newValue);
        });
    }

    private void setSendButtonDisable(String newValue) {
        sendButton.setDisable(true);

        if (newValue != null) {
            int answer = 0;

            try {
                answer = Integer.parseInt(newValue);
            } catch (NumberFormatException ex) {
                LOG.log(Level.WARNING, "Couldn't read answer: ", ex.getMessage());
            }

            sendButton.setDisable(questionsTable.getSelectionModel().getSelectedItem() == null || answer <= 0 || answer > 5);
        }
    }

    @FXML
    protected void loadQuestionsFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Text File");
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try (Scanner scanner = new Scanner(selectedFile)) {
                while (scanner.hasNextLine()) {
                    int number = Integer.parseInt(scanner.nextLine());
                    String topic = scanner.nextLine();
                    String subTopic = scanner.nextLine();
                    String question = scanner.nextLine();
                    String optionA = scanner.nextLine();
                    String optionB = scanner.nextLine();
                    String optionC = scanner.nextLine();
                    String optionD = scanner.nextLine();
                    String optionE = scanner.nextLine();
                    int correctAnswer = Integer.parseInt(scanner.nextLine());

                    Question model = new Question(
                            number,
                            topic,
                            subTopic,
                            question,
                            optionA,
                            optionB,
                            optionC,
                            optionD,
                            optionE,
                            correctAnswer);

                    questions.add(model);
                }

                Platform.runLater(() -> {
                    questionsTable.getSelectionModel().select(0);
                    answerTextField.requestFocus();
                    answerTextField.selectAll();
                });
            } catch (FileNotFoundException e) {
                LOG.log(Level.WARNING, "File not found: {0}", selectedFile.getAbsolutePath());
            } catch (NumberFormatException ex) {
                LOG.log(Level.WARNING, "Couldn't read number: {0}", ex);
            }
        }
    }

    void handle(Object object) {
        LOG.log(Level.INFO, "Needs to handle: {0}", object);
    }

    Button getSendButton() {
        return sendButton;
    }

    Question getQuestionToSend() {
        return questionsTable.getSelectionModel().getSelectedItem();
    }

    void setConnectedStatus(Socket socket) {
        connectionStatusLabel.setText("Connected to: " + socket.toString());
    }

}
