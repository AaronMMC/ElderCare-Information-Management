//package view;
//
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.*;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//import javafx.stage.Stage;
//import java.sql.Connection;
//
//public class GuardianAppointmentView {
//
//    private final Scene scene;
//    private final Stage stage;
//
//    public GuardianAppointmentView(Stage stage, Connection conn) {
//        this.stage = stage;
//
//        // Main container for appointments
//        VBox appointmentsContainer = new VBox(15);
//        appointmentsContainer.setPadding(new Insets(10));
//
//        // Sample appointments
//        appointmentsContainer.getChildren().addAll(
//                createAppointmentCard("Caregiver: Alice Johnson", "2023-10-20", "14:00", "Paid"),
//                createAppointmentCard("Caregiver: Bob Williams", "2023-10-22", "10:00", "Unpaid"),
//                createAppointmentCard("Caregiver: Carol Smith", "2023-10-25", "09:00", "Paid"),
//                createAppointmentCard("Caregiver: David Lee", "2023-10-28", "16:00", "Unpaid")
//        );
//
//        ScrollPane scrollPane = new ScrollPane(appointmentsContainer);
//        scrollPane.setFitToWidth(true);
//        scrollPane.setPrefHeight(400);
//
//        Label titleLabel = new Label("My Appointments");
//        titleLabel.setFont(Font.font("Arial", 24));
//        titleLabel.setStyle("-fx-font-weight: bold;");
//
//        VBox leftPane = new VBox(20, titleLabel, scrollPane);
//        leftPane.setPadding(new Insets(20));
//        leftPane.setStyle("-fx-background-color: white;");
//        leftPane.setPrefWidth(700);
//
//        // Right pane placeholder
//        VBox rightPane = new VBox();
//        rightPane.setPrefWidth(200);
//        rightPane.setStyle("-fx-background-color: #3BB49C;");
//
//        HBox root = new HBox(20, leftPane, rightPane);
//        root.setPadding(new Insets(20));
//
//        this.scene = new Scene(root, 950, 500);
//        stage.setTitle("Guardian Appointments");
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    // Create appointment card
//    private VBox createAppointmentCard(String caregiverInfo, String date, String time, String status) {
//        VBox card = new VBox(8);
//        card.setPadding(new Insets(10));
//        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-border-color: #ccc; -fx-border-radius: 15; -fx-cursor: hand;");
//        card.setMaxWidth(650);
//
//        Label caregiverLabel = new Label(caregiverInfo);
//        caregiverLabel.setFont(Font.font("Arial", 16));
//        caregiverLabel.setStyle("-fx-font-weight: bold;");
//
//        Label dateLabel = new Label("Date: " + date);
//        Label timeLabel = new Label("Time: " + time);
//        Label statusLabel = new Label("Status: " + status);
//        statusLabel.setTextFill(status.equals("Unpaid") ? Color.RED : Color.GREEN);
//
//        Button payButton = null;
//        if (status.equals("Unpaid")) {
//            payButton = new Button("Pay Balance");
//            payButton.setStyle("""
//                -fx-background-color: #3BB49C;
//                -fx-text-fill: white;
//                -fx-font-size: 14px;
//                -fx-background-radius: 15;
//            """);
//            payButton.setPrefWidth(130);
//            payButton.setOnAction(e -> {
//                showPaymentUI(caregiverInfo, date, time);
//            });
//        }
//
//        VBox infoBox = new VBox(4, caregiverLabel, dateLabel, timeLabel, statusLabel);
//        HBox cardContent;
//        if (payButton != null) {
//            cardContent = new HBox(10, infoBox, payButton);
//            cardContent.setAlignment(Pos.CENTER_LEFT);
//        } else {
//            cardContent = new HBox(infoBox);
//        }
//
//        card.getChildren().add(cardContent);
//
//        // Hover effect
//        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 15; -fx-border-color: #ccc; -fx-border-radius: 15; -fx-cursor: hand;"));
//        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-border-color: #ccc; -fx-border-radius: 15;"));
//
//        return card;
//    }
//
//    // Show payment UI for a specific appointment
//    private void showPaymentUI(String caregiverInfo, String date, String time) {
//        VBox paymentPane = new VBox(20);
//        paymentPane.setPadding(new Insets(20));
//        paymentPane.setStyle("-fx-background-color: #fff;");
//
//        Label title = new Label("Select Payment Method");
//        title.setFont(Font.font("Arial", 20));
//        title.setStyle("-fx-font-weight: bold;");
//
//        // Payment methods
//        ToggleGroup paymentMethods = new ToggleGroup();
//        RadioButton creditCardOption = new RadioButton("Credit Card");
//        creditCardOption.setToggleGroup(paymentMethods);
//        RadioButton paypalOption = new RadioButton("PayPal");
//        paypalOption.setToggleGroup(paymentMethods);
//        RadioButton bankTransferOption = new RadioButton("Bank Transfer");
//        bankTransferOption.setToggleGroup(paymentMethods);
//
//        VBox methodsBox = new VBox(10, creditCardOption, paypalOption, bankTransferOption);
//
//        // Confirm button
//        Button payButton = new Button("Pay");
//        payButton.setStyle("""
//            -fx-background-color: #3BB49C;
//            -fx-text-fill: white;
//            -fx-font-size: 14px;
//            -fx-background-radius: 15;
//        """);
//        payButton.setPrefWidth(100);
//        payButton.setOnAction(e -> {
//            RadioButton selectedMethod = (RadioButton) paymentMethods.getSelectedToggle();
//            if (selectedMethod == null) {
//                showAlert("Please select a payment method.");
//            } else {
//                String method = selectedMethod.getText();
//                // Simulate payment process
//                showAlert("Payment of appointment with " + caregiverInfo + " on " + date + " at " + time + " via " + method + " was successful!");
//                // After payment, you might want to refresh the view or update the appointment status
//                // For now, just go back to the appointment list
//                refreshView();
//            }
//        });
//
//        // Back button
//        Button backButton = new Button("Back");
//        backButton.setStyle("""
//            -fx-background-color: #ccc;
//            -fx-text-fill: black;
//            -fx-font-size: 14px;
//            -fx-background-radius: 15;
//        """);
//        backButton.setPrefWidth(100);
//        backButton.setOnAction(e -> refreshView());
//
//        HBox buttonsBox = new HBox(10, backButton, payButton);
//        buttonsBox.setAlignment(Pos.CENTER);
//
//        paymentPane.getChildren().addAll(title, methodsBox, buttonsBox);
//
//        // Replace current scene with payment UI
//        Scene paymentScene = new Scene(paymentPane, 400, 300);
//        stage.setScene(paymentScene);
//    }
//
//    // Utility method to show alerts
//    private void showAlert(String message) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    // Refresh the main appointment view
//    private void refreshView() {
//        new GuardianAppointmentView(stage, null); // Pass connection if needed
//    }
//
//    public Scene getScene() {
//        return scene;
//    }
//}

