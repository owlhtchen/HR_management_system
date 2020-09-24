package view;

import controller.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.user.Applicant;
import model.application.CV;
import model.application.CoverLetter;
import model.user.User;

public class ApplicantApply {

    private MainApp mainApp;
    private Applicant applicant;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setUser(User user) {
        this.applicant = (Applicant) user;
    }

    public void displayProfile(ChoiceBox cvChoiceBox, ChoiceBox coverLetterChoiceBox){
        coverLetterChoiceBox.getItems().clear();
        cvChoiceBox.getItems().clear();
        ObservableList<CoverLetter> coverLetterdata = FXCollections.observableArrayList();
        coverLetterdata.addAll(applicant.getListOfCoverLetter());
        coverLetterChoiceBox.setItems(coverLetterdata);
        ObservableList<CV> cvdata = FXCollections.observableArrayList();
        cvdata.addAll(applicant.getListOfCV());
        cvChoiceBox.setItems(cvdata);
    }

    public void showAlertBox(boolean result){
        Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Window");
        window.setMinWidth(250);

        Label label = new Label();
        if (result){
            label.setText("Successfully submitted your application.");
        }
        else{
            label.setText("Failed to submit your application. ");
        }

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

    }

}
