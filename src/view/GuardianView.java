package view;

import controller.GuardianController;
import controller.LoginController;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.layout.Priority;
import model.Guardian;

import java.sql.Connection;

public class GuardianView {

    private final GuardianController guardianController;
    private final Scene scene;

    public GuardianView(Stage stage, Connection conn, Guardian guardian) {
        this.guardianController = new GuardianController(conn);

        Label titleLabel = new Label("Guardian Homepage");
        titleLabel.setFont(Font.font("Arial", 28));
        titleLabel.setStyle("-fx-font-weight: bold;");

        // Form Fields
        TextField firstNameField = new TextField(guardian.getFirstName());
        TextField lastNameField = new TextField(guardian.getLastName());
        TextField contactField = new TextField(guardian.getContactNumber());
        TextField emailField = new TextField(guardian.getEmail());
        TextField addressField = new TextField(guardian.getAddress());

        // Style fields
        for (TextField field : new TextField[]{firstNameField, lastNameField, contactField, emailField, addressField}) {
            field.setStyle("-fx-background-color: #d9d9d9; -fx-background-radius: 20;");
            field.setPrefWidth(200);
        }

        // Form Grid
        GridPane formGrid = new GridPane();
        formGrid.setHgap(20);
        formGrid.setVgap(15);
        formGrid.setPadding(new Insets(20));

        formGrid.add(new VBox(5, new Label("First Name:"), firstNameField), 0, 0);
        formGrid.add(new VBox(5, new Label("Last Name:"), lastNameField), 1, 0);
        formGrid.add(new VBox(5, new Label("Contact Number:"), contactField), 1, 1);
        formGrid.add(new VBox(5, new Label("Email:"), emailField), 2, 1);
        formGrid.add(new VBox(5, new Label("Address:"), addressField), 0, 2);

        // Buttons
        Button cancelBtn = createTealButton("Cancel");
        Button saveBtn = createTealButton("Save Changes");

        cancelBtn.setOnAction(e -> {
            firstNameField.setText(guardian.getFirstName());
            lastNameField.setText(guardian.getLastName());
            contactField.setText(guardian.getContactNumber());
            emailField.setText(guardian.getEmail());
            addressField.setText(guardian.getAddress());
        });

        saveBtn.setOnAction(e -> {
            guardian.setFirstName(firstNameField.getText());
            guardian.setLastName(lastNameField.getText());
            guardian.setContactNumber(contactField.getText());
            guardian.setEmail(emailField.getText());
            guardian.setAddress(addressField.getText());
            guardianController.updateGuardian(guardian);
        });

        HBox actionButtons = new HBox(20, cancelBtn, saveBtn);
        actionButtons.setAlignment(Pos.CENTER_LEFT);
        actionButtons.setPadding(new Insets(30, 0, 0, 0));

        VBox leftContent = new VBox(20, titleLabel, formGrid, actionButtons);
        leftContent.setPadding(new Insets(40));
        leftContent.setPrefWidth(800);
        leftContent.setStyle("-fx-background-color: white;");

        Button appointmentBtn = createMenuButton("Your Appointments");
        Button elderBtn = createMenuButton("Elder");

        appointmentBtn.setOnAction(e -> {
            System.out.println("Switching to GuardianAppointmentView...");
            Platform.runLater(() -> {
               GuardianAppointmentView guardianAppointmentView = new GuardianAppointmentView(stage);
               stage.setScene(guardianAppointmentView.getScene());
            });
        });

        elderBtn.setOnAction(e -> {
            System.out.println("Switching to GuardianElderView...");
            Platform.runLater(() -> {
               GuardianElderView guardianElderView = new GuardianElderView(stage);
               stage.setScene(guardianElderView.getScene());
            });
        });

        VBox rightPane = new VBox(40, appointmentBtn, elderBtn);

        Button logOutButton = createMenuButton("Log Out");
        logOutButton.setOnAction(e -> {
            System.out.println("Logging out...");
            Platform.runLater(() -> {
                LoginController loginController = new LoginController(stage, conn);
                Scene loginScene = loginController.getLoginScene();
                stage.setScene(loginScene);
            });
        });

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        rightPane.getChildren().addAll(spacer, logOutButton);

        rightPane.setAlignment(Pos.TOP_CENTER);
        rightPane.setPadding(new Insets(60, 20, 20, 20));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setPrefWidth(300);

        HBox root = new HBox(leftContent, rightPane);
        this.scene = new Scene(root, 1100, 600);
        stage.setTitle("Guardian Homepage");
        stage.setScene(scene);
        stage.show();
    }

    private Button createTealButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("""
            -fx-background-color: #3BB49C;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-background-radius: 20;
        """);
        btn.setPrefHeight(40);
        btn.setPrefWidth(150);
        return btn;
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("""
            -fx-background-color: white;
            -fx-text-fill: black;
            -fx-font-size: 16px;
            -fx-background-radius: 20;
        """);
        btn.setPrefWidth(240);
        btn.setPrefHeight(60);
        return btn;
    }

    public Scene getScene() {
        return scene;
    }
}