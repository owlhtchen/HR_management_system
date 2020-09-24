package model.notification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MailBox implements Observer, Serializable {
    private String username;
    private List<Message> messages;

    public MailBox(String username) {
        this.username = username;
        this.messages = new ArrayList<>();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Message && ((Message) arg).getReceiverUsername().equals(username)){
            this.messages.add((Message) arg);
        }
    }

    public List<Message> getMessages() {
        return messages;
    }
}
