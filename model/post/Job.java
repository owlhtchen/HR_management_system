package model.post;

import java.io.Serializable;
import java.util.Set;

public class Job implements Serializable{
    private String title;
    private String content;
    private Integer numHiring;
    private Integer numRef;
    private Set<String> tags;
    private String location;

    public Job(String title, String content, Integer numHiring, Integer numRef, Set<String> tags, String location) {
        this.title = title;
        this.content = content;
        this.numHiring = numHiring;
        this.tags = tags;
        this.location = location;
        this.numRef = numRef;
    }

    public String getLocation(){
        return location;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Integer getNumHiring() {
        return numHiring;
    }

    public Integer getNumRef() { return numRef; }

    public Set<String> getTags() {
        return tags;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNumHiring(Integer numHiring) {
        this.numHiring = numHiring;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return title;
    }

}
