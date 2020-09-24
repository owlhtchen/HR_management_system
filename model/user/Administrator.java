package model.user;

import model.container.Company;
import model.container.PostManager;
import model.post.Post;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Administrator extends User implements Serializable {

    private Company company;
    private List<Coordinator> unRegisteredCoordinators;
    private List<Interviewer> unRegisteredInterviewers;

    public Administrator(String username, String password, Company company) {
        super(username, password);
        this.company = company;
        this.unRegisteredCoordinators = new ArrayList<>();
        this.unRegisteredInterviewers = new ArrayList<>();
    }

    public Company getCompany() {
        return company;
    }

    public List<Coordinator> getUnRegisteredCoordinators() {
        return unRegisteredCoordinators;
    }

    public List<Interviewer> getUnRegisteredInterviewers() { return unRegisteredInterviewers; }

    public void addUnregisteredCoordinators(Coordinator coordinator) {
        unRegisteredCoordinators.add(coordinator);
    }

    public void removeUnregisteredCoordinator(Coordinator coordinator) {
        int num = -1;
        for (int i=0 ; i < unRegisteredCoordinators.size(); i++) {
            if (unRegisteredCoordinators.get(i).getUsername().equals(coordinator.getUsername())) {
                num = i;
            }
        }
        if (num != -1) {
            unRegisteredCoordinators.remove(num);
        }
    }

    public void addUnregisteredInterviewer(Interviewer interviewer) {
        unRegisteredInterviewers.add(interviewer);
    }

    public void removeUnregisteredInterviewer(Interviewer interviewer) {
        int num = -1;
        for (int i=0 ; i < unRegisteredInterviewers.size(); i++) {
            if (unRegisteredInterviewers.get(i).getUsername().equals(interviewer.getUsername())) {
                num = i;
            }
        }
        if (num != -1) {
            unRegisteredInterviewers.remove(num);
        }
    }

}
