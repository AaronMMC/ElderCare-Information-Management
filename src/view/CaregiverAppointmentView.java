package view;

import controller.AppointmentController;
import controller.ElderController;
import controller.GuardianController;
import controller.PaymentController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CaregiverAppointmentView {

    private final Scene scene;
    private final Stage stage;
    private final Connection conn;
    private final Caregiver caregiver;
    private final FilteredList<Appointment> filteredAppointments;
    private final AppointmentController appointmentController;
    private final GuardianController guardianController;
    private final ElderController elderController;
    private final PaymentController paymentController;
    private final TableView<Appointment> appointmentTable; // Use TableView

    public CaregiverAppointmentView(Stage stage, Connection conn, Caregiver caregiver) throws SQLException {

        this.stage = stage;
        this.conn = conn;
        this.caregiver = caregiver;
        this.appointmentController = new AppointmentController(conn);
        this.guardianController = new GuardianController(conn);
        this.elderController = new ElderController(conn);
        this.paymentController = new PaymentController(conn);

        List<Appointment> appointmentList = appointmentController.getAllAppointmentsByCaregiver(caregiver.getCaregiverID());
        ObservableList<Appointment> observableList = FXCollections.observableArrayList(appointmentList); // Use ObservableList
        filteredAppointments = new FilteredList<>(observableList, p -> true);

        Label titleLabel = new Label("Your Appointments");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold;");

        TextField searchField = createRoundedTextField("Search by Guardian's First Name");
        ComboBox<String> sortBox = createRoundedComboBox("Filter by Status");
        sortBox.getItems().addAll("ALL", "PAID", "UNPAID", "FINISHED", "CANCELLED", "ONGOING");
        sortBox.setValue("PAID");

        // Initialize TableView
        appointmentTable = new TableView<>();
        appointmentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // Make columns fit table width

        // Define columns (Important: Use StringProperty for display)
        TableColumn<Appointment, String> guardianColumn = new TableColumn<>("Guardian");
        guardianColumn.setCellValueFactory(cellData -> {
            Appointment appointment = cellData.getValue();
            Guardian guardian = guardianController.getGuardianByAppointmentId(appointment.getAppointmentID());
            if (guardian != null) {
                return new SimpleStringProperty(guardian.getFirstName() + " " + guardian.getLastName() +
                        "\nPhone: " + guardian.getContactNumber() +
                        "\nEmail: " + guardian.getEmail() +
                        "\nAddress: " + guardian.getAddress());
            } else {
                return new SimpleStringProperty("No Guardian"); // Handle null guardian
            }
        });

        TableColumn<Appointment, String> detailsColumn = new TableColumn<>("Appointment Details");
        detailsColumn.setCellValueFactory(cellData -> {
            Appointment appointment = cellData.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            Payment payment = paymentController.getPaymentByAppointmentId(appointment.getAppointmentID());
            String balance = payment != null ? String.format("Php %.2f", payment.getTotalAmount()) : "Php 0.00";
            String dueDate = formatter.format(appointment.getCreatedDate().plusWeeks(1));

            return new SimpleStringProperty(
                    "Date posted: " + formatter.format(appointment.getCreatedDate()) +
                            "\nStatus: " + appointment.getStatus() +
                            "\nAppointment On: " + formatter.format(appointment.getAppointmentDate()) +
                            "\nBalance: " + balance +
                            "\nDue on: " + dueDate
            );
        });

        TableColumn<Appointment, Void> actionColumn = new TableColumn<>("Actions");
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button approveBtn = createBigGreenButton("Approve");
            private final Button activityButton = createBigGreenButton("Activity Log");
            private final HBox buttonBox = new HBox(5, approveBtn, activityButton); // Add spacing

            {
                buttonBox.setAlignment(Pos.CENTER); // Align buttons in the cell

                approveBtn.setOnAction(event -> {
                    Appointment appointment = getTableRow().getItem(); // Get the appointment
                    if (appointment != null) { //Make sure the appointment isn't null
                        appointment.setStatus(Appointment.AppointmentStatus.ONGOING);
                        appointmentController.updateAppointment(appointment);
                        // Refresh the table
                        updateTableData();
                    }
                });

                activityButton.setOnAction(event -> {
                    Appointment appointment = getTableRow().getItem();
                    if (appointment != null){
                        ActivityView activityView = new ActivityView(stage, conn, appointment, caregiver);
                        stage.setScene(activityView.getScene());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonBox);
                }
            }
        });

        // Add columns to the TableView
        appointmentTable.getColumns().addAll(guardianColumn, detailsColumn, actionColumn);
        appointmentTable.setItems(filteredAppointments); // Set the filtered list as the table's data source.

        // Add listeners
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            updateFilter(searchField, sortBox);
        });

        sortBox.setOnAction(e -> {
            updateFilter(searchField, sortBox);
        });

        updateFilter(searchField, sortBox);
        updateTableData(); // Initial population

        HBox searchSortBox = new HBox(20,
                new Label("Search:"), searchField,
                new Label("Sort by:"), sortBox);
        searchSortBox.setAlignment(Pos.CENTER_RIGHT);

        VBox leftPane = new VBox(20, titleLabel, searchSortBox, appointmentTable);
        leftPane.setPadding(new Insets(20));
        leftPane.setPrefWidth(800);

        Button goBackBtn = createSidebarButton("Go Back");

        goBackBtn.setOnAction(e -> {
            CaregiverView caregiverView = new CaregiverView(stage, conn, caregiver);
            stage.setScene(caregiverView.getScene());
        });

        VBox rightPane = new VBox(30);
        rightPane.setPadding(new Insets(30));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setAlignment(Pos.TOP_CENTER);
        rightPane.setPrefWidth(250);

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        rightPane.getChildren().addAll(spacer, goBackBtn);

        HBox root = new HBox(20, leftPane, rightPane);
        root.setPadding(new Insets(20));

        this.scene = new Scene(root, 1100, 600);
        stage.setTitle("Appointments");
        stage.setScene(scene);
        stage.show();
    }

    private void updateTableData() {
        try {
            List<Appointment> appointmentList = appointmentController.getAllAppointmentsByCaregiver(caregiver.getCaregiverID());
            // 1. Get the source list, which should be an ObservableList
            ObservableList<Appointment> sourceList = (ObservableList<Appointment>) filteredAppointments.getSource();
            // 2. Clear the source list first
            sourceList.clear();
            // 3. Add all the new appointments to the source list
            sourceList.addAll(appointmentList);
            appointmentTable.refresh(); // Refresh the table view
        } catch (Exception e) {
            e.printStackTrace(); // Handle the error appropriately (e.g., show an alert)
            // Consider showing an error message to the user
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading appointments: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void updateFilter(TextField searchField, ComboBox<String> sortBox) {
        String searchText = searchField.getText().toLowerCase();
        String selectedStatus = sortBox.getValue();

        filteredAppointments.setPredicate(appt -> {
            Guardian guardian = guardianController.getGuardianByAppointmentId(appt.getAppointmentID());
            if (guardian == null) return false;

            boolean matchesSearch = guardian.getFirstName().toLowerCase().contains(searchText);
            boolean matchesStatus = selectedStatus.equals("ALL") || appt.getStatus().name().equals(selectedStatus);
            return matchesSearch && matchesStatus;
        });
        appointmentTable.refresh();
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

    private Button createBigGreenButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("""
            -fx-background-color: #3BB49C;
            -fx-text-fill: white;
            -fx-font-size: 16px;
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