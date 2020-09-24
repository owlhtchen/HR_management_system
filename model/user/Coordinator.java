package model.user;

import model.container.Company;
import model.post.Post;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Coordinator extends User implements Serializable {
    private List<Post> posts = new ArrayList<>();
    private Company company;

    public Coordinator(String username, String password, Company company) {
        super(username, password);
        this.company = company;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public Company getCompany() {
        return company;
    }

    public void addPost(Post post) {
        posts.add(post);
        company.addPost(post);
    }

}
