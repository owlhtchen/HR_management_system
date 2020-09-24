package controller;

import model.application.Application;
import model.application.File;
import model.notification.Message;
import model.interview.Interview;
import model.interview.NormalInterview;
import model.interview.QuizInterview;
import model.user.Interviewer;
import model.user.User;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageGenerator {


    public Message generateMessageForRejected(Date date, Application application){
        String content;
        content = "Unfortunately you have not been hired for Post ID " + application.getPostID() +".";
        return new Message(date, "Application Update", content, application.getApplicant().getUsername());
    }

    public Message generateMessageForHired(Date date, Application application){
        String content;
        content = "Congratulations! You have been hired for Post ID " + application.getPostID() + ".";
        return new Message(date, "Application Update", content, application.getApplicant().getUsername());
    }

    public Message generateMessageForApplicantForQuiz(Date date, QuizInterview interview, Application application){
        String content;
        content = "You have an quiz interview for Post ID " + application.getPostID() + ".";
        content += "\nThe name of your interviewer is " + interview.getInterviewerName() + ".";
        return new Message(date, "Upcoming Interview", content, application.getApplicant().getUsername());
    }

    public Message generateMessageForInterviewerForQuiz(Date date, QuizInterview interview){
        String content;
        content = "You are in charge of a quiz interview for Post ID " + interview.getApplications().get(0).getPostID() + ".";
        content += "\nThe names of your interviewees are " + interview.getApplicantName() + ".";
        String interviewerName = interview.getInterviewers().get(0).getUsername();
        return new Message(date, "Upcoming Interview", content, interviewerName);
    }


    public Message generateMessageForApplicantForGroupInterview(Date date, NormalInterview interview, Application application){
        String content;
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date interviewDate = interview.getInterviewDate();
        content = "You have a group interview on " + formatter.format(interviewDate) + " for Post ID " + application.getPostID() + ".";
        content += "\nThe names of your interviewers are " + interview.getInterviewerName() + ".";
        return new Message(date, "Upcoming Interview", content, application.getApplicant().getUsername());
    }

    public Message generateMessageForInterviewerForGroupInterview(Date date, NormalInterview interview, Interviewer interviewer){
        String content;
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date interviewDate = interview.getInterviewDate();
        content = "You have a group interview on " + formatter.format(interviewDate) + " for Post ID " + interview.getApplications().get(0).getPostID() + ".";
        content += "\nThe names of your interviewees are " + interview.getApplicantName() + ".";
        String interviewerName = interview.getInterviewers().get(0).getUsername();
        return new Message(date, "Upcoming Interview", content, interviewer.getUsername());
    }

    public Message generateMessageForApplicantForOneToOne(Date date, Interview interview, Application application){
        String content;
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date interviewDate = interview.getInterviewDate();
        content = "You have an one-to-one interview on " + formatter.format(interviewDate) + " for Post ID " + application.getPostID() + ".";
        content += "\nThe name of your interviewer is " + interview.getInterviewerName() + ".";
        return new Message(date, "Upcoming Interview", content, application.getApplicant().getUsername());
    }

    public Message generateMessageForInterviewerForOneToOne(Date date, Interview interview, Application application){
        String content;
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date interviewDate = interview.getInterviewDate();
        content = "You have an one-to-one interview on " + formatter.format(interviewDate) + " for Post ID " + application.getPostID() + ".";
        content += "\nThe name of your interviewee is " + application.getApplicant().getUsername() + ".";
        return new Message(date, "Upcoming Interview", content, interview.getInterviewers().get(0).getUsername());
    }

    public Message generateAnnouncement(Date date, File file, User user){
        return new Message(date, file.getTitle(), file.getContent(), user.getUsername());

    }


}

