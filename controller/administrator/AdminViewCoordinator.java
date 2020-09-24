package controller.administrator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import model.user.Administrator;
import model.user.Coordinator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminViewCoordinator {
    private Administrator administrator;

    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
    }

    public void displayLocations(ListView<String> displayOfLocations) {
        displayOfLocations.getItems().clear();
        // clear
        ObservableList<String> locations = FXCollections.observableArrayList();
        ArrayList<String> temp = new ArrayList<>();
        Map<String, List<Coordinator>> map = administrator.getCompany().getMapLocationCoordinators();
        for (Map.Entry<String, List<Coordinator>> entry: map.entrySet()) {
            temp.add(entry.getKey());
        }
        locations.addAll(temp);
        displayOfLocations.setItems(locations);
    }

    public void displayCoordinators(String location, ListView<Coordinator> displayOfCoordinator, ChoiceBox<Coordinator> coordinatorChoiceBox) {
        displayOfCoordinator.getItems().clear();
        coordinatorChoiceBox.getItems().clear();
        ObservableList<Coordinator> coordinators = FXCollections.observableArrayList();
        coordinators.addAll(administrator.getCompany().getMapLocationCoordinators().get(location));
        displayOfCoordinator.setItems(coordinators);
        // setChoiceBox
        ObservableList<Coordinator> unregCoordinators = FXCollections.observableArrayList();
        unregCoordinators.addAll(administrator.getUnRegisteredCoordinators());
        coordinatorChoiceBox.setItems((unregCoordinators));
    }
}