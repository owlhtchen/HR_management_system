package controller.administrator;

import controller.Controller;
import controller.MainApp;
import controller.MessageGenerator;
import controller.UploadFileController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.application.CV;
import model.application.CoverLetter;
import model.application.File;
import model.notification.Message;
import model.user.*;

public class AdministratorPostAnnouncementController implements Controller {

    private MainApp mainApp;
    private Administrator administrator;

    @FXML
    private AnchorPane pane;

    @FXML
    private TextField titleTextField;

    @FXML
    private TextArea contentTextField;

    @FXML
    private Button cancelButton;

    @FXML
    private Text alertText;

    @FXML
    void cancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void submit() {
        MessageGenerator messageGenerator = new MessageGenerator();
        String title = titleTextField.getText();
        String content = contentTextField.getText();
        if (title.equals("") || content.equals("")){
            alertText.setText("Please fill the title and content");
        }
        else{
            File file = new File(title, content);
            for (User user: administrator.getCompany()){
                file.addObserver(user.getMailBox());
                Message message = messageGenerator.generateAnnouncement(mainApp.getHrSystem().getTime(), file, user);
                file.sendMessage(message);
            }
            cancel();
        }

    }

    @FXML
    void upload() {
        UploadFileController up = new UploadFileController(pane);
        up.pickFile();
        if(up.getTitle()!= null && up.getContent()!= null) {
            titleTextField.setText(up.getTitle());
            contentTextField.setText(up.getContent());}
    }

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

    }
}
