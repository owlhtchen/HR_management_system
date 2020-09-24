package controller.coordinator;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.ArrayList;

public class CoordinatorSetInteviewsController{

    private CoordinatorPostController coordinatorPostController;


    @FXML
    private ListView<String> interviewListView;


    @FXML
    private Button cancelButton;

    @FXML
    private ChoiceBox<String> typeChoiceBox;


    @FXML
    void add() {
        if(typeChoiceBox.getValue() != null) {
            String type = typeChoiceBox.getValue();
            interviewListView.getItems().add(type);
        }
    }

    @FXML
    void cancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void confirm() {
        ArrayList<String> types = new ArrayList<>();
        for (String type: interviewListView.getItems()){
            if (type.equals("Phone Interview")){
                types.add("phone");
            } else if (type.equals("One-to-one Interview")){
                types.add("oneToOne");
            } else if (type.equals("Take home Quiz")){
                types.add("quiz");
            } else if (type.equals("Group Interview")){
                types.add("group");
            }
        }
        coordinatorPostController.setInterviewTypes(types);
        cancel();
    }

    @FXML
    void delete() {
        if(interviewListView.getSelectionModel().getSelectedItem() != null) {
            String type = interviewListView.getSelectionModel().getSelectedItem();
            interviewListView.getItems().remove(type);
        }
    }


    public void setCoordinatorPostController(CoordinatorPostController coordinatorPostController) {
        this.coordinatorPostController = coordinatorPostController;
    }


    public void loadData() {
        typeChoiceBox.getItems().add("Phone Interview");
        typeChoiceBox.getItems().add("One-to-one Interview");
        typeChoiceBox.getItems().add("Take home Quiz");
        typeChoiceBox.getItems().add("Group Interview");
        for (String interview : coordinatorPostController.getInterviewTypes()) {
            interviewListView.getItems().add(interview);
        }
    }
}
