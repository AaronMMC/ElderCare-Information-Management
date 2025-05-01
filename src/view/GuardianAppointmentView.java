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

public class GuardianAppointmentView{

    private final Scene scene;

    public GuardianAppointmentView(Stage stage, Connection conn) {
        // Mock data: list of appointments
        // In real implementation, fetch from database
        VBox appointmentsContainer = new VBox(15);
        appointmentsContainer.setPadding(new Insets(10));

        // Example appointments
        appointmentsContainer.getChildren().addAll(
                createAppointmentCard("Caregiver: Alice Johnson", "2023-10-20", "14:00", "Paid"),
                createAppointmentCard("Caregiver: Bob Williams", "2023-10-22", "10:00", "Unpaid"),
                createAppointmentCard("Caregiver: Carol Smith", "2023-10-25", "09:00", "Paid"),
                createAppointmentCard("Caregiver: David Lee", "2023-10-28", "16:00", "Unpaid")
        );

        ScrollPane scrollPane = new ScrollPane(appointmentsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);

        Label titleLabel = new Label("My Appointments");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold;");

        VBox leftPane = new VBox(20, titleLabel, scrollPane);
        leftPane.setPadding(new Insets(20));
        leftPane.setStyle("-fx-background-color: white;");
        leftPane.setPrefWidth(700);

        // Right pane could be used for additional info or actions
        // For now, keeping it empty or for future extensions
        VBox rightPane = new VBox();
        rightPane.setPrefWidth(200);
        rightPane.setStyle("-fx-background-color: #3BB49C;");

        // Combine panes
        HBox root = new HBox(20, leftPane, rightPane);
        root.setPadding(new Insets(20));

        this.scene = new Scene(root, 950, 500);
        stage.setTitle("Guardian Appointments");
        stage.setScene(scene);
        stage.show();
    }

    // Helper method to create appointment cards
    private VBox createAppointmentCard(String caregiverInfo, String date, String time, String status) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-border-color: #ccc; -fx-border-radius: 15; -fx-cursor: hand;");
        card.setMaxWidth(650);

        Label caregiverLabel = new Label(caregiverInfo);
        caregiverLabel.setFont(Font.font("Arial", 16));
        caregiverLabel.setStyle("-fx-font-weight: bold;");

        Label dateLabel = new Label("Date: " + date);
        Label timeLabel = new Label("Time: " + time);
        Label statusLabel = new Label("Status: " + status);
        statusLabel.setTextFill(status.equals("Unpaid") ? Color.RED : Color.GREEN);

        // If unpaid, show "Pay Balance" button
        Button payButton = null;
        if (status.equals("Unpaid")) {
            payButton = new Button("Pay Balance");
            payButton.setStyle("""
                -fx-background-color: #3BB49C;
                -fx-text-fill: white;
                -fx-font-size: 14px;
                -fx-background-radius: 15;
            """);
            payButton.setPrefWidth(130);
            payButton.setOnAction(e -> {
                System.out.println("Redirect to payment for appointment with " + caregiverInfo);
                // TODO: Implement redirection to payment UI
            });
        }

        VBox infoBox = new VBox(4, caregiverLabel, dateLabel, timeLabel, statusLabel);
        HBox cardContent;
        if (payButton != null) {
            cardContent = new HBox(10, infoBox, payButton);
            cardContent.setAlignment(Pos.CENTER_LEFT);
        } else {
            cardContent = new HBox(infoBox);
        }

        card.getChildren().add(cardContent);

        // Optional: highlight on hover
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 15; -fx-border-color: #ccc; -fx-border-radius: 15; -fx-cursor: hand;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-border-color: #ccc; -fx-border-radius: 15;"));

        return card;
    }

    public Scene getScene() {
        return scene;
    }
}