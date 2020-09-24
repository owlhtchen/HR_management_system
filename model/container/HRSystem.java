package model.container;

import model.container.CompanyManager;
import model.container.PostManager;
import model.container.UserManager;

import java.io.Serializable;
import java.util.Date;

public class HRSystem implements Serializable {
    private UserManager userManager;
    private PostManager postManager;
    private CompanyManager companyManager;
    private Date time;


    public HRSystem() {
        this.userManager = new UserManager();
        this.postManager = new PostManager();
        this.companyManager = new CompanyManager();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date date){
        this.time = date;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public PostManager getPostManager() {
        return postManager;
    }

    public CompanyManager getCompanyManager() {
        return companyManager;
    }

}
