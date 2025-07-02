package org.htech.disasterproject.controllers;

import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.htech.disasterproject.dao.FamilyDao;
import org.htech.disasterproject.dao.ResourceDao;
import org.htech.disasterproject.modal.Family;
import org.htech.disasterproject.modal.Resource;
import org.htech.disasterproject.modal.User;
import org.htech.disasterproject.service.KnapsackAllocationService;
import org.htech.disasterproject.service.KnapsackAllocationTask;
import org.htech.disasterproject.service.SettingsView;
import org.htech.disasterproject.start.Navigation;
import org.htech.disasterproject.utilities.AllocationConfig;
import org.htech.disasterproject.utilities.ProgressDialog;
import org.htech.disasterproject.utilities.SessionManager;
import org.htech.disasterproject.utilities.UtilityMethods;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static org.htech.disasterproject.utilities.UtilityMethods.setupMovement;

public class ViewAllocationController extends Navigation implements Initializable {

    @FXML
    private ImageView allocationIcon, familyIcon, homeIcon, resourceIcon, settingsIcon;
    @FXML
    private Button runBtn, downloadReportBtn;
    @FXML
    protected PieChart allocationPieChart;
    @FXML
    private Label totalFamiliesLabel, totalResourcesLabel;

    private KnapsackAllocationService.AllocationResult lastAllocationResult;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        homeIcon.setOnMouseClicked(mouseEvent -> handleBackAction(homeIcon));
        resourceIcon.setOnMouseClicked(mouseEvent -> UtilityMethods.switchToScene(resourceIcon, "view_resources"));
        familyIcon.setOnMouseClicked(mouseEvent -> UtilityMethods.switchToScene(familyIcon, "view_families"));
        runBtn.setOnMouseClicked(mouseEvent -> runAllocationProcess());
        downloadReportBtn.setOnAction(mouseEvent -> generateAndSaveReport());
        settingsIcon.setOnMouseClicked(mouseEvent -> {
            if (SessionManager.getSelectedRole().equals(User.Role.ADMIN)) {
                Stage stage = (Stage) settingsIcon.getScene().getWindow();
                SettingsView.display(stage, new AllocationConfig());
            } else {
                UtilityMethods.showPopupWarning("Only admin can access settings.");
            }
        });
    }

    private void runAllocationProcess() {
        runBtn.setDisable(true);
        downloadReportBtn.setDisable(true);

        FamilyDao familyDao = new FamilyDao();
        ResourceDao resourceDao = new ResourceDao();
        List<Family> allFamilies = SessionManager.getSelectedRole().equals(User.Role.ADMIN)
                ? familyDao.getAllFamiliesWithBarangay()
                : familyDao.getAllFamiliesByBarangay(SessionManager.getCurrentBarangayId());
        List<Resource> allResources = resourceDao.getAllResources();

        if (allFamilies.isEmpty() || allResources.isEmpty()) {
            UtilityMethods.showPopupWarning("No families or resources found to allocate.");
            runBtn.setDisable(false);
            return;
        }

        ProgressDialog dialog = new ProgressDialog(homeIcon.getScene().getWindow());
        KnapsackAllocationTask allocationTask = new KnapsackAllocationTask(allFamilies, allResources);

        allocationTask.setOnSucceeded(event -> {
            this.lastAllocationResult = allocationTask.getValue();
            populateUIWithResults(this.lastAllocationResult);
            runBtn.setDisable(false);
            if (this.lastAllocationResult != null && !this.lastAllocationResult.allocatedItems().isEmpty()) {
                downloadReportBtn.setDisable(false);
            }
        });

        allocationTask.setOnFailed(event -> {
            System.err.println("The allocation task failed.");
            allocationTask.getException().printStackTrace();
            UtilityMethods.showPopupWarning("An error occurred during the allocation process.");
            runBtn.setDisable(false);
        });

        dialog.bindToTask(allocationTask);
        dialog.show();
        new Thread(allocationTask).start();
    }

    private void populateUIWithResults(KnapsackAllocationService.AllocationResult result) {
        if (result == null || result.allocatedItems().isEmpty()) {
            totalFamiliesLabel.setText("No allocations could be made.");
            totalResourcesLabel.setText("Check resources or knapsack capacity.");
            allocationPieChart.setData(FXCollections.observableArrayList());
            return;
        }

        Map<String, Integer> pieDataMap = result.allocatedItems().values().stream()
                .flatMap(map -> map.entrySet().stream())
                .filter(entry -> entry.getValue() > 0)
                .collect(Collectors.groupingBy(
                        entry -> entry.getKey().getName(),
                        Collectors.summingInt(Map.Entry::getValue)
                ));

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        pieDataMap.forEach((resourceName, totalQuantity) -> {
            pieChartData.add(new PieChart.Data(resourceName + " (" + totalQuantity + ")", totalQuantity));
        });
        allocationPieChart.setData(pieChartData);
        applyPieChartSliceAnimations();


        long distinctFamilies = result.allocatedItems().keySet().size();
        int totalItems = pieDataMap.values().stream().mapToInt(Integer::intValue).sum();

        totalFamiliesLabel.setText("Total Families Served: " + distinctFamilies);
        totalResourcesLabel.setText("Total Items Distributed: " + totalItems);

        showFinalReportModal(result);
    }

    private void showFinalReportModal(KnapsackAllocationService.AllocationResult result) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/final_report_model.fxml"));
            VBox page = loader.load();

            Stage modalStage = new Stage();
            modalStage.setTitle("Final Allocation Report");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initStyle(StageStyle.UNDECORATED);
            modalStage.initOwner(homeIcon.getScene().getWindow());

            Scene scene = new Scene(page);
            modalStage.setScene(scene);

            FinalReportController controller = loader.getController();
            controller.setData(result);

            setupMovement(modalStage, page);
            modalStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            UtilityMethods.showPopupWarning("Could not open the final report view.");
        }
    }

    private void generateAndSaveReport() {
        if (lastAllocationResult == null || lastAllocationResult.allocatedItems().isEmpty()) {
            UtilityMethods.showPopupWarning("No allocation data to generate a report.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Allocation Report");
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        fileChooser.setInitialFileName("AllocationReport_Knapsack_" + timestamp + ".csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(downloadReportBtn.getScene().getWindow());

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                List<Resource> headers = lastAllocationResult.allocatedItems().values().stream()
                        .flatMap(m -> m.keySet().stream())
                        .distinct()
                        .sorted(Comparator.comparing(Resource::getName))
                        .toList();

                writer.print("Family Name,");
                writer.println(headers.stream().map(Resource::getName).collect(Collectors.joining(",")));

                for (Map.Entry<Family, Map<Resource, Integer>> entry : lastAllocationResult.allocatedItems().entrySet()) {
                    Family family = entry.getKey();
                    Map<Resource, Integer> allocations = entry.getValue();
                    writer.print("\"" + family.getFamilyHeadName() + "\",");

                    String rowData = headers.stream()
                            .map(h -> allocations.getOrDefault(h, 0).toString())
                            .collect(Collectors.joining(","));
                    writer.println(rowData);
                }

                writer.print("Units Remaining,");
                Map<Resource, Integer> remaining = lastAllocationResult.remainingResources();
                String remainingData = headers.stream()
                        .map(h -> remaining.getOrDefault(h, 0).toString())
                        .collect(Collectors.joining(","));
                writer.println(remainingData);

                UtilityMethods.showPopup("Report saved successfully to " + file.getAbsolutePath());

            } catch (IOException e) {
                e.printStackTrace();
                UtilityMethods.showPopupWarning("Error saving report: " + e.getMessage());
            }
        }
    }

    protected void applyPieChartSliceAnimations() {
        for (PieChart.Data data : allocationPieChart.getData()) {
            data.getNode().setOnMouseEntered(e -> {
                ScaleTransition st = new ScaleTransition(Duration.millis(200), data.getNode());
                st.setToX(1.1);
                st.setToY(1.1);
                st.play();
            });
            data.getNode().setOnMouseExited(e -> {
                ScaleTransition st = new ScaleTransition(Duration.millis(200), data.getNode());
                st.setToX(1.0);
                st.setToY(1.0);
                st.play();
            });
        }
    }
}