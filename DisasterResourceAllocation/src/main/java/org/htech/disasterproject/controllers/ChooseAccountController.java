package org.htech.disasterproject.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.htech.disasterproject.dao.BarangayDao;
import org.htech.disasterproject.modal.Barangay;
import org.htech.disasterproject.modal.User;
import org.htech.disasterproject.utilities.UtilityMethods;

import java.net.URL;
import java.util.ResourceBundle;

public class ChooseAccountController implements Initializable {

    @FXML
    private ComboBox<User.Role> accountRoleComboBox;

    @FXML
    private ComboBox<Barangay> barangayComboBox;

    @FXML
    private Button continueBtn;

    @FXML
    private AnchorPane stage;

    @FXML
    void handleLogin(MouseEvent event) {
        UtilityMethods.switchToScene(stage, "login");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accountRoleComboBox.getItems().addAll(User.Role.values());
        accountRoleComboBox.getItems().remove(User.Role.ADMIN);

        barangayComboBox.setDisable(true);

        BarangayDao dao = new BarangayDao();
        barangayComboBox.getItems().addAll(dao.getAllBarangays());
        accountRoleComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == User.Role.VOLUNTEER) {
                barangayComboBox.setDisable(false);
            } else {
                barangayComboBox.setDisable(true);
                barangayComboBox.getSelectionModel().clearSelection();
            }
        });

        continueBtn.setOnAction(event -> {
            {
                User.Role role = accountRoleComboBox.getSelectionModel().getSelectedItem();
                Barangay barangay = barangayComboBox.getSelectionModel().getSelectedItem();

                if(barangay != null && role != null) {
                    CreateAccountController.passAccountCreationData(role, barangay);
                    UtilityMethods.switchToScene(stage, "create_account");
                }else if(role == User.Role.CHAIRMAN){
                    CreateAccountController.passAccountCreationDataForChairMan(User.Role.CHAIRMAN);
                    UtilityMethods.switchToScene(stage, "create_account");
                }
                else {
                    UtilityMethods.showPopupWarning("Please select a role" + (role == User.Role.VOLUNTEER ? " and barangay" : ""));
                }
            }
        });
    }
}
