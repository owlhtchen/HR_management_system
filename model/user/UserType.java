package model.user;

import java.io.Serializable;

public enum UserType{

    APPLICANT("applicant"),
    COORDINATOR("coordinator"),
    INTERVIEWER("interviewer"),
    REFEREE("referee");

    private String type;

    UserType(String theType) {
        this.type = theType;
    }



}
