package controller.applicant;

import controller.Controller;
import controller.MainApp;
import model.application.Application;
import model.application.ApplicationStatus;
import model.notification.MailBox;
import model.notification.Message;
import model.post.PostStatus;
import model.user.Applicant;
import model.user.Referee;
import model.user.User;
import view.ApplicantFunctions;
import view.VMailbox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;

public class ApplicantController implements Controller, Initializable {

    private MainApp mainApp;
    private Applicant applicant;
    private ApplicantFunctions applicantFunctions;

    @FXML
    private AnchorPane displayPane;

    @FXML
    private Text displayTitleText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        applicantFunctions = new ApplicantFunctions();
        applicantFunctions.setApplicantController(this);

    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @Override
    public void setUser(User user) {
        this.applicant = (Applicant) user;
    }

    @Override
    public void loadData() {
        deleteExpiredFiles();
    }

    public MainApp getMainApp() {
        return mainApp;
    }

    public Applicant getApplicant() {
        return applicant;
    }


    @FXML
    void displayPosting() {
        applicantFunctions.displayPosting(displayPane, displayTitleText);
    }

    @FXML
    void signOut() {
        mainApp.setScene("/view/resource/SignIn.fxml", null);
    }

    @FXML
    void viewMyApplication() {
        applicantFunctions.viewMyApplication(displayPane, displayTitleText);

    }

    public void withdrawApplication(Application application){
        if (!mainApp.getHrSystem().getPostManager().getPost(application.getPostID()).
                getStatus().equals(PostStatus.FILLED)) {
            application.setStatus(ApplicationStatus.CLOSED);
            application.setDateClosed(mainApp.getHrSystem().getTime());
            for (Referee referee : application.getReferenceMap().keySet()) {
                referee.getIncompleteApplications().remove(application);
                referee.getCompleteApplications().add(application);
            }
            this.applicantFunctions.popUpWindowWithdrawApplication();
            applicantFunctions.viewMyApplication(displayPane, displayTitleText);
        }
    }


    @FXML
    void viewMyInbox() {
        VMailbox vMailbox = new VMailbox();
        MailBox mailBox = this.applicant.getMailBox();
        List<Message> messages;
        if(mailBox != null) {
            messages = mailBox.getMessages();
        } else {
            messages = new ArrayList<>();
        }
        vMailbox.displayMailbox(messages);
    }

    @FXML
    void viewProfile() {
        applicantFunctions.viewProfile(displayPane, displayTitleText);
    }


    private void deleteExpiredFiles(){
        Date lastApplicationClosedDate = getlastApplicationClosedDate();
        if (lastApplicationClosedDate != null && testExpired(lastApplicationClosedDate)){
            this.applicant.getListOfCoverLetter().clear();
            this.applicant.getListOfCV().clear();
        }
    }

    private boolean testExpired(Date lastApplicationClosedDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(lastApplicationClosedDate);
        cal.add(Calendar.DATE, 30);
        return mainApp.getHrSystem().getTime().after(cal.getTime());
    }

    private Date getlastApplicationClosedDate(){
        Date lastApplicationClosedDate = null;
        for (Application application: applicant.getApplications()){
            if (lastApplicationClosedDate == null){
                lastApplicationClosedDate = application.getDateClosed();
            } else if (application.getDateClosed() != null && lastApplicationClosedDate != null){
                if(application.getDateClosed().after(lastApplicationClosedDate)){
                    lastApplicationClosedDate = application.getDateClosed();
                }
            }
        }
        return lastApplicationClosedDate;
    }


}
