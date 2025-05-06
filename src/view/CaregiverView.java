package view;

import controller.CaregiverController;
import controller.LoginController;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Caregiver;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

public class CaregiverView {

    private final CaregiverController caregiverController;
    private final Scene scene;

    public CaregiverView(Stage stage, Connection conn, Caregiver caregiver) {
        this.caregiverController = new CaregiverController(conn);

        Label titleLabel = new Label("Caregiver Homepage");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold;");

        TextField firstNameField = createRoundedTextField("First Name");
        TextField lastNameField = createRoundedTextField("Last Name");
        TextField birthdayField = createRoundedTextField("Birthday");

        ComboBox<String> genderBox = createRoundedComboBox("Gender");
        TextField contactField = createRoundedTextField("Contact Number");
        TextField emailField = createRoundedTextField("Email");
        TextField addressField = createRoundedTextField("Address");
        ComboBox<String> employmentBox = createRoundedComboBox("Employment Type");

        TextArea certificationsArea = new TextArea("(Insert at least (1) Certification/s Here: only in PDF)");
        certificationsArea.setWrapText(true);
        certificationsArea.setPrefRowCount(4);
        certificationsArea.setStyle("""
        -fx-background-color: #D9D9D9;
        -fx-background-radius: 15;
        -fx-padding: 10;
        -fx-border-color: transparent;
        -fx-font-style: italic;
    """);

        // Populate fields with caregiver data
        firstNameField.setText(caregiver.getFirstName());
        lastNameField.setText(caregiver.getLastName());
        birthdayField.setText(caregiver.getDateOfBirth().toLocalDate().toString());
        genderBox.getItems().addAll("MALE", "FEMALE", "OTHER");
        genderBox.setValue(caregiver.getGender().toString());
        contactField.setText(caregiver.getContactNumber());
        emailField.setText(caregiver.getEmail());
        addressField.setText(caregiver.getAddress());
        employmentBox.getItems().addAll("FULL_TIME", "PART_TIME", "CONTRACT");
        employmentBox.setValue(caregiver.getEmploymentType().toString());
        certificationsArea.setText(String.join(",", caregiver.getCertifications()));

        // Save original values for Cancel
        final String originalFirstName = caregiver.getFirstName();
        final String originalLastName = caregiver.getLastName();
        final String originalDOB = caregiver.getDateOfBirth().toLocalDate().toString();
        final String originalGender = caregiver.getGender().toString();
        final String originalContact = caregiver.getContactNumber();
        final String originalEmail = caregiver.getEmail();
        final String originalAddress = caregiver.getAddress();
        final String originalEmployment = caregiver.getEmploymentType().toString();
        final String originalCerts = String.join(",", caregiver.getCertifications());

        // Form layout
        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(15);

        formGrid.add(new Label("First Name:"), 0, 0);
        formGrid.add(firstNameField, 1, 0);
        formGrid.add(new Label("Last Name:"), 2, 0);
        formGrid.add(lastNameField, 3, 0);
        formGrid.add(new Label("Birthday:"), 0, 1);
        formGrid.add(birthdayField, 1, 1);
        formGrid.add(new Label("Gender:"), 0, 2);
        formGrid.add(genderBox, 1, 2);
        formGrid.add(new Label("Contact Number:"), 2, 2);
        formGrid.add(contactField, 3, 2);
        formGrid.add(new Label("Email:"), 0, 3);
        formGrid.add(emailField, 1, 3);
        formGrid.add(new Label("Address:"), 0, 4);
        formGrid.add(addressField, 1, 4);
        formGrid.add(new Label("Employment Type:"), 2, 4);
        formGrid.add(employmentBox, 3, 4);

        VBox leftPane = new VBox(20, titleLabel, formGrid, new Label("Certifications:"), certificationsArea);
        leftPane.setPadding(new Insets(20));
        leftPane.setPrefWidth(600);

        // Buttons
        Button cancelBtn = createGreenButton("Cancel");
        Button saveBtn = createGreenButton("Save Changes");

        cancelBtn.setOnAction(e -> {
            firstNameField.setText(originalFirstName);
            lastNameField.setText(originalLastName);
            birthdayField.setText(originalDOB);
            genderBox.setValue(originalGender);
            contactField.setText(originalContact);
            emailField.setText(originalEmail);
            addressField.setText(originalAddress);
            employmentBox.setValue(originalEmployment);
            certificationsArea.setText(originalCerts);
        });

        saveBtn.setOnAction(e -> {
            try {
                caregiver.setFirstName(firstNameField.getText());
                caregiver.setLastName(lastNameField.getText());
                caregiver.setDateOfBirth(LocalDate.parse(birthdayField.getText()).atStartOfDay());
                caregiver.setGender(Caregiver.Gender.valueOf(genderBox.getValue()));
                caregiver.setContactNumber(contactField.getText());
                caregiver.setEmail(emailField.getText());
                caregiver.setAddress(addressField.getText());
                caregiver.setEmploymentType(Caregiver.EmploymentType.valueOf(employmentBox.getValue()));
                caregiver.setCertifications(Arrays.asList(certificationsArea.getText().split(",")));

                caregiverController.updateCaregiver(caregiver);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Profile updated successfully.");
                alert.showAndWait();
            } catch (DateTimeParseException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText(null);
                alert.setContentText("Please enter the birthday in correct format");
                alert.showAndWait();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox buttonBox = new HBox(20, cancelBtn, saveBtn);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        leftPane.getChildren().add(buttonBox);

        Button appointmentsBtn = createSidebarButton("Your Appointments");
        Button eldersBtn = createSidebarButton("Your Elders");
        Button servicesBtn = createSidebarButton("Your Services");
        Button logoutBtn = createSidebarButton("Log Out");

        appointmentsBtn.setOnAction(event -> {
            // To be implemented
        });

        eldersBtn.setOnAction(event -> {
            // To be implemented
        });

        servicesBtn.setOnAction(event -> {
            // To be implemented
        });

        logoutBtn.setOnAction(e -> {
            System.out.println("Logging out...");
            Platform.runLater(() -> {
                LoginController loginController = new LoginController(stage, conn);
                Scene loginScene = loginController.getLoginScene();
                stage.setScene(loginScene);
            });
        });

        VBox topButtons = new VBox(20, appointmentsBtn, eldersBtn, servicesBtn);
        topButtons.setAlignment(Pos.TOP_CENTER);

        VBox rightPane = new VBox();
        rightPane.setPadding(new Insets(30));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setAlignment(Pos.TOP_CENTER);
        rightPane.setPrefWidth(250);
        rightPane.setPrefHeight(Double.MAX_VALUE);

        VBox.setVgrow(topButtons, Priority.ALWAYS);
        rightPane.getChildren().addAll(topButtons, logoutBtn);

        HBox root = new HBox(20, leftPane, rightPane);
        root.setPadding(new Insets(20));

        this.scene = new Scene(root, 1000, 600);
        stage.setTitle("Caregiver Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    private TextField createRoundedTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle("""
            -fx-background-radius: 15;
            -fx-border-radius: 15;
            -fx-background-color: #D9D9D9;
            -fx-border-color: transparent;
            -fx-padding: 8 12;
        """);
        return tf;
    }

    private ComboBox<String> createRoundedComboBox(String prompt) {
        ComboBox<String> cb = new ComboBox<>();
        cb.setPromptText("(Dropdown)");
        cb.setStyle("""
            -fx-background-radius: 15;
            -fx-border-radius: 15;
            -fx-background-color: #D9D9D9;
            -fx-border-color: transparent;
            -fx-padding: 4 8;
        """);
        return cb;
    }

    private Button createGreenButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("""
            -fx-background-color: #3BB49C;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-background-radius: 20;
            -fx-cursor: hand;
            -fx-padding: 8 20;
        """);
        return btn;
    }

    private Button createSidebarButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("""
            -fx-background-color: white;
            -fx-text-fill: black;
            -fx-font-size: 14px;
            -fx-background-radius: 15;
            -fx-cursor: hand;
            -fx-padding: 10 20;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 1);
        """);
        btn.setPrefWidth(200);
        return btn;
    }

    public Scene getScene() {
        return scene;
    }
}
