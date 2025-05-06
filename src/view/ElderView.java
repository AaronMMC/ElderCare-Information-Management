package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ElderView {

    private final Scene scene;

    public ElderView(Stage stage) {
        HBox root = new HBox();
        root.setPadding(new Insets(20));

        this.scene = new Scene(root, 1100, 600);
        stage.setTitle("Add an Elder");
        stage.setScene(scene);
        stage.show();
    }

    public Scene getScene() {return scene;}
}
