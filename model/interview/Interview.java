package model.interview;

import model.application.Application;
import model.notification.Message;
import model.Visitable;
import model.Visitor;
import model.user.Interviewer;

import java.io.Serializable;
import java.util.*;

public abstract class Interview extends Observable implements Visitable, Serializable {
    List<Application> applications;
    List<Interviewer> interviewers;
    Integer threshold;
    String status;
    Map<Application, List<Interviewer>> applicationInterviewers;
    Date interviewDate;
    String interviewType;

    public Interview(List<Application> applications, List<Interviewer> interviewers, Integer threshold) {
        this.applications = applications;
        this.interviewers = interviewers;
        this.threshold = threshold;
        this.status = "incomplete";
        this.applicationInterviewers = new HashMap<>();
        for(Application application: applications){
            applicationInterviewers.put(application, new ArrayList<>());
        }
    }

    // call when recommend or unrecommend
    public void addInterviewerToApplication(Interviewer interviewer, Application application) {
        applicationInterviewers.get(application).add(interviewer);
    }

    // call when withdraw by subclass
    public void removeApplication(Application application) {
        applications.remove(application);
        applicationInterviewers.remove(application);
    }

    public Map<Application, List<Interviewer>> getApplicationInterviewers() {
        return applicationInterviewers;
    }

    public String getApplicantName(){
        String result = "";
        StringBuilder stringBuilder = new StringBuilder(result);
        for(Application application: applications){
            stringBuilder.append(application.getApplicant().getUsername());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }


    public List<Application> getApplications() {
        return applications;
    }


    public List<Interviewer> getInterviewers() {
        return interviewers;
    }

    public String getInterviewerName(){
        String result = "";
        StringBuilder stringBuilder = new StringBuilder(result);
        for(Interviewer interviewer: interviewers){
            stringBuilder.append(interviewer.getUsername());
            stringBuilder.append( "\n");
        }
        return stringBuilder.toString();
    }

    public abstract String getStatus();

    public abstract List<Application> getRecommendedApplications();

    public Date getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(Date interviewDate) {
        this.interviewDate = interviewDate;
    }

    public String getInterviewType() {
        return interviewType;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void sendMessage(Message message){
        setChanged();
        notifyObservers(message);
    }
}
