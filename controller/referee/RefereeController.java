package controller.referee;

import controller.Controller;
import controller.MainApp;
import model.notification.MailBox;
import model.notification.Message;
import model.user.Referee;
import model.user.User;
import view.RefereeFunctions;
import view.VMailbox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RefereeController implements Controller, Initializable {

    private MainApp mainApp;
    private Referee referee;
    private RefereeFunctions refereeFunctions;

    @FXML
    private Text nameField;

    @FXML
    private TableView applicationTable;

    @FXML
    private Text note;

    @FXML
    private Text tableTitle;

    @FXML
    void browseComplete() {
        refereeFunctions.displayApplications(applicationTable, tableTitle, note, "complete");
    }

    @FXML
    void browseIncomplete() {
        refereeFunctions.displayApplications(applicationTable, tableTitle, note, "incomplete");
    }

    @FXML
    void signOut() {
        mainApp.setScene("/view/resource/SignIn.fxml", null);
    }

    @FXML
    void viewInbox() {
        VMailbox vMailbox = new VMailbox();
        MailBox mailBox = this.referee.getMailBox();
        List<Message> messages;
        if(mailBox != null) {
            messages = mailBox.getMessages();
        } else {
            messages = new ArrayList<>();
        }
        vMailbox.displayMailbox(messages);
    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @Override
    public void setUser(User user) {
        this.referee = (Referee) user;
    }

    public MainApp getMainApp() {
        return mainApp;
    }

    public Referee getReferee() {
        return referee;
    }

    @Override
    public void loadData() {
        nameField.setText(referee.getUsername());
        browseIncomplete();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refereeFunctions = new RefereeFunctions();
        refereeFunctions.setRefereeController(this);
    }

}
