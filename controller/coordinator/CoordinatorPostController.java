package controller.coordinator;

import controller.Controller;
import controller.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.container.Company;
import model.interview.InterviewRound;
import model.container.PostManager;
import model.post.*;
import model.user.Coordinator;
import model.user.User;


import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CoordinatorPostController implements Controller {
    private MainApp mainApp;
    private Coordinator coordinator;

    private List<String> interviewTypes = new ArrayList<>();

    public void setInterviewTypes(List<String> interviewTypes) {
        this.interviewTypes = interviewTypes;
    }

    private HashSet<String> tags = new HashSet<>();

    public void setTags(HashSet<String> tags) {
        this.tags = tags;
    }

    public List<String> getInterviewTypes() {
        return interviewTypes;
    }

    public HashSet<String> getTags() {
        return tags;
    }

    @FXML
    private Label note;

    @FXML
    private TextField titleField;

    @FXML
    private TextArea contentField;

    @FXML
    private Label today;

    @FXML
    private TextField employeeNum;

    @FXML
    private TextField dateClosedField;

    @FXML
    private TextField refNum;

    @FXML
    private TextField refDeadline;


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
        Date date = mainApp.getHrSystem().getTime();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String strToday = dateFormat.format(date);
        today.setText("Date Posted: " + strToday + " (TODAY)");
    }

    @FXML
    void backCoordinator() {
        mainApp.setScene("/view/resource/Coordinator.fxml", coordinator);
    }


    public void post() {
        try {
            Date time = mainApp.getHrSystem().getTime();
            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Date closedDate = formatter.parse(dateClosedField.getText());
            Date refDdl = formatter.parse(refDeadline.getText());
            if (time.after(closedDate)) {
                note.setText("Date Closed must be after Today");
            } else if (closedDate.after(refDdl)) {
                note.setText("Reference Deadline must be after Date Closed");
            } else if (interviewTypes == null || interviewTypes.isEmpty()){
                note.setText("You have not set the interviews for this posting");
            } else {
                String title = titleField.getText();
                String content = contentField.getText();
                String numRef = refNum.getText();
                String numEmployee = employeeNum.getText();
                ArrayList<InterviewRound> rounds = new ArrayList<>();
                for (String type: interviewTypes){
                    InterviewRound interviewRound = new InterviewRound(type);
                    rounds.add(interviewRound);
                }
                if (!title.isEmpty() && !content.isEmpty() && !numRef.isEmpty() && !numEmployee.isEmpty()) {

                    Company company = coordinator.getCompany();

                    String location = null;
                    for (Map.Entry<String, List<Coordinator>> entry : company.getMapLocationCoordinators().entrySet()) {
                        if (entry.getValue().contains(coordinator)) {
                            location = entry.getKey();
                        }
                    }

                    tags.add(company.getCompanyName());
                    tags.add(location);

                    Job job = new Job(title, content, Integer.valueOf(numEmployee), Integer.valueOf(numRef), tags,
                            location);
                    Post post = new Post(time, closedDate, refDdl, rounds, job);
                    //constructor

                    PostManager postManager = mainApp.getHrSystem().getPostManager();
                    postManager.addPost(post);

                    coordinator.addPost(post);

                    reset();

                    note.setText("Successfully posted! The PostID is " + post.getPostID());
                } else {
                    note.setText("Please fill all fields");
                }
            }
        } catch (ParseException dateException) {
            note.setText("Wrong date format");
        }
    }

    public void reset(){
        titleField.clear();
        contentField.clear();
        dateClosedField.clear();
        refNum.clear();
        refDeadline.clear();
        employeeNum.clear();
        this.tags = new HashSet<>();
        this.interviewTypes = new ArrayList<>();
    }


    @FXML
    void setInterview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/resource/CoordinatorSetInterviews.fxml"));
            AnchorPane layout = loader.load();
            CoordinatorSetInteviewsController controller = loader.getController();
            controller.setCoordinatorPostController(this);
            controller.loadData();
            Scene scene = new Scene(layout);
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Set Interviews");
            window.setScene(scene);
            window.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void setTags() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/resource/CoordinatorSetTags.fxml"));
            AnchorPane layout = loader.load();
            CoordinatorSetTagsController controller = loader.getController();
            controller.setCoordinatorPostController(this);
            controller.loadData();
            Scene scene = new Scene(layout);
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Set Tags");
            window.setScene(scene);
            window.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void back() {
        mainApp.setScene("/view/resource/Coordinator.fxml", coordinator);
    }
}
