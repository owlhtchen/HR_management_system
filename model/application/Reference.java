package model.application;
import model.user.Referee;

import java.io.Serializable;

public class Reference extends File implements Serializable {

    private Referee referee;

    public Reference(String title, String content, Referee referee) {
        super(title,content);
        this.referee = referee;

    }

    public Referee getReferee() {
        return referee;
    }


}
