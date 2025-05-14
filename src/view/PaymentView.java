package view;

import controller.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.*;

import java.sql.Connection;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PaymentView {

    private Scene scene;
    private double totalAmount;
    private double totalPaidAmount = 0;
    private double totalAmountToPayLeft;
    private final PaymentController paymentController;
    private final Appointment appointment;
    private final Connection conn;
    private List<Payment> payments;
    private final AppointmentController appointmentController;
    private CaregiverServiceController caregiverServiceController;

    private Label amountLabel; // Keep a reference
    private TextField amountField; // Keep a reference
    private TextArea breakdownArea; // Keep a reference to update breakdown if needed
    private NumberFormat pesoFormat;
    private Label titleLabel;
    private Label breakdownLabel;
    private StringBuilder initialBreakdownText;

    public PaymentView(Stage stage, Connection conn, Guardian guardian, Appointment appointment) {
        this.conn = conn;
        this.caregiverServiceController = new CaregiverServiceController(conn);
        CaregiverController caregiverController = new CaregiverController(conn);
        ServiceController serviceController = new ServiceController(conn);
        this.paymentController = new PaymentController(conn);
        this.appointmentController = new AppointmentController(conn);
        this.appointment = appointment;

        this.pesoFormat = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
        computeCosts();
        int caregiverID = appointment.getCaregiverID();
        Caregiver caregiver = caregiverController.getCaregiverById(caregiverID);
        CaregiverService caregiverService = caregiverServiceController.getCaregiverService(caregiverID, appointment.getServiceID());
        Service service = serviceController.getServiceById(appointment.getServiceID());

        doBreakdownOfFees(service, caregiverService, caregiver);
        this.breakdownArea = new TextArea(initialBreakdownText.toString());
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
        this.amountLabel = new Label("Amount to be paid (Balance): ");
        this.amountField = new TextField(pesoFormat.format(totalAmountToPayLeft));
        amountField.setEditable(false); // Make it non-editable
        amountField.setStyle("""
            -fx-background-color: #D9D9D9;
            -fx-border-color: transparent;
            -fx-background-radius: 20;
            -fx-padding: 10;
        """);
        amountField.setPrefWidth(150);

        VBox leftPane = new VBox(20,
                titleLabel,
                breakdownLabel, breakdownArea,
                amountLabel, amountField
        );
        leftPane.setPadding(new Insets(30));
        leftPane.setAlignment(Pos.TOP_LEFT);
        leftPane.setPrefWidth(700);

        // === Right Sidebar ===
        Label paymentModeLabel = new Label("Choose which mode of payment:");
        paymentModeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
        paymentModeLabel.setFont(Font.font("Arial", 16));

        Button debitButton = createSidebarButton("Debit");
        Button creditButton = createSidebarButton("Credit");
        Button ewalletButton = createSidebarButton("E-Wallet");
        Button othersButton = createSidebarButton("Others");
        Button goBackButton = createSidebarButton("Go Back");

        debitButton.setOnAction(e -> showPaymentInputDialog(Payment.PaymentMethod.DEBIT));
        creditButton.setOnAction(e -> showPaymentInputDialog(Payment.PaymentMethod.CREDIT));
        ewalletButton.setOnAction(e -> showPaymentInputDialog(Payment.PaymentMethod.E_WALLET));
        othersButton.setOnAction(e -> showPaymentInputDialog(Payment.PaymentMethod.OTHER));

        goBackButton.setOnAction(e -> {
            GuardianAppointmentView guardianAppointmentView = new GuardianAppointmentView(stage, conn, guardian);
            stage.setScene(guardianAppointmentView.getScene());
        });

        VBox rightPane = new VBox(20,
                paymentModeLabel, debitButton, creditButton, ewalletButton, othersButton, goBackButton
        );
        rightPane.setPadding(new Insets(30));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setAlignment(Pos.TOP_CENTER);
        rightPane.setPrefWidth(250);

        reshowScreen(rightPane, goBackButton, leftPane, stage);
    }

    private void reshowScreen(VBox rightPane, Button goBackButton, VBox leftPane, Stage stage) {
        // Push goBack button to the bottom
        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        rightPane.getChildren().add(spacer);
        rightPane.getChildren().remove(goBackButton); // optional safety if needed
        rightPane.getChildren().add(goBackButton); // move to bottom


        // === Root Layout ===
        HBox root = new HBox(20, leftPane, rightPane);
        root.setPadding(new Insets(20));

        this.scene = new Scene(root, 1000, 600);
        stage.setTitle("Payment Details");
        stage.setScene(scene);
        stage.show();
    }


    private void doBreakdownOfFees(Service service, CaregiverService caregiverService, Caregiver caregiver) {
        // === Title ===
        titleLabel = new Label("Payment Details");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold;");

        // === Balance Breakdown ===
        breakdownLabel = new Label("Balance breakdown:");
        initialBreakdownText = new StringBuilder();

        initialBreakdownText.append(String.format("Service: %s (%s)%n", service.getServiceName(), service.getCategory()));
        initialBreakdownText.append(String.format("Caregiver: %s %s%n", caregiver.getFirstName(), caregiver.getLastName()));
        initialBreakdownText.append(String.format("Duration (Hour): %s%n", appointment.getDuration()));
        initialBreakdownText.append(String.format("Caregiver's Hourly Rate: %s%n",pesoFormat.format(caregiverService.getHourlyRate())));
        initialBreakdownText.append(String.format("------------------------------------%n"));
        initialBreakdownText.append(String.format("Total Cost: %s%n", pesoFormat.format(totalAmount)));
        initialBreakdownText.append(String.format("Total Paid Amount: %s%n", pesoFormat.format(totalPaidAmount)));
        initialBreakdownText.append(String.format("------------------------------------%n"));
        initialBreakdownText.append(String.format("Balance: %s%n", pesoFormat.format(totalAmountToPayLeft)));
    }


    private void computeCosts() {
        payments = paymentController.getPaymentsByAppointmentId(appointment.getAppointmentID());
        for (Payment payment : payments)
            totalPaidAmount += payment.getAmountPaid();
        totalAmount = appointment.getTotalCost();
        totalAmountToPayLeft = totalAmount - totalPaidAmount;
    }

    private void showPaymentInputDialog(Payment.PaymentMethod paymentMethod) {
        Stage paymentStage = new Stage();
        paymentStage.setTitle("Enter Payment Amount");

        Label amountLabel = new Label("Enter Amount:");
        TextField amountInput = new TextField();
        Button payButton = new Button("Pay");

        VBox layout = new VBox(10, amountLabel, amountInput, payButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 300, 150);
        paymentStage.setScene(scene);
        paymentStage.show();

        payButton.setOnAction(event -> {
            try {
                double amountEntered = Double.parseDouble(amountInput.getText());

                if (amountEntered <= 0) {
                    showAlert("Invalid Input", "Please enter an amount greater than zero.");
                    return;
                }

                if (amountEntered > totalAmountToPayLeft) {
                    showAlert("Invalid Input", "Amount entered exceeds the remaining balance (" + formatPeso(totalAmountToPayLeft) + ").");
                    return;
                }

                // Record payment
                Payment payment = new Payment(appointment.getAppointmentID(), amountEntered, paymentMethod);
                paymentController.addPayment(payment);

                totalAmountToPayLeft -= amountEntered;

                if (totalAmountToPayLeft <= 0) {
                    appointment.setPaymentStatus(Appointment.PaymentStatus.PAID);
                    appointmentController.updateAppointmentPaymentStatus(appointment.getAppointmentID(), Appointment.PaymentStatus.PAID);
                    showAlert("Payment Successful", "Appointment has been fully paid.");
                } else {
                    showAlert("Payment Received", "Amount paid: " + formatPeso(amountEntered) + ". Remaining balance: " + formatPeso(totalAmountToPayLeft));
                }

                // Update UI labels
                amountLabel.setText("Amount to be paid (Balance): " + formatPeso(totalAmountToPayLeft));
                amountField.setText(formatPeso(totalAmountToPayLeft));

                // Append payment record to breakdown area
                breakdownArea.appendText(String.format("%nPayment of %s via %s recorded.", formatPeso(amountEntered), paymentMethod));

                paymentStage.close();

            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter a numeric amount.");
            }
        });

    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private String formatPeso(double amount) {
        NumberFormat pesoFormat = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
        return pesoFormat.format(amount);
    }

    private CaregiverService findCaregiverServiceForService(Service service, List<CaregiverService> caregiverServices) {
        return caregiverServices.stream()
                .filter(cs -> cs.getServiceId() == service.getServiceID())
                .findFirst()
                .orElse(null);
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
