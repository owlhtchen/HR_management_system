package model.user;


import model.container.Company;

import java.io.Serializable;

public class UserFactory implements Serializable {

    public User getUser(String userType, String username, String password, Company company){
        if (userType == null){
            return null;
        }
        else if (userType.equals("applicant")){
            return new Applicant(username, password);
        }
        else if (userType.equals("coordinator")){
            return new Coordinator(username, password, company);
        }
        else if (userType.equals("interviewer")) {
            return new Interviewer(username, password, company);
        }
        else if (userType.equals("referee")){
            return new Referee(username, password);
        }
        else if (userType.equals("administrator")){
            return new Administrator(username, password, company);
        }

        else {return null;}
    }

}

