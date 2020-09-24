package model.interview;

import model.application.Application;

import java.util.ArrayList;
import java.util.List;

public class QuizResult {
    public Application application;
    public Integer marks;

    public Application getApplication() {
        return application;
    }

    public Integer getMarks() {
        return marks;
    }

    public QuizResult(Application application, Integer marks) {
        this.application = application;
        this.marks = marks;
    }
}
