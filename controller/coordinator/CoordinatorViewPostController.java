package controller.coordinator;

import controller.Controller;
import controller.MainApp;
import javafx.event.ActionEvent;
import model.user.Coordinator;
import model.user.User;
import view.CoordinatorViewPost;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import model.post.*;

import java.net.URL;
import java.util.ResourceBundle;

public class CoordinatorViewPostController implements Controller, Initializable {

    @FXML
    private ListView<Post> displayOfPosts;

    @FXML
    private TabPane displayOfInterviews;


    @FXML
    private Label currentRoundLabel;
    private CoordinatorViewPost coordinatorViewPost;

    private MainApp mainApp;
    private Coordinator coordinator;

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
        coordinatorViewPost.displayPosts(displayOfPosts);
    }

    public MainApp getMainApp() {
        return mainApp;
    }

    public Coordinator getCoordinator() {
        return coordinator;
    }

    @FXML
    public void updateTabPane() {
        displayOfInterviews.getTabs().clear();
        coordinatorViewPost.displayInterviews(displayOfInterviews);
    }


    @FXML
    public void back() {
        mainApp.setScene("/view/resource/Coordinator.fxml", coordinator);

    }

    @FXML
    public void changeCurrentRoundText() {
        Post post = displayOfPosts.getSelectionModel().getSelectedItem();
        if(post.getCurrInterviewRound() == null){
            currentRoundLabel.setText("post has not closed.");
        }else{
            currentRoundLabel.setText(post.getCurrInterviewRound().getType());
        }
    }


    public void createInterview(){
        Post temp = coordinatorViewPost.getSelectedPost();
        if(temp != null && !(temp.getStatus().equals(PostStatus.FILLED) || temp.getStatus().equals(PostStatus.OPEN))){
            InterviewFactory interviewFactory = new InterviewFactory(this);
            interviewFactory.createInterview(temp.getCurrInterviewRound(), coordinator, mainApp);
        }
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        coordinatorViewPost = new CoordinatorViewPost();
        coordinatorViewPost.setController(this);
    }
}

