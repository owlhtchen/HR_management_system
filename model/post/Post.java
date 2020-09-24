package model.post;
import model.interview.InterviewRound;
import model.application.Application;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

public class Post implements Serializable {
    private Job job;
    private Integer PostID;
    private Date datePosted;
    private Date dateClosed;
    private List<Application> applications;
    private List<InterviewRound> interviewRounds;
    private InterviewRound currInterviewRound;
    private PostStatus status;
    private Date deadlineDate;


    public Post(Date datePosted, Date dateClosed, Date deadlineDate, ArrayList<InterviewRound> interviewRounds,
                Job job) {
        this.datePosted = datePosted;
        this.dateClosed = dateClosed;
        this.applications = new ArrayList<>();
        this.interviewRounds = interviewRounds;
        this.status = PostStatus.OPEN;
        this.deadlineDate = deadlineDate;
        this.job = job;
    }



    public InterviewRound getCurrInterviewRound() {
        return currInterviewRound;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void addApplication(Application application){
        this.applications.add(application);
    }


    public void removeApplication(Application application){
        applications.remove(application);
    }

    public void setPostID(Integer postID) {
        PostID = postID;
    }

    public Integer getPostID() {
        return PostID;
    }

    public Job getJob() {return job;}

    public Date getDateClosed() {
        return dateClosed;
    }

    public Date getDeadlineDate() {
        return deadlineDate;
    }

    public List<InterviewRound> getInterviewRounds() {
        return interviewRounds;
    }

    public PostStatus getStatus() {
        return status;
    }

    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public String getDatePostedString(){
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        return formatter.format(datePosted);
    }

    public String getDateClosedString(){
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        return formatter.format(dateClosed);
    }

    public String getDeadlineDateString(){
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        return formatter.format(deadlineDate);
    }

    public void setCurrInterviewRound(InterviewRound currInterviewRound) {
        this.currInterviewRound = currInterviewRound;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }

    public String getTitle(){
        return job.getTitle();
    }

    public String getContent(){
        return job.getContent();
    }

    public void setDateClosed(Date dateClosed) {
        this.dateClosed = dateClosed;
    }

    @Override
    public String toString(){
        return job.toString();
    }
}