package controller.coordinator;

import controller.MainApp;
import controller.coordinator.CreateGroupInterviewController;
import controller.coordinator.OneToOneInterviewController;
import controller.coordinator.QuizController;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.interview.InterviewRound;
import model.user.Coordinator;

import java.io.IOException;

public class InterviewFactory {
    CoordinatorViewPostController controller;

    public InterviewFactory(CoordinatorViewPostController coordinatorViewPostController) {
        this.controller = coordinatorViewPostController;
    }

    public void createInterview(InterviewRound interviewRound, Coordinator coordinator, MainApp mainApp) {
        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane layout;
            if (interviewRound.getType().equals("oneToOne")) {
                loader.setLocation(MainApp.class.getResource("/view/resource/OneToOneInterview.fxml"));
                layout = loader.load();
                OneToOneInterviewController controller = loader.getController();
                controller.setMainApp(mainApp);
                controller.setInterviewRound(interviewRound);
                controller.setUser(coordinator);
                controller.loadData();
            } else if (interviewRound.getType().equals("group")) {
                loader.setLocation(MainApp.class.getResource("/view/resource/CreateGroupInterview.fxml"));
                layout = loader.load();
                CreateGroupInterviewController controller = loader.getController();
                controller.setMainApp(mainApp);
                controller.setInterviewRound(interviewRound);
                controller.setUser(coordinator);
                controller.loadData();
            } else if (interviewRound.getType().equals("phone")) {
                loader.setLocation(MainApp.class.getResource("/view/resource/OneToOneInterview.fxml"));
                layout = loader.load();
                OneToOneInterviewController controller = loader.getController();
                controller.setMainApp(mainApp);
                controller.setInterviewRound(interviewRound);
                controller.setUser(coordinator);
                controller.loadData();
            } else {
                loader.setLocation(MainApp.class.getResource("/view/resource/Quiz.fxml"));
                layout = loader.load();
                QuizController controller = loader.getController();
                controller.setInterviewRound(interviewRound);
                controller.setMainApp(mainApp);
                controller.setUser(coordinator);
                controller.loadData();
            }
            Scene scene = new Scene(layout);
            Stage window = new Stage();

            window.initModality(Modality.APPLICATION_MODAL);
            window.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    controller.updateTabPane();
                }
            });
            window.setTitle("Create Interview");
            window.setScene(scene);
            window.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            System.out.println("select a post.");
        }
    }


}
