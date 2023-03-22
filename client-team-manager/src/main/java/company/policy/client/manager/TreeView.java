package company.policy.client.manager;

import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;

class TreeView {

    private final Parent root;
    @FXML
    private TextArea treeTextArea;
    private final String tree;

    TreeView(String tree) {
        this.tree = tree;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("tree-pane.fxml"));
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
        treeTextArea.setText(tree);
    }

    Optional<ButtonType> showDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setDialogPane((DialogPane) root);

        dialog.setResultConverter(bt -> {
            if (bt == ButtonType.OK) {
                return bt;
            }
            return null;
        });
        
        return dialog.showAndWait();
    }

}
