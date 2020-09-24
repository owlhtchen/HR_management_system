package controller.interviewer;

import controller.MainApp;
import javafx.scene.control.TextField;
import model.application.Application;
import model.interview.NormalInterview;
import model.interview.QuizInterview;
import model.notification.MailBox;
import model.notification.Message;
import model.user.Interviewer;
import model.user.User;
import view.VInterviewer;
import view.VMailbox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.interview.Interview;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InterviewerController implements controller.Controller{
    private MainApp mainApp;
    private Interviewer interviewer;
    private view.VInterviewer vInterviewer;
    private Interview selectedInterview;
    private NormalInterview selectedNormalInterview;
    private QuizInterview selectedQuizInterview;
    private boolean showComplete;
    private TableView<Application> applicationInfo;

    @FXML
    private TableView<Interview> interviewDate;

    @FXML
    private TableColumn<Interview, Date> interviewDateCol;

    @FXML
    private TableColumn<Interview, String> interviewType;

    @FXML
    private Button recommendBtn;

    @FXML
    private Button unRecommendBtn;

    @FXML
    private Text applicationInfoLabel;

    @FXML
    private VBox vboxPane;

    @FXML
    private TextField score;

    @FXML
    private Button scoreBtn;

    @FXML
    private Text scorePrompt;

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @Override
    public void setUser(User user) {
        this.interviewer = (Interviewer) user;
    }

    @Override
    public void loadData() {

    }

    @FXML
    public void initialize() {
        vInterviewer = new VInterviewer();
        vInterviewer.setInvisible(recommendBtn, unRecommendBtn, scoreBtn, score, scorePrompt);
        applicationInfoLabel.setVisible(false);
    }

    @FXML
    void showIncompleteInterviews(ActionEvent event) {
        vInterviewer.setInvisible(recommendBtn, unRecommendBtn, scoreBtn, score, scorePrompt);

        this.showComplete = false;
        vInterviewer.displayInterviews(
                interviewDate, interviewType, interviewDateCol, interviewer.getIncompleteInterviews());
    }

    @FXML
    void showInterviewHistory(ActionEvent event) {
        vInterviewer.setInvisible(recommendBtn, unRecommendBtn, scoreBtn, score, scorePrompt);

        this.showComplete = true;
        vInterviewer.displayInterviews(
                interviewDate, interviewType, interviewDateCol, interviewer.getCompleteInterviews());
    }

    public void getInterviewDetail() {
        vInterviewer.setInvisible(recommendBtn, unRecommendBtn, scoreBtn, score, scorePrompt);

        Interview interview = interviewDate.getSelectionModel().getSelectedItem();
        if(interview != null) {
            this.selectedInterview = interview;
            if(interview instanceof NormalInterview) {
                this.selectedNormalInterview = (NormalInterview) interview;
                vInterviewer.setNormalInterviewSelected(true);
                vInterviewer.setApplicationLabel(applicationInfoLabel, showComplete);
                vInterviewer.setButtonsVisibility(recommendBtn, unRecommendBtn, showComplete);
            } else {
                // interview instanceof QuizInterview
                this.selectedQuizInterview = (QuizInterview) interview;
                vInterviewer.setNormalInterviewSelected(false);
                vInterviewer.setSelectedQuizInterview(this.selectedQuizInterview);
                vInterviewer.setScoreVisibility(score, scoreBtn, scorePrompt,showComplete);
            }
            if(showComplete) {
                applicationInfo = vInterviewer.displayVBox(
                        vboxPane, interview.getApplications());
            } else {
                applicationInfo = vInterviewer.displayVBox(
                        vboxPane, this.getIncompleteApplications());
            }
        }
    }

    @FXML
    void showMailbox() {
        VMailbox vMailbox = new VMailbox();
        MailBox mailBox = this.interviewer.getMailBox();
        List<Message> messages;
        if(mailBox != null) {
            messages = mailBox.getMessages();
        } else {
            messages = new ArrayList<>();
        }
        vMailbox.displayMailbox(messages);
    }


    @FXML
    void signOut() {
        mainApp.setScene("/view/resource/SignIn.fxml", null);
    }


    @FXML
    void unRecommendApplication() {
        Application application = applicationInfo.getSelectionModel().getSelectedItem();
        if(this.selectedNormalInterview != null && application != null) {
            unrecommendApplication(application);
        }
    }

    @FXML
    void recommendApplication() {
        Application application = applicationInfo.getSelectionModel().getSelectedItem();
        if(this.selectedNormalInterview != null && application != null) {
            recommendApplication(application);
        }
    }

    @FXML
    void setScore() {
        Application application = applicationInfo.getSelectionModel().getSelectedItem();
        String scoreText = score.getText();
        if(this.selectedQuizInterview != null && application != null && scoreText != null && !scoreText.isEmpty()) {
            Integer score = Integer.parseInt(scoreText);
            if(score < 0 || score > 100) {
                return;
            }
            selectedQuizInterview.setMark(application, score);
            selectedQuizInterview.addInterviewerToApplication(interviewer, application);
            reDisplay();
        }
    }

    public void recommendApplication(Application application) {
        selectedNormalInterview.incrementMapElement(application);
        selectedNormalInterview.addInterviewerToApplication(interviewer, application);
        reDisplay();
    }

    public void unrecommendApplication(Application application) {
        selectedNormalInterview.incrementInterviewTime(application);
        selectedNormalInterview.addInterviewerToApplication(interviewer, application);
        reDisplay();
    }

    private void reDisplay() {
        boolean allApplicationDone = true;
        for(Application application: selectedInterview.getApplications()) {
            if(!selectedInterview.getApplicationInterviewers().get(application).contains(interviewer)) {
                allApplicationDone = false;
            }
        }
        if(allApplicationDone) {
            interviewer.addCompleteInterview(selectedInterview);
            interviewer.removeIncompleteInterview(selectedInterview);
            vInterviewer.clearInterviewDateTable(interviewDate);
            vInterviewer.clearApplicationInfoTable(applicationInfo);
        } else {
            vInterviewer.clearApplicationInfoTable(applicationInfo);
            vInterviewer.displayApplicationsDetail(applicationInfo, this.getIncompleteApplications());
        }
    }

    private List<Application> getIncompleteApplications() {
        List<Application> incomplete = new ArrayList<>();
        for(Application application: selectedInterview.getApplications()) {
            if(!selectedInterview.getApplicationInterviewers().get(application).contains(interviewer)) {
                incomplete.add(application);
            }
        }
        return incomplete;
    }
}


