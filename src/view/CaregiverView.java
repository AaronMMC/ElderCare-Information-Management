//package view;
//
//import controller.CaregiverController;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.*;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//import javafx.stage.Stage;
//import model.Caregiver;
//
//import java.sql.Connection;
//
//public class CaregiverView {
//
//    private final CaregiverController caregiverController;
//    private final Scene scene;
//
//    public CaregiverView(Stage stage, Connection conn, Caregiver caregiver) {
//        this.caregiverController = new CaregiverController(conn);
//
//        // === Left Pane: Appointments List ===
//        Label titleLabel = new Label("Caregiver Appointments");
//        titleLabel.setFont(Font.font("Arial", 24));
//        titleLabel.setStyle("-fx-font-weight: bold;");
//
//        VBox appointmentsBox = new VBox(15);
//        // Add mock appointments
//        appointmentsBox.getChildren().addAll(
//                createAppointmentCard("John Doe", "2023-10-15", "09:00 AM", "Pending"),
//                createAppointmentCard("Jane Smith", "2023-10-16", "11:00 AM", "Pending")
//        );
//
//        ScrollPane scrollPane = new ScrollPane(appointmentsBox);
//        scrollPane.setFitToWidth(true);
//        scrollPane.setPrefHeight(300);
//
//        VBox leftPane = new VBox(20, titleLabel, scrollPane);
//        leftPane.setPadding(new Insets(20));
//        leftPane.setStyle("-fx-background-color: white;");
//        leftPane.setPrefWidth(600);
//
//        // === Right Pane: Controls ===
//        Label actionsLabel = new Label("Actions");
//        actionsLabel.setFont(Font.font("Arial", 20));
//        actionsLabel.setStyle("-fx-font-weight: bold;");
//        actionsLabel.setTextFill(Color.WHITE);
//
//        Button acceptBtn = createWhiteButton("Accept Appointment");
//        Button deleteBtn = createWhiteButton("Delete Appointment");
//        Button closeBtn = createWhiteButton("Close Appointment");
//
//        // Placeholder for selected appointment info
//        Label selectedAppointmentLabel = new Label("No appointment selected");
//        selectedAppointmentLabel.setWrapText(true);
//        selectedAppointmentLabel.setStyle("-fx-font-size: 14px; -fx-padding: 10;");
//
//        // Handlers for buttons (to be connected later)
//        acceptBtn.setOnAction(e -> {
//            System.out.println("Accept clicked");
//            // TODO: Implement accept logic
//        });
//        deleteBtn.setOnAction(e -> {
//            System.out.println("Delete clicked");
//            // TODO: Implement delete logic
//        });
//        closeBtn.setOnAction(e -> {
//            System.out.println("Close clicked");
//            // TODO: Implement close logic
//        });
//
//        VBox actionsBox = new VBox(15, acceptBtn, deleteBtn, closeBtn);
//        actionsBox.setAlignment(Pos.TOP_CENTER);
//        actionsBox.setPadding(new Insets(20));
//        actionsBox.setStyle("-fx-background-color: #3BB49C; -fx-background-radius: 10;");
//        actionsBox.setPrefWidth(250);
//
//        VBox rightPane = new VBox(20, actionsLabel, selectedAppointmentLabel, actionsBox);
//        rightPane.setPadding(new Insets(30));
//        rightPane.setStyle("-fx-background-color: #3BB49C;");
//        rightPane.setPrefWidth(300);
//
//        // === Combine Panes ===
//        HBox root = new HBox(20, leftPane, rightPane);
//        root.setPadding(new Insets(20));
//
//        this.scene = new Scene(root, 900, 500);
//        stage.setTitle("Caregiver Dashboard");
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    // Helper to create appointment cards
//    private VBox createAppointmentCard(String patientName, String date, String time, String status) {
//        VBox card = new VBox(8);
//        card.setPadding(new Insets(10));
//        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-cursor: hand;");
//        card.setMaxWidth(550);
//
//        Label nameLabel = new Label("Patient: " + patientName);
//        nameLabel.setFont(Font.font("Arial", 16));
//        nameLabel.setStyle("-fx-font-weight: bold;");
//
//        Label dateLabel = new Label("Date: " + date);
//        Label timeLabel = new Label("Time: " + time);
//        Label statusLabel = new Label("Status: " + status);
//        statusLabel.setTextFill(status.equals("Pending") ? Color.ORANGE : Color.GREEN);
//
//        card.getChildren().addAll(nameLabel, dateLabel, timeLabel, statusLabel);
//
//        // Select appointment on click (for demo)
//        card.setOnMouseClicked(e -> {
//            System.out.println("Selected appointment with " + patientName);
//            // TODO: Update selected appointment info
//        });
//
//        return card;
//    }
//
//    public Scene getScene() {
//        return scene;
//    }
//
//    // Helper for styled buttons
//    private Button createWhiteButton(String text) {
//        Button btn = new Button(text);
//        btn.setStyle("""
//            -fx-background-color: white;
//            -fx-text-fill: black;
//            -fx-font-size: 14px;
//            -fx-background-radius: 15;
//            -fx-border-color: #ccc;
//            -fx-border-radius: 15;
//            -fx-cursor: hand;
//            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 1);
//        """);
//        btn.setPrefHeight(40);
//        btn.setPrefWidth(200);
//        return btn;
//    }
//}

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

public class CaregiverView {

    private final CaregiverController caregiverController;
    private final Scene scene;

    public CaregiverView(Stage stage, Connection conn, Caregiver caregiver) {
        this.caregiverController = new CaregiverController(conn);

        // === Left Form ===
        Label titleLabel = new Label("Caregiver Homepage");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold;");

        // Form fields
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

        // Form grid
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

        HBox buttonBox = new HBox(20, cancelBtn, saveBtn);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        leftPane.getChildren().add(buttonBox);

        // === Right Sidebar (Fixed) ===
        Button appointmentsBtn = createSidebarButton("Your Appointments");
        Button eldersBtn = createSidebarButton("Your Elders");
        Button servicesBtn = createSidebarButton("Your Services");
        Button logoutBtn = createSidebarButton("Log Out");

        logoutBtn.setOnAction(e -> {
            System.out.println("Logging out...");
            Platform.runLater(() -> {
                LoginController loginController = new LoginController(stage, conn);
                Scene loginScene = loginController.getLoginScene();
                stage.setScene(loginScene); // Now the scene will be shown again
            });
        });

// Push logout button to bottom using VBox with spacing
        VBox topButtons = new VBox(20, appointmentsBtn, eldersBtn, servicesBtn);
        topButtons.setAlignment(Pos.TOP_CENTER);

        VBox rightPane = new VBox();
        rightPane.setPadding(new Insets(30));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setAlignment(Pos.TOP_CENTER);
        rightPane.setPrefWidth(250);
        rightPane.setPrefHeight(Double.MAX_VALUE); // Fill vertical space

// VBox with VBox.setVgrow to push Log Out to bottom
        VBox.setVgrow(topButtons, Priority.ALWAYS);
        rightPane.getChildren().addAll(topButtons, logoutBtn);


        // === Root Layout ===
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
