package org.htech.disasterproject.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.htech.disasterproject.modal.Family;
import org.htech.disasterproject.modal.Resource;
import org.htech.disasterproject.service.KnapsackAllocationService.AllocationResult;

import java.util.*;
import java.util.stream.Collectors;

public class FinalReportController {

    @FXML
    private TableView<Map<String, String>> allocationTableView;

    @FXML
    private Button closeButton;

    @FXML
    private Label remainingWeightLabel;


    @FXML
    public void initialize() {
        closeButton.setOnAction(event -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });
    }

    public void setData(AllocationResult result) {
        if (result == null || result.allocatedItems().isEmpty()) {
            remainingWeightLabel.setText("No data to display.");
            return;
        }

        allocationTableView.getColumns().clear();

        List<Family> sortedFamilies = new ArrayList<>(result.allocatedItems().keySet());
        sortedFamilies.sort(Comparator.comparingInt(Family::getFamilySize).reversed());

        Map<Family, Integer> familyRanks = new HashMap<>();
        int rank = 0;
        int lastSize = -1;
        for (int i = 0; i < sortedFamilies.size(); i++) {
            Family f = sortedFamilies.get(i);
            if (f.getFamilySize() != lastSize) {
                rank = i + 1;
            }
            familyRanks.put(f, rank);
            lastSize = f.getFamilySize();
        }

        TableColumn<Map<String, String>, Void> priorityCol = new TableColumn<>("Priority");
        priorityCol.setPrefWidth(70);
        priorityCol.setSortable(false);

        priorityCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setStyle("");
                setTooltip(null);

                if (empty || getIndex() >= getTableView().getItems().size()) {
                    return;
                }

                Map<String, String> rowData = getTableView().getItems().get(getIndex());
                String rankStr = rowData.get("rank");

                if (rankStr != null) {
                    int rank = Integer.parseInt(rankStr);
                    String color;
                    String tooltipText;

                    if (rank <= 2) {
                        color = "#ff0000";
                        tooltipText = "High Priority (Rank 1-2)";
                    } else if (rank <= 4) {
                        color ="#ffff00";
                        tooltipText = "Moderate Priority (Rank 3-4)";
                    } else {
                        color = "#00ff00";
                        tooltipText = "Low Priority";
                    }
                    setStyle("-fx-background-color: " + color + "; -fx-border-color: #e0e0e0; -fx-border-width: 0 1 1 0;");
                    setTooltip(new Tooltip(tooltipText));
                }else{
                    setStyle("");
                }
            }
        });
        allocationTableView.getColumns().add(priorityCol);

        TableColumn<Map<String, String>, String> familyNameCol = new TableColumn<>("Family Name");
        familyNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("Family Name")));
        familyNameCol.setPrefWidth(150);
        allocationTableView.getColumns().add(familyNameCol);

        List<Resource> resourceHeaders = result.allocatedItems().values().stream()
                .flatMap(m -> m.keySet().stream())
                .distinct()
                .sorted(Comparator.comparing(Resource::getName))
                .toList();

        for (Resource resource : resourceHeaders) {
            TableColumn<Map<String, String>, String> resourceCol = new TableColumn<>(resource.getName());
            resourceCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(resource.getName())));
            resourceCol.setStyle("-fx-alignment: CENTER;");
            allocationTableView.getColumns().add(resourceCol);
        }

        ObservableList<Map<String, String>> tableData = FXCollections.observableArrayList();

        for (Family family : sortedFamilies) {
            Map<String, String> row = new LinkedHashMap<>();
            row.put("rank", String.valueOf(familyRanks.get(family)));
            row.put("Family Name", family.getFamilyHeadName());

            Map<Resource, Integer> allocations = result.allocatedItems().get(family);
            for (Resource header : resourceHeaders) {
                int quantity = allocations.getOrDefault(header, 0);
                row.put(header.getName(), String.valueOf(quantity));
            }
            tableData.add(row);
        }

        Map<String, String> remainingRow = new LinkedHashMap<>();
        remainingRow.put("Family Name", "Units Remaining");
        for (Resource header : resourceHeaders) {
            int remainingQty = result.remainingResources().getOrDefault(header, 0);
            remainingRow.put(header.getName(), String.valueOf(remainingQty));
        }
        tableData.add(remainingRow);

        allocationTableView.setItems(tableData);

        remainingWeightLabel.setText(String.format("Final Remaining Knapsack Capacity: %.2f KG", result.finalRemainingWeight()));
    }
}