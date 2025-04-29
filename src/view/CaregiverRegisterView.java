package view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;

public class CaregiverRegisterView {

    private final Scene scene;

    //TODO: Fix design. Parameter should always have (conn) so that you can utilize CaregiverController to register a new caregiver to the DB.
    public CaregiverRegisterView(Stage stage, Connection conn) {
        //Dummy
        Label title = new Label("Register as Caregiver");
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        TextField emailField = new TextField();
        Button registerButton = new Button("Register");
        Button backButton = new Button("Back to Login");

        VBox layout = new VBox(10, title, usernameField, passwordField, emailField, registerButton, backButton);
        layout.setStyle("-fx-padding: 20;");

        registerButton.setOnAction(e -> {});
        backButton.setOnAction(e -> {});

        this.scene = new Scene(layout, 400, 400);
        stage.setScene(scene);
    }

    public Scene getScene() {
        return scene;
    }
}
