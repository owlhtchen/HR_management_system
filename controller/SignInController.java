package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.container.UserManager;
import model.user.*;


import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SignInController implements Controller, Initializable {

    private MainApp main;
    private User user;
    private UpdatePostStrategy updatePostStrategy;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label time;


    @FXML
    private TextField timeField;

    @FXML
    private Label signInLabel;

    public void changeToSignUp() {
        main.setScene("/view/resource/SignUp.fxml", null);
    }

    public void changeLabelText(){
        signInLabel.setText("Invalid username or password.");
    }

    public void checkSignInInfo() {
        this.user = getUser(usernameField.getText(), passwordField.getText());
        if (checkUnregistered(usernameField.getText(), passwordField.getText())){
            signInLabel.setText("Waiting for admin to approve");
        } else if(user == null){
            changeLabelText();
        } else if(user instanceof Applicant){
            main.setScene("/view/resource/Applicant.fxml", user);
        }else if(user instanceof Interviewer){
            main.setScene("/view/resource/Interviewer.fxml", user);
        }else if(user instanceof Coordinator){
            main.setScene("/view/resource/Coordinator.fxml", user);
        }else if(user instanceof Referee) {
            main.setScene("/view/resource/Referee.fxml", user);
        }else if(user instanceof Administrator){
            main.setScene("/view/resource/Administer.fxml", user);
        }else
            changeLabelText();
    }

    private User getUser(String username, String password){
        UserManager userManager = main.getHrSystem().getUserManager();
        for(User user: userManager){
            if(username.equals(user.getUsername()) && password.equals(user.getPassword())){
                return user;
            }
        }return null;
    }

    private boolean checkUnregistered(String username, String password){
        for (User user1: main.getHrSystem().getUserManager()){
            if (user1 instanceof Administrator){
                Administrator administrator = (Administrator) user1;
                for (Interviewer interviewer: administrator.getUnRegisteredInterviewers()){
                    if (interviewer.getUsername().equals(username) && interviewer.getPassword().equals(password)){
                        return true;
                    }
                }
                for (Coordinator coordinator: administrator.getUnRegisteredCoordinators()){
                    if (coordinator.getUsername().equals(username) && coordinator.getPassword().equals(password)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void setMainApp(MainApp main){
        this.main = main;
    }

    @Override
    public void setUser(User user) {
    }

    @Override
    public void loadData() {
        if(main.getHrSystem().getTime() == null) {
            main.getHrSystem().setTime(Calendar.getInstance().getTime());
        }
        updatePostStrategy = new UpdatePostStrategy(main.getHrSystem());
        updatePostStrategy.updatePostStatus();
        updatePostStrategy.updatePostInterview();
        changeTimeText();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signInLabel.setText("");
    }

    public void changeTimeText(){
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String time = format.format(main.getHrSystem().getTime());
        this.time.setText(time);
    }

    public void setTime(){
        try{
            DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date date = format.parse(timeField.getText());
            if(date.after(main.getHrSystem().getTime())){
                main.getHrSystem().setTime(date);
                changeTimeText();
            }
        }catch (ParseException e){
            System.out.println(e.getMessage());
        }finally {
            updatePostStrategy.updatePostStatus();
            updatePostStrategy.updatePostInterview();
        }
    }

}
