package model.user;

import model.application.Application;
import model.Visitable;
import model.Visitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Referee extends User implements Visitor, Serializable {

    private List<Application> incompleteApplications = new ArrayList<>();
    private List<Application> completeApplications = new ArrayList<>();


    public Referee(String username, String password) {
        super(username, password);
    }

    public List<Application> getIncompleteApplications() {
        return incompleteApplications;
    }

    public List<Application> getCompleteApplications() {
        return completeApplications;
    }



    @Override
    public void visit(Visitable visitable) {
        this.incompleteApplications.add((Application) visitable);
    }
}
