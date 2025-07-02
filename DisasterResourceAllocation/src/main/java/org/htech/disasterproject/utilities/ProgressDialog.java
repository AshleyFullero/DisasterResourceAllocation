package org.htech.disasterproject.utilities;

import javafx.animation.*;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

public class ProgressDialog {

    private final Stage dialog;
    private final Label statusLabel;
    private final ParallelTransition animation;

    public ProgressDialog(Window owner) {
        dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setResizable(false);

        StackPane animationPane = new StackPane();
        animationPane.setPrefSize(150, 150);

        Circle c1 = createPulseCircle();
        Circle c2 = createPulseCircle();
        Circle c3 = createPulseCircle();

        animationPane.getChildren().addAll(c1, c2, c3);

        animation = new ParallelTransition(
            createSinglePulse(c1, Duration.ZERO),
            createSinglePulse(c2, Duration.millis(300)),
            createSinglePulse(c3, Duration.millis(600))
        );
        animation.setCycleCount(Animation.INDEFINITE);


        statusLabel = new Label("Initializing...");
        statusLabel.setFont(Font.font("Arial", 16));
        statusLabel.setTextFill(Color.WHITE);

        VBox content = new VBox(30, animationPane, statusLabel);
        content.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(content);
        root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6); -fx-background-radius: 20;");
        root.setPrefSize(350, 300);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
    }

    private Circle createPulseCircle() {
        Circle circle = new Circle(50, Color.TRANSPARENT);
        circle.setStroke(Color.valueOf("#00BFFF"));
        circle.setStrokeWidth(3);
        circle.setOpacity(0.9);
        return circle;
    }

    private Transition createSinglePulse(Circle circle, Duration delay) {
        FadeTransition ft = new FadeTransition(Duration.seconds(2), circle);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);

        ScaleTransition st = new ScaleTransition(Duration.seconds(2), circle);
        st.setFromX(0.1);
        st.setToX(1.0);
        st.setFromY(0.1);
        st.setToY(1.0);

        ParallelTransition pt = new ParallelTransition(circle, ft, st);
        pt.setDelay(delay);
        return pt;
    }

    public void bindToTask(Task<?> task) {
        statusLabel.textProperty().bind(task.messageProperty());

        task.runningProperty().addListener((obs, wasRunning, isRunning) -> {
            if (!isRunning) {
                close();
            }
        });
    }

    public void show() {
        animation.play();
        dialog.show();
    }

    public void close() {
        animation.stop();
        dialog.close();
    }
}