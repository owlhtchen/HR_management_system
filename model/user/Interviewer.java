package model.user;


import model.container.Company;
import model.interview.Interview;
import model.Visitable;
import model.Visitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Interviewer extends User implements Visitor, Serializable {

    private Company company;
    private List<Interview> incompleteInterviews;
    private List<Interview> completeInterviews;

    public Interviewer(String username, String password, Company company){
        super(username, password);
        this.company = company;
        this.incompleteInterviews = new ArrayList<>();
        this.completeInterviews = new ArrayList<>();
    }

    public List<Interview> getIncompleteInterviews() {
        return incompleteInterviews;
    }

    public List<Interview> getCompleteInterviews() {
        return completeInterviews;
    }

    public void addCompleteInterview(Interview interview) {
        this.completeInterviews.add(interview);
    }

    public void removeIncompleteInterview(Interview interview) {
        this.incompleteInterviews.remove(interview);
    }

    @Override
    public void visit(Visitable visitable) {
        incompleteInterviews.add((Interview) visitable);
    }

}
