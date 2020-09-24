package controller.coordinator;

import controller.Controller;
import controller.MainApp;
import controller.MessageGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.application.Application;
import model.application.ApplicationStatus;
import model.interview.InterviewRound;
import model.interview.QuizResult;
import model.post.*;
import model.interview.Interview;
import model.interview.QuizInterview;
import model.user.User;
import view.ViewQuizInterview;

import java.util.Map;

public class ViewQuizInterviewController implements Controller {

    private Post post;
    private QuizInterview quizInterview;
    private MainApp main;

    @FXML
    private Button hireButton;

    @FXML
    private TableView<QuizResult> resultTable;

    @FXML
    private TableColumn<QuizResult, Application> applicantColumn;

    @FXML
    private TableColumn<QuizResult, Integer> gradeColumn;

    @FXML
    void back() {
        Stage stage = (Stage) hireButton.getScene().getWindow();
        stage.close();

    }
    @FXML
    void hire() {
        MessageGenerator messageGenerator = new MessageGenerator();
        int i = 0;
        Application application = resultTable.getSelectionModel().getSelectedItem().getApplication();
        if(application.getStatus() == ApplicationStatus.HIRED || post.getStatus() == PostStatus.FILLED)
            return;
        application.setStatus(ApplicationStatus.HIRED);
        application.setDateClosed(main.getHrSystem().getTime());
        application.sendMessage(messageGenerator.generateMessageForHired(main.getHrSystem().getTime(), application));
        for(Interview interview: post.getCurrInterviewRound().getInterviews()){
            for(Application temp: interview.getApplications()){
                if(temp.getStatus() == ApplicationStatus.HIRED){
                    i ++;
                }
            }
        }
        if(i == post.getJob().getNumHiring()){
            post.setStatus(PostStatus.FILLED);
            for(Interview temp: post.getCurrInterviewRound().getInterviews()){
                // post is filled and all other applications should be set to rejected unless hired
                for(Application app: temp.getApplications()) {
                    if (app.getStatus() != ApplicationStatus.HIRED) {
                        app.setStatus(ApplicationStatus.REJECTED);
                        app.setDateClosed(main.getHrSystem().getTime());
                        app.sendMessage(messageGenerator.generateMessageForRejected(main.getHrSystem().getTime(), app));
                    }
                }
                }
            }
        }




    public void setPost(Post post) {
        this.post = post;
    }


    @Override
    public void setMainApp(MainApp mainApp) {
        this.main = mainApp;
    }

    @Override
    public void setUser(User user) {

    }

    public void setInterview(Interview interview){
        this.quizInterview = (QuizInterview) interview;
    }

    public Post getPost() {
        return post;
    }

    public QuizInterview getQuizInterview() {
        return quizInterview;
    }

    @Override
    public void loadData() {
        hireButton.setVisible(false);
        if(post.getStatus() == PostStatus.STUCK){
            hireButton.setVisible(true);
        }
        ViewQuizInterview viewQuizInterview = new ViewQuizInterview(this);
        viewQuizInterview.getTableView(resultTable, applicantColumn, gradeColumn);

    }



}
