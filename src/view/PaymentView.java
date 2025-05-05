package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class PaymentView{

    private final Scene scene;

    public PaymentView(Stage stage) {

        // === Title ===
        Label titleLabel = new Label("Payment Details");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold;");

        // === Balance Breakdown ===
        Label breakdownLabel = new Label("Balance breakdown:");
        TextArea breakdownArea = new TextArea("""
            (Additional charges which will compose of set of constant
            values in java i.e. base rate for appointment query, duration to
            hourly total amount etc.. )
        """);
        breakdownArea.setWrapText(true);
        breakdownArea.setEditable(false);
        breakdownArea.setStyle("""
            -fx-background-color: #D9D9D9;
            -fx-border-color: transparent;
            -fx-background-radius: 20;
            -fx-padding: 10;
            -fx-font-style: italic;
            -fx-font-weight: bold;
        """);
        breakdownArea.setPrefHeight(200);

        // === Amount Field ===
        Label amountLabel = new Label("Amount to be paid:");
        TextField amountField = new TextField();
        amountField.setStyle("""
            -fx-background-color: #D9D9D9;
            -fx-border-color: transparent;
            -fx-background-radius: 20;
            -fx-padding: 10;
        """);

        // === Pay Button ===
        Button payButton = new Button("Pay");
        payButton.setStyle("""
            -fx-background-color: #3BB49C;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-background-radius: 20;
            -fx-padding: 10 30;
            -fx-cursor: hand;
        """);
        payButton.setPrefWidth(180);

        VBox leftPane = new VBox(20,
                titleLabel,
                breakdownLabel, breakdownArea,
                amountLabel, amountField,
                payButton
        );
        leftPane.setPadding(new Insets(30));
        leftPane.setAlignment(Pos.TOP_LEFT);
        leftPane.setPrefWidth(700);

        // === Right Sidebar ===
        Label paymentModeLabel = new Label("Choose which mode of payment:");
        paymentModeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
        paymentModeLabel.setFont(Font.font("Arial", 16));

        Button debitButton = createSidebarButton("Debit/ Credit");
        Button ewalletButton = createSidebarButton("E-Wallet");
        Button othersButton = createSidebarButton("Others");
        Button goBackButton = createSidebarButton("Go Back");

        VBox rightPane = new VBox(20,
                paymentModeLabel, debitButton, ewalletButton, othersButton
        );
        rightPane.setPadding(new Insets(30));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setAlignment(Pos.TOP_CENTER);
        rightPane.setPrefWidth(250);

        // Push goBack button to the bottom
        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        rightPane.getChildren().addAll(spacer, goBackButton);

        // === Root Layout ===
        HBox root = new HBox(20, leftPane, rightPane);
        root.setPadding(new Insets(20));

        this.scene = new Scene(root, 1000, 600);
        stage.setTitle("Payment Details");
        stage.setScene(scene);
        stage.show();
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
