package view;

import controller.CaregiverController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Caregiver;

import java.sql.Connection;

public class CaregiverView {

    private final CaregiverController caregiverController;
    private final Scene scene;

    public CaregiverView(Stage stage, Connection conn, Caregiver caregiver) {
        this.caregiverController = new CaregiverController(conn);

        // GUI Layout
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        // TODO: Add GUI components to root

        this.scene = new Scene(root, 500, 400);
        stage.setTitle("Caregiver Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    public Scene getScene() {
        return scene;
    }
}
