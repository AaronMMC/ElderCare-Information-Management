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

public class GuardianAppointmentView {

    private final Scene scene;
    private final Stage stage;

    public GuardianAppointmentView(Stage stage, Connection conn) {
        this.stage = stage;

        // Main container for appointments
        VBox appointmentsContainer = new VBox(15);
        appointmentsContainer.setPadding(new Insets(10));

        // Sample appointments
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

        // Right pane placeholder
        VBox rightPane = new VBox();
        rightPane.setPrefWidth(200);
        rightPane.setStyle("-fx-background-color: #3BB49C;");

        HBox root = new HBox(20, leftPane, rightPane);
        root.setPadding(new Insets(20));

        this.scene = new Scene(root, 950, 500);
        stage.setTitle("Guardian Appointments");
        stage.setScene(scene);
        stage.show();
    }

    // Create appointment card
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
                showPaymentUI(caregiverInfo, date, time);
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

        // Hover effect
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 15; -fx-border-color: #ccc; -fx-border-radius: 15; -fx-cursor: hand;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-border-color: #ccc; -fx-border-radius: 15;"));

        return card;
    }

    // Show payment UI for a specific appointment
    private void showPaymentUI(String caregiverInfo, String date, String time) {
        VBox paymentPane = new VBox(20);
        paymentPane.setPadding(new Insets(20));
        paymentPane.setStyle("-fx-background-color: #fff;");

        Label title = new Label("Select Payment Method");
        title.setFont(Font.font("Arial", 20));
        title.setStyle("-fx-font-weight: bold;");

        // Payment methods
        ToggleGroup paymentMethods = new ToggleGroup();
        RadioButton creditCardOption = new RadioButton("Credit Card");
        creditCardOption.setToggleGroup(paymentMethods);
        RadioButton paypalOption = new RadioButton("PayPal");
        paypalOption.setToggleGroup(paymentMethods);
        RadioButton bankTransferOption = new RadioButton("Bank Transfer");
        bankTransferOption.setToggleGroup(paymentMethods);

        VBox methodsBox = new VBox(10, creditCardOption, paypalOption, bankTransferOption);

        // Confirm button
        Button payButton = new Button("Pay");
        payButton.setStyle("""
            -fx-background-color: #3BB49C;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-background-radius: 15;
        """);
        payButton.setPrefWidth(100);
        payButton.setOnAction(e -> {
            RadioButton selectedMethod = (RadioButton) paymentMethods.getSelectedToggle();
            if (selectedMethod == null) {
                showAlert("Please select a payment method.");
            } else {
                String method = selectedMethod.getText();
                // Simulate payment process
                showAlert("Payment of appointment with " + caregiverInfo + " on " + date + " at " + time + " via " + method + " was successful!");
                // After payment, you might want to refresh the view or update the appointment status
                // For now, just go back to the appointment list
                refreshView();
            }
        });

        // Back button
        Button backButton = new Button("Back");
        backButton.setStyle("""
            -fx-background-color: #ccc;
            -fx-text-fill: black;
            -fx-font-size: 14px;
            -fx-background-radius: 15;
        """);
        backButton.setPrefWidth(100);
        backButton.setOnAction(e -> refreshView());

        HBox buttonsBox = new HBox(10, backButton, payButton);
        buttonsBox.setAlignment(Pos.CENTER);

        paymentPane.getChildren().addAll(title, methodsBox, buttonsBox);

        // Replace current scene with payment UI
        Scene paymentScene = new Scene(paymentPane, 400, 300);
        stage.setScene(paymentScene);
    }

    // Utility method to show alerts
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Refresh the main appointment view
    private void refreshView() {
        new GuardianAppointmentView(stage, null); // Pass connection if needed
    }

    public Scene getScene() {
        return scene;
    }
}