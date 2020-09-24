package controller.coordinator;

import controller.Controller;
import controller.MainApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import model.application.Application;
import model.application.Reference;
import model.user.Applicant;
import model.user.Coordinator;
import model.user.Referee;
import model.user.User;
import view.CoordinatorTableFiller;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.post.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class CoordinatorCheckPostsController implements Controller {

    private MainApp mainApp;
    private Coordinator coordinator;

    @FXML
    private TableView postTable;

    @FXML
    private TableView applicationTable;

    @FXML
    private Label info;

    @FXML
    private Label content;

    @FXML
    private Label applications;

    @FXML
    private Label postNote;

    @FXML
    private Label appNote;

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
        CoordinatorTableFiller coordinatorTableFiller = new CoordinatorTableFiller(postTable);
        coordinatorTableFiller.fillPosts((ArrayList) coordinator.getPosts());
        info.setText("Postings by " + coordinator.getUsername() + ", at "
                + coordinator.getCompany().getCompanyName());
    }

    public void postDetail() {
        try {
            Post post = (Post) postTable.getSelectionModel().getSelectedItem();
            content.setText("Content:\n" + post.getJob().getContent() + "\nTags: \n" + post.getJob().getTags());
            applications.setText("Applications under Post #" + post.getPostID());
            CoordinatorTableFiller coordinatorTableFiller = new CoordinatorTableFiller(applicationTable);
            coordinatorTableFiller.fillApplications((ArrayList) post.getApplications());
        } catch (NullPointerException w) {
            postNote.setText("Please select a Post.");
        }
    }

    public void applicationDetail() {
        try {
            Application application = (Application) applicationTable.getSelectionModel().getSelectedItem();

            Applicant applicant = application.getApplicant();
            String applied = "#" + application.getPostID();
            for (Post post : coordinator.getCompany().getPosts()) {
                for (Application app : applicant.getApplications()) {
                    if (!post.getPostID().equals(application.getPostID())
                            && post.getPostID().equals(app.getPostID())) {
                        applied += ", #" + post.getPostID();
                    }
                }
            }

            Stage stage = new Stage();
            stage.initModality(Modality.NONE);
            stage.initOwner(mainApp.getPrimaryStage());
            VBox vBox = new VBox(20);
            vBox.getChildren().add(new Text("Applicant: " + applicant +
                    "\n" + application.getCoverLetter() + "\n" + application.getCoverLetter().getContent() +
                    "\n" + application.getCv() + "\n" + application.getCv().getContent() +
                    "\nApplied to Post: " + applied));
            Scene scene = new Scene(vBox, 300, 200);
            stage.setScene(scene);
            stage.show();

        } catch (NullPointerException w) {
            appNote.setText("Please select an Application.");
        }
    }

    public void deleteApplication() {
        try {
            Application application = (Application) applicationTable.getSelectionModel().getSelectedItem();
            Post post;
            post = mainApp.getHrSystem().getPostManager().getPost(application.getPostID());
            post.getApplications().remove(application);
            // delete applicant submit
            application.getApplicant().getApplications().remove(application);
            // refresh the application table
            content.setText("Content:\n" + post.getJob().getContent() + "\nTags: \n" + post.getJob().getTags());
            applications.setText("Applications under Post #" + post.getPostID());
            CoordinatorTableFiller coordinatorTableFiller = new CoordinatorTableFiller(applicationTable);
            coordinatorTableFiller.fillApplications((ArrayList) post.getApplications());

        } catch (NullPointerException w) {
            appNote.setText("Please select an Application.");
        }
    }

    public void modifyPost() {
        try {
            Post post = (Post) postTable.getSelectionModel().getSelectedItem();
            if (!post.getStatus().equals(PostStatus.OPEN)) {
                postNote.setText("You may not modify a Post after it's CLOSED.");
            } else {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainApp.class.getResource("/view/resource/CoordinatorModifyPost.fxml"));
                AnchorPane layout = loader.load();
                CoordinatorModifyPostController controller = loader.getController();
                controller.setPost(post);
                controller.setMainApp(mainApp);
                controller.loadData();
                Scene scene = new Scene(layout);
                Stage window = new Stage();
                window.initModality(Modality.APPLICATION_MODAL);
                window.setTitle("Modify Post #" + post.getPostID());
                window.setScene(scene);
                window.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException w) {
            postNote.setText("Please select a Post.");
        }
    }

    public void back() {
        mainApp.setScene("/view/resource/Coordinator.fxml", coordinator);
    }
}
