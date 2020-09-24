package controller.coordinator;

import controller.Controller;
import controller.MainApp;
import controller.MessageGenerator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.interview.InterviewRound;
import model.application.Application;
import model.notification.Message;
import model.interview.NormalInterview;
import model.user.Coordinator;
import model.user.Interviewer;
import model.user.User;
import view.CreateInterview;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class CreateGroupInterviewController implements Controller, Initializable {



    private MainApp mainApp;
    private Coordinator coordinator;
    private InterviewRound interviewRound;
    private CreateInterview createInterviewHelper;
    private List<Interviewer> chosenInterviewers;
    private List<Application> chosenApplications;


    @FXML
    private ListView<Interviewer> interviewerListView;

    @FXML
    private ListView<Application> applicationListView;

    @FXML
    private ChoiceBox<Application> applicationChoiceBox;

    @FXML
    private ChoiceBox<Interviewer> interviewerChoiceBox;

    @FXML
    private ChoiceBox<?> yearChoiceBox;

    @FXML
    private ChoiceBox<?> monthChoiceBox;

    @FXML
    private ChoiceBox<?> dayChoiceBox;

    @FXML
    private Button cancelButton;

    @FXML
    private ChoiceBox<Integer> numRequired;

    @FXML
    void cancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void setInterviewRound(InterviewRound interviewRound) {
        this.interviewRound = interviewRound;
    }

    public void addInterviewer(){
        Interviewer interviewer = interviewerChoiceBox.getValue();
        if( !chosenInterviewers.contains(interviewer)) {
            interviewerListView.getItems().add(interviewer);
            chosenInterviewers.add(interviewer);
            createInterviewHelper.loadNumRequired(chosenInterviewers, numRequired);
        }
    }

    public void removeInterviewer(){
        Interviewer temp = interviewerListView.getSelectionModel().getSelectedItem();
        if(temp != null){
            interviewerListView.getItems().remove(temp);
            chosenInterviewers.remove(temp);
            createInterviewHelper.loadNumRequired(chosenInterviewers, numRequired);
        }
    }

    public void addApplication(){
        Application temp = applicationChoiceBox.getValue();
        if( !chosenApplications.contains(temp)) {
            applicationListView.getItems().add(temp);
            chosenApplications.add(temp);
        }
    }

    public void removeApplication(){
        Application temp = applicationListView.getSelectionModel().getSelectedItem();
        if(temp != null){
            applicationListView.getItems().remove(temp);
            chosenApplications.remove(temp);
        }
    }

    public void createInterview() {
        try {
            Integer year = (Integer) yearChoiceBox.getValue();
            Integer month = (Integer) monthChoiceBox.getValue();
            Integer day = (Integer) dayChoiceBox.getValue();
            int num = numRequired.getValue();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(day + "/" + month + "/" + year);
            if (!chosenApplications.isEmpty() && !chosenInterviewers.isEmpty()) {
                MessageGenerator messageGenerator = new MessageGenerator();
                NormalInterview interview = new NormalInterview(chosenApplications, chosenInterviewers, date, num);
                interviewRound.addInterview(interview);
                interviewRound.removeApplications(chosenApplications);
                notifyInterviewer(interview, chosenInterviewers);
                for (Application application: chosenApplications) {
                    interview.addObserver(application.getApplicant().getMailBox());
                    Message messageForApplicant = messageGenerator.generateMessageForApplicantForGroupInterview(mainApp.getHrSystem().getTime(), interview, application);
                    interview.sendMessage(messageForApplicant);
                }
                for (Interviewer interviewer: chosenInterviewers) {
                    interview.addObserver(interviewer.getMailBox());
                    Message messageForInterviewer = messageGenerator.generateMessageForInterviewerForGroupInterview(mainApp.getHrSystem().getTime(), interview, interviewer);
                    interview.sendMessage(messageForInterviewer);
                }

                cancel();
            }
        }
        catch (ParseException e){
            System.out.println("wrong date");
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
        createInterviewHelper.setCoordinator(coordinator);
        createInterviewHelper.setMainApp(mainApp);
        createInterviewHelper.setInterviewRound(interviewRound);
        createInterviewHelper.setGroupInterviewController(this);
        createInterviewHelper.loadUsers(interviewerChoiceBox, applicationChoiceBox);
        createInterviewHelper.loadDate(yearChoiceBox, monthChoiceBox, dayChoiceBox);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chosenInterviewers = new ArrayList<>();
        chosenApplications = new ArrayList<>();
        this.createInterviewHelper = new CreateInterview();
    }


    public void notifyInterviewer(NormalInterview interview, List<Interviewer> interviewers){
        for (Interviewer interviewer: interviewers){
            interview.accept(interviewer);}
    }

}
