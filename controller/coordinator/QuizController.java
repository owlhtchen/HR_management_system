package controller.coordinator;

import controller.Controller;
import controller.MainApp;
import controller.MessageGenerator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import model.interview.InterviewRound;
import model.application.Application;
import model.notification.Message;
import model.interview.QuizInterview;
import model.user.Coordinator;
import model.user.Interviewer;
import model.user.User;
import view.CreateInterview;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class QuizController implements Controller, Initializable {

    private MainApp mainApp;
    private Coordinator coordinator;
    private InterviewRound interviewRound;
    private CreateInterview createInterviewHelper;

    @FXML
    private ChoiceBox<Interviewer> InterviewerChoiceBox;


    @FXML
    private Button cancelButton;

    @FXML
    private ChoiceBox<Integer> numEmployeePassed;

    @FXML
    void cancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void createInterview() {
        MessageGenerator messageGenerator = new MessageGenerator();
        if (InterviewerChoiceBox.getValue() != null && numEmployeePassed.getValue() != null) {
            Interviewer interviewer = InterviewerChoiceBox.getValue();
            ArrayList<Interviewer> interviewers = new ArrayList<>();
            interviewers.add(interviewer);
            ArrayList<Application> applications = new ArrayList<>(interviewRound.getApplications());
            QuizInterview quiz = new QuizInterview(applications, interviewers, numEmployeePassed.getValue());
            interviewRound.addInterview(quiz);
            interviewRound.removeApplications(quiz.getApplications());

            notifyInterviewer(quiz, interviewer);
            for (Application application : quiz.getApplications()) {
                quiz.addObserver(application.getApplicant().getMailBox());
                Message messageForApplicant = messageGenerator.generateMessageForApplicantForQuiz(mainApp.getHrSystem().getTime(), quiz, application);
                quiz.sendMessage(messageForApplicant);
            }
            quiz.addObserver(interviewer.getMailBox());
            Message messageForInterviewer = messageGenerator.generateMessageForInterviewerForQuiz(mainApp.getHrSystem().getTime(), quiz);
            quiz.sendMessage(messageForInterviewer);
            cancel();
        }
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
        createInterviewHelper.setQuizController(this);
        createInterviewHelper.setInterviewRound(interviewRound);
        createInterviewHelper.loadUsers(InterviewerChoiceBox, null);
        createInterviewHelper.loadNumPassed(numEmployeePassed);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void notifyInterviewer(QuizInterview interview, Interviewer interviewer) {
        interview.accept(interviewer);
    }

    public void setInterviewRound(InterviewRound interviewRound) {
        this.interviewRound = interviewRound;
    }


}
