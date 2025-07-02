package org.htech.disasterproject.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.htech.disasterproject.dao.ResourceDao;
import org.htech.disasterproject.modal.Resource;
import org.htech.disasterproject.modal.User;
import org.htech.disasterproject.start.Navigation;
import org.htech.disasterproject.utilities.CustomConfirmationDialog;
import org.htech.disasterproject.utilities.SessionManager;
import org.htech.disasterproject.utilities.UtilityMethods;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class EditResourcesController extends Navigation {

    @FXML public Button DeleteBtn;
    @FXML private TextField nameField;
    @FXML private Spinner<Double> weightSpinner;
    @FXML private Spinner<Integer> importanceSpinner;
    @FXML private Spinner<Integer> stockSpinner;
    @FXML private ComboBox<String> unitTypeCombo;
    @FXML private ImageView iconUpload;
    @FXML private Button saveBtn;
    @FXML private ImageView allocationIcon;
    @FXML private ImageView familyIcon;
    @FXML private ImageView homeIcon;
    @FXML private ImageView resourceIcon;

    private ResourceDao resourceDao;
    private static Resource selectedResource;
    private byte[] selectedImageBytes = null;

    @FXML
    public void initialize() {
        this.resourceDao = new ResourceDao();
        setupSpinnersAndCombo();

        homeIcon.setOnMouseClicked(event -> handleBackAction(homeIcon));
        resourceIcon.setOnMouseClicked(event -> UtilityMethods.switchToScene(resourceIcon,"view_resources"));
        familyIcon.setOnMouseClicked(event -> UtilityMethods.switchToScene(familyIcon,"view_families"));
        allocationIcon.setOnMouseClicked(event -> UtilityMethods.switchToScene(allocationIcon,"view_allocation"));

        saveBtn.setOnAction(event -> handleSave());
        DeleteBtn.setOnAction(event -> handleDelete());
        iconUpload.setOnMouseClicked(event -> handleImageUpload());

        nameField.setText(selectedResource.getName());
        unitTypeCombo.setValue(selectedResource.getUnitType());
        weightSpinner.getValueFactory().setValue(selectedResource.getWeightKg());
        importanceSpinner.getValueFactory().setValue(selectedResource.getImportanceScore());
        stockSpinner.getValueFactory().setValue(selectedResource.getTotalQuantity());

        if(selectedResource.getImageBytes() != null){
            iconUpload.setImage(new Image(new ByteArrayInputStream(selectedResource.getImageBytes())));
            selectedImageBytes = selectedResource.getImageBytes();
        }
    }

    private void handleImageUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Resource Image (Max 2MB)");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(iconUpload.getScene().getWindow());
        if (selectedFile != null) {
            if (selectedFile.length() > 2 * 1024 * 1024) {
                UtilityMethods.showPopupWarning("Image size must be less than or equal to 2MB.");
                return;
            }
            try (FileInputStream fis = new FileInputStream(selectedFile);
                 ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }
                selectedImageBytes = bos.toByteArray();
                Image image = new Image(selectedFile.toURI().toString(), 100, 100, true, true);
                iconUpload.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
                UtilityMethods.showPopupWarning("Failed to load image.");
            }
        }
    }

    public static void initData(Resource resource) {
        selectedResource = resource;
    }

    private void handleSave() {
        if(SessionManager.getSelectedRole() == User.Role.VOLUNTEER){
            UtilityMethods.showPopupWarning("Only chairman or admin can update");
            return;
        }
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            UtilityMethods.showPopupWarning("Name cannot be empty.");
            return;
        }

        String unitType = unitTypeCombo.getValue();
        Double weight = weightSpinner.getValue();
        int importance = importanceSpinner.getValue();
        int stock = stockSpinner.getValue();

        Resource updatedResource = new Resource(selectedResource.getId(), name, unitType, weight, importance, stock, selectedImageBytes);

        if (resourceDao.updateResource(updatedResource)) {
            UtilityMethods.showPopup("Resource updated successfully!");
            UtilityMethods.switchToScene(saveBtn, "view_resources");
        } else {
            UtilityMethods.showPopupWarning("Failed to update resource.");
        }
    }

    private void handleDelete() {
        if(SessionManager.getSelectedRole() == User.Role.VOLUNTEER){
            UtilityMethods.showPopupWarning("Only chairman or admin can delete");
            return;
        }
        CustomConfirmationDialog.show(
                "Are you sure you want to delete this Resource?",
                new CustomConfirmationDialog.ConfirmationHandler() {
                    @Override
                    public void onConfirm() {
                        if (resourceDao.deleteResource(selectedResource.getId())) {
                            UtilityMethods.showPopup("Resource deleted successfully!");
                            UtilityMethods.switchToScene(DeleteBtn, "view_resources");
                        }
                    }
                    @Override
                    public void onCancel() {
                        System.out.println("Delete cancelled.");
                    }
                }
        );
    }

    private void setupSpinnersAndCombo() {
        weightSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 1000.0, 1.0, 0.1));
        importanceSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 3)); // Importance score 1-5
        stockSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 10));
        unitTypeCombo.getItems().addAll("pcs", "bags", "packs", "bots", "kits", "boxes", "kg", "liters");
    }
}