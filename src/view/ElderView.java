package view;

import controller.ElderController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Elder;
import model.Guardian;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.Pattern;

public class ElderView {

    private final Scene scene;
    private final ElderController elderController;


    public ElderView(Stage stage, Connection conn, Guardian guardian) {
        this.elderController = new ElderController(conn);

        if (guardian != null) {
            System.out.println("ElderView received Guardian ID: " + guardian.getGuardianID());
        } else {
            System.err.println("ElderView received a NULL Guardian object!");

            showAlert(Alert.AlertType.ERROR, "Initialization Error", "Guardian data not available. Cannot add elder.");

        }


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
        TextField contactField = createRoundedField("Contact Number (09xxxxxxxxx or +639xxxxxxxxx)");
        TextField addressField = createRoundedField("Address");
        TextField emailField = createRoundedField("Email (e.g., user@example.com)");
        TextField relationshipField = createRoundedField("Relationship");


        birthdayPicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(LocalDate.now()));
            }
        });


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
            firstNameField.clear();
            lastNameField.clear();
            birthdayPicker.setValue(null);
            contactField.clear();
            addressField.clear();
            emailField.clear();
            relationshipField.clear();
        });

        addButton.setOnAction(e -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            LocalDate birthdayDate = birthdayPicker.getValue();
            String contactNumber = contactField.getText();
            String address = addressField.getText();
            String email = emailField.getText();
            String relationship = relationshipField.getText();


            if (guardian == null || guardian.getGuardianID() <= 0) {
                showAlert(Alert.AlertType.ERROR, "System Error", "Guardian information is missing. Cannot add elder.");
                return;
            }


            if (firstName.isEmpty() || lastName.isEmpty() || birthdayDate == null ||
                    contactNumber.isEmpty() || address.isEmpty() || email.isEmpty() || relationship.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required.");
                return;
            }

            if (birthdayDate.isAfter(LocalDate.now())) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Birthday cannot be in the future.");
                return;
            }


            LocalDateTime birthdayDateTime = birthdayDate.atTime(LocalTime.MIDNIGHT);

            Elder newElder = new Elder(firstName, lastName, birthdayDateTime, contactNumber, email, address, guardian.getGuardianID(), relationship);

            try {
                elderController.addElder(newElder);


                if (newElder.getElderID() > 0) {
                    try {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Elder added and linking process initiated successfully.");

                        firstNameField.clear();
                        lastNameField.clear();
                        birthdayPicker.setValue(null);
                        contactField.clear();
                        addressField.clear();
                        emailField.clear();
                        relationshipField.clear();
                    } catch (Exception linkEx) {
                        showAlert(Alert.AlertType.ERROR, "Link Error", "Elder added, but an error occurred during linking: " + linkEx.getMessage());
                        linkEx.printStackTrace();
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Add Error", "Failed to add elder or retrieve elder ID for linking. Please check elder list or try again. The elder might not have been saved.");
                }
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Operation Error", "An error occurred while adding the elder: " + ex.getMessage());
                ex.printStackTrace();
            }
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
            GuardianElderView guardianElderView = new GuardianElderView(stage, conn, guardian, elderController);
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

        HBox root = new HBox(20, leftPane, rightPane);
        root.setPadding(new Insets(20));

        this.scene = new Scene(root, 1100, 650);
        stage.setTitle("Add Elder");
        stage.setScene(scene);

    }

    private TextField createRoundedField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle("-fx-background-color: lightgray; -fx-background-radius: 20; -fx-padding: 8 16;");
        field.setPrefWidth(300);
        return field;
    }

    private DatePicker createRoundedDatePicker(String prompt) {
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText(prompt);
        datePicker.setStyle("-fx-background-color: lightgray; -fx-background-radius: 20; -fx-padding: 8 16;");
        datePicker.setPrefWidth(300);
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

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Scene getScene() {
        return scene;
    }
}