package view;

import controller.coordinator.ViewNormalInterviewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import model.application.Application;

import java.util.ArrayList;
import java.util.List;

public class ViewNormalInterview {
    ViewNormalInterviewController controller;

    public ViewNormalInterview(ViewNormalInterviewController controller) {
        this.controller = controller;
    }

    public void updateRecommendList(ListView<Application> list){
        ObservableList<Application> recom = FXCollections.observableArrayList();
        recom.addAll(controller.getNormalInterview().getRecommendedApplications());
        list.setItems(recom);
    }

    public void updateUnrecommendList(ListView<Application> list){
        ObservableList<Application> unrecom = FXCollections.observableArrayList();
        List<Application> total = new ArrayList<>(controller.getNormalInterview().getApplications());
        List<Application> recom = new ArrayList<>(controller.getNormalInterview().getRecommendedApplications());
        total.removeAll(recom);
        unrecom.addAll(total);
        list.setItems(unrecom);
    }

}
