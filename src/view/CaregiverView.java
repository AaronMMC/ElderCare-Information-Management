package view;

import controller.CaregiverController;
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

        // === Left Pane: Appointments List ===
        Label titleLabel = new Label("Caregiver Appointments");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold;");

        VBox appointmentsBox = new VBox(15);
        // Add mock appointments
        appointmentsBox.getChildren().addAll(
                createAppointmentCard("John Doe", "2023-10-15", "09:00 AM", "Pending"),
                createAppointmentCard("Jane Smith", "2023-10-16", "11:00 AM", "Pending")
        );

        ScrollPane scrollPane = new ScrollPane(appointmentsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(300);

        VBox leftPane = new VBox(20, titleLabel, scrollPane);
        leftPane.setPadding(new Insets(20));
        leftPane.setStyle("-fx-background-color: white;");
        leftPane.setPrefWidth(600);

        // === Right Pane: Controls ===
        Label actionsLabel = new Label("Actions");
        actionsLabel.setFont(Font.font("Arial", 20));
        actionsLabel.setStyle("-fx-font-weight: bold;");
        actionsLabel.setTextFill(Color.WHITE);

        Button acceptBtn = createWhiteButton("Accept Appointment");
        Button deleteBtn = createWhiteButton("Delete Appointment");
        Button closeBtn = createWhiteButton("Close Appointment");

        // Placeholder for selected appointment info
        Label selectedAppointmentLabel = new Label("No appointment selected");
        selectedAppointmentLabel.setWrapText(true);
        selectedAppointmentLabel.setStyle("-fx-font-size: 14px; -fx-padding: 10;");

        // Handlers for buttons (to be connected later)
        acceptBtn.setOnAction(e -> {
            System.out.println("Accept clicked");
            // TODO: Implement accept logic
        });
        deleteBtn.setOnAction(e -> {
            System.out.println("Delete clicked");
            // TODO: Implement delete logic
        });
        closeBtn.setOnAction(e -> {
            System.out.println("Close clicked");
            // TODO: Implement close logic
        });

        VBox actionsBox = new VBox(15, acceptBtn, deleteBtn, closeBtn);
        actionsBox.setAlignment(Pos.TOP_CENTER);
        actionsBox.setPadding(new Insets(20));
        actionsBox.setStyle("-fx-background-color: #3BB49C; -fx-background-radius: 10;");
        actionsBox.setPrefWidth(250);

        VBox rightPane = new VBox(20, actionsLabel, selectedAppointmentLabel, actionsBox);
        rightPane.setPadding(new Insets(30));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setPrefWidth(300);

        // === Combine Panes ===
        HBox root = new HBox(20, leftPane, rightPane);
        root.setPadding(new Insets(20));

        this.scene = new Scene(root, 900, 500);
        stage.setTitle("Caregiver Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    // Helper to create appointment cards
    private VBox createAppointmentCard(String patientName, String date, String time, String status) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-cursor: hand;");
        card.setMaxWidth(550);

        Label nameLabel = new Label("Patient: " + patientName);
        nameLabel.setFont(Font.font("Arial", 16));
        nameLabel.setStyle("-fx-font-weight: bold;");

        Label dateLabel = new Label("Date: " + date);
        Label timeLabel = new Label("Time: " + time);
        Label statusLabel = new Label("Status: " + status);
        statusLabel.setTextFill(status.equals("Pending") ? Color.ORANGE : Color.GREEN);

        card.getChildren().addAll(nameLabel, dateLabel, timeLabel, statusLabel);

        // Select appointment on click (for demo)
        card.setOnMouseClicked(e -> {
            System.out.println("Selected appointment with " + patientName);
            // TODO: Update selected appointment info
        });

        return card;
    }

    public Scene getScene() {
        return scene;
    }

    // Helper for styled buttons
    private Button createWhiteButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("""
            -fx-background-color: white;
            -fx-text-fill: black;
            -fx-font-size: 14px;
            -fx-background-radius: 15;
            -fx-border-color: #ccc;
            -fx-border-radius: 15;
            -fx-cursor: hand;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 1);
        """);
        btn.setPrefHeight(40);
        btn.setPrefWidth(200);
        return btn;
    }
}