package org.htech.disasterproject.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.htech.disasterproject.dao.BarangayDao;
import org.htech.disasterproject.modal.User;
import org.htech.disasterproject.start.Navigation;
import org.htech.disasterproject.utilities.SessionManager;
import org.htech.disasterproject.utilities.UtilityMethods;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController extends Navigation implements Initializable {

    @FXML
    private ImageView allocationIcon;

    @FXML
    private ImageView familyIcon;

    @FXML
    private ImageView homeIcon;

    @FXML
    private ImageView resourceIcon;

    @FXML
    private Label nameLocationLabel;

    @FXML
    private ImageView profileIcon;

    @FXML
    private ImageView logoutIcon;

    @FXML
    private ImageView addBarangayIcon;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameLocationLabel.setText(SessionManager.getCurrentUser().getUsername());
        if(SessionManager.getCurrentUser().getRole().equals(User.Role.CHAIRMAN)){
            nameLocationLabel.setText(SessionManager.getCurrentUser().getUsername()+", Manila , Philippines");
        }
        allocationIcon.setOnMouseClicked(mouseEvent -> {
            UtilityMethods.switchToScene(allocationIcon, "view_allocation");
        });
        familyIcon.setOnMouseClicked(mouseEvent -> {
            UtilityMethods.switchToScene(allocationIcon, "view_families");
        });
        homeIcon.setOnMouseClicked(mouseEvent -> {
            UtilityMethods.switchToScene(allocationIcon, "home_view");
        });
        resourceIcon.setOnMouseClicked(mouseEvent -> {
            UtilityMethods.switchToScene(allocationIcon, "view_resources");
        });
//        profileIcon.setOnMouseClicked(mouseEvent -> {
//            UtilityMethods.switchToScene(allocationIcon, "view_profile");
//        });
        logoutIcon.setOnMouseClicked(mouseEvent -> {
            UtilityMethods.showPopup("Logout Successfully");
            UtilityMethods.switchToScene(allocationIcon, "login");
        });

        addBarangayIcon.setOnMouseClicked(mouseEvent -> {
            if (!SessionManager.getCurrentUser().getRole().equals(User.Role.ADMIN)) {
                UtilityMethods.showPopupWarning("You are not an Admin");
                return;
            }

            String result = UtilityMethods.getUserInput("Barangay", "Enter Barangay name:");
            if (result == null || result.trim().isEmpty()) {
                UtilityMethods.showPopupWarning("Barangay name is invalid");
                return;
            }

            BarangayDao barangayDao = new BarangayDao();
            String name = result.trim();

            if (barangayDao.existsByName(name)) {
                UtilityMethods.showPopupWarning("Barangay '" + name + "' already exists.");
                return;
            }

            int id = barangayDao.add(name, "");
            if (id > 0) {
                UtilityMethods.showPopup("Barangay added successfully with ID: " + id);
            } else {
                UtilityMethods.showPopupWarning("Failed to add barangay.");
            }
        });


    }
}
