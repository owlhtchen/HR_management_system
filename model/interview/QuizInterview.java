package model.interview;

import model.application.Application;
import model.Visitable;
import model.user.Interviewer;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;

public class QuizInterview extends Interview implements Visitable, Serializable {

    private Map<Application, Integer> result;

    public QuizInterview(List<Application> applications, List<Interviewer> interviewers, Integer numIntervieweePassed){
        super(applications, interviewers, numIntervieweePassed);
        result = new HashMap<>();
        for(Application application: applications){
            result.put(application, null);
        }
        super.interviewDate = null;
        super.interviewType = "Quiz Interview";
    }

    public void removeApplication(Application application) {
        super.removeApplication(application);
        result.remove(application);
    }

    public void setMark(Application application, Integer newMark){
        if(newMark > -1 && newMark <101){
            result.replace(application, newMark);
        }
    }

    public Map<Application, Integer> getResult() {
        return result;
    }

    @Override
    public String getStatus() {
        status = "complete";
        for(Map.Entry entry: result.entrySet()){
            if(entry.getValue() == null) {
                status = "incomplete";
                break;
            }
        }
        return status;
    }

    public LinkedHashMap<Application, Integer> getSortedResult() {
        List<Map.Entry<Application, Integer>> temp = new ArrayList<>(result.entrySet());
        int n = temp.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (temp.get(j).getValue() == null) {
                    Collections.swap(temp, i, j + 1);
                } else if (temp.get(j).getValue() < temp.get(j + 1).getValue()) {
                    // swap arr[j+1] and arr[i]
                    Collections.swap(temp, i, j + 1);
                }
            }
        }
        LinkedHashMap<Application, Integer> result = new LinkedHashMap<>();
        for(Map.Entry<Application, Integer> entry: temp){
            result.put(entry.getKey(), entry.getValue());
        }
        return result;

    }

    @Override
    public List<Application> getRecommendedApplications() {
        Map<Application, Integer> sorted = getSortedResult();
        ArrayList<Application> target = new ArrayList<>(sorted.keySet());
        return target.subList(0, threshold);
    }

}
