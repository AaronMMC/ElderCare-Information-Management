package view;

import controller.AppointmentController;
import controller.CaregiverController;
import controller.CaregiverServiceController;
import controller.PaymentController;
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

public class PaymentView{

    private final Scene scene;
    private Payment payment = new Payment();
    private final PaymentController paymentController;
    private final Appointment appointment;
    private final Connection conn;
    private final AppointmentController appointmentController;
    private final List<Service> services;

    private Label amountLabel; // Keep a reference
    private TextField amountField; // Keep a reference
    private TextArea breakdownArea; // Keep a reference to update breakdown if needed
    private NumberFormat pesoFormat;

    public PaymentView(Stage stage, Connection conn, Guardian guardian, Appointment appointment) {
        this.conn = conn;
        CaregiverController caregiverController = new CaregiverController(conn);
        CaregiverServiceController caregiverServiceController = new CaregiverServiceController(conn);
        this.paymentController = new PaymentController(conn);
        this.appointmentController = new AppointmentController(conn);
        this.appointment = appointment;

        this.pesoFormat = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));

        int caregiverID = appointment.getCaregiverID();
        Caregiver caregiver = caregiverController.getCaregiverById(caregiverID);
        this.services = caregiverServiceController.getAllServicesByCaregiverId(caregiverID);

        List<CaregiverService> caregiverServices = services.stream()
                .map(service -> new CaregiverService(service.getServiceID(), caregiver.getCaregiverID()))
                .toList();

        payment = paymentController.getPaymentByAllServices(appointment, caregiverServices, services);

        // === Title ===
        Label titleLabel = new Label("Payment Details");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold;");

        // === Balance Breakdown ===
        Label breakdownLabel = new Label("Balance breakdown:");
        StringBuilder initialBreakdownText = new StringBuilder();
        double totalServiceCost = 0.0;
        double totalAdditionalCharges = 0.0;
        int durationInSeconds = appointment.getDuration();
        double durationInHours = durationInSeconds / 3600.0; // Convert to hours

        for (Service service : services) {
            totalServiceCost += service.getPrice();
            CaregiverService correspondingCaregiverService = findCaregiverServiceForService(service, caregiverServices);
            double additionalCharge = 0.0;
            double hourlyRate = 0.0;

            if (correspondingCaregiverService != null) {
                hourlyRate = correspondingCaregiverService.getHourlyRate();
                additionalCharge = hourlyRate * durationInHours;
                totalAdditionalCharges += additionalCharge;
                initialBreakdownText.append(String.format("- Service: %s (%s), Price: %s, Caregiver Rate: %s/hour, Additional Charge: %s%n",
                        service.getServiceName(), service.getCategory(), pesoFormat.format(service.getPrice()), pesoFormat.format(hourlyRate), pesoFormat.format(additionalCharge)));
            } else {
                initialBreakdownText.append(String.format("- Service: %s (%s), Price: %s, Caregiver Rate: N/A, Additional Charge: N/A (No matching caregiver service)%n",
                        service.getServiceName(), service.getCategory(), pesoFormat.format(service.getPrice())));
            }
        }

        initialBreakdownText.append(String.format("%nTotal Service Cost: %s%n", pesoFormat.format(totalServiceCost)));
        initialBreakdownText.append(String.format("Total Additional Charges: %s%n", pesoFormat.format(totalAdditionalCharges)));
        initialBreakdownText.append(String.format("-----------------------------%n"));
        initialBreakdownText.append(String.format("Total Amount: %s%n", pesoFormat.format(payment.getTotalAmount())));

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
        this.amountLabel = new Label("Amount to be paid: " + pesoFormat.format(payment.getTotalAmount()));
        this.amountField = new TextField(pesoFormat.format(payment.getTotalAmount()));
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
                double amountPaid = Double.parseDouble(amountInput.getText());
                if (amountPaid > 0) {
                    double currentTotal = payment.getTotalAmount();
                    payment.setTotalAmount(Math.max(0, currentTotal - amountPaid));
                    payment.setPaymentMethod(paymentMethod);
                    paymentController.addPayment(payment);

                    // Add payment history to the breakdown area
                    breakdownArea.appendText(String.format("%nPayment of %s via %s recorded.", formatPeso(amountPaid), paymentMethod));

                    if (payment.getTotalAmount() <= 0) {
                        appointment.setStatus(Appointment.AppointmentStatus.PAID);
                        appointmentController.updateAppointment(appointment);
                        showAlert("Payment Successful", "Appointment has been fully paid.");
                    } else {
                        showAlert("Payment Received", "Amount paid: " + formatPeso(amountPaid) + ". Remaining balance: " + formatPeso(payment.getTotalAmount()));
                    }

                    // Update the amountLabel in the main PaymentView
                    amountLabel.setText("Amount to be paid: " + formatPeso(payment.getTotalAmount()));
                    amountField.setText(formatPeso(payment.getTotalAmount()));

                    paymentStage.close();
                } else {
                    showAlert("Invalid Input", "Please enter a valid amount.");
                }
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
