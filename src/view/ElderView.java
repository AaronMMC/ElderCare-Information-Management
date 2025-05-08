package view;

import controller.ElderController;
import controller.GuardianElderController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Elder;
import model.Guardian;
import model.GuardianElder;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ElderView {

    private final Scene scene;
    private final ElderController elderController;
    private final GuardianElderController guardianElderController;

    public ElderView(Stage stage, Connection conn, Guardian guardian) {
        this.elderController = new ElderController(conn);
        this.guardianElderController = new GuardianElderController(conn);

        // === Title ===
        Label title = new Label("Add an Elder");
        title.setFont(new Font("Arial", 24));
        title.setStyle("-fx-font-weight: bold;");

        // === Elder Form ===
        GridPane formGrid = new GridPane();
        formGrid.setHgap(20);
        formGrid.setVgap(20);
        formGrid.setAlignment(Pos.CENTER_LEFT);

        TextField firstNameField = createRoundedField("First Name");
        TextField lastNameField = createRoundedField("Last Name");
        DatePicker birthdayPicker = createRoundedDatePicker("Birthday");
        TextField contactField = createRoundedField("Contact Number");
        TextField addressField = createRoundedField("Address");
        TextField emailField = createRoundedField("Email");
        TextField relationshipField = createRoundedField("Relationship");

        formGrid.add(new Label("First Name:"), 0, 0);
        formGrid.add(firstNameField, 0, 1);
        formGrid.add(new Label("Last Name:"), 1, 0);
        formGrid.add(lastNameField, 1, 1);
        formGrid.add(new Label("Birthday:"), 0, 2);
        formGrid.add(birthdayPicker, 0, 3);
        formGrid.add(new Label("Contact Number:"), 1, 2);
        formGrid.add(contactField, 1, 3);
        formGrid.add(new Label("Address:"), 0, 4);
        formGrid.add(addressField, 0, 5);
        formGrid.add(new Label("Email:"), 1, 4);
        formGrid.add(emailField, 1, 5);
        formGrid.add(new Label("Relationship:"), 0, 6);
        formGrid.add(relationshipField, 0, 7);

        // === Left Buttons ===
        Button cancelButton = createMainButton("Cancel");
        Button addButton = createMainButton("Add");

        cancelButton.setOnAction(e -> {
            firstNameField.setPromptText("First Name");
            lastNameField.setPromptText("Last Name");
            birthdayPicker.setValue(null); // Clear the datepicker
            contactField.setPromptText("Contact Number");
            addressField.setPromptText("Address");
            emailField.setPromptText("Email");
            relationshipField.setPromptText("Relationship");
            cancelButton.setDisable(true);
            addButton.setDisable(true);

            System.out.println("Cancel button clicked.");
        });

        addButton.setOnAction(e -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            LocalDate birthdayDate = birthdayPicker.getValue();
            LocalDateTime birthdayDateTime = null;
            if (birthdayDate != null) {
                birthdayDateTime = birthdayDate.atTime(LocalTime.MIDNIGHT);
            }
            String contactNumber = contactField.getText();
            String address = addressField.getText();
            String email = emailField.getText();
            String relationship = relationshipField.getText();

            Elder newElder = new Elder(firstName, lastName, birthdayDateTime, contactNumber, email, address);
            elderController.addElder(newElder);

            GuardianElder newGELink = new GuardianElder(guardian.getGuardianID(), newElder.getElderID(), relationship);
            guardianElderController.linkGuardianToElder(newGELink);

            System.out.println("Elder added.");
        });

        HBox buttonBox = new HBox(20, cancelButton, addButton);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        VBox leftPane = new VBox(20, title, formGrid, buttonBox);
        leftPane.setPadding(new Insets(40));
        leftPane.setPrefWidth(800);
        leftPane.setAlignment(Pos.TOP_CENTER);

        // === Right Sidebar ===
        Button goBackBtn = createSidebarButton("Go Back");
        goBackBtn.setOnAction(e -> {
            GuardianElderView guardianElderView = new GuardianElderView(stage, conn, guardian);
            stage.setScene(guardianElderView.getScene());
        });

        VBox rightPane = new VBox();
        rightPane.setPadding(new Insets(30));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setAlignment(Pos.BOTTOM_CENTER);
        rightPane.setPrefWidth(250);

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        rightPane.getChildren().addAll(spacer, goBackBtn);

        // === Root Layout ===
        HBox root = new HBox(20, leftPane, rightPane);
        root.setPadding(new Insets(20));

        this.scene = new Scene(root, 1100, 600);
        stage.setTitle("Your Elders");
        stage.setScene(scene);
        stage.show();

    }

    private TextField createRoundedField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle("-fx-background-color: lightgray; -fx-background-radius: 20; -fx-padding: 8 16;");
        field.setPrefWidth(250);
        return field;
    }

    private DatePicker createRoundedDatePicker(String prompt) {
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText(prompt);
        datePicker.setStyle("-fx-background-color: lightgray; -fx-background-radius: 20; -fx-padding: 8 16;");
        datePicker.setPrefWidth(250);
        return datePicker;
    }

    private Button createMainButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #3BB49C; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20;");
        button.setPrefWidth(120);
        return button;
    }

    private Button createSidebarButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 14px; -fx-background-radius: 20;");
        button.setPrefWidth(120);
        return button;
    }

    public Scene getScene() {
        return scene;
    }
}