package controller.coordinator;

import controller.Controller;
import controller.MainApp;
import controller.MessageGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.interview.InterviewRound;
import model.application.Application;
import model.application.ApplicationStatus;
import model.post.*;
import model.interview.Interview;
import model.interview.NormalInterview;
import model.user.User;
import view.ViewNormalInterview;

public class ViewNormalInterviewController implements Controller {

    private Post post;
    private NormalInterview normalInterview;
    private MainApp mainApp;


    @FXML
    private ListView<Application> unrecommended;

    @FXML
    private Button hireButton;

    @FXML
    private ListView<Application> recommended;

    @FXML
    void back() {
        Stage stage = (Stage) hireButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void hire() {
        MessageGenerator messageGenerator = new MessageGenerator();
        int i = 0;
        Application application = recommended.getSelectionModel().getSelectedItem();
        if(application.getStatus() == ApplicationStatus.HIRED || post.getStatus() == PostStatus.FILLED)
            return;
        application.setStatus(ApplicationStatus.HIRED);
        application.setDateClosed(mainApp.getHrSystem().getTime());
        application.sendMessage(messageGenerator.generateMessageForHired(mainApp.getHrSystem().getTime(), application));
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
                        app.setDateClosed(mainApp.getHrSystem().getTime());
                        app.sendMessage(messageGenerator.generateMessageForRejected(mainApp.getHrSystem().getTime(), app));
                    }
                }
            }
        }
    }



    public void setNormalInterview(Interview interview){
        this.normalInterview = (NormalInterview) interview;
    }

    public void setPost(Post post){this.post = post;}


    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @Override
    public void setUser(User user) {

    }

    public NormalInterview getNormalInterview() {
        return normalInterview;
    }

    @Override
    public void loadData() {
        hireButton.setVisible(false);
        if(post.getStatus() == PostStatus.STUCK){
            hireButton.setVisible(true);
        }
        ViewNormalInterview viewNormalInterview = new ViewNormalInterview(this);
        viewNormalInterview.updateRecommendList(recommended);
        viewNormalInterview.updateUnrecommendList(unrecommended);
    }
}

