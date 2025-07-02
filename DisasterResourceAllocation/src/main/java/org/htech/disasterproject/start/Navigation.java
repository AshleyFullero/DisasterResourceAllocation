package org.htech.disasterproject.start;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import org.htech.disasterproject.utilities.UtilityMethods;


public abstract class Navigation {

    @FXML
    public void handleBackAction(ActionEvent event) {
        UtilityMethods.switchToScene(event, "home_view");
    }

    @FXML
    public void handleBackAction(Node node) {
        UtilityMethods.switchToScene(node, "home_view");
    }

    @FXML
    public void handleNavigateToFamilies(ActionEvent event) {
        UtilityMethods.switchToScene(event, "view_families");
    }


    @FXML
    public void handleNavigateToResources(ActionEvent event) {
        UtilityMethods.switchToScene(event, "view_resources");
    }

    @FXML
    public void handleNavigateToAllocation(ActionEvent event) {
        UtilityMethods.switchToScene(event, "view_allocation");
    }

}
