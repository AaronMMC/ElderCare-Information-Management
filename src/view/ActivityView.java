package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Appointment;

import java.sql.Connection;

public class ActivityView {

    private final Scene scene;

    public ActivityView(Stage stage, Connection conn, Appointment appointment) {
        // === Root Layout ===
        HBox root = new HBox(20);
        root.setPadding(new Insets(20));

        this.scene = new Scene(root, 1000, 600);
        stage.setTitle("Activity Details");
        stage.setScene(scene);
        stage.show();
    }

    public Scene getScene() {
        return scene;
    }
}
