package controller.administrator;

import controller.Controller;
import controller.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.notification.Message;
import model.user.Administrator;
import model.user.Coordinator;
import model.user.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class AdministratorController implements Controller, Initializable {

    @FXML
    private ListView<String> locationListView;

    @FXML
    private ListView<Coordinator> coordinatorListView;

    @FXML
    private TextField locationTextField;

    @FXML
    private ChoiceBox<Coordinator> coordinatorChoiceBox;


    private AdminViewCoordinator adminViewCoordinator;
    private MainApp mainApp;
    private Administrator administrator;
    private String tempLocation;


    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @Override
    public void setUser(User user) {
        this.administrator = (Administrator) user;
    }

    @Override
    public void loadData() {
        AdminViewCoordinator adminViewCoordinator = new AdminViewCoordinator();
        adminViewCoordinator.setAdministrator(administrator);
        this.adminViewCoordinator = adminViewCoordinator;
        adminViewCoordinator.displayLocations(locationListView);


    }

    public MainApp getMainApp() {
        return mainApp;
    }

    public Administrator getAdministrator() {
        return administrator;
    }

    @FXML
    public void logOut() {
        mainApp.setScene("/view/resource/SignIn.fxml", null);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void addNewLocation() {
        String text = locationTextField.getText();
        if (text == null || text.equals("")) {
            return;
        }

        for (Map.Entry<String, List<Coordinator>> entry: administrator.getCompany().
                getMapLocationCoordinators().entrySet()) {
            if (entry.getKey().equals(text)) {
                return;
            }
        }
        administrator.getCompany().getMapLocationCoordinators().put(text, new ArrayList<>());
        locationTextField.clear();
        loadData();
    }

    @FXML
    public void selectLocation() {
        if (locationListView.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        String location = locationListView.getSelectionModel().getSelectedItem();
        adminViewCoordinator.displayCoordinators(location, coordinatorListView, coordinatorChoiceBox);
        tempLocation = location;
    }

    @FXML
    public void addCoordinator() {
        Coordinator coordinator = coordinatorChoiceBox.getValue();
        if(coordinator == null)
            return;
        administrator.getCompany().getMapLocationCoordinators().get(tempLocation).add(coordinator);
        administrator.removeUnregisteredCoordinator(coordinator);
        mainApp.getHrSystem().getUserManager().addUser(coordinator);
        Message message = new Message(mainApp.getHrSystem().getTime(), "Registered",
                "You are now registered Coordinator at " + tempLocation, administrator.getUsername());
        coordinator.getMailBox().getMessages().add(message);
        adminViewCoordinator.displayCoordinators(tempLocation, coordinatorListView, coordinatorChoiceBox);
    }

    @FXML
    public void unregisterInterviewer() {
        mainApp.setScene("/view/resource/viewUnregistaredInterviewer.fxml", administrator);
    }

    @FXML
    public void postAnnouncement() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/resource/AdministratorPostAnnouncement.fxml"));
            AnchorPane layout = loader.load();
            AdministratorPostAnnouncementController controller = loader.getController();
            controller.setUser(administrator);
            controller.setMainApp(mainApp);
            controller.loadData();
            Scene scene = new Scene(layout);
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Post Announcement");
            window.setScene(scene);
            window.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

