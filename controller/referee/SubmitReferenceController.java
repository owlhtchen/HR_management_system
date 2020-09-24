package controller.referee;

import controller.Controller;
import controller.MainApp;
import controller.UploadFileController;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.application.Application;
import model.application.Reference;
import model.notification.Message;
import model.post.*;
import model.user.Referee;
import model.user.User;

import java.util.Date;

public class SubmitReferenceController implements Controller {

    private MainApp mainApp;
    private Referee referee;
    private Application application;
    private String status;

    @FXML
    private Text usernameField;

    @FXML
    private Text postIDField;

    @FXML
    private Text statusField;

    @FXML
    private Text dueDateField;

    @FXML
    private AnchorPane pane;

    @FXML
    private TextField contentTextField;

    @FXML
    private TextField titleTextField;

    @FXML
    private Button submitButton;

    @FXML
    private Button cancelButton;

    @Override
    public void setMainApp(MainApp main){
        this.mainApp = main;
    }

    @Override
    public void setUser(User user) {
        this.referee = (Referee) user;
    }

    @Override
    public void loadData() {
        usernameField.setText(application.getApplicant().getUsername());
        postIDField.setText(String.valueOf(application.getPostID()));
        statusField.setText(application.getStatus().getStatus());
        dueDateField.setText(findPost(application.getPostID()).getDeadlineDateString());
        if (status.equals("complete")){
            submitButton.setVisible(false);
            titleTextField.setText(application.getReferenceMap().get(referee).getTitle());
            titleTextField.setEditable(false);
            contentTextField.setText(application.getReferenceMap().get(referee).getContent());
            contentTextField.setEditable(false);
        }
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @FXML
    void cancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void submit() {
        String title = titleTextField.getText();
        String content = contentTextField.getText();
        saveFile(title,content);
    }

    @FXML
    void upload(){
        UploadFileController up = new UploadFileController(pane);
        up.pickFile();
        saveFile(up.getTitle(),up.getContent());
    }

    void saveFile(String title,String content){
        if (!title.equals("") && !content.equals("")){

            Reference reference = new Reference(title, content, referee);
            application.getReferenceMap().put(referee, reference);
            referee.getIncompleteApplications().remove(application);
            referee.getCompleteApplications().add(application);
            application.sendMessage(generateMessage(application));
            showAlertBox(true);
            cancel();
        }
        else{
            showAlertBox(false);
        }
    }


    private Message generateMessage(Application application){
        Date date = mainApp.getHrSystem().getTime();
        String content;
        content = "Your request of professional reference from " + referee.getUsername() + " for post number " + application.getPostID() + " has been completed.";
        return new Message(date, "Professional reference submitted", content, application.getApplicant().getUsername());
    }

    private Post findPost(Integer postID){
        for (Post post: mainApp.getHrSystem().getPostManager()){
            if (post.getPostID().equals(postID)){
                return post;
            }
        }
        return null;
    }

    public void showAlertBox(boolean result){
        Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Window");
        window.setMinWidth(250);

        Label label = new Label();
        if (result){
            label.setText("Successfully submitted your reference letter.");
        }
        else{
            label.setText("Failed to submit your reference letter. ");
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
