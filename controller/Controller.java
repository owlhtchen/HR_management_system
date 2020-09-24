package controller;
import model.user.User;

public interface Controller {
    void setMainApp(MainApp mainApp);
    void setUser(User user);

    void loadData();
}
