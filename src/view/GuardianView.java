package view;

import controller.GuardianController;
import controller.GuardianElderController;
import model.Guardian;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GuardianView {

    private final GuardianController guardianController;
    //private final GuardianElderController guardianElderController;

    //TODO: Depending on the UI, the parameter can have GuardianElderController as well.
    public GuardianView(GuardianController controller) {
        this.guardianController = controller;
    }

    //TODO: Fix design for Guardian Home Page. Class name is can be refactored.
    public void start(Stage primaryStage) {
        // Layout
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        // Input Fields
        TextField idField = new TextField();
        idField.setPromptText("Guardian ID");

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");

        TextField contactField = new TextField();
        contactField.setPromptText("Contact Number");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        TextField addressField = new TextField();
        addressField.setPromptText("Address");

        // Buttons
        Button addButton = new Button("Add Guardian");
        Button fetchButton = new Button("Fetch Guardian");
        Button deleteButton = new Button("Delete Guardian");
        Button updateButton = new Button("Update Guardian");

        Label outputLabel = new Label();

        // Add Guardian
        addButton.setOnAction(e -> {

        });

        // Fetch Guardian
        fetchButton.setOnAction(e -> {

        });

        // Delete Guardian
        deleteButton.setOnAction(e -> {

        });

        // Update Guardian
        updateButton.setOnAction(e -> {

        });

        // Layout structure
        root.getChildren().addAll(
                new Label("Guardian ID (for update/fetch/delete):"), idField,
                firstNameField, lastNameField, contactField, emailField, addressField,
                new HBox(10, addButton, fetchButton, updateButton, deleteButton),
                outputLabel
        );

        // Set up Stage
        Scene scene = new Scene(root, 500, 400);
        primaryStage.setTitle("Guardian Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}