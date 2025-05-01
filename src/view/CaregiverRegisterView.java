//package view;
//
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//
//import java.sql.Connection;
//
//public class CaregiverRegisterView {
//
//    private final Scene scene;
//
//    //TODO: Fix design. Parameter should always have (conn) so that you can utilize CaregiverController to register a new caregiver to the DB.
//    public CaregiverRegisterView(Stage stage, Connection conn) {
//        //Dummy
//        Label title = new Label("Register as Caregiver");
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

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;

public class CaregiverRegisterView {

    private final Scene scene;

    public CaregiverRegisterView(Stage stage, Connection conn) {
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
        TextField employmentTypeField = createRoundedField("Employment Type");

        TextArea availabilityField = new TextArea();
        availabilityField.setPromptText("Availability Schedule (e.g., Mon-Fri: 8AM - 5PM)");
        styleRoundedArea(availabilityField);

        TextArea certificationsArea = new TextArea();
        certificationsArea.setPromptText("Certifications (one per line)");
        styleRoundedArea(certificationsArea);

        TextField backgroundCheckField = createRoundedField("Background Check Details");
        TextField medicalClearanceField = createRoundedField("Medical Clearance Details");

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
        personalInfoGrid.add(new Label("Employment Type"), 1, 4);
        personalInfoGrid.add(employmentTypeField, 1, 5);
        personalInfoGrid.add(new Label("Availability Schedule"), 0, 6);
        personalInfoGrid.add(availabilityField, 0, 7);
        personalInfoGrid.add(new Label("Certifications"), 1, 6);
        personalInfoGrid.add(certificationsArea, 1, 7);
        personalInfoGrid.add(new Label("Background Check Details"), 0, 8);
        personalInfoGrid.add(backgroundCheckField, 0, 9);
        personalInfoGrid.add(new Label("Medical Clearance Details"), 1, 8);
        personalInfoGrid.add(medicalClearanceField, 1, 9);

        VBox leftPane = new VBox(20, personalInfoTitle, personalInfoGrid);
        leftPane.setPadding(new Insets(40));
        leftPane.setPrefWidth(600);
        leftPane.setAlignment(Pos.TOP_CENTER);

        // Right pane - login credentials
        Label registerLabel = new Label("Register");
        registerLabel.setFont(new Font("Arial", 24));
        registerLabel.setTextFill(Color.WHITE);
        registerLabel.setStyle("-fx-font-weight: bold;");

        TextField usernameField = createRoundedField("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        styleRoundedField(passwordField);

        Button registerButton = new Button("Register");
        registerButton.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 14px; -fx-background-radius: 20;");
        registerButton.setPrefWidth(120);

        Button backButton = new Button("Back to Login");
        backButton.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 14px; -fx-background-radius: 20;");
        backButton.setPrefWidth(120);

        VBox rightPane = new VBox(20, registerLabel, usernameField, passwordField, registerButton, backButton);
        rightPane.setPadding(new Insets(60));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setAlignment(Pos.CENTER);
        rightPane.setPrefWidth(300);

        HBox rootLayout = new HBox(leftPane, rightPane);
        this.scene = new Scene(rootLayout, 1000, 650);

        registerButton.setOnAction(e -> {
            System.out.println("Registering guardian...");
            // TODO: Add DB logic through GuardianController
        });

        backButton.setOnAction(e -> {
            System.out.println("Going back to login...");
        });

        stage.setScene(scene);
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

    private void styleRoundedArea(TextArea area) {
        area.setStyle("-fx-background-color: lightgray; -fx-background-radius: 20; -fx-padding: 8 16;");
        area.setPrefWidth(200);
        area.setPrefRowCount(3);
        area.setWrapText(true);
    }

    public Scene getScene() {
        return scene;
    }
}







