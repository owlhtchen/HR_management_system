package model.interview;


import model.application.Application;
import model.application.ApplicationStatus;
import model.notification.Message;
import model.Visitable;
import model.user.Interviewer;

import java.io.Serializable;
import java.util.*;

public class NormalInterview extends Interview implements Visitable, Serializable {
    private Map<Application, Integer> interviewResult;
    private Integer editedTimesCount;
    private Map<Application, Integer> interviewTimes;

    public NormalInterview(List<Application> applications, List<Interviewer> interviewers, Date interviewDate, Integer requiredRecommendNumber) {
        super(applications, interviewers, requiredRecommendNumber);
        super.interviewDate = interviewDate;
        this.editedTimesCount = 0;
        this.interviewResult = new HashMap<>();
        this.interviewTimes = new HashMap<>();
        for(Application application: applications){
            interviewResult.put(application, 0);
            interviewTimes.put(application, 0);
        }
        this.threshold = requiredRecommendNumber;
        super.interviewType = "Normal Interview";
    }

    public void notifyInterview(Message message){
        setChanged();
        notifyObservers(message);
    }

    // call when recommend
    public void incrementMapElement(Application application){
        Integer count = interviewResult.get(application);
        interviewResult.replace(application, count + 1);
        incrementInterviewTime(application);
    }

    // call when unrecommend
    public void incrementInterviewTime(Application application){
        Integer time = interviewTimes.get(application);
        interviewTimes.replace(application, time + 1);
        addEditedTimesCount();
    }

    // call when withdraw
    public void removeApplication(Application application) {
        super.removeApplication(application);
        substractInterviewtimes(application);
    }


    private void substractInterviewtimes(Application application){
        Integer time = interviewTimes.get(application);
        substractEditedTimes(time);
    }

    private void substractEditedTimes(Integer num){
        editedTimesCount -= num;
    }

    public Integer getTotalEditedTimes(){
        return applications.size() * interviewers.size();
    }

    public Integer getEditedTimesCount() {
        return editedTimesCount;
    }


    public List<Application> getRecommendedApplications() {
        ArrayList<Application> result = new ArrayList<>();
        for (Application application: interviewResult.keySet()){
            if(interviewResult.get(application) >= threshold && application.getStatus() != ApplicationStatus.CLOSED){
                result.add(application);
            }
        }
        return result;
    }


    public List<Application> getApplications() {
        return applications;
    }


    public List<Interviewer> getInterviewers() {
        return interviewers;
    }

    public void setApplications(Message message){
        setChanged();
        notifyObservers(message);
    }

    public void addEditedTimesCount() {
        this.editedTimesCount++;

        if(editedTimesCount >= interviewers.size()*applications.size()){
            status = "complete";
        }
    }

    public String getStatus() {
        if(editedTimesCount >= interviewers.size()*applications.size()){
            status = "complete";
        }
        return status;
    }

}
