package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Guardian;

import java.sql.Connection;

public class AppointmentView {

    private final Scene scene;

    public AppointmentView(Stage stage, Connection conn, Guardian guardian) {
        HBox root = new HBox();
        root.setPadding(new Insets(20));

        this.scene = new Scene(root, 1100, 600);
        stage.setTitle("Add an Elder");
        stage.setScene(scene);
        stage.show();
    }

    public Scene getScene() {
        return scene;
    }
}
