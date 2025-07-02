package org.htech.disasterproject.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.htech.disasterproject.utilities.UtilityMethods;

import java.net.URL;
import java.util.ResourceBundle;

public class ContinueController implements Initializable {

    @FXML
    private Button continueBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        continueBtn.setOnAction(e -> {
            UtilityMethods.switchToScene(continueBtn,"continue_view_two");
        });
    }
}
