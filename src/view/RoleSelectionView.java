package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.function.Consumer;

public class RoleSelectionView {

    private final Scene scene;

    public RoleSelectionView(Stage stage, List<String> roles, Consumer<String> onRoleSelected) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.getChildren().add(new Label("Multiple roles found. Please choose your login role:"));

        //Note: Do not alter the for loop since it is responsible for creating buttons of which role is available for a given user based of the string of roles passed into this class.
        for (String role : roles) {
            Button button = new Button(role);
            button.setOnAction(e -> onRoleSelected.accept(role));
            root.getChildren().add(button);
        }

        this.scene = new Scene(root, 300, 200);
    }

    public Scene getScene() {
        return scene;
    }
}