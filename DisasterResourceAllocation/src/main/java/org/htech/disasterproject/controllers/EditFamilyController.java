package org.htech.disasterproject.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.htech.disasterproject.dao.FamilyDao;
import org.htech.disasterproject.modal.Family;
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
import java.net.URL;
import java.util.ResourceBundle;

public class EditFamilyController extends Navigation implements Initializable {

    @FXML private ImageView allocationIcon;
    @FXML private ImageView familyIcon;
    @FXML private ImageView homeIcon;
    @FXML private ImageView resourceIcon;
    @FXML private ImageView iconUpload;
    @FXML private TextField contactTextField;
    @FXML private TextField familyAddressTextField;
    @FXML private TextField familyNameTextField;
    @FXML private Button saveBtn;
    @FXML private Button deleteBtn;
    @FXML private Spinner<Integer> familySizeSpinner;

    static Family family;
    FamilyDao familyDAO;
    private byte[] selectedImageBytes = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        familyDAO = new FamilyDao();

        homeIcon.setOnMouseClicked(event -> handleBackAction());
        familyIcon.setOnMouseClicked(event -> UtilityMethods.switchToScene(familyAddressTextField, "view_families"));
        resourceIcon.setOnMouseClicked(e -> UtilityMethods.switchToScene(resourceIcon, "view_resources"));
        allocationIcon.setOnMouseClicked(event -> UtilityMethods.switchToScene(allocationIcon, "view_allocation"));
        saveBtn.setOnAction(e -> handleUpdateFamily());
        deleteBtn.setOnAction(e -> handleDelete());

        iconUpload.setOnMouseClicked(event -> handleImageUpload());

        familyNameTextField.setText(family.getFamilyHeadName());
        familyAddressTextField.setText(family.getAddress());
        contactTextField.setText(family.getNotes());

        if (family.getImageBytes() != null) {
            iconUpload.setImage(new Image(new ByteArrayInputStream(family.getImageBytes())));
            selectedImageBytes = family.getImageBytes();
        }

        familyNameTextField.setEditable(false);

        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, family.getFamilySize());
        familySizeSpinner.setValueFactory(valueFactory);
    }

    private void handleImageUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Family Image (Max 2MB)");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(iconUpload.getScene().getWindow());
        if (selectedFile != null) {
            if (selectedFile.length() > 2 * 1024 * 1024) { // 2MB limit
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
                iconUpload.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
                UtilityMethods.showPopupWarning("Failed to load image.");
            }
        }
    }

    private void handleDelete() {
        if (SessionManager.getSelectedRole() == User.Role.VOLUNTEER) {
            UtilityMethods.showPopupWarning("Only chairman or admin can delete");
            return;
        }
        CustomConfirmationDialog.show(
                "Are you sure you want to delete this Family?",
                new CustomConfirmationDialog.ConfirmationHandler() {
                    @Override
                    public void onConfirm() {
                        if (familyDAO.deleteFamily(family.getId())) {
                            UtilityMethods.showPopup("Family deleted successfully!");
                            UtilityMethods.switchToScene(familyIcon, "view_families");
                        }
                    }
                    @Override
                    public void onCancel() {
                        System.out.println("Delete cancelled.");
                    }
                }
        );
    }

    private void handleUpdateFamily() {
        String headName = familyNameTextField.getText().trim();
        String address = familyAddressTextField.getText().trim();

        if (headName.isEmpty() || address.isEmpty()) {
            UtilityMethods.showPopupWarning("Family head name and address cannot be empty.");
            return;
        }

        int familySize = familySizeSpinner.getValue();
        String notes = contactTextField.getText().trim();
        int barangayId = family.getBarangayId();

        Family updatedFamily = new Family(family.getId(), headName, familySize, address, notes, barangayId, selectedImageBytes);

        boolean updated = familyDAO.updateFamily(updatedFamily);
        if (updated) {
            UtilityMethods.showPopup("Family updated successfully!");
            UtilityMethods.switchToScene(saveBtn, "view_families");
        } else {
            UtilityMethods.showPopupWarning("Failed to update family.");
        }
    }

    public static void initResourceData(Family selectedFamily) {
        family = selectedFamily;
    }

    public void handleBackAction() {
        UtilityMethods.switchToScene(homeIcon, "home_view");
    }
}