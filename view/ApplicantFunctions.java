package view;

import controller.applicant.AddFileController;
import controller.applicant.ApplicantController;
import controller.applicant.ApplyController;
import controller.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.application.Application;
import model.application.CV;
import model.application.CoverLetter;
import model.application.File;
import model.container.PostManager;
import model.post.*;
import model.user.Applicant;


import java.io.IOException;
import java.util.*;



public class ApplicantFunctions {

    private ApplicantController applicantController;

    public ApplicantController getApplicantController() {
        return applicantController;
    }

    public void setApplicantController(ApplicantController applicantController) {
        this.applicantController = applicantController;
    }

    public void displayPosting(AnchorPane displayPane, Text displayTitleText){
        displayPane.getChildren().clear();
        TableView table = new TableView();
        table.setMinWidth(200);
        table.setMaxSize(350, 300);
        displayPane.getChildren().addAll(table);
        displayTitleText.setText("Posting:");
        TableColumn<Post, Integer> postIDCol = new TableColumn<>("PostID");
        TableColumn<Post, String> titleCol = new TableColumn<>("Title");
        TableColumn<Post, String> descriptionCol = new TableColumn<>("Content");
        TableColumn<Post, String> datePostedCol = new TableColumn<>("DatePosted");
        TableColumn<Post, String> dateClosedCol = new TableColumn<>("DateClosed");
        TableColumn<Post, String> statusCol = new TableColumn<>("Status");
        TableColumn<Post, String> deadlineCol = new TableColumn<>("Reference Deadline");
        table.getColumns().addAll(postIDCol, titleCol, descriptionCol, datePostedCol, dateClosedCol,
                deadlineCol, statusCol);
        PostManager postManager = applicantController.getMainApp().getHrSystem().getPostManager();


        ObservableList<Post> allPostData = FXCollections.observableArrayList(postManager.getPosts());
        postIDCol.setCellValueFactory(new PropertyValueFactory<>("PostID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("content"));
        datePostedCol.setCellValueFactory(new PropertyValueFactory<>("datePostedString"));
        dateClosedCol.setCellValueFactory(new PropertyValueFactory<>("dateClosedString"));
        deadlineCol.setCellValueFactory(new PropertyValueFactory<>("DeadlineDateString"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        table.setItems(allPostData);

        Button apply = new Button("Apply");
        displayPane.getChildren().add(apply);
        apply.setLayoutX(400);
        apply.setLayoutY(260);
        apply.setOnAction(event1 -> {
            if(table.getSelectionModel().getSelectedItem() != null) {
                Post post = (Post) table.getSelectionModel().getSelectedItem();
                showApply(post);
            }
        })
          ;
        MenuButton tags = new MenuButton();
        displayPane.getChildren().add(tags);
        tags.setLayoutX(400);
        tags.setLayoutY(50);
        HashSet<String> allTags = getAllTag();
        ArrayList<CheckMenuItem> toCheck = new ArrayList<>();
        for(String tag: allTags){
            CheckMenuItem item = new CheckMenuItem(tag);
            tags.getItems().add(item);
            toCheck.add(item);
        }

        Button filter = new Button("Filter");
        displayPane.getChildren().add(filter);
        filter.setLayoutX(400);
        filter.setLayoutY(90);
        filter.setOnAction(event1 -> {
            HashSet<String> selected = new HashSet<>();
            for(CheckMenuItem tag: toCheck){
                if (tag.isSelected()){
                    selected.add(tag.getText());
                }
            }
            if(!selected.isEmpty()) {
                ObservableList<Post> filteredPost = FXCollections.observableArrayList(filterPost(selected,allPostData));
                table.setItems(filteredPost);
            }
        });

        Button clear = new Button("Clear");
        displayPane.getChildren().add(clear);
        clear.setLayoutX(400);
        clear.setLayoutY(120);
        clear.setOnAction(event1 -> {
            table.setItems(allPostData);
        });
    }


    public void showApply(Post post) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/resource/Apply.fxml"));
            AnchorPane layout = loader.load();
            ApplyController controller = loader.getController();
            controller.setMainApp(applicantController.getMainApp());
            controller.setUser(applicantController.getApplicant());
            controller.setPost(post);
            controller.loadData();
            Scene scene = new Scene(layout);
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Apply");
            window.setScene(scene);
            window.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void viewProfile(AnchorPane displayPane, Text displayTitleText) {
        Applicant applicant = applicantController.getApplicant();
        Button addNewButton = new Button("Add");
        ChoiceBox choicebox = new ChoiceBox();
        choicebox.getItems().add("Add CoverLetter");
        choicebox.getItems().add("Add CV");
        choicebox.setLayoutX(300);
        choicebox.setLayoutY(80);
        addNewButton.setLayoutX(400);
        addNewButton.setLayoutY(110);
        displayPane.getChildren().clear();
        displayTitleText.setText("My Profiles:");
        TableView table = new TableView();
        addNewFileButton(addNewButton, table, choicebox);
        table.setMinWidth(200);
        table.setMaxSize(400, 300);
        displayPane.getChildren().addAll(table, addNewButton, choicebox);
        TableColumn<File, String> titleCol = new TableColumn<>("Title");
        TableColumn<File, String> descriptionCol = new TableColumn<>("Content");
        TableColumn<File, String> typeCol = new TableColumn<>("File Type");
        table.getColumns().addAll(typeCol, titleCol, descriptionCol);
        ObservableList<File> filedata = FXCollections.observableArrayList();
        filedata.addAll(applicant.getListOfCoverLetter());
        filedata.addAll(applicant.getListOfCV());
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("content"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        table.setItems(filedata);
        table.setRowFactory(tv -> {
            TableRow<File> row = new TableRow<>();
            row.setOnMouseClicked(newevent -> {
                if (newevent.getClickCount() == 2 && (! row.isEmpty()) ) {
                    File file = row.getItem();
                    popUpWindowForViewProfile(file);
                    filedata.clear();
                    filedata.addAll(applicant.getListOfCoverLetter());
                    filedata.addAll(applicant.getListOfCV());
                }
            });
            return row;
        });
    }

    private void addNewFileButton(Button addNewButton, TableView table, ChoiceBox choicebox ){
        Applicant applicant = applicantController.getApplicant();
        addNewButton.setOnAction(event1 -> {
            String operation;
            if(choicebox.getValue() != null) {
                operation = (String) choicebox.getValue();
                if (operation.equals("Add CoverLetter")) {
                    showAddFile(applicant, "CoverLetter");
                } else if (operation.equals("Add CV")) {
                    showAddFile(applicant, "CV");
                }
            }
            table.getItems().clear();
            ObservableList<File> newFileData = FXCollections.observableArrayList();
            newFileData.addAll(applicant.getListOfCoverLetter());
            newFileData.addAll(applicant.getListOfCV());
            table.setItems(newFileData);
    });
    }

    private void showAddFile(Applicant applicant, String fileType){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/resource/AddFile.fxml"));
            AnchorPane layout = loader.load();
            AddFileController controller = loader.getController();
            controller.setUser(applicant);
            controller.setType(fileType);
            Scene scene = new Scene(layout);
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Add new file");
            window.setScene(scene);
            window.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void popUpWindowForViewProfile(File file){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Window");
        window.setMinWidth(250);

        Button closeButton = new Button("Exit");
        closeButton.setOnAction(e -> window.close());
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            Applicant applicant = applicantController.getApplicant();
            if (file instanceof CoverLetter){
                applicant.deleteCoverLetter((CoverLetter) file);
            }else if (file instanceof CV){
                applicant.deleteCV((CV) file);
            }
            window.close();
        });

        Text text = new Text();
        text.wrappingWidthProperty().set(300);
        text.setText(file.getContent());
        text.setTextAlignment(TextAlignment.CENTER);


        VBox layout = new VBox(10);
        layout.getChildren().addAll(text, closeButton,deleteButton);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public void viewMyApplication(AnchorPane displayPane, Text displayTitleText) {
        displayPane.getChildren().clear();
        displayTitleText.setText("Applications:");
        TableView table = new TableView();
        table.setMinWidth(200);
        table.setMaxSize(400, 300);
        displayPane.getChildren().addAll(table);
        TableColumn<Application, Integer> postIDCol = new TableColumn<Application, Integer>("PostID");
        TableColumn<Application, String> dateAppliedCol = new TableColumn<Application, String>("Date Applied");
        TableColumn<Application, String> applicationStatusCol = new TableColumn<Application, String>("Status");
        table.getColumns().addAll(postIDCol, dateAppliedCol, applicationStatusCol);
        Applicant applicant = applicantController.getApplicant();

        ObservableList<Application> applicationData = FXCollections.observableArrayList();
        applicationData.addAll(applicant.getApplications());
        postIDCol.setCellValueFactory(new PropertyValueFactory<>("postID"));
        dateAppliedCol.setCellValueFactory(new PropertyValueFactory<>("dateAppliedString"));
        applicationStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        table.setItems(applicationData);
        Button withdrawButton = new Button("Withdraw Application");
        displayPane.getChildren().add(withdrawButton);
        withdrawButton.setLayoutX(300);
        withdrawButton.setLayoutY(80);
        withdrawButton.setOnAction(event1 -> {
            if(table.getSelectionModel().getSelectedItem() != null) {
                Application application = (Application) table.getSelectionModel().getSelectedItem();
                applicantController.withdrawApplication(application);
            }
        });


    }

    public void popUpWindowWithdrawApplication(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Window");
        window.setMinWidth(250);
        Button closeButton = new Button("Exit");
        closeButton.setOnAction(e -> window.close());
        Text text = new Text();
        text.setTextAlignment(TextAlignment.CENTER);
        text.setText("Successfully withdrawn your application");
        VBox layout = new VBox(10);
        layout.getChildren().addAll(text, closeButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    private HashSet<String> getAllTag() {
        HashSet<String> allTag = new HashSet<>();

        List<Post> allPost = this.applicantController.getMainApp().getHrSystem().getPostManager().getPosts();
        //maybe with a parameter

        for(Post post: allPost){
            if (post.getJob().getTags() != null) {
                allTag.addAll(post.getJob().getTags());
            }
        }
        return allTag;
    }

    private List<Post> filterPost(Set<String> myTag, List<Post> posts){
        //myTag cant be null
        ArrayList<Post> filtered = new ArrayList<>();
        for(Post post: posts){
            if (post.getJob().getTags() != null) {
                if (post.getJob().getTags().containsAll(myTag)) {
                    filtered.add(post);
                }
            }
        }
        return filtered;
    }




}
