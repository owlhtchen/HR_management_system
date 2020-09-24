package model.application;

import java.io.Serializable;

public class CoverLetter extends File implements Serializable {

    private String type = "CoverLetter";

    public CoverLetter(String title, String content){
        super(title,content);
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "CoverLetter: " + super.getTitle();
    }
}
