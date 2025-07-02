package org.htech.disasterproject.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.htech.disasterproject.dao.UserDao;
import org.htech.disasterproject.modal.User;
import org.htech.disasterproject.start.Navigation;
import org.htech.disasterproject.utilities.SessionManager;
import org.htech.disasterproject.utilities.UtilityMethods;

import java.net.URL;
import java.util.ResourceBundle;


public class LoginController extends Navigation implements Initializable {

    @FXML
    public AnchorPane stage;

    @FXML
    public TextField usernameField;
    @FXML
    public TextField passwordField;

    @FXML
    public Button loginBtn;

    @FXML
    private Label registerLbl;


    @FXML
    private void handleLogin() {

        String username = usernameField.getText();
        String password = passwordField.getText();

        if(username.isEmpty() || password.isEmpty()) {
            UtilityMethods.showPopupWarning("Username and password are required");
            return;
        }

        UserDao userDao = new UserDao();
       User user = userDao.login(username, password);
       if(user != null) {
           UtilityMethods.switchToScene(stage,"continue_view_one");
           if(user.getRole() == User.Role.ADMIN) {
               System.out.println("admin");
           } else if (user.getRole() == User.Role.CHAIRMAN) {
               System.out.println("chairman");
               SessionManager.setCurrentBarangayId(user.getBarangayId());
           }else{
               user.setBarangayId(user.getBarangayId());
               System.out.println("volunteer");
           }
           SessionManager.setCurrentUser(user);
           SessionManager.setSelectedRole(user.getRole());
//           UtilityMethods.switchToScene(usernameField,"home_view");
       }else{
           UtilityMethods.showPopupWarning("Login Failed");
       }

    }

    @FXML
    private void handleSignUp() {
        UtilityMethods.switchToScene(stage, "register");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        registerLbl.setOnMouseClicked(mouseEvent -> {
            UtilityMethods.switchToScene(stage, "choose_account");
        });
    }
}
