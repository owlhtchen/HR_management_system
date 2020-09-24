package model.container;

import model.user.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class UserManager implements Serializable, Iterable<User> {
    private List<User> users;

    public UserManager() {
        users = new ArrayList<User>();
    }

    public List<User> getUsers() {
        return users;
    }

    public User getUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void addUser(User user){
        users.add(user);
    }

    @Override
    public Iterator<User> iterator() {
        return new UserIterator();
    }

    private class UserIterator implements Iterator<User>{

        private int current = 0;
        @Override
        public boolean hasNext() {
            return current < users.size();
        }

        @Override
        public User next() {
            User temp;
            try {
                temp = users.get(current);
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
            current += 1;
            return temp;
        }
    }
}

