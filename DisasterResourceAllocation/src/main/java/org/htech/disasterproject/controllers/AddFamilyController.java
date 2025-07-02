package org.htech.disasterproject.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.htech.disasterproject.dao.FamilyDao;
import org.htech.disasterproject.modal.Family;
import org.htech.disasterproject.utilities.SessionManager;
import org.htech.disasterproject.utilities.UtilityMethods;
import javafx.stage.FileChooser;

import javafx.scene.image.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;

public class AddFamilyController {

    @FXML private ImageView allocationIcon;
    @FXML private TextField contactTextField;
    @FXML private TextField familyAddressTextField;
    @FXML private ImageView familyIcon;
    @FXML private TextField familyNameTextField;
    @FXML private ImageView homeIcon;
    @FXML private ImageView resourceIcon;
    @FXML private Button saveBtn;
    @FXML private Button backBtn;
    @FXML private ImageView uploadIcon;

    @FXML
    private Spinner<Integer> familySizeSpinner;

    private byte[] selectedImageBytes = null;
    private FamilyDao familyDAO;

    @FXML
    public void initialize() {
        this.familyDAO = new FamilyDao();

        saveBtn.setOnAction(event -> handleSaveFamily());
        backBtn.setOnAction(event -> UtilityMethods.switchToScene(backBtn,"view_families"));

        homeIcon.setOnMouseClicked(event -> UtilityMethods.switchToScene(homeIcon,"home_view"));
        resourceIcon.setOnMouseClicked(event -> UtilityMethods.switchToScene(resourceIcon,"view_resources"));
        allocationIcon.setOnMouseClicked(event -> UtilityMethods.switchToScene(allocationIcon,"view_allocation"));

        uploadIcon.setOnMouseClicked(event -> handleImageUpload());

        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 1);
        familySizeSpinner.setValueFactory(valueFactory);
    }

    private void handleImageUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Family Image (Max 2MB)");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(uploadIcon.getScene().getWindow());
        if (selectedFile != null) {
            if (selectedFile.length() > 2 * 1024 * 1024) { // 2 MB limit
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

                Image image = new Image(selectedFile.toURI().toString(), 66, 61, true, true);
                uploadIcon.setImage(image);

            } catch (IOException e) {
                e.printStackTrace();
                UtilityMethods.showPopupWarning("Failed to load image.");
            }
        }
    }

    @FXML
    private void handleSaveFamily() {
        String headName = familyNameTextField.getText().trim();
        String address = familyAddressTextField.getText().trim();

        if (headName.isEmpty()) {
            UtilityMethods.showPopupWarning("Family head name cannot be empty.");
            return;
        }
        if (address.isEmpty()) {
            UtilityMethods.showPopupWarning("Family address cannot be empty.");
            return;
        }

        int familySize = familySizeSpinner.getValue();
        if (familySize <= 0) {
            UtilityMethods.showPopupWarning("Family size must be at least 1.");
            return;
        }

        String notes = contactTextField.getText().trim();

        if (SessionManager.getCurrentBarangayId() == 0) {
            UtilityMethods.showPopupWarning("Error: No barangay is currently selected.");
            SessionManager.setCurrentBarangayId(1);
        }
        int barangayId = SessionManager.getCurrentBarangayId();

        Family newFamily = new Family(headName, familySize, address, notes, barangayId, selectedImageBytes);

        int newFamilyId = familyDAO.addFamily(newFamily);
        if (newFamilyId > 0) {
            UtilityMethods.showPopup("Family added successfully!");
            clearFields();
            UtilityMethods.switchToScene(saveBtn,"view_families");
        } else {
            UtilityMethods.showPopupWarning("Failed to add family. A family with this name may already exist.");
        }
    }

    private void clearFields() {
        familyNameTextField.clear();
        familyAddressTextField.clear();
        contactTextField.clear();

        familySizeSpinner.getValueFactory().setValue(1);

        uploadIcon.setImage(new Image(getClass().getResourceAsStream("/icons/add-family-icon.png")));
        selectedImageBytes = null;
    }
}