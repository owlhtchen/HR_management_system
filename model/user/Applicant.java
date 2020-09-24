package model.user;

import model.application.Application;
import model.application.CV;
import model.application.CoverLetter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Applicant extends User implements Serializable {

    private List<CV> listOfCV;
    private List<CoverLetter> listOfCoverLetter;
    private List<Application> applications;


    public Applicant(String username, String password){
        super(username, password);
        this.listOfCoverLetter = new ArrayList<>();
        this.listOfCV = new ArrayList<>();
        this.applications = new ArrayList<>();
    }

    public List<CV> getListOfCV() {
        return listOfCV;
    }

    public List<CoverLetter> getListOfCoverLetter() {
        return listOfCoverLetter;
    }

    public void addCoverLetter(CoverLetter coverLetter){
        this.listOfCoverLetter.add(coverLetter);
    }

    public void addCV(CV cv){
        this.listOfCV.add(cv);
    }

    public void deleteCV(CV cv) {
        listOfCV.remove(cv);
    }

    public void deleteCoverLetter(CoverLetter coverLetter) {
        listOfCoverLetter.remove(coverLetter);
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public void addApplication(Application application){
        applications.add(application);
    }


}
