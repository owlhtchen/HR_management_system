package view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.application.Application;
import model.application.CV;
import model.application.CoverLetter;
import model.application.Reference;
import model.interview.Interview;
import model.interview.QuizInterview;
import model.user.Referee;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class VInterviewer {
    private boolean normalInterviewSelected;
    private QuizInterview selectedQuizInterview;

    public void setNormalInterviewSelected(boolean normalInterviewSelected) {
        this.normalInterviewSelected = normalInterviewSelected;
    }

    public void setSelectedQuizInterview(QuizInterview selectedQuizInterview) {
        this.selectedQuizInterview = selectedQuizInterview;
    }

    public void displayInterviews(TableView<Interview> interviewDate,
                                  TableColumn<Interview, String> interviewType,
                                  TableColumn<Interview, Date> interviewDateCol,
                                  List<Interview> interviews) {
        interviewDateCol.setCellValueFactory(new PropertyValueFactory<>("interviewDate"));
        interviewType.setCellValueFactory(new PropertyValueFactory<>("interviewType"));
        ObservableList<Interview> interviewList = FXCollections.observableArrayList(interviews);
        interviewDate.setItems(interviewList);
    }

    public void displayApplicationsDetail(TableView<Application> applicationInfo, List<Application> applications) {
        applicationInfo.getColumns().clear();
        applicationInfo.getItems().clear();

        TableColumn<Application, String> applicantCol = new TableColumn<>("Applicant");
        applicantCol.setCellValueFactory((TableColumn.CellDataFeatures<Application, String> param) ->
                new SimpleStringProperty(param.getValue().getApplicant().getUsername())

        );

        TableColumn<Application, String> applyDateCol = new TableColumn<>("Apply Date");
        applyDateCol.setCellValueFactory(new PropertyValueFactory<>("dateAppliedString"));

        applicationInfo.getColumns().addAll(applicantCol, applyDateCol);

        TableColumn<Application, Number> scoreCol = new TableColumn<>("Score");
        if(!this.normalInterviewSelected) {
            scoreCol.setCellValueFactory((TableColumn.CellDataFeatures<Application, Number> param) -> {
                        Application selectedApplication = param.getValue();
                        Map<Application, Integer> result = selectedQuizInterview.getResult();
                        for(Map.Entry<Application, Integer> entry: result.entrySet()) {
                            if(entry.getKey() == selectedApplication) {
                                if(entry.getValue() != null) {
                                    return new SimpleIntegerProperty(entry.getValue());
                                } else {
                                    return new SimpleIntegerProperty(0);
                                }
                            }
                        }
                        return new SimpleIntegerProperty(0);
                    }
            );
            applicationInfo.getColumns().add(scoreCol);
        }

        ObservableList<Application> applicationsList = FXCollections.observableArrayList(applications);
        applicationInfo.setItems(applicationsList);

        applicationInfo.setRowFactory( tv -> {
            TableRow<Application> row = new TableRow<>();
            row.setOnMouseClicked(doubleClickEvent -> {
                if (doubleClickEvent.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Application rowData = row.getItem();
                    popUpWindowForApplicantProfile(rowData);
                }
            });
            return row ;
        });
    }

    public TableView<Application>  displayVBox(VBox vboxPane,
                                               List<Application> applicationList) {
        TableView<Application> applicationInfo = new TableView<>();
        vboxPane.getChildren().clear();
        vboxPane.setAlignment(Pos.CENTER);

        displayApplicationsDetail(applicationInfo, applicationList);

        vboxPane.getChildren().addAll(applicationInfo);
        return applicationInfo;
    }


    private void popUpWindowForApplicantProfile(Application application){
        Stage window = new Stage();


        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("More info");
        window.setMinWidth(500);

        CV cv = application.getCv();
        CoverLetter coverLetter = application.getCoverLetter();
        String applicantInfo = "-- CV -- \nTitle: " + cv.getTitle() + "\nContent:" + cv.getContent() + "\n\n";
        applicantInfo += "-- Cover letter -- \nTitle: " + coverLetter.getTitle() + "\nContent: " + coverLetter.getContent()+ "\n\n";
        applicantInfo += "-- Reference -- \n";
        if(application.getReferenceMap() != null) {
            for(Map.Entry<Referee, Reference> entry: application.getReferenceMap().entrySet()) {
                applicantInfo += "Title: " + entry.getValue().getTitle() + ";    Content: "
                        + entry.getValue().getContent() + ";   " + "referred by " + entry.getKey().getUsername() + "\n";
            }
            applicantInfo += '\n';
        }
        Text text = new Text();
        text.wrappingWidthProperty().set(500);
        text.setText(applicantInfo);
        text.setTextAlignment(TextAlignment.CENTER);


        VBox layout = new VBox(10);
        layout.getChildren().addAll(text);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public void clearInterviewDateTable(TableView<Interview> tableView) {
        tableView.getItems().clear();
    }

    public void clearApplicationInfoTable(TableView<Application> tableView) {
        tableView.getColumns().clear();
        tableView.getItems().clear();
    }

    public void setButtonsVisibility(Button button1, Button button2, Boolean showComplete) {
        button1.setVisible(!showComplete);
        button2.setVisible(!showComplete);
    }

    public void setApplicationLabel(Text applicationInfoLabel, boolean showComplete) {
        if(showComplete) {
            applicationInfoLabel.setText("You have completed the interview. No change allowed.\n" +
                    "Please note that the recommendation results also depend on other interviewers, so " +
                    "the recommendation results in table might be changed by post."
            );
        } else {
            applicationInfoLabel.setText("Set interview complete/incomplete here");
        }
    }

    public void setScoreVisibility(TextField score, Button scoreBtn,Text scorePrompt, boolean showComplete) {
        score.setVisible(!showComplete);
        scoreBtn.setVisible(!showComplete);
        scorePrompt.setVisible(!showComplete);
    }

    public void setInvisible(Button recommend, Button unrecommend, Button scoreBtn, TextField score, Text scorePrompt) {
        setScoreVisibility(score, scoreBtn, scorePrompt,true);
        setButtonsVisibility(recommend, unrecommend, true);
    }
}
