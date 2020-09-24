package model.interview;

import model.application.Application;
import model.interview.Interview;
import model.interview.NormalInterview;
import model.user.Interviewer;

import java.io.Serializable;
import java.util.*;

public class InterviewRound implements Serializable, Iterable<Interview>{

    private List<Application> applications;
    private List<Interview> interviews;
    private String type;

    public InterviewRound(String type){
        this.type = type;
        interviews = new ArrayList<>();
    }

    public List<Application> getApplications() {
        return applications;
    }

    public List<Interview> getInterviews() {
        return interviews;
    }

    public String getType() {
        return type;
    }

    public void setApplications(List<Application> applications){
        this.applications = applications;
    }


    public void createInterview(List<Application> applications, List<Interviewer> interviewers, Date interviewDate, Integer requiredRecommendNumber){
        NormalInterview normalInterview = new NormalInterview(applications, interviewers, interviewDate, requiredRecommendNumber);
        interviews.add(normalInterview);
        this.applications.removeAll(applications);
    }



    public void addInterview(Interview interview){
        this.interviews.add(interview);
    }

    public void removeApplications(List<Application> applications){
        this.applications.removeAll(applications);
    }

    @Override
    public Iterator<Interview> iterator() {
        return new RoundIterator();
    }

    private class RoundIterator implements Iterator<Interview>{
        private int count = 0;

        @Override
        public boolean hasNext() {
            return count < interviews.size();
        }

        @Override
        public Interview next() {
            Interview res;
            try {
                res = interviews.get(count);
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
            count ++;
            return res;
        }
    }
}
