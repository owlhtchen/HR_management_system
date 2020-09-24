package model.application;

import model.notification.Message;
import model.Visitable;
import model.Visitor;
import model.user.Applicant;
import model.user.Referee;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

public class Application extends Observable implements Visitable, Serializable {
    private Applicant applicant;
    private CV cv;
    private CoverLetter coverLetter;
    private Date dateApplied;
    private Integer postID;
    private ApplicationStatus status;
    private Map<Referee, Reference> referenceMap;
    private Date dateClosed;

    public Application(Applicant applicant, Date dateApplied, CV cv, CoverLetter coverLetter, Integer postID, Map<Referee, Reference> referenceMap) {
        this.applicant = applicant;
        this.cv = cv;
        this.coverLetter = coverLetter;
        this.dateApplied = dateApplied;
        this.postID = postID;
        this.status = ApplicationStatus.SUBMITTED;
        this.referenceMap = referenceMap;
    }

    public String getDateAppliedString(){
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        return formatter.format(this.dateApplied);
    }

    public Date getDateClosed() {
        return dateClosed;
    }

    public void setDateClosed(Date dateClosed) {
        this.dateClosed = dateClosed;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public CV getCv() {
        return cv;
    }

    public void setCv(CV cv) {
        this.cv = cv;
    }

    public CoverLetter getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(CoverLetter coverLetter) {
        this.coverLetter = coverLetter;
    }

    public Date getDateApplied() {
        return dateApplied;
    }
    
    public Integer getPostID() {
        return postID;
    }

    public void setPostID(Integer postID) {
        this.postID = postID;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public Map<Referee, Reference> getReferenceMap() {
        return referenceMap;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void sendMessage(Message message){
        setChanged();
        notifyObservers(message);
    }

    @Override
    public String toString(){
        return applicant.getUsername();
    }

}
