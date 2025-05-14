package view;

import controller.AppointmentController;
import controller.CaregiverController;
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
import java.util.List;

public class GuardianAppointmentView {

    private final Scene scene;
    private final Stage stage;
    private final Connection conn;
    private final Guardian guardian;
    private final PaymentController paymentController;

    public GuardianAppointmentView(Stage stage, Connection conn, Guardian guardian) {
        this.stage = stage;
        this.conn = conn;
        this.guardian = guardian;
        this.paymentController = new PaymentController(conn);

        Label titleLabel = new Label("Your Appointments");
        titleLabel.setFont(Font.font("Arial", 28));
        titleLabel.setStyle("-fx-font-weight: bold;");

        TextField searchField = createRoundedTextField("Search by caregiver's first name");
        ComboBox<String> sortBox = createRoundedComboBox("Filter by status");

        HBox searchSortBox = new HBox(20,
                new Label("Search:"), searchField,
                new Label("Sort by:"), sortBox);
        searchSortBox.setAlignment(Pos.CENTER_RIGHT);

        VBox headerBox = new VBox(10, titleLabel, searchSortBox);
        headerBox.setAlignment(Pos.TOP_LEFT);

        GridPane table = new GridPane();
        table.setHgap(10);
        table.setVgap(10);
        table.setMaxWidth(Double.MAX_VALUE);
        table.setPadding(new Insets(20));
        table.setStyle("-fx-border-color: #CBA5F5; -fx-border-width: 2; -fx-background-color: #FDFBFF;");

        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background:transparent;");
        scrollPane.setPrefHeight(500);

        VBox leftPane = new VBox(20, headerBox, scrollPane);

        leftPane.setPadding(new Insets(20));
        leftPane.setPrefWidth(850);

        Button submitBtn = createSidebarButton("Submit an Appointment");
        Button goBackBtn = createSidebarButton("Go Back");

        submitBtn.setOnAction(e -> {
            AppointmentView appointmentView = new AppointmentView(stage, conn, guardian);
            stage.setScene(appointmentView.getScene());
        });

        goBackBtn.setOnAction(e -> {
            GuardianView guardianView = new GuardianView(stage, conn, guardian);
            stage.setScene(guardianView.getScene());
        });

        VBox rightPane = new VBox(30, submitBtn);
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setPadding(new Insets(30));
        rightPane.setAlignment(Pos.TOP_CENTER);
        rightPane.setPrefWidth(250);

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        rightPane.getChildren().addAll(spacer, goBackBtn);

        HBox root = new HBox(20, leftPane, rightPane);
        root.setPadding(new Insets(20));

        this.scene = new Scene(root, 1150, 650);
        stage.setTitle("Guardian Appointments");
        stage.setScene(scene);
        stage.show();

        sortBox.getItems().addAll("PENDING", "ONGOING", "FINISHED", "CANCELLED");
        sortBox.setValue("PENDING");

        searchField.textProperty().addListener((obs, oldVal, newVal) ->
                populateAppointments(table, newVal));

        sortBox.setOnAction(e ->
                populateAppointments(table, searchField.getText()));

        populateAppointments(table, "");
    }

    private void populateAppointments(GridPane table, String searchTerm) {
        table.getChildren().clear();
        addHeaderRow(table);

        AppointmentController appointmentController = new AppointmentController(conn);
        CaregiverController caregiverController = new CaregiverController(conn);

        List<Appointment> appointments = appointmentController.getAllAppointmentsByGuardian(guardian.getGuardianID());
        int row = 1;

        for (Appointment appt : appointments) {
            Caregiver caregiver = caregiverController.getCaregiverById(appt.getCaregiverID());

            // Debugging: Check if caregiver is null and log details
            if (caregiver == null) {
                System.out.println("DEBUG: Caregiver is null for Appointment ID: " + appt.getAppointmentID() +
                        ", Caregiver ID: " + appt.getCaregiverID());
                continue; // Skip to the next appointment
            }

            String caregiverFullName = caregiver.getFirstName() + " " + caregiver.getLastName();

            // Debugging: Log the queried full name and the search term
            System.out.println("DEBUG: Queried Full Name: \"" + caregiverFullName + "\", Search Term: \"" + searchTerm + "\"");

            if (!caregiverFullName.toLowerCase().contains(searchTerm.toLowerCase())) {
                System.out.println("DEBUG: Full name does not contain search term, skipping.");
                continue; // Skip if the full name doesn't contain the search term
            }

            String details = String.format("""
                                Date posted: %s
                                Appointment On: %s
                                Total Cost: %s
                                Payment Status: %s
                                """,
                    appt.getCreatedDate().toLocalDate(),
                    appt.getAppointmentDate().toLocalDate(),
                    appt.getTotalCost(),
                    appt.getPaymentStatus()
            );

            addAppointmentRow(table, row++, caregiverFullName, details, appt); // Pass the full name
        }
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

    private void addAppointmentRow(GridPane table, int rowIndex, String caregiver, String details, Appointment appointment) {
        // Debugging: Log the caregiver name received by this method
        System.out.println("DEBUG: Adding row for Caregiver: \"" + caregiver + "\" (Appointment ID: " + appointment.getAppointmentID() + ")");

        Label caregiverLabel = new Label(caregiver);
        caregiverLabel.setPrefWidth(150);
        caregiverLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16pt; -fx-background-color: yellow;"); // Add these styles

        Label detailsLabel = new Label(details);
        detailsLabel.setStyle("-fx-text-fill: black; -fx-font-size: 12pt; -fx-background-color: lightgray;");
        detailsLabel.setWrapText(true);
        detailsLabel.setPrefWidth(400);

        HBox btnBox = new HBox();
        btnBox.setAlignment(Pos.CENTER_RIGHT);
        btnBox.setPrefWidth(150);
        System.out.println("The appointment id in the guardian appointment view is : " + appointment.getAppointmentID());
        System.out.println("And it's payment status is : " + appointment.getPaymentStatus());
        System.out.println(appointment.getPaymentStatus() == Appointment.PaymentStatus.PENDING);

        if (appointment.getPaymentStatus() == Appointment.PaymentStatus.PENDING) {
            Button payBtn = createPayButton("Pay");
            payBtn.setOnAction(e -> {
                PaymentView paymentView = new PaymentView(stage, conn, guardian, appointment);
                stage.setScene(paymentView.getScene());
                populateAppointments(table, "");
            });
            btnBox.getChildren().add(payBtn);
        }

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