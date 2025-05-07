package view;

import dao.AppointmentDAO;
import dao.ElderDAO;
import dao.GuardianDAO;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CaregiverAppointmentView {

    private final Scene scene;
    private final FilteredList<Appointment> filteredAppointments;

    public CaregiverAppointmentView(Stage stage, Connection conn, Caregiver caregiver) throws SQLException {

        AppointmentDAO appointmentDAO = new AppointmentDAO(conn);
        GuardianDAO guardianDAO = new GuardianDAO(conn);
        ElderDAO elderDAO = new ElderDAO(conn);

        List<Appointment> appointmentList = appointmentDAO.getAllAppointmentsByCaregiver(caregiver.getCaregiverID());
        filteredAppointments = new FilteredList<>(FXCollections.observableArrayList(appointmentList), p -> true);

        Label titleLabel = new Label("Your Appointments");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold;");

        TextField searchField = createRoundedTextField("Search by Guardian's First Name");
        ComboBox<String> sortBox = createRoundedComboBox("Filter by Status");
        sortBox.getItems().addAll("ALL", "PAID", "UNPAID", "FINISHED", "CANCELLED", "ONGOING");
        sortBox.setValue("ALL");

        searchField.textProperty().addListener((obs, oldVal, newVal) -> updateFilter(searchField, sortBox, guardianDAO));
        sortBox.setOnAction(e -> updateFilter(searchField, sortBox, guardianDAO));

        HBox searchSortBox = new HBox(20,
                new Label("Search:"), searchField,
                new Label("Sort by:"), sortBox);
        searchSortBox.setAlignment(Pos.CENTER_RIGHT);

        GridPane table = new GridPane();
        table.setHgap(20);
        table.setVgap(20);
        table.setPadding(new Insets(20));
        table.setStyle("-fx-border-color: #B891F1; -fx-border-width: 2; -fx-background-color: #F9F9F9;");
        table.setPrefWidth(750);

        Label guardiansHeader = new Label("Guardians");
        guardiansHeader.setStyle("-fx-font-weight: bold; -fx-background-color: #E2C8FD; -fx-padding: 10;");
        Label detailsHeader = new Label("Appointment Details");
        detailsHeader.setStyle("-fx-font-weight: bold; -fx-background-color: #E2C8FD; -fx-padding: 10;");
        Label actionHeader = new Label("");

        guardiansHeader.setPrefWidth(150);
        detailsHeader.setPrefWidth(400);

        table.add(guardiansHeader, 0, 0);
        table.add(detailsHeader, 1, 0);
        table.add(actionHeader, 2, 0);

        populateTable(table, guardianDAO, elderDAO, appointmentDAO);

        VBox leftPane = new VBox(20, titleLabel, searchSortBox, table);
        leftPane.setPadding(new Insets(20));
        leftPane.setPrefWidth(800);

        Button scheduleBtn = createSidebarButton("Your Schedule");
        Button goBackBtn = createSidebarButton("Go Back");

        scheduleBtn.setOnAction(e -> {
            CaregiverScheduleView scheduleView = new CaregiverScheduleView(stage, conn, caregiver);
            stage.setScene(scheduleView.getScene());
        });

        goBackBtn.setOnAction(e -> {
            CaregiverView caregiverView = new CaregiverView(stage, conn, caregiver);
            stage.setScene(caregiverView.getScene());
        });

        VBox rightPane = new VBox(30, scheduleBtn);
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

    private void populateTable(GridPane table, GuardianDAO guardianDAO, ElderDAO elderDAO, AppointmentDAO appointmentDAO) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        table.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0);

        int rowIndex = 1;
        for (Appointment appointment : filteredAppointments) {
            Guardian guardian = guardianDAO.getGuardianByAppointmentId(appointment.getAppointmentID());
            List<Elder> elders = elderDAO.getAllEldersByAppointmentId(appointment.getAppointmentID());

            StringBuilder guardianInfo = new StringBuilder();
            guardianInfo.append(guardian.getFirstName()).append(" ").append(guardian.getLastName())
                    .append("\nPhone: ").append(guardian.getContactNumber())
                    .append("\nEmail: ").append(guardian.getEmail())
                    .append("\nAddress: ").append(guardian.getAddress());

            StringBuilder details = new StringBuilder();
            details.append("Created: ").append(formatter.format(appointment.getCreatedDate()))
                    .append("\nScheduled: ").append(formatter.format(appointment.getAppointmentDate()))
                    .append("\nDuration: ").append(appointment.getDuration() / 60).append(" hours")
                    .append("\nStatus: ").append(appointment.getStatus().name());

            for (Elder elder : elders) {
                int age = Period.between(elder.getDateOfBirth().toLocalDate(), LocalDate.now()).getYears();
                details.append("\n\nElder: ").append(elder.getFirstName()).append(" ").append(elder.getLastName())
                        .append("\nAge: ").append(age)
                        .append("\nPhone: ").append(elder.getContactNumber())
                        .append("\nEmail: ").append(elder.getEmail())
                        .append("\nAddress: ").append(elder.getAddress());
            }

            Label guardianLabel = new Label(guardianInfo.toString());
            guardianLabel.setPrefWidth(150);
            guardianLabel.setWrapText(true);

            Label detailsLabel = new Label(details.toString());
            detailsLabel.setPrefWidth(400);
            detailsLabel.setWrapText(true);

            Button approveBtn = createBigGreenButton("Approve");
            approveBtn.setOnAction(e -> {
                appointment.setStatus(Appointment.AppointmentStatus.ONGOING);
                appointmentDAO.updateAppointment(appointment);

                // Refresh status line in the label
                String updatedDetails = details.toString().replaceFirst(
                        "Status: \\w+", "Status: " + appointment.getStatus().name()
                );
                detailsLabel.setText(updatedDetails);
            });

            HBox buttonBox = new HBox(approveBtn);
            buttonBox.setAlignment(Pos.CENTER_RIGHT);
            buttonBox.setPrefWidth(150);

            table.add(guardianLabel, 0, rowIndex);
            table.add(detailsLabel, 1, rowIndex);
            table.add(buttonBox, 2, rowIndex);
            rowIndex++;
        }
    }

    private void updateFilter(TextField searchField, ComboBox<String> sortBox, GuardianDAO guardianDAO) {
        String searchText = searchField.getText().toLowerCase();
        String selectedStatus = sortBox.getValue();

        filteredAppointments.setPredicate(appt -> {
            Guardian guardian = guardianDAO.getGuardianByAppointmentId(appt.getAppointmentID());
            boolean matchesSearch = guardian.getFirstName().toLowerCase().contains(searchText);
            boolean matchesStatus = selectedStatus.equals("ALL") || appt.getStatus().name().equals(selectedStatus);
            return matchesSearch && matchesStatus;
        });
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