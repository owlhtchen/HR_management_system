package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import model.container.Company;
import model.container.UserManager;
import model.user.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Controller, Initializable{

    private MainApp main;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label signUpLabel;

    @FXML
    private TextField companyNameField;

    @FXML
    private ChoiceBox<String> userTypeChoice;


    public boolean verifyValidUsername(String username){
        UserManager userManager = main.getHrSystem().getUserManager();
        for(User user: userManager){
            if(username.equals(user.getUsername()))
                return false;
        }return true;
    }


    public void signUp() {
        String type = userTypeChoice.getValue();
        String username = usernameField.getText();
        String password = passwordField.getText();
        UserFactory userFactory = new UserFactory();
        String companyName = companyNameField.getText();
        Company company = getCompany(companyName);
        User user = userFactory.getUser(type, username, password, company);
        if (verifyValidUsername(username)) {
            if (type == null) {
                signUpLabel.setText("choose a user type");
            } else if(checkUnregistered(user)){
                signUpLabel.setText("You have already signed up.");
            } else if (type.equals("administrator")) {
                Company newCompany = new Company(companyName);
                User newUser = userFactory.getUser(type, username, password, newCompany);
                signUpAdministrator(newCompany, newUser);
            } else if (type.equals("interviewer")) {
                signUpInterviewer(companyName, user);
            } else if (type.equals("coordinator")) {
                signUpCoordinator(companyName, user);
            } else if (type.equals("applicant")) {
                main.getHrSystem().getUserManager().addUser(user);
                main.setScene("/view/resource/SignIn.fxml", null);
            } else if (type.equals("referee")) {
                User temp = main.getHrSystem().getUserManager().getUser(username);
                temp.setPassword(password);
                main.setScene("/view/resource/SignIn.fxml", null);
            }
        }else
            signUpLabel.setText("Invalid username.");
    }


    private Company getCompany(String companyName){
        for (Company company: main.getHrSystem().getCompanyManager().getCompanies()){
            if (company.getCompanyName().equals(companyName)){
                return company;
            }
        }
        return null;
    }

    public void changeToSignIn() {
        main.setScene("/view/resource/SignIn.fxml", null);
    }

    @Override
    public void setMainApp(MainApp main) {
        this.main = main;
    }

    @Override
    public void setUser(User user) {

    }

    @Override
    public void loadData() {
        companyNameField.setVisible(false);
        userTypeChoice.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) ->
        {if (newValue.equals("coordinator") || newValue.equals("interviewer") || newValue.equals("administrator")){
            this.companyNameField.setVisible(true);
        }
        else{
            companyNameField.setVisible(false);
        }
        });

    }


    private void signUpAdministrator(Company company, User user) {
        if (!company.getCompanyName().equals("")) {
            if (testCompanyExist(company.getCompanyName())) {
                signUpLabel.setText("Each company can only have one administrator");
            } else {
                main.getHrSystem().getCompanyManager().addCompany(company);
                main.getHrSystem().getUserManager().addUser(user);
                company.setAdminister((Administrator) user);
                main.setScene("/view/resource/SignIn.fxml", null);
            }
        }else
            signUpLabel.setText("Please enter valid company name.");

    }

    private void signUpCoordinator(String companyName, User user){
        if (!companyName.equals("")){
            if (testCompanyExist(companyName) && !getCompany(companyName).checkCoordinator((Coordinator) user)) {
                Company company = getCompany(companyName);
                company.getAdminister().addUnregisteredCoordinators((Coordinator) user);
                main.setScene("/view/resource/SignIn.fxml", null);
            }else
                signUpLabel.setText("Company does not exist.");
        }else
            signUpLabel.setText("Please enter valid company name.");
    }

    private void signUpInterviewer(String companyName, User user){
        if (!companyName.equals("")){
            if (testCompanyExist(companyName) && !getCompany(companyName).checkInterviewer((Interviewer) user)) {
                Company company = getCompany(companyName);
                company.getAdminister().addUnregisteredInterviewer((Interviewer) user);
                main.setScene("/view/resource/SignIn.fxml", null);
            }else
                signUpLabel.setText("Company does not exist.");
        }else
            signUpLabel.setText("Please enter valid company name.");
    }

    private boolean testCompanyExist(String companyName){
        for (Company company: main.getHrSystem().getCompanyManager().getCompanies()){
            if (company.getCompanyName().equals(companyName)){
                return true;
            }
        }
        return false;
    }

    private boolean checkUnregistered(User user){
        for (User user1: main.getHrSystem().getUserManager()){
            if (user1 instanceof Administrator){
                Administrator administrator = (Administrator) user1;
                for (Interviewer interviewer: administrator.getUnRegisteredInterviewers()){
                    if (interviewer.getUsername().equals(user.getUsername()) && interviewer.getPassword().equals(user.getPassword())){
                        return true;
                    }
                }
                for (Coordinator coordinator: administrator.getUnRegisteredCoordinators()){
                    if (coordinator.getUsername().equals(user.getUsername()) && coordinator.getPassword().equals(user.getPassword())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signUpLabel.setText("");
        userTypeChoice.setItems(FXCollections.observableArrayList("administrator", "applicant", "interviewer", "coordinator", "referee"));
    }
}
