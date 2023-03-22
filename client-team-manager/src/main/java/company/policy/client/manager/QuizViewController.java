package company.policy.client.manager;

import company.policy.client.core.Attempt;
import company.policy.client.manager.dll.DoublyLinkedList;
import company.policy.client.core.ChatClient;
import company.policy.client.core.ChatClientThread;
import company.policy.client.core.PolicyQuestionModel;
import static company.policy.client.manager.QuestionsApplication.RB;
import company.policy.client.manager.bt.BinaryTree;
import company.policy.client.manager.bt.BinaryTreePrinter;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import static java.text.MessageFormat.format;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class QuizViewController implements ChatClient {

    @Override
    public void stop() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void handle(Object object) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

//    private final ObservableList<PolicyQuestionModel> fileReadData = FXCollections.observableArrayList();
//    private final String serverName = "localhost";
//    private final int serverPort = 4444;
//    private Socket socket;
//    private ObjectOutputStream streamOut;
//    private ChatClientThread client;
//    private double xOffset;
//    private double yOffset;
//    @FXML
//    private VBox VboxMain;
//    @FXML
//    private Button btnConnect;
//    @FXML
//    private Button btnExit;
//    @FXML
//    private Button btnExit_2;
//    @FXML
//    private Button btnLoadTableData;
//    @FXML
//    private Button btnMinimise;
//    @FXML
//    private Button btnOpenFile;
//    @FXML
//    private Button btnQuestionNumberSort;
//    @FXML
//    private Button btnSend;
//    @FXML
//    private Button btnSubTopicSort;
//    @FXML
//    private Button btnTopicSort;
//    @FXML
//    private Label lblMessage;
//    @FXML
//    private Label lblQuestionNumber;
//    @FXML
//    private TableView<PolicyQuestionModel> tableVeiw;
//    @FXML
//    private TableColumn<PolicyQuestionModel, Integer> questionNumberColumn;
//    @FXML
//    private TableColumn<PolicyQuestionModel, String> subTopicColumn;
//    @FXML
//    private TableColumn<PolicyQuestionModel, String> topicColumn;
//    @FXML
//    private TextField txtAnswerA;
//    @FXML
//    private TextField txtAnswerB;
//    @FXML
//    private TextField txtAnswerC;
//    @FXML
//    private TextField txtAnswerD;
//    @FXML
//    private TextField txtAnswerE;
//    @FXML
//    private TextField txtCorrectAnswer;
//    @FXML
//    private TextArea txtQuestionText;
//    @FXML
//    private TextField txtSearchField;
//    @FXML
//    private TextField txtSubTopic;
//    @FXML
//    private TextField txtTopic;
//    @FXML
//    private TextArea linkedListTArea;
//    @FXML
//    private TextArea treeTArea;
//
//    public QuizViewController() {
//
//    }
//    private ObjectOutputStream out;
//
//    @Override
//    public void stop() {
//        if (Platform.isFxApplicationThread()) {
//            doStop();
//        } else {
//            Platform.runLater(this::doStop);
//        }
//    }
//
//    private void doStop() {
//        try {
//            if (streamOut != null) {
//                streamOut.close();
//            }
//            if (socket != null) {
//                socket.close();
//            }
//        } catch (IOException ioe) {
//            println(format(RB.getString("error_closing")));
//        }
//
//        if (client != null) {
//            client.close();
//            client.stop();
//        }
//    }
//
//    @Override
//    public void handle(Object object) {
//        if (Platform.isFxApplicationThread()) {
//            doHandle(object);
//        } else {
//            System.out.printf("company.policy.client.manager.QuizViewController.handle(), about to handle: %s\n", object);
//
//            Platform.runLater(() -> doHandle(object));
//        }
//    }
//    private final List<PolicyQuestionModel> answeredModels = new ArrayList<>();
//
//    private void doHandle(Object object) {
//        System.out.printf("company.policy.client.manager.QuizViewController.doHandle(); received object: %s\n", object);
//
//        if (object instanceof String) {
//            if (object.equals(".bye")) {
//                println(format(RB.getString("goodbye_press_exit")));
//                stop();
//            }
//        } else if (object instanceof PolicyQuestionModel model) {
//
////            object = object.substring(object.indexOf(":") + 1, object.length());
////            PolicyQuestionModel model = PolicyQuestionModel.valueOf(object);
//            if ((model.getStaffName() == null || model.getStaffName().trim().isEmpty()) || model.getGivenAnswer() == 0) {
//                return;
//            }
//
//            answeredModels.add(model);
//
////            Map<Integer, Set<PolicyQuestionModel>> map = answeredModels.stream().collect(Collectors.groupingBy(m -> m.getPolicyQuestionNumber(), Collectors.toSet()));
//            Map<Integer, Set<PolicyQuestionModel>> map = answeredModels.stream().collect(Collectors.groupingBy(m -> m.getQuestionNumber(), Collectors.toSet()));
//
//            List<PolicyQuestionModel> toPrint = new ArrayList<>();
//
//            for (Map.Entry<Integer, Set<PolicyQuestionModel>> entry : map.entrySet()) {
//                Set<PolicyQuestionModel> value = entry.getValue();
//
//                int attempted = value.size();
//                int correct = (int) value.stream().filter(m -> m.getGivenAnswer() == m.getCorrectAnswer()).count();
//
//                PolicyQuestionModel pqm = new ArrayList<>(value).get(0);
//
////                pqm.setAttemptedCount(total);
////                pqm.setAnsweredCorrectCount(correctCount);
//                pqm = new PolicyQuestionModel(
//                        pqm.getStaffName(),
//                        pqm.getQuestionNumber(),
//                        pqm.getTopic(),
//                        pqm.getSubTopic(),
//                        pqm.getQuestion(),
//                        pqm.getOptionA(),
//                        pqm.getOptionB(),
//                        pqm.getOptionC(),
//                        pqm.getOptionD(),
//                        pqm.getOptionE(),
//                        pqm.getCorrectAnswer(),
//                        pqm.getGivenAnswer(),
//                        new Attempt(attempted, correct));
//
//                toPrint.add(pqm);
//            }
//
//            DoublyLinkedList dll = new DoublyLinkedList();
//            tree = new BinaryTree();
//
//            for (PolicyQuestionModel pqm : toPrint) {
//                dll.append(pqm);
//            }
//
//            for (PolicyQuestionModel pqm : dll.getAll()) {
//                tree.add(pqm);
//            }
//
//            linkedListTArea.setText(dll.toString());
//
//            if (order == null) {
//                order = new InOrder(tree);
//            } else if (order instanceof InOrder) {
//                order = new InOrder(tree);
//            } else if (order instanceof PreOrder) {
//                order = new PreOrder(tree);
//            } else {
//                order = new PostOrder(tree);
//            }
//
//            treeTArea.setText(order.toString());
//        }
//    }
//    private BinaryTree tree;
//
//    private interface Order {
//
//    }
//
//    private static class InOrder implements Order {
//
//        private final List<PolicyQuestionModel> traversedModels;
//
//        private InOrder(BinaryTree tree) {
//            this(tree.traverseInOrderWithoutRecursion());
//        }
//
//        private InOrder(List<PolicyQuestionModel> traversedModels) {
//            this.traversedModels = new ArrayList<>(traversedModels);
//        }
//
//        @Override
//        public String toString() {
//            return traversedModels.stream()
//                    /*.map(value -> String.format("%d - %s (%s)", value.getPolicyQuestionNumber(), value.getPolicyTopic(), value.getPolicySubTopic()))
//                    .collect(Collectors.joining(", ", format(RB.getString("inorder_delimiter")), ""));*/
//                    .map(value -> String.format("%d - %s (%s)", value.getQuestionNumber(), value.getTopic(), value.getSubTopic()))
//                    .collect(Collectors.joining(", ", format(RB.getString("inorder_delimiter")), ""));
//        }
//
//    }
//
//    private static class PreOrder implements Order {
//
//        private final List<PolicyQuestionModel> traversedModels;
//
//        private PreOrder(BinaryTree tree) {
//            this(tree.traversePreOrderWithoutRecursion());
//        }
//
//        private PreOrder(List<PolicyQuestionModel> traversedModels) {
//            this.traversedModels = new ArrayList<>(traversedModels);
//        }
//
//        @Override
//        public String toString() {
//            return traversedModels.stream()
//                    /*.map(value -> String.format("%d - %s (%s)", value.getPolicyQuestionNumber(), value.getPolicyTopic(), value.getPolicySubTopic()))
//                    .collect(Collectors.joining(", ", format(RB.getString("preorder_delimiter")), ""));*/
//                    .map(value -> String.format("%d - %s (%s)", value.getQuestionNumber(), value.getTopic(), value.getSubTopic()))
//                    .collect(Collectors.joining(", ", format(RB.getString("preorder_delimiter")), ""));
//        }
//
//    }
//
//    private static class PostOrder implements Order {
//
//        private final List<PolicyQuestionModel> traversedModels;
//
//        private PostOrder(BinaryTree tree) {
//            this(tree.traversePostOrderWithoutRecursion());
//        }
//
//        private PostOrder(List<PolicyQuestionModel> traversedModels) {
//            this.traversedModels = new ArrayList<>(traversedModels);
//        }
//
//        @Override
//        public String toString() {
//            return traversedModels.stream()
//                    /*.map(value -> String.format("%d - %s (%s)", value.getPolicyQuestionNumber(), value.getPolicyTopic(), value.getPolicySubTopic()))
//                    .collect(Collectors.joining(", ", format(RB.getString("postorder_delimiter")), ""));*/
//                    .map(value -> String.format("%d - %s (%s)", value.getQuestionNumber(), value.getTopic(), value.getSubTopic()))
//                    .collect(Collectors.joining(", ", format(RB.getString("postorder_delimiter")), ""));
//        }
//
//    }
//
//    @FXML
//    private Button displayInOrderBtn;
//    @FXML
//    private Button displayPreOrderBtn;
//    @FXML
//    private Button displayPostOrderBtn;
//    @FXML
//    private Button displayTreeBtn;
//    private Order order;
//
//    @FXML
//    public void initialize() {
//        displayInOrderBtn.setOnAction(e -> {
//            if (tree != null) {
//                order = new InOrder(tree);
//                treeTArea.setText(order.toString());
//            }
//            e.consume();
//        });
//        displayPreOrderBtn.setOnAction(e -> {
//            if (tree != null) {
//                order = new PreOrder(tree);
//                treeTArea.setText(order.toString());
//            }
//            e.consume();
//        });
//        displayPostOrderBtn.setOnAction(e -> {
//            if (tree != null) {
//                order = new PostOrder(tree);
//                treeTArea.setText(order.toString());
//            }
//            e.consume();
//        });
//        displayTreeBtn.setOnAction(e -> {
//            try {
//                stop();
//
//                Stage stage = (Stage) btnExit.getScene().getWindow();
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("tree-view.fxml"));
//                fxmlLoader.setResources(RB);
//                stage.setScene(new Scene(fxmlLoader.load()));
//
//                TreeViewController controller = fxmlLoader.getController();
//
//                TextArea treeViewTArea = controller.getTreeViewTextArea();
//
//                if (tree != null) {
//                    BinaryTreePrinter btp = new BinaryTreePrinter(tree.root);
//
//                    treeViewTArea.setText(btp.print());
//                }
//            } catch (IOException ex) {
//            }
//
//            e.consume();
//        });
//        String perfectPoliciesQuizDatatxt = /*Locale.getDefault().equals(Locale.ITALY) ? "" :*/ "PerfectPoliciesQuizData.txt";
//
//        // Reading in of the data file
//        try (DataInputStream in = new DataInputStream(getClass().getResourceAsStream(perfectPoliciesQuizDatatxt)); BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
//
//            String lineInDataFile;
//
//            for (int i = 0; i < 10; i++) {
//                lineInDataFile = br.readLine();
//            }
//
//            while ((lineInDataFile = br.readLine()) != null) {
//
//                /*PolicyQuestionModel model = new PolicyQuestionModel();
//
//                model.setPolicyQuestionNumber(Integer.parseInt(lineInDataFile));
//                model.setPolicyTopic(br.readLine());
//                model.setPolicySubTopic(br.readLine());
//                model.setPolicyQuestionText(br.readLine());
//                model.setAnswerOptionA(br.readLine());
//                model.setAnswerOptionB(br.readLine());
//                model.setAnswerOptionC(br.readLine());
//                model.setAnswerOptionD(br.readLine());
//                model.setAnswerOptionE(br.readLine());
//                model.setCorrectAnswer(Integer.parseInt(br.readLine()));*/
//                int questionNumber = Integer.parseInt(lineInDataFile);
//                String topic = br.readLine();
//                String subTopic = br.readLine();
//                String question = br.readLine();
//                String optionA = br.readLine();
//                String optionB = br.readLine();
//                String optionC = br.readLine();
//                String optionD = br.readLine();
//                String optionE = br.readLine();
//                int correctAnswer = Integer.parseInt(br.readLine());
//
//                PolicyQuestionModel model = new PolicyQuestionModel(
//                        questionNumber,
//                        topic,
//                        subTopic,
//                        question,
//                        optionA,
//                        optionB,
//                        optionC,
//                        optionD,
//                        optionE,
//                        correctAnswer);
//
//                System.out.printf("company.policy.client.manager.QuizViewController.initialize(); model = %s\n", model);
//
//                fileReadData.add(model);
//            }
//
//        } catch (IOException e) {
//            println(format(RB.getString("error_occurred_when_attempting_to_read_in_the_file"), e.getMessage()));
//        }
//
//        tableVeiw.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//
//        questionNumberColumn.setCellValueFactory(new PropertyValueFactory<>("questionNumber"));
//        topicColumn.setCellValueFactory(new PropertyValueFactory<>("topic"));
//        subTopicColumn.setCellValueFactory(new PropertyValueFactory<>("subTopic"));
//        // Adjusted this one :
//        // subTopicColumn.setCellValueFactory(new PropertyValueFactory<PolicyQuestionModel, StringProperty>("policySubTopic"));
//
//        tableVeiw.setItems(fileReadData);
////        tableVeiw.setEditable(true);
////        questionNumberColumn.setEditable(true);
//
//        // Initial Filtered List
//        FilteredList<PolicyQuestionModel> filteredData = new FilteredList<>(fileReadData, b -> true);
//        txtSearchField.textProperty().addListener((Observable, oldValue, newValue)
//                -> filteredData.setPredicate(PolicyQuestionModel -> {
//
//                    // If no search value then display all records or whatever records it currently has. no changes.
//                    if (newValue.isEmpty() || newValue.isBlank()) {
//                        return true;
//                    }
//
//                    String searchKeyword = newValue.toLowerCase();
//                    //No match has been found
//                    if (PolicyQuestionModel.getTopic().toLowerCase().contains(searchKeyword)) {     //toString().indexOf(searchKeyword)
//                        return true;    //Means we found a match in policy topic
//                    } else {
//                        return PolicyQuestionModel.getSubTopic().toLowerCase().contains(searchKeyword);
//                    }
//                    // PolicyQuestionModel.getPolicySubTopic().toLowerCase().indexOf(searchKeyword) > -1) {
//                }));
//
//        SortedList<PolicyQuestionModel> sortedData = new SortedList<>(filteredData);
//        // Bind sorted result with table view
//        sortedData.comparatorProperty().bind(tableVeiw.comparatorProperty());
//        tableVeiw.setItems(sortedData);
//
//        btnSend.setOnAction(ev -> {
//            PolicyQuestionModel selectedModel = tableVeiw.getSelectionModel().getSelectedItem();
//
//            System.out.printf("company.policy.client.manager.QuizViewController.initialize(); sending model: %s\n", selectedModel);
//
//            if (selectedModel != null) {
//                send(selectedModel);
//            }
//
//            ev.consume();
//        });
//    }
//
//    @FXML
//    void btnSelectionSort(ActionEvent event) {
//        selectionSort(fileReadData);
//        tableVeiw.refresh();
//    }
//
//    @FXML
//    void buttonConnectFunction(ActionEvent event) {
//        System.out.println("company.policy.client.manager.QuizViewController.buttonConnectFunction(); starting to connect...");
//
//        connect(serverName, serverPort);
//    }
//
//    @FXML
//    void buttonInsertion_Sort(ActionEvent event) {
//        insertionSort(fileReadData);
//        tableVeiw.refresh();
//    }
//
//    @FXML
//    void buttonQN_Sort(ActionEvent event) {
//        bubbleSort(fileReadData);
//        tableVeiw.refresh();
//    }
//
//    @FXML
//    void handleClickAction(MouseEvent event) {
//        Stage stage = (Stage) VboxMain.getScene().getWindow();
//        xOffset = stage.getX() - event.getScreenX();
//        yOffset = stage.getY() - event.getScreenY();
//    }
//
//    @FXML
//    void handleCloseAction(ActionEvent event) {
//        //Chat stuff
//        stop();
//
//        Stage stage = (Stage) btnExit.getScene().getWindow();
//        stage.close();
//    }
//
//    @FXML
//    void handleCloseAction_2(ActionEvent event) {
//        handleCloseAction(event);
//    }
//
//    @FXML
//    void handleMinimiseAction(ActionEvent event) {
//        Stage stage = (Stage) btnMinimise.getScene().getWindow();
//        stage.setIconified(true);
//    }
//
//    @FXML
//    void handleMovementAction(MouseEvent event) {
//        Stage stage = (Stage) VboxMain.getScene().getWindow();
//        stage.setX(event.getScreenX() + xOffset);
//        stage.setY(event.getScreenY() + yOffset);
//    }
//
//    @FXML
//    void onTableRowClick(MouseEvent event) {
//        //Check for click, change to 2 for double click
//        if (event.getClickCount() == 1) {
//            txtTopic.setText(tableVeiw.getSelectionModel().getSelectedItem().getTopic());
//            txtSubTopic.setText(tableVeiw.getSelectionModel().getSelectedItem().getSubTopic());
//            //txtQuestionNumber.setText(String.valueOf(tableVeiw.getSelectionModel().getSelectedItem().getPolicyQuestionNumber()));   //String.value of turns the int into a string similar to toString() method
//            txtQuestionText.setText(tableVeiw.getSelectionModel().getSelectedItem().getQuestion());
//            lblQuestionNumber.setText(String.valueOf(tableVeiw.getSelectionModel().getSelectedItem().getQuestionNumber()));
//            txtAnswerA.setText(tableVeiw.getSelectionModel().getSelectedItem().getOptionA());
//            txtAnswerB.setText(tableVeiw.getSelectionModel().getSelectedItem().getOptionB());
//            txtAnswerC.setText(tableVeiw.getSelectionModel().getSelectedItem().getOptionC());
//            txtAnswerD.setText(tableVeiw.getSelectionModel().getSelectedItem().getOptionD());
//            txtAnswerE.setText(tableVeiw.getSelectionModel().getSelectedItem().getOptionE());
//            txtCorrectAnswer.setText(String.valueOf(tableVeiw.getSelectionModel().getSelectedItem().getCorrectAnswer()));
//        }
//    }
//    private static final Logger LOG = Logger.getLogger(QuizViewController.class.getName());
//
//    private void connect(String serverName, int serverPort) {
//        println(format(RB.getString("establishing_connection")));
//
////        System.out.printf("company.policy.client.manager.QuizViewController.connect(); connecting to server: %s; port: %d\n", serverName, serverPort);
////
////        try {
////            socket = new Socket(serverName, serverPort);
////            println(format(RB.getString("connected"), socket));
////
////            System.out.printf("company.policy.client.manager.QuizViewController.connect(); opening connection using socket: %s\n", socket);
////
////            open();
////
////            System.out.println("company.policy.client.manager.QuizViewController.connect(); disabling btnConnect button...");
////
////            btnSend.setDisable(false);
////            btnConnect.setDisable(true);
////        } catch (UnknownHostException uhe) {
////            println(format(RB.getString("host_unknown"), uhe.getMessage()));
////        } catch (IOException ioe) {
////            println(format(RB.getString("unexpected_exception"), ioe.getMessage()));
////        }
////
////        try (Socket sock = new Socket("localhost", 8080); ObjectInputStream in = new ObjectInputStream(sock.getInputStream()); ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream())) {
////            new Thread(() -> {
////                try {
////                    out.writeObject("I'm here bitches!");
////                    
////                    while (true) {
////                        LOG.log(Level.INFO, "Value read from stream: {0}", in.readObject());
////                    }
////                } catch (IOException | ClassNotFoundException ex) {
////                    LOG.log(Level.SEVERE, "Error reading message from server: {0}", ex.getMessage());
////                }
////            }).start();
////        } catch (IOException ex) {
////            LOG.log(Level.SEVERE, "Error reading message from server: {0}", ex.getMessage());
////        }
//        btnSend.setDisable(false);
//        btnConnect.setDisable(true);
//    }
//
//    private void open() {
//        try {
//            streamOut = new ObjectOutputStream(socket.getOutputStream());
//
//            System.out.printf("company.policy.client.manager.QuizViewController.open(); created output stream: %s\n", streamOut);
//
//            client = new ChatClientThread(this, socket);
//
//            System.out.println("company.policy.client.manager.QuizViewController.open(); created chat client thread");
//        } catch (IOException ioe) {
//            println(format(RB.getString("error_opening_output_stream"), ioe));
//        }
//    }
//
//    private void send(PolicyQuestionModel model) {
////        client.sendQuestion(model);
////        
////        try {
////            streamOut.writeObject(model);
//////            streamOut.flush();
////        } catch (IOException ioe) {
////            println(format(RB.getString("sending_error"), ioe.getMessage()));
////            stop();
////        }
//        try (Socket sock = new Socket("localhost", 8080); ObjectInputStream in = new ObjectInputStream(sock.getInputStream()); ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream())) {
//            out.writeObject(model);
//
//            new Thread(() -> {
//                try {
//                    while (true) {
//                        LOG.log(Level.INFO, "Value read from stream: {0}", in.readObject());
//                    }
//                } catch (IOException | ClassNotFoundException ex) {
//                    LOG.log(Level.SEVERE, "Error reading message from server: {0}", ex);
//                }
//            }).start();
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, "Error reading message from server: {0}", ex);
//        }
//    }
//
//    private void println(String msg) {
//        //display.appendText(msg + System.lineSeparator());
//        //this prints message whats happening
//        lblMessage.setText(msg);
//    }
//
//    private void selectionSort(ObservableList<PolicyQuestionModel> list) {
//        int i, j; //first, temp
//        PolicyQuestionModel first;
//        for (i = 0; i < list.size() - 1; i++) {
//            first = list.get(i); //initialize to subscript of first element
//            for (j = i + 1; j < list.size(); j++) //locate smallest element between positions 1 and i.
//            {
//                //if (num[j] < num[first])
//                //first = j;
//                if (list.get(j).getSubTopic().compareToIgnoreCase(first.getSubTopic()) > 0) //< num.get(i).policySubTopicProperty().toString())
//                {
//                    first = list.get(j);
//                    //temp = num[first]; //swap smallest found with element in position i.
//                    PolicyQuestionModel temp = first;
//                    //num[first] = num[i];
//                    list.set(j, list.get(i));
//                    // num[i] = temp;
//                    list.set(i, temp);
//                }
//            }
//            try {
//                System.out.println((list.get(j).getSubTopic()) + " - " + (list.get(j).getSubTopic()));
//            } catch (Exception e) {
//            }
//        }
//    }
//
//    private void insertionSort(ObservableList<PolicyQuestionModel> list) {
//        int j; // the number of items sorted so far
//        PolicyQuestionModel key; // the item to be inserted
//        int i;
//        for (j = 1; j < list.size(); j++) // Start with 1 (not 0)
//        {
//            key = list.get(j);
//            for (i = j - 1; (i >= 0) && (list.get(i).getTopic().compareToIgnoreCase(key.getTopic()) < 0); i--) // Smaller values are moving up
//            {
//                list.set(i + 1, list.get(i));
//            }
//            list.set(i + 1, key); // Put the key in its proper location
//        }
//        try {
//            System.out.println((list.get(j).getTopic()) + " - " + (list.get(j).getTopic()));
//        } catch (Exception e) {
//        }
//    }
//
//    private void bubbleSort(ObservableList<PolicyQuestionModel> list) {
//        for (int j = 0; j < list.size(); j++) {
//            for (int i = j + 1; i < list.size(); i++) {
//                if ((list.get(i).getQuestionNumber() + "").compareToIgnoreCase((list.get(j).getQuestionNumber() + "")) > 0) {
//                    PolicyQuestionModel qn = list.get(j);
//                    list.set(j, list.get(i));
//                    list.set(i, qn);
//                }
//            }
//            try {
//                System.out.println((list.get(j).getQuestionNumber() + "") + " - " + (list.get(j).getQuestionNumber() + ""));
//            } catch (Exception e) {
//            }
//        }
//    }

}