package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GuardianAppointmentView {

    private final Scene scene;

    public GuardianAppointmentView(Stage stage) {
        // === Logo ===
        ImageView logo = new ImageView(new Image("file:resources/logo.png"));
        logo.setFitWidth(150);
        logo.setPreserveRatio(true);

        // === Title ===
        Label titleLabel = new Label("Your Appointments");
        titleLabel.setFont(Font.font("Arial", 28));
        titleLabel.setStyle("-fx-font-weight: bold;");

        // === Search/Sort Controls ===
        TextField searchField = createRoundedTextField("(Searchfield)");
        ComboBox<String> sortBox = createRoundedComboBox("(Dropdown)");

        HBox searchSortBox = new HBox(20,
                new Label("Search:"), searchField,
                new Label("Sort by:"), sortBox);
        searchSortBox.setAlignment(Pos.CENTER_RIGHT);

        // === Header Section (Title + Controls) ===
        VBox headerBox = new VBox(10, titleLabel, searchSortBox);
        headerBox.setAlignment(Pos.TOP_LEFT);

        // === Appointments Table ===
        GridPane table = new GridPane();
        table.setHgap(10);
        table.setVgap(10);
        table.setPadding(new Insets(20));
        table.setStyle("-fx-border-color: #CBA5F5; -fx-border-width: 2; -fx-background-color: #FDFBFF;");

        // Header row
        addHeaderRow(table);

        // Data rows
        addAppointmentRow(table, 1, "Jose", "Date posted: 01/13/2025\nStatus: Pending\nAppointment On: 02/02/2025\nBalance: 0.00Php\nDue on: xxx");
        addAppointmentRow(table, 2, "Maria", "Date posted: 01/13/2025\nStatus: Pending\nAppointment On: 02/02/2025\nBalance: 0.00Php\nDue on: xxx");
        addAppointmentRow(table, 3, "Pedro", "Date posted: 01/13/2025\nStatus: Pending\nAppointment On: 02/02/2025\nBalance: 0.00Php\nDue on: xxx");

        VBox leftPane = new VBox(20, logo, headerBox, table);
        leftPane.setPadding(new Insets(20));
        leftPane.setPrefWidth(850);

        // === Sidebar ===
        Button submitBtn = createSidebarButton("Submit an Appointment");
        Button goBackBtn = createSidebarButton("Go Back");

        VBox rightPane = new VBox(30, submitBtn);
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setPadding(new Insets(30));
        rightPane.setAlignment(Pos.TOP_CENTER);
        rightPane.setPrefWidth(250);

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        rightPane.getChildren().addAll(spacer, goBackBtn);

        // === Root Layout ===
        HBox root = new HBox(20, leftPane, rightPane);
        root.setPadding(new Insets(20));

        this.scene = new Scene(root, 1150, 650);
        stage.setTitle("Guardian Appointments");
        stage.setScene(scene);
        stage.show();
    }

    private void addHeaderRow(GridPane table) {
        Label caregiverHeader = new Label("Caregivers");
        caregiverHeader.setStyle("-fx-font-weight: bold; -fx-background-color: #E6D1FB; -fx-padding: 10;");
        caregiverHeader.setPrefWidth(150);

        Label detailsHeader = new Label("Appointment Details");
        detailsHeader.setStyle("-fx-font-weight: bold; -fx-background-color: #E6D1FB; -fx-padding: 10;");
        detailsHeader.setPrefWidth(400);

        Label payHeader = new Label();
        payHeader.setPrefWidth(150);

        table.add(caregiverHeader, 0, 0);
        table.add(detailsHeader, 1, 0);
        table.add(payHeader, 2, 0);
    }

    private void addAppointmentRow(GridPane table, int rowIndex, String caregiver, String details) {
        Label caregiverLabel = new Label(caregiver);
        caregiverLabel.setPrefWidth(150);

        Label detailsLabel = new Label(details);
        detailsLabel.setWrapText(true);
        detailsLabel.setPrefWidth(400);

        Button payBtn = createPayButton("Pay");
        HBox btnBox = new HBox(payBtn);
        btnBox.setAlignment(Pos.CENTER_RIGHT);
        btnBox.setPrefWidth(150);

        table.add(caregiverLabel, 0, rowIndex);
        table.add(detailsLabel, 1, rowIndex);
        table.add(btnBox, 2, rowIndex);
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
        cb.setPromptText(prompt);
        cb.setStyle("""
            -fx-background-radius: 15;
            -fx-border-radius: 15;
            -fx-background-color: #D9D9D9;
            -fx-border-color: transparent;
            -fx-padding: 4 8;
        """);
        return cb;
    }

    private Button createPayButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("""
            -fx-background-color: #3BB49C;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-background-radius: 20;
            -fx-cursor: hand;
            -fx-padding: 10 30;
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
