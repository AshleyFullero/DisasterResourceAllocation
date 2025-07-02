package org.htech.disasterproject.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.htech.disasterproject.dao.ResourceDao;
import org.htech.disasterproject.modal.Resource;
import org.htech.disasterproject.start.Navigation;
import org.htech.disasterproject.utilities.UtilityMethods;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ViewResourcesController extends Navigation implements Initializable {

    @FXML
    private Button addResourceBtn;

    @FXML
    private ImageView allocationIcon;

    @FXML
    private ImageView familyIcon;

    @FXML
    private ImageView homeIcon;

    @FXML
    private ImageView resourceIcon;

    @FXML
    private VBox ResourcesVBox;

    @FXML
    private ScrollPane scrollPane;

    private ResourceDao resourceDao;

    @FXML
    private TextField searchField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceDao = new ResourceDao();

        scrollPane.setFitToWidth(true);

        addResourceBtn.setOnAction(event -> {
            UtilityMethods.switchToScene(addResourceBtn, "add_resource");
        });

        homeIcon.setOnMouseClicked(event -> {
            handleBackAction(homeIcon);
        });

        familyIcon.setOnMouseClicked(event -> {
            UtilityMethods.switchToScene(familyIcon, "view_families");
        });

        allocationIcon.setOnMouseClicked(event -> {
            UtilityMethods.switchToScene(allocationIcon, "view_allocation");
        });

        loadResources();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterResources(newValue.trim());
        });

    }

    private void loadResources() {
        ResourcesVBox.getChildren().clear();

        List<Resource> resourceList = resourceDao.getAllResources();

        if (resourceList.isEmpty()) {
            Label emptyLabel = new Label("No resources found. Click 'Add' to create one.");
            emptyLabel.setFont(new Font("System", 16));
            ResourcesVBox.getChildren().add(emptyLabel);
            return;
        }

        HBox currentHBox = null;
        for (int i = 0; i < resourceList.size(); i++) {
            if (i % 2 == 0) {
                currentHBox = new HBox(20);
                currentHBox.setAlignment(Pos.TOP_LEFT);
                currentHBox.setPadding(new Insets(0, 0, 20, 0));
                ResourcesVBox.getChildren().add(currentHBox);
            }

            final Resource currentResource = resourceList.get(i);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/resource_card.fxml"));
                Node resourceCardNode = loader.load();

                resourceCardNode.setOnMouseClicked(event -> handleResourceClick(currentResource));

                ResourceCardController cardController = loader.getController();
                cardController.setData(resourceList.get(i));

                if (currentHBox != null) {
                    currentHBox.getChildren().add(resourceCardNode);
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Failed to load resource_card.fxml");
            }
        }
    }

    private void filterResources(String query) {
        ResourcesVBox.getChildren().clear();

        List<Resource> resourceList = resourceDao.getAllResources();

        List<Resource> filteredList = resourceList.stream()
                .filter(resource -> resource.getName().toLowerCase().contains(query.toLowerCase()))
                .toList();

        if (filteredList.isEmpty()) {
            Label emptyLabel = new Label("No matching resources found.");
            emptyLabel.setFont(new Font("System", 16));
            ResourcesVBox.getChildren().add(emptyLabel);
            return;
        }

        HBox currentHBox = null;
        for (int i = 0; i < filteredList.size(); i++) {
            if (i % 2 == 0) {
                currentHBox = new HBox(20);
                currentHBox.setAlignment(Pos.TOP_LEFT);
                currentHBox.setPadding(new Insets(0, 0, 20, 0));
                ResourcesVBox.getChildren().add(currentHBox);
            }

            final Resource currentResource = filteredList.get(i);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/resource_card.fxml"));
                Node resourceCardNode = loader.load();

                resourceCardNode.setOnMouseClicked(event -> handleResourceClick(currentResource));

                ResourceCardController cardController = loader.getController();
                cardController.setData(currentResource);

                if (currentHBox != null) {
                    currentHBox.getChildren().add(resourceCardNode);
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Failed to load resource_card.fxml");
            }
        }
    }


    private void handleResourceClick(Resource resource) {
            EditResourcesController.initData(resource);
            UtilityMethods.switchToScene(addResourceBtn, "edit_resources");
    }
}