package org.htech.disasterproject.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.htech.disasterproject.utilities.UtilityMethods;

public class ChooseActionController {

    @FXML
    public AnchorPane stage;

    @FXML
    private void handleLogin() {
        UtilityMethods.switchToScene(stage, "login");

    }

    @FXML
    private void handleSignUp() {
        UtilityMethods.switchToScene(stage, "choose_account");
    }

}
