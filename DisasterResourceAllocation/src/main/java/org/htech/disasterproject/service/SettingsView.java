package org.htech.disasterproject.service;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.htech.disasterproject.utilities.AllocationConfig;

public class SettingsView {

    public static void display(Stage parentStage, AllocationConfig config) {

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(parentStage);
        dialogStage.setTitle("Allocation Settings");

        Spinner<Double> knapsackCapacitySpinner = new Spinner<>(1.0, 10000.0, config.getKnapsackCapacity(), 10.0);
        knapsackCapacitySpinner.setEditable(true);

        CheckBox infantCheckBox = new CheckBox("Prioritize Families with Infants (for Baby Formula)");

        CheckBox waterCheckBox = new CheckBox("Guarantee 1 Water per Family (Heuristic Only)");
        CheckBox riceCheckBox = new CheckBox("Guarantee 1 Rice per Family (Heuristic Only)");
        CheckBox bonusCheckBox = new CheckBox("Give Bonus Item to Largest Family (Heuristic Only)");
        CheckBox kitsLastCheckBox = new CheckBox("Distribute 'Kits' Last (Heuristic Only)");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 0, 20, 0));
        grid.add(new Label("Total Knapsack Capacity (KG):"), 0, 0);
        grid.add(knapsackCapacitySpinner, 1, 0);
        root.setTop(grid);

        VBox featuresBox = new VBox(10);
        featuresBox.setPadding(new Insets(10));
        featuresBox.getChildren().addAll(
                infantCheckBox, waterCheckBox, riceCheckBox, bonusCheckBox, kitsLastCheckBox
        );
        TitledPane featuresPane = new TitledPane("Algorithm Features (On/Off)", featuresBox);
        featuresPane.setCollapsible(false);
        root.setCenter(featuresPane);

        Button saveButton = new Button("Save");
        saveButton.setDefaultButton(true);
        Button cancelButton = new Button("Cancel");
        cancelButton.setCancelButton(true);

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(20, 0, 10, 0));
        root.setBottom(buttonBox);

        infantCheckBox.setSelected(config.isPrioritizeInfantFamilies());
        waterCheckBox.setSelected(config.isGuaranteeWaterPerFamily());
        riceCheckBox.setSelected(config.isGuaranteeRicePerFamily());
        bonusCheckBox.setSelected(config.isEnableBonusForLargestFamily());
        kitsLastCheckBox.setSelected(config.isDistributeKitsLast());

        cancelButton.setOnAction(e -> dialogStage.close());

        saveButton.setOnAction(e -> {
            config.setKnapsackCapacity(knapsackCapacitySpinner.getValue());
            config.setPrioritizeInfantFamilies(infantCheckBox.isSelected());

            config.setGuaranteeWaterPerFamily(waterCheckBox.isSelected());
            config.setGuaranteeRicePerFamily(riceCheckBox.isSelected());
            config.setEnableBonusForLargestFamily(bonusCheckBox.isSelected());
            config.setDistributeKitsLast(kitsLastCheckBox.isSelected());

            config.save();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Settings saved successfully!");
            alert.showAndWait();

            dialogStage.close();
        });

        Scene scene = new Scene(root);
        dialogStage.setScene(scene);
        dialogStage.sizeToScene();
        dialogStage.setResizable(false);
        dialogStage.showAndWait();
    }
}