package model.notification;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable {
    private Date date;
    private String content;
    private String title;
    private boolean hasRead;
    private String dateString;
    private String hasReadString;
    private String receiverUsername;

    public Message(Date date, String title, String content, String receiverUsername){
        this.date = date;
        this.content = content;
        this.hasRead = false;
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        this.dateString = formatter.format(date);
        this.hasReadString = "Unread";
        this.title = title;
        this.receiverUsername = receiverUsername;
    }

    public String getReceiverUsername(){return receiverUsername;}

    public void read() {
        this.hasRead = true;
        this.hasReadString = "Read";
    }

    public String getHasReadString() {
        return hasReadString;
    }

    public String getDateString() {
        return dateString;
    }

    public Date getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public boolean isHasRead() {
        return hasRead;
    }

    public String getTitle() {
        return title;
    }

    public void setHasRead(boolean hasRead) {
        this.hasRead = hasRead;
    }
}
