package controller;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.container.HRSystem;

import java.io.*;
import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import model.user.User;


public class MainApp extends Application {

    private HRSystem hrSystem;
    private Stage primaryStage;
    private java.io.File file;


    @Override
    public void init() {
        file = new File("./hrSystem.txt");
        this.hrSystem = new HRSystem();
        loadSys();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("HRM 2.0");
        setScene("/view/resource/SignIn.fxml", null);
    }

    @Override
    public void stop() {
        writeSys(hrSystem);
    }

    private void loadSys() {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(file));
            hrSystem = (HRSystem) ois.readObject();
            System.out.println("File load successfully!");
        } catch (Exception error) {
            System.out.println("Not able to load the file: " + error.getMessage());
        } finally {
            try {
                if (ois != null)
                    ois.close();
            } catch (IOException error) {
                System.out.println("Unable to close the file.");
            }
        }
    }

    private void writeSys(HRSystem hrSystem) {
        ObjectOutputStream oos;
        if (file == null) {
            file = new File("./hrSystem.txt");
            try {
                if(file.createNewFile())
                    System.out.println("new file created");
            }catch (IOException e){
                System.out.println(e.getMessage());
            }
        } 
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
            oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(hrSystem);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException error) {
            System.out.println("Unable to write in, " + error.getMessage() + " try with a new file");
        }
    }

    public void setScene(String file, User user) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(file));
            Pane layout = loader.load();
            Controller controller = loader.getController();
            controller.setMainApp(this);
            controller.setUser(user);
            controller.loadData();
            Scene scene = new Scene(layout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HRSystem getHrSystem() {
        return hrSystem;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}