package controller.coordinator;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.user.Coordinator;

import java.util.HashSet;

public class CoordinatorSetTagsController {

    private CoordinatorPostController coordinatorPostController;

    @FXML
    private ListView<String> tagsListView;


    @FXML
    private Button cancelButton;


    @FXML
    private TextField tagField;

    @FXML
    void add() {
        if (!tagField.getText().equals("") && !tagsListView.getItems().contains(tagField.getText())) {
            String tag = tagField.getText();
            tagsListView.getItems().add(tag);
            tagField.clear();
        }
    }

    @FXML
    void cancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void confirm() {
        HashSet<String> tags = new HashSet<>(tagsListView.getItems());
        coordinatorPostController.setTags(tags);
        cancel();
    }

    @FXML
    void delete() {
        if(tagsListView.getSelectionModel().getSelectedItem() != null) {
            String type = tagsListView.getSelectionModel().getSelectedItem();
            tagsListView.getItems().remove(type);
        }
    }

    public void setCoordinatorPostController(CoordinatorPostController coordinatorPostController) {
        this.coordinatorPostController = coordinatorPostController;
    }

    public void loadData() {
        for (String tag : coordinatorPostController.getTags()) {
            tagsListView.getItems().add(tag);
        }
    }
}
