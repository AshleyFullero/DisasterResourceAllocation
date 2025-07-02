package org.htech.disasterproject.utilities;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.htech.disasterproject.modal.Family;

import java.io.ByteArrayInputStream;
import java.util.function.Consumer;

public class FamilyListCell extends ListCell<Family> {

    private final HBox hbox;
    private final Text familyNameText;
    private Label priorityScoreLabel;
    private final Label familySizeLabel;
    private final Button editButton;
    private final Consumer<Family> editAction;
    private final ImageView profileImageView;

    public FamilyListCell(Consumer<Family> editAction) {
        super();
        this.editAction = editAction;

        profileImageView = new ImageView();
        profileImageView.setFitHeight(50);
        profileImageView.setFitWidth(50);
        profileImageView.setSmooth(true);
        profileImageView.setClip(new javafx.scene.shape.Circle(25, 25, 25));
        profileImageView.setPreserveRatio(false);


        Text familyLabelText = new Text("FAMILY ");
        familyLabelText.setFont(Font.font("System", FontWeight.BOLD, 16));
        familyLabelText.setFill(Color.WHITE);

        familyNameText = new Text();
        familyNameText.setFont(Font.font("System", FontWeight.BOLD, 16));
        familyNameText.setFill(Color.web("#FFD700"));

        TextFlow familyTitle = new TextFlow(familyLabelText, familyNameText);

        priorityScoreLabel = new Label();
        priorityScoreLabel.setTextFill(Color.WHITE);

        familySizeLabel = new Label();
        familySizeLabel.setTextFill(Color.WHITE);

        VBox familyInfoVBox = new VBox(5, familyTitle, priorityScoreLabel, familySizeLabel);
        familyInfoVBox.setAlignment(Pos.CENTER_LEFT);

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/Edit.png")));
        editIcon.setFitHeight(24);
        editIcon.setFitWidth(24);
        editButton = new Button("", editIcon);
        editButton.setStyle("-fx-background-color: transparent;");

        hbox = new HBox(15, profileImageView, familyInfoVBox, spacer, editButton);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(10));
        hbox.setStyle("-fx-background-color: #110D65; -fx-background-radius: 20;");
    }

    @Override
    protected void updateItem(Family family, boolean empty) {
        super.updateItem(family, empty);

        if (empty || family == null) {
            setText(null);
            setGraphic(null);
        } else {
            familyNameText.setText(family.getFamilyHeadName());
            priorityScoreLabel.setText("Priority Score: " + String.format("%.3f", family.getPriorityScore()));
            familySizeLabel.setText("Size: " + family.getFamilySize());

            if (family.getImageBytes() != null) {
                profileImageView.setImage(new Image(new ByteArrayInputStream(family.getImageBytes())));
            } else {
                profileImageView.setImage(new Image(getClass().getResourceAsStream("/icons/circle.png")));
            }

            editButton.setOnAction(event -> {
                if (editAction != null) {
                    editAction.accept(family);
                }
            });

            setGraphic(hbox);
        }
    }
}
