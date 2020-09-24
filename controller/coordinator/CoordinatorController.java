package controller.coordinator;

import controller.Controller;
import controller.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.container.Company;
import model.notification.MailBox;
import model.notification.Message;
import model.user.Coordinator;
import model.user.User;
import view.VMailbox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CoordinatorController implements Controller {

    private MainApp mainApp;
    private Coordinator coordinator;

    @FXML
    private Label username;

    @FXML
    private Label company;

    @FXML
    private Label locationLabel;

    @FXML
    void checkPosts() {
        mainApp.setScene("/view/resource/CoordinatorCheckPosts.fxml", coordinator);
    }

    @FXML
    void createPost() {
        mainApp.setScene("/view/resource/CoordinatorPost.fxml", coordinator);
    }

    @FXML
    void manageInterviews() {
        mainApp.setScene("/view/resource/CoordinatorViewPost.fxml", coordinator);

    }

    @FXML
    void signOut() {
        mainApp.setScene("/view/resource/SignIn.fxml", null);

    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @Override
    public void setUser(User user) {
        this.coordinator = (Coordinator) user;
    }

    @Override
    public void loadData() {
        username.setText("Username: " + coordinator.getUsername());

        Company comp = coordinator.getCompany();
        company.setText("Company: " + comp.getCompanyName());

        String loc = "";
        for (Map.Entry<String, List<Coordinator>> entry : comp.getMapLocationCoordinators().entrySet()) {
            if (entry.getValue().contains(coordinator)) {
                loc = entry.getKey();
            }
        }
        locationLabel.setText("Location: " + loc);
    }

    @FXML
    void showMailbox(ActionEvent event) {
        VMailbox vMailbox = new VMailbox();
        MailBox mailBox = this.coordinator.getMailBox();
        List<Message> messages;
        if(mailBox != null) {
            messages = mailBox.getMessages();
        } else {
            messages = new ArrayList<>();
        }
        vMailbox.displayMailbox(messages);
    }
}
