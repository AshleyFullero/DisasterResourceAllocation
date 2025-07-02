package org.htech.disasterproject.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.htech.disasterproject.modal.Resource;

import java.io.ByteArrayInputStream;
import java.text.DecimalFormat;

public class ResourceCardController {

    @FXML
    private VBox cardPane;

    @FXML
    private Label importanceLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private ImageView resourceImageView;

    @FXML
    private Label stockLabel;

    private static final DecimalFormat df = new DecimalFormat("0.##");

    public void setData(Resource resource) {
        String weightString = df.format(resource.getWeightKg());
        nameLabel.setText(String.format("%s (%s%s)", resource.getName(), weightString, resource.getUnitType()));
        importanceLabel.setText("Importance: " + resource.getImportanceScore());
        stockLabel.setText("In Stock: " + resource.getTotalQuantity());

        if(resource.getImageBytes() != null) {
        resourceImageView.setImage(new Image(new ByteArrayInputStream(resource.getImageBytes())));
        }
    }
}