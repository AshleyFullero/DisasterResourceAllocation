package org.htech.disasterproject.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.htech.disasterproject.dao.FamilyDao;
import org.htech.disasterproject.modal.Family;
import org.htech.disasterproject.modal.User;
import org.htech.disasterproject.utilities.FamilyListCell;
import org.htech.disasterproject.utilities.SessionManager;
import org.htech.disasterproject.utilities.UtilityMethods;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ViewFamiliesController implements Initializable {

    @FXML
    private ListView<Family> familiesListView;

    @FXML private ImageView allocationIcon;
    @FXML private ImageView familyIcon;
    @FXML private ImageView homeIcon;
    @FXML private ImageView resourceIcon;
    @FXML private TextField searchBar;
    @FXML private Button addFamilyBtn;

    private FamilyDao familyDao;
    private ObservableList<Family> allFamilies = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        familyDao = new FamilyDao();

        familiesListView.setCellFactory(listView -> new FamilyListCell(this::handleEditFamily));

        loadFamilies();

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filterFamilies(newValue);
        });

        addFamilyBtn.setOnAction(event -> {
            UtilityMethods.switchToScene(addFamilyBtn, "add_family");
        });
        homeIcon.setOnMouseClicked(event -> {
            UtilityMethods.switchToScene(homeIcon, "home_view");
        });
        resourceIcon.setOnMouseClicked(event -> {
            UtilityMethods.switchToScene(resourceIcon, "view_resources");
        });
        allocationIcon.setOnMouseClicked(event -> {
            UtilityMethods.switchToScene(allocationIcon, "view_allocation");
        });
    }

    private void loadFamilies() {
        int barangayId = SessionManager.getCurrentBarangayId();
        if (barangayId == 0) {
            barangayId = 1;
        }

        List<Family> familiesFromDb;
        System.out.println(SessionManager.getSelectedRole());
        System.out.println(barangayId);
        if(SessionManager.getCurrentUser().getRole().equals(User.Role.ADMIN)){
            familiesFromDb = familyDao.getAllFamilies();
        }
        else if(SessionManager.getCurrentUser().getRole().equals(User.Role.CHAIRMAN)){
            familiesFromDb = familyDao.getAllFamiliesByBarangay(barangayId);
        }else{
            familiesFromDb = familyDao.getAllFamiliesByBarangay(SessionManager.getCurrentUser().getBarangayId());
        }

        int totalPeople = familiesFromDb.stream().mapToInt(Family::getFamilySize).sum();

        for (Family family : familiesFromDb) {
            double score = (totalPeople == 0) ? 0.0 : (double) family.getFamilySize() / totalPeople;
            family.setPriorityScore(score);
        }


        allFamilies.setAll(familiesFromDb);

        familiesListView.setItems(allFamilies);
    }

    private void filterFamilies(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            familiesListView.setItems(allFamilies);
            return;
        }

        ObservableList<Family> filteredList = FXCollections.observableArrayList();
        String lowerCaseFilter = searchText.toLowerCase();

        for (Family family : allFamilies) {
            if (family.getFamilyHeadName().toLowerCase().contains(lowerCaseFilter)) {
                filteredList.add(family);
            }
        }
        familiesListView.setItems(filteredList);
    }

    private void handleEditFamily(Family family) {

         SessionManager.setSelectedFamily(family);
         EditFamilyController.initResourceData(family);
         UtilityMethods.switchToScene(addFamilyBtn, "edit_family");
        UtilityMethods.showPopup("Edit functionality for " + family.getFamilyHeadName() + " would be here.");
    }
}