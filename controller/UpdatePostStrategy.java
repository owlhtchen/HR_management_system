package controller;


import model.interview.InterviewRound;
import model.application.Application;
import model.application.ApplicationStatus;
import model.container.HRSystem;
import model.post.*;
import model.interview.Interview;
import model.user.Referee;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UpdatePostStrategy {
    private HRSystem hrSystem;

    public UpdatePostStrategy(HRSystem hrSystem) {
        this.hrSystem = hrSystem;
    }

    public void updatePostStatus() {
        Date today = hrSystem.getTime();
        for (Post post : hrSystem.getPostManager()) {
            if (post.getStatus() == PostStatus.OPEN && !today.before(post.getDateClosed())) {
                post.setStatus(PostStatus.CLOSED);
            }
            if (post.getStatus() == PostStatus.CLOSED && !today.before(post.getDeadlineDate())) {
                removeUnqualifiedApplication(post);
                removeClosedApplication(post);
                post.setStatus(PostStatus.INTERVIEWING);
                post.setCurrInterviewRound(post.getInterviewRounds().get(0));
                List<Application> temp = post.getApplications();
                if (!temp.isEmpty()) {
                    post.getCurrInterviewRound().setApplications(temp);
                }
            }
        }
    }

    private boolean checkLastRound(Post post){
        if(post.getInterviewRounds() != null) {
            return post.getInterviewRounds().indexOf(post.getCurrInterviewRound()) == post.getInterviewRounds().size() - 1;
        }
        return false;
    }


    public void updatePostInterview(){
        for(Post post: hrSystem.getPostManager()){
            if(post.getStatus() == PostStatus.INTERVIEWING) {
                removeClosedApplication(post);
                // update Interview information for withdrawed application
                updateClosedApplicationInterview(post);
                if (checkRoundFinished(post.getCurrInterviewRound()) && !checkFilled(post)) {
                    if (!checkLastRound(post)) {
                        enterNextInterviewRound(post);
                    } else
                        post.setStatus(PostStatus.STUCK);
                }
            }else if(post.getStatus() == PostStatus.STUCK){
                removeClosedApplication(post);
                // update Interview information for withdrawed application ??
                updateClosedApplicationInterview(post);
                checkFilled(post);
            }
        }
    }

    private boolean checkFilled(Post post){
        ArrayList<ArrayList<Application>> result = getInterviewResult(post);
        ArrayList<Application> last = result.get(0);
        ArrayList<Application> rest = result.get(1);
        if(last.size() <= post.getJob().getNumHiring()){
            post.setStatus(PostStatus.FILLED);
            updateApplicationStatus(last, rest);
            return true;
        }
        return false;
    }

    private void updateApplicationStatus(ArrayList<Application> last, ArrayList<Application> rest){
        MessageGenerator messageGenerator = new MessageGenerator();
        for(Application application: last){
            application.setStatus(ApplicationStatus.HIRED);
            application.setDateClosed(hrSystem.getTime());
            application.sendMessage(messageGenerator.generateMessageForHired(hrSystem.getTime(), application));
        }
        for(Application application: rest){
            application.setStatus(ApplicationStatus.REJECTED);
            application.setDateClosed(hrSystem.getTime());
            application.sendMessage(messageGenerator.generateMessageForRejected(hrSystem.getTime(), application));
        }
    }

    private void enterNextInterviewRound(Post post){
        ArrayList<ArrayList<Application>> result = getInterviewResult(post);
        ArrayList<Application> last = result.get(0);
        ArrayList<Application> rest = result.get(1);
        updateApplicationStatus(new ArrayList<>(), rest);
        int currentIndex = post.getInterviewRounds().indexOf(post.getCurrInterviewRound());
        if(!last.isEmpty() && currentIndex < post.getInterviewRounds().size() - 1){
            post.setCurrInterviewRound(post.getInterviewRounds().get(currentIndex + 1));
            post.getCurrInterviewRound().setApplications(last);
        }else if(last.isEmpty()){
            post.setStatus(PostStatus.CLOSED);
        }
    }

    private ArrayList<ArrayList<Application>> getInterviewResult(Post post){
        ArrayList<ArrayList<Application>> target = new ArrayList<>();
        ArrayList<Application> last = new ArrayList<>();
        ArrayList<Application> rest = new ArrayList<>();
        for(Interview interview : post.getCurrInterviewRound()){
            List<Application> temp = interview.getRecommendedApplications();
            last.addAll(temp);
            rest.addAll(interview.getApplications());
            rest.removeAll(last);
        }
        target.add(last);
        target.add(rest);
        return  target;
    }


    private void removeUnqualifiedApplication(Post post){
        MessageGenerator messageGenerator = new MessageGenerator();
        for(Application application: post.getApplications()) {
            for (Referee referee: application.getReferenceMap().keySet()){
                if (application.getReferenceMap().get(referee) == null){
                    post.removeApplication(application);
                    application.setStatus(ApplicationStatus.REJECTED);
                    application.setDateClosed(hrSystem.getTime());
                    messageGenerator.generateMessageForRejected(hrSystem.getTime(), application);
                }
            }
        }
    }

    private void removeClosedApplication(Post post){
        for(Application application: post.getApplications()){
            if(application.getStatus() == ApplicationStatus.CLOSED){
                if(post.getCurrInterviewRound() != null) {
                    post.getCurrInterviewRound().getApplications().remove(application);
                }
            }
        }
    }

    private void updateClosedApplicationInterview(Post post) {
        for(InterviewRound interviewRound: post.getInterviewRounds()) {
            for(Interview interview: interviewRound.getInterviews()) {
                for(Application application: interview.getApplications()) {
                    if(application.getStatus() == ApplicationStatus.CLOSED) {
                        // Polymorphism will call the appropriate subclass method in NormalInterview or QuizInterview
                        interview.removeApplication(application);
                    }
                }
            }
        }
    }


    private boolean checkRoundFinished(InterviewRound interviewRound){
        if(interviewRound.getInterviews().size() == 0)
            return false;
        for(Interview interview : interviewRound){
            if(interview.getStatus().equals("incomplete")){
                return false;
            }
        }
        return true;
    }
}
