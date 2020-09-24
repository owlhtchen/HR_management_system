package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.application.Application;
import model.post.Post;

import java.util.ArrayList;


public class CoordinatorTableFiller {

    private TableView table;

    public CoordinatorTableFiller(TableView table) {
        this.table = table;
    }

    public void fillPosts(ArrayList<Post> posts) {
        TableColumn<Post, Integer> idCol = new TableColumn<>("PostID");
        TableColumn<Post, String> titleCol = new TableColumn<>("Title");
        TableColumn<Post, String> datePostedCol = new TableColumn<>("DatePosted");
        TableColumn<Post, String> dateClosedCol = new TableColumn<>("DateClosed");
        TableColumn<Post, String> statusCol = new TableColumn<>("Status");
        table.getColumns().addAll(idCol, titleCol, dateClosedCol, datePostedCol, statusCol);

        ObservableList<Post> postData = FXCollections.observableArrayList(posts);
        idCol.setCellValueFactory(new PropertyValueFactory<>("postID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        datePostedCol.setCellValueFactory(new PropertyValueFactory<>("datePostedString"));
        dateClosedCol.setCellValueFactory(new PropertyValueFactory<>("dateClosedString"));
        //add ref info

        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        table.setItems(postData);
    }

    public void fillApplications(ArrayList<Application> applications) {
        table.getColumns().clear();

        TableColumn<Application, String> usernameCol = new TableColumn<>("Applicant");
        TableColumn<Application, String> dateAppliedCol = new TableColumn<>("DateApplied");
        TableColumn<Application, String> statusCol = new TableColumn<>("Status");
        table.getColumns().addAll(usernameCol, dateAppliedCol, statusCol);

        ObservableList<Application> applicationData = FXCollections.observableArrayList(applications);
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("applicant"));
        dateAppliedCol.setCellValueFactory(new PropertyValueFactory<>("dateAppliedString"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        //not sure

        table.setItems(applicationData);
    }
}