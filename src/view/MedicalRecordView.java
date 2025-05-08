package view;

import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Elder;

import java.sql.Connection;

public class MedicalRecordView {

    private Scene scene;
    private final Connection conn;

    public MedicalRecordView(Stage stage, Connection conn, Elder elder) {
        this.conn = conn;
    }

    public Scene getScene() {
        return scene;
    }
}
