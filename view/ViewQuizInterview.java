package view;

import controller.coordinator.ViewQuizInterviewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.application.Application;
import model.interview.QuizResult;
import model.user.Applicant;

import java.util.Map;


public class ViewQuizInterview {
    ViewQuizInterviewController controller;

    public ViewQuizInterview(ViewQuizInterviewController controller) {
        this.controller = controller;
    }

    public void getTableView(TableView<QuizResult> tableView,
                             TableColumn<QuizResult, Application> applications,
                             TableColumn<QuizResult, Integer> grades) {
        applications.setCellValueFactory(new PropertyValueFactory<>("application"));
        grades.setCellValueFactory(new PropertyValueFactory<>("marks"));
        ObservableList<QuizResult> items = FXCollections.observableArrayList();
        for(Map.Entry<Application, Integer> entry: controller.getQuizInterview().getSortedResult().entrySet()){
            items.add(new QuizResult(entry.getKey(), entry.getValue()));
        }
        tableView.setItems(items);
    }
}
