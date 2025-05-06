//package view;
//
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//
//import java.sql.Connection;
//
//public class GuardianRegisterView {
//
//    private final Scene scene;
//
//    //TODO: Fix design. Parameter should always have (conn) so that you can utilize GuardianController to register a new caregiver to the DB.
//    public GuardianRegisterView(Stage stage, Connection conn) {
//        // Dummy
//        Label title = new Label("Register as Guardian");
//        TextField usernameField = new TextField();
//        PasswordField passwordField = new PasswordField();
//        TextField emailField = new TextField();
//        Button registerButton = new Button("Register");
//        Button backButton = new Button("Back to Login");
//
//        VBox layout = new VBox(10, title, usernameField, passwordField, emailField, registerButton, backButton);
//        layout.setStyle("-fx-padding: 20;");
//
//        registerButton.setOnAction(e -> {});
//        backButton.setOnAction(e -> {});
//
//        this.scene = new Scene(layout, 400, 400);
//        stage.setScene(scene);
//    }
//
//    public Scene getScene() {
//        return scene;
//    }
//}



package view;

import controller.GuardianController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Guardian;

import java.sql.Connection;

public class GuardianRegisterView {

    private final Scene scene;

    public GuardianRegisterView(Stage stage, Connection conn) {
        // Left pane - personal information
        Label personalInfoTitle = new Label("Fill up Personal Information");
        personalInfoTitle.setFont(new Font("Arial", 24));
        personalInfoTitle.setStyle("-fx-font-weight: bold;");

        GridPane personalInfoGrid = new GridPane();
        personalInfoGrid.setHgap(20);
        personalInfoGrid.setVgap(20);
        personalInfoGrid.setAlignment(Pos.CENTER_LEFT);

        TextField firstNameField = createRoundedField("First Name");
        TextField lastNameField = createRoundedField("Last Name");
        TextField birthdayField = createRoundedField("Birthday");
        TextField contactNumberField = createRoundedField("Contact Number");
        TextField addressField = createRoundedField("Address");

        personalInfoGrid.add(new Label("First Name"), 0, 0);
        personalInfoGrid.add(firstNameField, 0, 1);
        personalInfoGrid.add(new Label("Last Name"), 1, 0);
        personalInfoGrid.add(lastNameField, 1, 1);
        personalInfoGrid.add(new Label("Birthday"), 0, 2);
        personalInfoGrid.add(birthdayField, 0, 3);
        personalInfoGrid.add(new Label("Contact Number"), 1, 2);
        personalInfoGrid.add(contactNumberField, 1, 3);
        personalInfoGrid.add(new Label("Address"), 0, 4);
        personalInfoGrid.add(addressField, 0, 5);

        VBox leftPane = new VBox(20, personalInfoTitle, personalInfoGrid);
        leftPane.setPadding(new Insets(40));
        leftPane.setPrefWidth(600);
        leftPane.setAlignment(Pos.TOP_CENTER);

        // Right pane - registration
        Label registerLabel = new Label("Register");
        registerLabel.setFont(new Font("Arial", 24));
        registerLabel.setTextFill(Color.WHITE);
        registerLabel.setStyle("-fx-font-weight: bold;");

        TextField usernameField = createRoundedField("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        styleRoundedField(passwordField);

        Button signInButton = new Button("Sign in");
        signInButton.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 14px; -fx-background-radius: 20;");
        signInButton.setPrefWidth(120);

        VBox rightPane = new VBox(20, registerLabel, usernameField, passwordField, signInButton);
        rightPane.setPadding(new Insets(60));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setAlignment(Pos.CENTER);
        rightPane.setPrefWidth(300);

        HBox rootLayout = new HBox(leftPane, rightPane);
        this.scene = new Scene(rootLayout, 900, 500);

        // Actions
        signInButton.setOnAction(e -> {
            System.out.println("Signing in...");
            String username = usernameField.getText();
            String password = passwordField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String birthday = birthdayField.getText();
            String contactNumber = contactNumberField.getText();
            String address = addressField.getText();

            if (firstName.isEmpty() || lastName.isEmpty() || birthday.isEmpty() || contactNumber.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Missing Information", "Please fill in all required fields.");
            }
            Guardian guardian = new Guardian(username, password, firstName, lastName, birthday, contactNumber, address);
            boolean submissionSuccess = false;

            try {
                GuardianController guardianController = new GuardianController(conn);
                guardianController.addGuardian(guardian);
                submissionSuccess = true;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            if (submissionSuccess){
                System.out.println("Switching to Guardian Landing Page.");
                GuardianView guardianView = new GuardianView(stage,conn,guardian);
                stage.setScene(guardianView.getScene());
            } else {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", "Could not submit registration data. Please try again later.");
            }
        });

        stage.setScene(scene);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private TextField createRoundedField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        styleRoundedField(field);
        return field;
    }

    private void styleRoundedField(TextField field) {
        field.setStyle("-fx-background-color: lightgray; -fx-background-radius: 20; -fx-padding: 8 16;");
        field.setPrefWidth(200);
    }

    public Scene getScene() {
        return scene;
    }
}