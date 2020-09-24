package controller.coordinator;

import controller.Controller;
import controller.MainApp;
import controller.MessageGenerator;
import model.application.Application;
import model.interview.Interview;
import model.interview.NormalInterview;
import model.notification.Message;
import model.user.Coordinator;
import model.user.Interviewer;
import model.user.User;
import view.CreateInterview;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import model.interview.InterviewRound;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class OneToOneInterviewController implements Controller, Initializable {

    private MainApp mainApp;
    private Coordinator coordinator;
    private InterviewRound interviewRound;
    private CreateInterview createInterviewHelper;


    @FXML
    private ChoiceBox<?> yeatChoiceBox;

    @FXML
    private ChoiceBox<?> monthChoiceBox;

    @FXML
    private ChoiceBox<?> dayChoiceBox;

    @FXML
    private ChoiceBox InterviewerChoiceBox;

    @FXML
    private Button cancelButton;

    @FXML
    private ChoiceBox ApplicantChoiceBox;

    @FXML
    void cancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void setInterviewRound(InterviewRound interviewRound) {
        this.interviewRound = interviewRound;
    }

    @FXML
    void createInterview() {
        try{
            Integer year = (Integer) yeatChoiceBox.getValue();
            Integer month = (Integer) monthChoiceBox.getValue();
            Integer day = (Integer) dayChoiceBox.getValue();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(day + "/" + month + "/" + year);
            if (InterviewerChoiceBox.getValue() != null && ApplicantChoiceBox.getValue() != null){
                MessageGenerator messageGenerator = new MessageGenerator();
                Interviewer interviewer = (Interviewer) InterviewerChoiceBox.getValue();
                Application application = (Application) ApplicantChoiceBox.getValue();
                ArrayList<Interviewer> interviewers = new ArrayList<>();
                interviewers.add(interviewer);
                ArrayList<Application> applications = new ArrayList<>();
                applications.add(application);
                NormalInterview interview = new NormalInterview(applications, interviewers, date, 1);
                interviewRound.addInterview(interview);
                interviewRound.removeApplications(applications);

                notifyInterviewer(interview, interviewer);
                interview.addObserver(application.getApplicant().getMailBox());
                interview.addObserver(interviewer.getMailBox());
                Message messageForInterviewer = messageGenerator.generateMessageForInterviewerForOneToOne(mainApp.getHrSystem().getTime(), interview, application);
                Message messageForApplicant = messageGenerator.generateMessageForApplicantForOneToOne(mainApp.getHrSystem().getTime(), interview, application);
                interview.sendMessage(messageForApplicant);
                interview.sendMessage(messageForInterviewer);
                cancel();
            }
        }
        catch (ParseException e){
            System.out.println("wrong date");
        }
    }


    public void notifyInterviewer(Interview interview, Interviewer interviewer){
        interview.accept(interviewer);
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
        this.createInterviewHelper = new CreateInterview();
        createInterviewHelper.setCoordinator(coordinator);
        createInterviewHelper.setMainApp(mainApp);
        createInterviewHelper.setOneToOneInterviewController(this);
        createInterviewHelper.setInterviewRound(interviewRound);
        createInterviewHelper.loadUsers(InterviewerChoiceBox, ApplicantChoiceBox);
        createInterviewHelper.loadDate(yeatChoiceBox, monthChoiceBox, dayChoiceBox);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
