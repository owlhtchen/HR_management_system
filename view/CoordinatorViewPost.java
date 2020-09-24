package view;

import controller.*;
import controller.coordinator.CoordinatorViewPostController;
import controller.coordinator.ViewNormalInterviewController;
import controller.coordinator.ViewQuizInterviewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.interview.InterviewRound;
import model.post.*;
import model.interview.Interview;
import model.interview.NormalInterview;

import java.io.IOException;

public class CoordinatorViewPost {

    ListView displayOfPosts;
    Post selectedPost;
    Tab currentTab;
    public CoordinatorViewPostController controller;

    public void setController(CoordinatorViewPostController coordinatorViewPostController) {
        controller = coordinatorViewPostController;
    }

    public void displayPosts(ListView<Post> displayOfPosts){
        this.displayOfPosts = displayOfPosts;
        ObservableList<Post> posts = FXCollections.observableArrayList();
        posts.addAll(controller.getCoordinator().getPosts());
        displayOfPosts.setItems(posts);
    }

    public Post getSelectedPost(){
        if(displayOfPosts.getSelectionModel().getSelectedItem() == null){
            return null;
        }else
            return (Post) displayOfPosts.getSelectionModel().getSelectedItem();
    }

    public void displayInterviews(TabPane displayOfInterviews){
        if(displayOfPosts.getSelectionModel().getSelectedItem() != null){
            Post post = (Post) displayOfPosts.getSelectionModel().getSelectedItem();
            controller.changeCurrentRoundText();
            for(InterviewRound interviewRound: post.getInterviewRounds()){
                Tab tab = new Tab(interviewRound.getType());
                AnchorPane tabContent = new AnchorPane();
                generateTableView(interviewRound, tab);
                displayOfInterviews.getTabs().add(tab);
            }
        }
    }

    public void generateTableView(InterviewRound interviewRound, Tab tab){
        CoordinatorInterviewsTableViewFactory factory = new CoordinatorInterviewsTableViewFactory();
        AnchorPane pane = new AnchorPane();
        tab.setContent(pane);
        TableView<Interview> tableView = factory.getTableView(interviewRound);
        tableView.setMinWidth(400);
        tableView.setMaxSize(438, 350);
        pane.getChildren().add(tableView);
        tableView.setLayoutX(10);
        tableView.setLayoutY(10);
        tableView.setRowFactory(tv -> {
            TableRow<Interview> row = new TableRow<>();
            row.setOnMouseClicked(newevent -> {
                if (newevent.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Interview interview = row.getItem();
                    popUpWindow(interview);
                }
            });
            return row;
        });

    }



    private void popUpWindow(Interview interview){
        Stage window = new Stage();
        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                controller.updateTabPane();
            }
        });
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Details");
        try {
            FXMLLoader loader = new FXMLLoader();
            Pane layout;
            if(interview instanceof NormalInterview) {
                loader.setLocation(MainApp.class.getResource("/view/resource/ViewNormalInterview.fxml"));
                layout = loader.load();
                ViewNormalInterviewController controller = loader.getController();
                controller.setPost((Post) displayOfPosts.getSelectionModel().getSelectedItem());
                controller.setMainApp(this.controller.getMainApp());
                controller.setNormalInterview(interview);
                controller.loadData();
            }
            else {
                loader.setLocation(MainApp.class.getResource("/view/resource/ViewQuizInterview.fxml"));
                layout = loader.load();
                ViewQuizInterviewController controller = loader.getController();
                controller.setPost((Post) displayOfPosts.getSelectionModel().getSelectedItem());
                controller.setMainApp(this.controller.getMainApp());
                controller.setInterview(interview);
                controller.loadData();
            }

            Scene scene = new Scene(layout);
            window.setScene(scene);
            window.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
;
    }
}
