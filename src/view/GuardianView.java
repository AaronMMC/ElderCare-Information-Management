package view;

import controller.GuardianController;
import controller.GuardianElderController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Guardian;

import java.sql.Connection;

public class GuardianView {

    private final GuardianController guardianController;
    private final GuardianElderController guardianElderController;
    private final Scene scene;

    public GuardianView(Stage stage, Connection conn, Guardian guardian) {
        this.guardianController = new GuardianController(conn);
        this.guardianElderController = new GuardianElderController(conn);

        // GUI Layout
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        // TODO: Add GUI components to root

        this.scene = new Scene(root, 500, 400);
        stage.setTitle("Guardian Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    public Scene getScene() {
        return scene;
    }
}