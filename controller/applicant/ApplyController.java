package controller.applicant;

import controller.Controller;
import controller.MainApp;
import controller.SignInController;
import controller.SignUpController;
import model.application.Application;
import model.application.CV;
import model.application.CoverLetter;
import model.application.Reference;
import model.notification.Message;
import model.user.Applicant;
import model.user.Referee;
import model.user.User;
import model.user.UserFactory;
import view.ApplicantApply;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.post.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApplyController implements Controller, Initializable{

    private MainApp mainApp;
    private Applicant applicant;
    private Post post;
    private ApplicantApply applicantApply;
    private ArrayList<String> emails = new ArrayList<>();

    @FXML
    private Button ExitButton;

    @FXML
    private ChoiceBox<CV> cvChoiceBox;

    @FXML
    private ChoiceBox<CoverLetter> coverLetterChoiceBox;

    @FXML
    private ListView referenceListView;

    @FXML
    private TextField emailAddressTextField;

    @FXML
    private Text numberText;

    @FXML
    private Label incorrectNotice;

    @FXML
    void AddEmailAddress() {
        if (emailAddressTextField.getText() != null && checkEmailFormat(emailAddressTextField.getText())
                && !emails.contains(emailAddressTextField.getText())) {
            emails.add(emailAddressTextField.getText());
            updateEmailList();
            incorrectNotice.setText("");
        } else {
            incorrectNotice.setText("Incorrect format of Email address");
        }


    }

    private boolean checkEmailFormat(String email) {
        // check email address has right format
        String pattern = "^([a-zA-Z0-9_\\-.]+)@([a-zA-Z0-9_\\-.]+)\\.([a-zA-Z]{2,5})$";
        Pattern r = Pattern.compile(pattern);
        // Now create matcher object.
        Matcher m = r.matcher(email);
        return m.find();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void displayProfile(){
        applicantApply.displayProfile(cvChoiceBox, coverLetterChoiceBox);
    }

    @FXML
    void apply() {
        SignUpController signUpController = new SignUpController();
        signUpController.setMainApp(mainApp);
        SignInController signInController = new SignInController();
        signInController.setMainApp(mainApp);

        CoverLetter coverLetter = coverLetterChoiceBox.getValue();
        CV cv = cvChoiceBox.getValue();
        boolean eligible = !(coverLetter == null || cv == null) && post.getJob().getNumRef().equals(referenceListView.getItems().size());
        if (testValidApplicant() && testValidPost() && eligible){
            Map<Referee, Reference> referenceMap = new HashMap<Referee, Reference>();
            for (String email: emails){
                Referee referee;
                if (signUpController.verifyValidUsername(email)){
                    UserFactory userFactory = new UserFactory();
                    referee = (Referee) userFactory.getUser("referee", email, "default", null);
                    mainApp.getHrSystem().getUserManager().addUser(referee);
                }
                else {
                    referee = (Referee) mainApp.getHrSystem().getUserManager().getUser(email);
                }
                referenceMap.put(referee, null);
            }
            Date date = mainApp.getHrSystem().getTime();
            Application application = new Application(applicant, date, cv, coverLetter, post.getPostID(), referenceMap);
            notifyReferee(application);
            application.addObserver(application.getApplicant().getMailBox());
            for (Referee referee: referenceMap.keySet()){
                application.addObserver(referee.getMailBox());
                Message message = generateMessageForReferee(application, referee);
                application.sendMessage(message);
            }
            applicant.addApplication(application);
            post.addApplication(application);
            applicantApply.showAlertBox(true);
        }
        else{
            applicantApply.showAlertBox(false);
        }
        exit();
    }

    public Message generateMessageForReferee(Application application, Referee referee){
        Date date = mainApp.getHrSystem().getTime();
        String content;
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        content = "You have been inivited to write professional reference for " + application.getApplicant().getUsername() + ".";
        content += "\nThe deadline is " + formatter.format(post.getDeadlineDate());
        return new Message(date, "Professional reference invitation", content, referee.getUsername());
    }

    @FXML
    void exit() {
        Stage stage = (Stage) ExitButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void setMainApp(MainApp main){
        this.mainApp = main;
    }

    @Override
    public void setUser(User user) {
        this.applicant = (Applicant) user;

    }

    @Override
    public void loadData() {
        this.applicantApply = new ApplicantApply();
        applicantApply.setMainApp(mainApp);
        applicantApply.setUser(applicant);
        displayProfile();
        this.numberText.setText(String.valueOf(post.getJob().getNumRef()));
    }

    public void setPost(Post post){
        this.post = post;
    }


    public boolean testValidApplicant(){
        for (Application application: applicant.getApplications()){
            if (application.getPostID().equals(post.getPostID())){
                return false;
            }
        }
        return true;
    }

    public boolean testValidPost(){
        return post.getDateClosed().after(mainApp.getHrSystem().getTime());
    }

    public void updateEmailList(){
        referenceListView.getItems().clear();
        ObservableList<String> emailData = FXCollections.observableArrayList(emails);
        referenceListView.setItems(emailData);
    }

    public void notifyReferee(Application application){
        for (Referee referee: application.getReferenceMap().keySet()){
            application.accept(referee);
        }

    }


}
