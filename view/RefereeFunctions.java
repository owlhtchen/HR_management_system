package view;

import controller.MainApp;
import controller.referee.RefereeController;
import controller.referee.SubmitReferenceController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.application.Application;
import model.user.Applicant;

import java.io.IOException;

public class RefereeFunctions {

    private RefereeController refereeController;
    private String status;


    public void setRefereeController(RefereeController refereeController) {
        this.refereeController = refereeController;
    }

    public void displayApplications(TableView table, Text tableTitle, Text note, String status) {
        table.getColumns().clear();
        TableColumn<Application, Integer> postIDCol = new TableColumn<Application, Integer>("PostID");
        TableColumn<Application, Applicant> applicantCol = new TableColumn<Application, Applicant>("Applicant");
        table.getColumns().addAll(postIDCol, applicantCol);
        ObservableList<Application> applicationData = FXCollections.observableArrayList();
        this.status = status;
        if (status.equals("complete")) {
            applicationData.addAll(refereeController.getReferee().getCompleteApplications());
            tableTitle.setText("Complete Applications");
            note.setText("(double click to view)");
        } else if (status.equals("incomplete")) {
            applicationData.addAll(refereeController.getReferee().getIncompleteApplications());
            tableTitle.setText("Incomplete Applications");
            note.setText("(double click to submit)");
        }
        postIDCol.setCellValueFactory(new PropertyValueFactory<>("postID"));
        applicantCol.setCellValueFactory(new PropertyValueFactory<>("applicant"));
        table.setItems(applicationData);
        table.setRowFactory(tv -> {
            TableRow<Application> row = new TableRow<>();
            row.setOnMouseClicked(newevent -> {
                if (newevent.getClickCount() == 2 && (!row.isEmpty())) {
                    Application application = row.getItem();
                    showSubmitReference(application);
                }
            });
            return row;
        });
    }

    public void showSubmitReference(Application application) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/resource/SubmitReference.fxml"));
            AnchorPane layout = loader.load();
            SubmitReferenceController controller = loader.getController();
            controller.setUser(refereeController.getReferee());
            controller.setMainApp(refereeController.getMainApp());
            controller.setApplication(application);
            controller.setStatus(status);
            controller.loadData();
            Scene scene = new Scene(layout);
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Submit Reference");
            window.setScene(scene);
            window.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
