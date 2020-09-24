package model.application;

import java.io.Serializable;

public class CV extends File implements Serializable {

    private String type = "CV";

    public CV(String title, String content){
        super(title,content);
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "CV: " + super.getTitle();
    }
    
}
