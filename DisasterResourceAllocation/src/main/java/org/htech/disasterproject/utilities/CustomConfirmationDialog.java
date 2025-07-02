package org.htech.disasterproject.utilities;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static org.htech.disasterproject.utilities.UtilityMethods.setupMovement;

public class CustomConfirmationDialog {

    public interface ConfirmationHandler {
        void onConfirm();
        void onCancel();
    }

    public static void show(String message, ConfirmationHandler handler) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);


        VBox root = new VBox(20);
        root.setStyle("-fx-background-color: white; -fx-border-color: navy; -fx-border-radius: 12; -fx-background-radius: 12;");
        root.setAlignment(Pos.CENTER);
        root.setPrefWidth(320);
        root.setPrefHeight(420);

        setupMovement(dialog,root);

        Polygon triangle = new Polygon(
            0.0, 40.0,
            20.0, 0.0,
            40.0, 40.0
        );
        triangle.setFill(Color.GOLD);

        Label exclamation = new Label("!");
        exclamation.setTextFill(Color.DARKSLATEGRAY);
        exclamation.setFont(new Font(20));

        StackPane icon = new StackPane(triangle, exclamation);
        icon.setPrefSize(60, 60);

        Label lblMessage = new Label(message);
        lblMessage.setFont(Font.font("Arial", 18));
        lblMessage.setWrapText(true);
        lblMessage.setTextFill(Color.DARKBLUE);
        lblMessage.setAlignment(Pos.CENTER);
        lblMessage.setMaxWidth(250);

        Button confirmBtn = new Button("Confirm Deletion");
        confirmBtn.setStyle("-fx-background-color: white; -fx-text-fill: navy; -fx-border-color: navy; -fx-border-radius: 10; -fx-background-radius: 10;");
        confirmBtn.setOnAction(e -> {
            dialog.close();
            if (handler != null) handler.onConfirm();
        });

        Button cancelBtn = new Button("Go Back");
        cancelBtn.setStyle("-fx-background-color: navy; -fx-text-fill: white; -fx-background-radius: 14;");
        cancelBtn.setOnAction(e -> {
            dialog.close();
            if (handler != null) handler.onCancel();
        });

        confirmBtn.setPrefWidth(180);
        cancelBtn.setPrefWidth(180);

        VBox buttonBox = new VBox(10, confirmBtn, cancelBtn);
        buttonBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(icon, lblMessage, buttonBox);

        Scene scene = new Scene(root);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
}
