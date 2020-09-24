package controller.coordinator;

import controller.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.post.Post;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

public class CoordinatorModifyPostController {
    private Post post;
    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @FXML
    private Label note;

    @FXML
    private Label info;

    @FXML
    private TextField titleField;

    @FXML
    private TextArea contentField;

    @FXML
    private TextField employeeNum;

    @FXML
    private TextField dateClosedField;

    @FXML
    private TextField refDeadline;

    @FXML
    private ListView<String> tagsListView;

    @FXML
    private TextField tagField;

    void loadData() {
        info.setText("Post #" + post.getPostID() + "; posted on: " + post.getDatePostedString());
        titleField.setText(post.getJob().getTitle());
        contentField.setText(post.getJob().getContent());
        dateClosedField.setText(post.getDateClosedString());
        employeeNum.setText(Integer.toString(post.getJob().getNumHiring()));
        refDeadline.setText(post.getDeadlineDateString());
        for (String tag : post.getJob().getTags()) {
            tagsListView.getItems().add(tag);
        }
    }

    @FXML
    void save() {
        try {
            Date today = mainApp.getHrSystem().getTime();
            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Date closedDate = formatter.parse(dateClosedField.getText());
            Date refDdl = formatter.parse(refDeadline.getText());
            if (today.after(closedDate)) {
                note.setText("Date Closed must be after Today");
            } else if (closedDate.after(refDdl)) {
                note.setText("Reference Deadline must be after Date Closed");
            } else {
                String title = titleField.getText();
                String content = contentField.getText();
                String numEmployee = employeeNum.getText();
                if (!title.isEmpty() && !content.isEmpty() && !numEmployee.isEmpty()) {
                    post.getJob().setTitle(title);
                    post.getJob().setContent(content);
                    post.getJob().setNumHiring(Integer.valueOf(numEmployee));
                    HashSet<String> tags = new HashSet<>(tagsListView.getItems());
                    post.getJob().setTags(tags);
                    post.setDateClosed(closedDate);
                    post.setDeadlineDate(refDdl);
                    //modify
                    cancel();
                } else {
                    note.setText("Please fill all fields");
                }
            }
        } catch (ParseException dateException) {
            note.setText("Wrong date format");
        }
    }

    @FXML
    void add() {
        if (!tagField.getText().equals("") && !tagsListView.getItems().contains(tagField.getText())) {
            String tag = tagField.getText();
            tagsListView.getItems().add(tag);
            tagField.clear();
        }
    }

    @FXML
    void delete() {
        if (tagsListView.getSelectionModel().getSelectedItem() != null) {
            String type = tagsListView.getSelectionModel().getSelectedItem();
            tagsListView.getItems().remove(type);
        }
    }

    @FXML
    void cancel() {
        Stage stage = (Stage) note.getScene().getWindow();
        stage.close();
    }
}
