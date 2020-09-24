package model.application;

import model.notification.Message;

import java.io.Serializable;
import java.util.Observable;

public class File extends Observable implements Serializable {
    private String title;
    private String content;


    public File(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent(){
        return this.content;
    }

    public String getTitle(){
        return this.title;
    }

    @Override
    public String toString() {
        return getTitle() + "\n" + getContent();
    }

    public void sendMessage(Message message){
        setChanged();
        notifyObservers(message);
    }
}
