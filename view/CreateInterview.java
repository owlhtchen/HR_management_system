package view;

import controller.*;
import controller.coordinator.CreateGroupInterviewController;
import controller.coordinator.OneToOneInterviewController;
import controller.coordinator.QuizController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import model.interview.InterviewRound;
import model.application.Application;
import model.user.Coordinator;
import model.user.Interviewer;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateInterview {

    private OneToOneInterviewController oneToOneInterviewController;
    private CreateGroupInterviewController createGroupInterviewController;
    private QuizController quizController;

    private MainApp mainApp;
    private Coordinator coordinator;
    private InterviewRound interviewRound;

    public void setInterviewRound(InterviewRound interviewRound) {
        this.interviewRound = interviewRound;
    }

    public void setOneToOneInterviewController(OneToOneInterviewController oneToOneInterviewController) {
        this.oneToOneInterviewController = oneToOneInterviewController;
    }

    public void setGroupInterviewController(CreateGroupInterviewController createGroupInterviewController){
        this.createGroupInterviewController = createGroupInterviewController;
    }

    public void setQuizController(QuizController quizController) {
        this.quizController = quizController;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }


    public void loadUsers(ChoiceBox InterviewerChoiceBox, ChoiceBox ApplicantChoiceBox) {
        if (InterviewerChoiceBox != null) {
            InterviewerChoiceBox.getItems().clear();
            ObservableList<Interviewer> interviewers = FXCollections.observableArrayList();
            interviewers.addAll(coordinator.getCompany().getInterviewers());
            InterviewerChoiceBox.setItems(interviewers);
        }
        if (ApplicantChoiceBox != null) {
            ApplicantChoiceBox.getItems().clear();
            ObservableList<Application> applications = FXCollections.observableArrayList();
            applications.addAll(interviewRound.getApplications());
            ApplicantChoiceBox.setItems(applications);
        }
    }

    public void loadDate(ChoiceBox yearChoiceBox, ChoiceBox monthChoiceBox, ChoiceBox dayChoiceBox) {
        Date date = mainApp.getHrSystem().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Integer year = calendar.get(calendar.YEAR);
        for (int i = 0; i <= 5; i++) {
            yearChoiceBox.getItems().add(year + i);
        }
        for (int j = 1; j <= 12; j++) {
            monthChoiceBox.getItems().add(j);
        }
        for (int m = 1; m <= 31; m++) {
            dayChoiceBox.getItems().add(m);
        }
    }

    public void loadNumRequired(List list, ChoiceBox<Integer> box){
        box.getItems().clear();
        int temp = list.size();
        for(int i = 1; i<= temp; i++){
            box.getItems().add(i);
        }
    }

    public void loadNumPassed(ChoiceBox<Integer> numEmployeePassed){
        for (int i = 1; i <= interviewRound.getApplications().size(); i++){
            numEmployeePassed.getItems().add(i);
        }

    }








}
