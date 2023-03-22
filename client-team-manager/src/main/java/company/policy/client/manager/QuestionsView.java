package company.policy.client.manager;

import company.policy.client.core.Attempt;
import company.policy.client.core.Question;
import company.policy.client.manager.bt.BinaryTree;
import company.policy.client.manager.bt.BinaryTreePrinter;
import company.policy.client.manager.dll.DoublyLinkedList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
import javafx.scene.control.TextArea;
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
    private BinaryTree tree;
    private Order order;

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

        inOrderButton.setOnAction(e -> {
            if (tree != null) {
                order = new InOrder(tree);
                treeTextArea.setText(order.toString());
            }
            e.consume();
        });
        preOrderButton.setOnAction(e -> {
            if (tree != null) {
                order = new PreOrder(tree);
                treeTextArea.setText(order.toString());
            }
            e.consume();
        });
        postOrderButton.setOnAction(e -> {
            if (tree != null) {
                order = new PostOrder(tree);
                treeTextArea.setText(order.toString());
            }
            e.consume();
        });
        treeViewButton.setOnAction(e -> {
            if (tree != null) {
                new TreeView(new BinaryTreePrinter(tree.root).print()).showDialog();
            }
        });
    }
    @FXML
    private Button inOrderButton;
    @FXML
    private Button preOrderButton;
    @FXML
    private Button postOrderButton;
    @FXML
    private Button treeViewButton;

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
    private final List<Question> answeredQuestions = new ArrayList<>();

    void handle(Question question) {
        LOG.log(Level.INFO, "Needs to handle: {0}", question);

        if (question.getGivenAnswer() <= 0) {
            return;
        }

        answeredQuestions.add(question);

        Map<Integer, Set<Question>> map = answeredQuestions.stream().collect(Collectors.groupingBy(m -> m.getNumber(), Collectors.toSet()));
        List<Question> queriesToPrint = new ArrayList<>();

        for (Map.Entry<Integer, Set<Question>> entry : map.entrySet()) {
            Set<Question> queries = entry.getValue();

            int attempted = queries.size();
            int correct = (int) queries.stream().filter(query -> query.getGivenAnswer() == query.getCorrectAnswer()).count();

            Question query = new ArrayList<>(queries).get(0);
            query = query.from(new Attempt(attempted, correct));

            queriesToPrint.add(query);
        }

        DoublyLinkedList linkedList = new DoublyLinkedList();
        tree = new BinaryTree();

        for (Question query : queriesToPrint) {
            linkedList.append(query);
        }

        for (Question query : linkedList.getAll()) {
            tree.add(query);
        }

        linkedListTextArea.setText(linkedList.toString());

        if (order == null) {
            order = new InOrder(tree);
        } else if (order instanceof InOrder) {
            order = new InOrder(tree);
        } else if (order instanceof PreOrder) {
            order = new PreOrder(tree);
        } else {
            order = new PostOrder(tree);
        }

        treeTextArea.setText(order.toString());
    }

    @FXML
    private TextArea linkedListTextArea;
    @FXML
    private TextArea treeTextArea;

    private interface Order {

    }

    private class InOrder implements Order {

        private final List<Question> traversedModels;

        private InOrder(BinaryTree tree) {
            this(tree.traverseInOrderWithoutRecursion());
        }

        private InOrder(List<Question> traversedModels) {
            this.traversedModels = new ArrayList<>(traversedModels);
        }

        @Override
        public String toString() {
            return traversedModels.stream()
                    .map(value -> String.format("%d - %s (%s)", value.getNumber(), value.getTopic(), value.getSubTopic()))
                    .collect(Collectors.joining(", ", "IN-ORDER:  ", ""));
        }

    }

    private class PreOrder implements Order {

        private final List<Question> traversedModels;

        private PreOrder(BinaryTree tree) {
            this(tree.traversePreOrderWithoutRecursion());
        }

        private PreOrder(List<Question> traversedModels) {
            this.traversedModels = new ArrayList<>(traversedModels);
        }

        @Override
        public String toString() {
            return traversedModels.stream()
                    .map(value -> String.format("%d - %s (%s)", value.getNumber(), value.getTopic(), value.getSubTopic()))
                    .collect(Collectors.joining(", ", "PRE-ORDER: ", ""));
        }

    }

    private class PostOrder implements Order {

        private final List<Question> traversedModels;

        private PostOrder(BinaryTree tree) {
            this(tree.traversePostOrderWithoutRecursion());
        }

        private PostOrder(List<Question> traversedModels) {
            this.traversedModels = new ArrayList<>(traversedModels);
        }

        @Override
        public String toString() {
            return traversedModels.stream()
                    .map(value -> String.format("%d - %s (%s)", value.getNumber(), value.getTopic(), value.getSubTopic()))
                    .collect(Collectors.joining(", ", "POST-ORDER: ", ""));
        }

    }

    Button getSendButton() {
        return sendButton;
    }

    Question getQuestionToSend() {
        return questionsTable.getSelectionModel().getSelectedItem();
    }

    void setConnectedStatus(Socket socket) {
        Platform.runLater(() -> connectionStatusLabel.setText("Connected to: " + socket.toString()));
    }

}
