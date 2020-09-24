package model.container;

import model.post.Post;
import model.user.Administrator;
import model.user.Coordinator;
import model.user.Interviewer;
import model.user.User;

import java.io.Serializable;
import java.util.*;

public class Company implements Serializable, Iterable<User> {
    private String companyName;
    private Map<String, List<Coordinator>> mapLocationCoordinators;
    private List<Interviewer> interviewers;
    private Administrator administer;
    private List<Post> posts;
    private Integer companyID;

    public Company(String companyName) {
        this.companyName = companyName;
        mapLocationCoordinators = new HashMap<>();
        interviewers = new ArrayList<>();
        posts = new ArrayList<>();
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void addPost(Post post) {
        posts.add(post);
    }

    public Map<String, List<Coordinator>> getMapLocationCoordinators() {
        return mapLocationCoordinators;
    }

    public List<Interviewer> getInterviewers() {
        return interviewers;
    }

    public void addInterviewer(Interviewer interviewer){
        this.interviewers.add(interviewer);
    }

    public void setAdminister(Administrator administer) {
        this.administer = administer;
    }

    public boolean checkAdminister() {
       return (administer != null);
    }

    public Administrator getAdminister() {
        return administer;
    }

    public boolean checkCoordinator(Coordinator coordinator) {
        List<Coordinator> coordinators = new ArrayList<>();
        coordinators.addAll(administer.getUnRegisteredCoordinators());
        for (Map.Entry<String, List<Coordinator>> entry: mapLocationCoordinators.entrySet()) {
            coordinators.addAll(entry.getValue());
        }
        for (Coordinator co: coordinators) {
            if (co.getUsername().equals((coordinator.getUsername()))) {
                return true;
            }
        }
        return false;
    }

    public boolean checkInterviewer(Interviewer interviewer) {
        List<Interviewer> interviewers = new ArrayList<>();
        interviewers.addAll(administer.getUnRegisteredInterviewers());
        interviewers.addAll(this.interviewers);
        for (Interviewer in: interviewers) {
            if (in.getUsername().equals(interviewer.getUsername())) {
                return true;
            }
        }
        return false;
    }

    public List<Coordinator> getCoordinators(){
        ArrayList<Coordinator> coordinators = new ArrayList<>();
        for(String location: mapLocationCoordinators.keySet()){
            List<Coordinator> locationCoordinators = mapLocationCoordinators.get(location);
            coordinators.addAll(locationCoordinators);
        }
        return coordinators;
    }


    @Override
    public Iterator<User> iterator() {
        return new UserIterator();
    }

    private class UserIterator implements Iterator<User> {

        private int count = 0;
        private List<User> temp = new ArrayList<>();

        public UserIterator(){
            temp.addAll(interviewers);
            temp.addAll(getCoordinators());
        }

        @Override
        public boolean hasNext() {
            return count < temp.size();
        }

        @Override
        public User next() {
            User res;
            try {
                res = temp.get(count);
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
            count ++;
            return res;
        }
    }

}
