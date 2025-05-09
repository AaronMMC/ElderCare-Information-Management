package view;

import controller.ElderController;
import controller.GuardianController;
import controller.LoginController;
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

        guardian.getGuardianID();
        System.out.println(guardian.getGuardianID());
        TextField firstNameField = new TextField(guardian.getFirstName());
        TextField lastNameField = new TextField(guardian.getLastName());
        TextField contactField = new TextField(guardian.getContactNumber());
        TextField emailField = new TextField(guardian.getEmail());
        TextField addressField = new TextField(guardian.getAddress());


        for (TextField field : new TextField[]{firstNameField, lastNameField, contactField, emailField, addressField}) {
            field.setStyle("-fx-background-color: #d9d9d9; -fx-background-radius: 20;");
            field.setMaxWidth(Double.MAX_VALUE);
        }


        GridPane formGrid = new GridPane();
        formGrid.setVgap(15);
        formGrid.setPadding(new Insets(20));


        Label firstNameLabel = new Label("First Name:");
        Label lastNameLabel = new Label("Last Name:");
        Label contactLabel = new Label("Contact Number:");
        Label emailLabel = new Label("Email:");
        Label addressLabel = new Label("Address:");


        formGrid.add(new VBox(5, firstNameLabel, firstNameField), 0, 0);
        formGrid.add(new VBox(5, lastNameLabel, lastNameField), 0, 1);
        formGrid.add(new VBox(5, contactLabel, contactField), 0, 2);
        formGrid.add(new VBox(5, emailLabel, emailField), 0, 3);
        formGrid.add(new VBox(5, addressLabel, addressField), 0, 4);


        ColumnConstraints column1 = new ColumnConstraints();


        column1.setPrefWidth(360);
        formGrid.getColumnConstraints().add(column1);


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
            try {
                guardianController.updateGuardian(guardian);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Guardian details update attempt finished.");

            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update guardian details: " + ex.getMessage());
            }
        });

        HBox actionButtons = new HBox(20, cancelBtn, saveBtn);
        actionButtons.setAlignment(Pos.CENTER_LEFT);


        actionButtons.setPadding(new Insets(30, 0, 0, 20));


        VBox leftContent = new VBox(20, titleLabel, formGrid, actionButtons);
        leftContent.setPadding(new Insets(40));
        leftContent.setPrefWidth(800);
        leftContent.setStyle("-fx-background-color: white;");

        leftContent.setAlignment(Pos.TOP_CENTER);


        Button appointmentBtn = createMenuButton("Your Appointments");
        Button elderBtn = createMenuButton("Elder");

        appointmentBtn.setOnAction(e -> {
            System.out.println("Switching to GuardianAppointmentView...");
                GuardianAppointmentView guardianAppointmentView = new GuardianAppointmentView(stage, conn, guardian);
                stage.setScene(guardianAppointmentView.getScene());

        });

        elderBtn.setOnAction(e -> {
            System.out.println("Switching to GuardianElderView...");
                GuardianElderView guardianElderView = new GuardianElderView(stage, conn, guardian, new ElderController(conn));
                stage.setScene(guardianElderView.getScene());
        });

        VBox rightPane = new VBox(40, appointmentBtn, elderBtn);

        Button logOutButton = createMenuButton("Log Out");
        logOutButton.setOnAction(e -> {
            LoginController loginController = new LoginController(stage, conn);
            Scene loginScene = loginController.getLoginScene();
            stage.setTitle("ElderCare");
            stage.setScene(loginScene);
            stage.show();

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