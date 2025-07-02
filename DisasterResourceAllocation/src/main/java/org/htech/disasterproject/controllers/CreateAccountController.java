package org.htech.disasterproject.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import org.htech.disasterproject.dao.BarangayDao;
import org.htech.disasterproject.dao.UserDao;
import org.htech.disasterproject.modal.Barangay;
import org.htech.disasterproject.modal.User;
import org.htech.disasterproject.utilities.UtilityMethods;

import java.util.Objects;
import java.util.Optional;


public class CreateAccountController {

    @FXML
    private TextField confirmPasswordField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private AnchorPane stage;

    @FXML
    private TextField usernameField;


    static User.Role selectedRole;
    static Barangay barangay;


    @FXML
    public void handleLogin() {
        UtilityMethods.switchToScene(stage, "login");
    }

    @FXML
    void handleRegister(ActionEvent event) {

        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if(username.isEmpty() || password.isEmpty() || email.isEmpty() || confirmPassword.isEmpty()) {
            UtilityMethods.showPopupWarning("Fields cannot be empty");
            return;
        }

        UserDao userDao = new UserDao();

        if(barangay != null) {
            userDao.add(username,password,String.valueOf(selectedRole),barangay.getId());
            UtilityMethods.showPopup("Account created successfully");
        }else{

            Barangay barangay = new Barangay();

            String result = getUserInput("Enter Barangay ", "Please enter the name of the Barangay:");
            barangay.setName(Objects.requireNonNullElse(result, username));

            BarangayDao barangayDao = new BarangayDao();
            if (barangayDao.existsByName(barangay.getName())) {
                UtilityMethods.showPopupWarning("Barangay '" + barangay.getName() + "' already exists.");
                return;
            }
            int id = barangayDao.add(barangay.getName(), "");

            userDao.add(username,password,String.valueOf(selectedRole),id);
            UtilityMethods.showPopup("Account created successfully");
        }

        UtilityMethods.switchToScene(stage, "login");

    }

    public static void passAccountCreationData(User.Role selectedRole, Barangay barangay) {
        CreateAccountController.selectedRole = selectedRole;
        CreateAccountController.barangay = barangay;
    }

    public static void passAccountCreationDataForChairMan(User.Role selectedRole) {
        CreateAccountController.selectedRole = selectedRole;
    }

    public String getUserInput(String title, String content) {
        return UtilityMethods.getUserInput(title, content);
    }

}
