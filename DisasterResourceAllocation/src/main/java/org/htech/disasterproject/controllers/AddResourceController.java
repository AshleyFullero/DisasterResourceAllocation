package org.htech.disasterproject.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.htech.disasterproject.dao.ResourceDao;
import org.htech.disasterproject.modal.Resource;
import org.htech.disasterproject.utilities.UtilityMethods;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AddResourceController {

    @FXML
    private TextField NameField;

    @FXML
    private ComboBox<String> unitTypeCombo;

    @FXML
    private Spinner<Double> weightSpinner;

    @FXML
    private Spinner<Integer> importanceSpinner;

    @FXML
    private Spinner<Integer> stockSpinner;

    @FXML
    private Button saveBtn;

    @FXML
    private Button addResourceBtn;

    @FXML
    private ImageView allocationIcon;

    @FXML
    private Button backBtn;

    @FXML
    private ImageView familyIcon;

    @FXML
    private ImageView homeIcon;

    @FXML
    private ImageView resourceIcon;

    @FXML private ImageView uploadIcon;
    private byte[] selectedImageBytes = null;

    private ResourceDao resourceDAO;

    @FXML
    public void initialize() {
        this.resourceDAO = new ResourceDao();
        unitTypeCombo.getItems().addAll("bags", "packs", "bottles", "kits", "boxes");
        unitTypeCombo.setEditable(true);
        saveBtn.setOnAction(event -> {
                handleSaveResource();
        });
        resourceIcon.setOnMouseClicked(event -> {
            UtilityMethods.switchToScene(addResourceBtn,"view_resources");
        });
        backBtn.setOnAction(event -> {
            UtilityMethods.switchToScene(backBtn,"view_resources");
        });
        homeIcon.setOnMouseClicked(event -> {
            UtilityMethods.switchToScene(homeIcon,"home_view");
        });
        allocationIcon.setOnMouseClicked(event -> {
            UtilityMethods.switchToScene(allocationIcon,"view_allocation");
        });
        familyIcon.setOnMouseClicked(event -> {
            UtilityMethods.switchToScene(familyIcon,"view_families");
        });

        uploadIcon.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Resource Image (Max 2MB)");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );

            File selectedFile = fileChooser.showOpenDialog(uploadIcon.getScene().getWindow());
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

                    Image image = new Image(selectedFile.toURI().toString(), 273, 82, true, true);
                    uploadIcon.setImage(image);

                } catch (IOException e) {
                    e.printStackTrace();
                    UtilityMethods.showPopupWarning("Failed to load image.");
                }
            }
        });

        weightSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 1000.0, 1.0, 0.1));
        importanceSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1, 1));
        stockSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 1, 1));


        weightSpinner.setPromptText("Weight");
        importanceSpinner.setPromptText("Importance");
        stockSpinner.setPromptText("Stock");
    }

    private void handleSaveResource() {
        String name = NameField.getText().trim();
        String unitType = unitTypeCombo.getEditor().getText().trim();
        Double weight = weightSpinner.getValue();
        Integer importance = importanceSpinner.getValue();
        Integer quantity = stockSpinner.getValue();

        if (name.isEmpty()) {
            UtilityMethods.showPopupWarning("Resource name cannot be empty.");
            return;
        }
        if (unitType.isEmpty()) {
            UtilityMethods.showPopupWarning("Unit type cannot be empty (e.g., 'kg', 'pack', 'bottle').");
            return;
        }
        if (weight == null || weight <= 0) {
            UtilityMethods.showPopupWarning("Weight must be greater than 0.");
            return;
        }
        if (importance == null || importance < 0) {
            UtilityMethods.showPopupWarning("Importance score cannot be negative.");
            return;
        }
        if (quantity == null || quantity < 0) {
            UtilityMethods.showPopupWarning("Stock quantity cannot be negative.");
            return;
        }

        Resource newResource = new Resource(name, unitType, weight, importance, quantity,selectedImageBytes);

        if (resourceDAO.add(newResource)) {
            UtilityMethods.showPopup("Resource added successfully!");
            clearFields();
        } else {
            UtilityMethods.showPopupWarning("Failed to add resource. Please check console for errors.");
        }
    }

    private void clearFields() {
        NameField.clear();
        unitTypeCombo.setValue(null);
        weightSpinner.getValueFactory().setValue(0.0);
        importanceSpinner.getValueFactory().setValue(0);
        stockSpinner.getValueFactory().setValue(0);
    }
}