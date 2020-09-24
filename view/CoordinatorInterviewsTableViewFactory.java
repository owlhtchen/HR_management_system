package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.interview.InterviewRound;
import model.interview.Interview;


public class CoordinatorInterviewsTableViewFactory {
    public TableView<Interview> getTableView(InterviewRound interviewRound) {
        if (interviewRound.getType().equals("oneToOne") || interviewRound.getType().equals("phone") || interviewRound.getType().equals("group")) {
            TableView<Interview> tableView = new TableView<>();
            TableColumn<Interview, String> applicantColumn = new TableColumn<>("applicant");
            applicantColumn.setCellValueFactory(new PropertyValueFactory<>("ApplicantName"));
            TableColumn<Interview, String> interviewerColumn = new TableColumn<>("interviewer");
            interviewerColumn.setCellValueFactory(new PropertyValueFactory<>("InterviewerName"));
            TableColumn<Interview, String> timeColumn = new TableColumn<>("time");
            timeColumn.setCellValueFactory(new PropertyValueFactory<>("interviewDate"));
            TableColumn<Interview, String> statusColumn = new TableColumn<>("status");
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
            ObservableList<Interview> items = FXCollections.observableArrayList(interviewRound.getInterviews());
            tableView.getColumns().addAll(applicantColumn, interviewerColumn, timeColumn, statusColumn);
            tableView.setItems(items);
            return tableView;
        } else if (interviewRound.getType().equals("quiz")) {
            TableView<Interview> tableView = new TableView<>();
            TableColumn<Interview, String> interviewerColumn = new TableColumn<>("interviewer");
            interviewerColumn.setCellValueFactory(new PropertyValueFactory<>("InterviewerName"));
            TableColumn<Interview, String> statusColumn = new TableColumn<>("status");
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
            ObservableList<Interview> items = FXCollections.observableArrayList(interviewRound.getInterviews());
            tableView.getColumns().addAll(interviewerColumn, statusColumn);
            tableView.setItems(items);
            return tableView;
        } else
            return null;
    }
}
