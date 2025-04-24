package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Caregiver;

public class CaregiverManagementView extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #f0f4f7;");

        // Title Section
        Label title = new Label("Caregiver Management");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        // Caregiver Selection Table
        TableView<Caregiver> caregiverTable = new TableView<>();
        caregiverTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        caregiverTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Caregiver Columns
        TableColumn<Caregiver, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cell -> cell.getValue().nameProperty());

        TableColumn<Caregiver, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(cell -> cell.getValue().emailProperty());

        TableColumn<Caregiver, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(cell -> cell.getValue().phoneProperty());

        caregiverTable.getColumns().addAll(nameCol, emailCol, phoneCol);

        // Form for Adding/Updating Caregiver
        GridPane formGrid = new GridPane();
        formGrid.setVgap(15);
        formGrid.setHgap(10);
        formGrid.setPadding(new Insets(10));
        formGrid.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 8px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 8, 0, 0, 4);");

        Label nameLabel = new Label("Full Name:");
        TextField nameField = new TextField();

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();

        Label phoneLabel = new Label("Phone:");
        TextField phoneField = new TextField();

        formGrid.addColumn(0, nameLabel, emailLabel, phoneLabel);
        formGrid.addColumn(1, nameField, emailField, phoneField);

        // Buttons for Actions
        Button addBtn = new Button("Add Caregiver");
        addBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-border-radius: 5px; -fx-padding: 10 20;");

        Button updateBtn = new Button("Update Caregiver");
        updateBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16px; -fx-border-radius: 5px; -fx-padding: 10 20;");
        updateBtn.setDisable(true);  // Initially disabled

        Button deleteBtn = new Button("Delete Caregiver");
        deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 16px; -fx-border-radius: 5px; -fx-padding: 10 20;");
        deleteBtn.setDisable(true);  // Initially disabled

        // Action for adding a new caregiver
        addBtn.setOnAction(event -> {
            // Create a new caregiver object
            Caregiver newCaregiver = new Caregiver(nameField.getText(), emailField.getText(), phoneField.getText());
            // In a real scenario, you would save this to the database
            caregiverTable.getItems().add(newCaregiver);

            // Clear the form fields
            nameField.clear();
            emailField.clear();
            phoneField.clear();
        });

        // Action for updating an existing caregiver
        updateBtn.setOnAction(event -> {
            Caregiver selectedCaregiver = caregiverTable.getSelectionModel().getSelectedItem();
            if (selectedCaregiver != null) {
                selectedCaregiver.setName(nameField.getText());
                selectedCaregiver.setEmail(emailField.getText());
                selectedCaregiver.setPhone(phoneField.getText());

                // In a real scenario, save the updated caregiver to the database

                caregiverTable.refresh();  // Refresh the table to show the updated data
            }
        });

        // Action for deleting a caregiver
        deleteBtn.setOnAction(event -> {
            Caregiver selectedCaregiver = caregiverTable.getSelectionModel().getSelectedItem();
            if (selectedCaregiver != null) {
                // In a real scenario, delete the caregiver from the database
                caregiverTable.getItems().remove(selectedCaregiver);
            }
        });

        // Handle selection from table to populate the form
        caregiverTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                nameField.setText(newValue.getName());
                emailField.setText(newValue.getEmail());
                phoneField.setText(newValue.getPhone());

                updateBtn.setDisable(false);  // Enable the update button
                deleteBtn.setDisable(false);  // Enable the delete button
            }
        });

        // Add components to the root layout
        root.getChildren().addAll(title, caregiverTable, formGrid, addBtn, updateBtn, deleteBtn);

        // Scene Setup
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("Administrator - Caregiver Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
