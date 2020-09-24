package controller.administrator;

import controller.Controller;
import controller.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.notification.Message;
import model.user.Administrator;
import model.user.Interviewer;
import model.user.User;

import java.net.URL;
import java.util.ResourceBundle;

public class UnregisteredInterviewerController implements Controller, Initializable {

    @FXML
    private ListView<Interviewer> interviewerListView;

    private MainApp mainApp;
    private Administrator administrator;

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }



    @Override
    public void setUser(User user) {
        this.administrator = (Administrator) user;
    }

    @Override
    public void loadData() {
        ObservableList<Interviewer> interviewers = FXCollections.observableArrayList();
        interviewers.addAll(administrator.getUnRegisteredInterviewers());
        interviewerListView.setItems(interviewers);
    }

    public MainApp getMainApp() {
        return mainApp;
    }

    public Administrator getAdminister() {
        return administrator;
    }

    @FXML
    public void back(ActionEvent event) {
        mainApp.setScene("/view/resource/Administer.fxml", administrator);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void accept() {
        if (interviewerListView.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        Interviewer interviewer = interviewerListView.getSelectionModel().getSelectedItem();
        mainApp.getHrSystem().getUserManager().addUser(interviewer);
        Message message = new Message(mainApp.getHrSystem().getTime(), "Registered",
                "You are now registered interviewer ", administrator.getUsername());
        interviewer.getMailBox().getMessages().add(message);
        administrator.removeUnregisteredInterviewer(interviewer);
        administrator.getCompany().addInterviewer(interviewer);
        loadData();
    }
}

