package view;

import controller.AppointmentController;
import controller.ElderController;
import controller.ServiceController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Appointment;
import model.Caregiver;
import model.Elder;
import model.Service;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CaregiverElderView {

    private final Scene scene;
    private final Stage stage;
    private final GridPane table = new GridPane();

    private final Connection conn;
    private final AppointmentController appointmentController;
    private final ElderController elderController;
    private final ServiceController serviceController;
    private final Caregiver caregiver;
    private final ComboBox<String> sortBox = createRoundedComboBox("Filter by Status");

    public CaregiverElderView(Stage stage, Connection conn, Caregiver caregiver) {
        this.caregiver = caregiver;
        this.conn = conn;
        this.stage = stage;
        this.appointmentController = new AppointmentController(conn);
        this.elderController = new ElderController(conn);
        this.serviceController = new ServiceController(conn);

        Label titleLabel = new Label("Your Elders");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold;");

        sortBox.getItems().addAll("PENDING", "FINISHED", "CANCELLED", "ONGOING");
        sortBox.setValue("ONGOING");

        sortBox.setOnAction(e -> populateTable());

        HBox filterBox = new HBox(20, new Label("Filter by:"), sortBox);
        filterBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(sortBox, Priority.ALWAYS);

        table.setHgap(10);
        table.setVgap(10);
        table.setPadding(new Insets(15));
        table.setStyle("-fx-border-color: #B891F1; -fx-border-width: 2; -fx-background-color: #F9F9F9;");

        // Use percentages for column constraints to make it responsive
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(20);
        col1.setHgrow(Priority.ALWAYS);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(60);
        col2.setHgrow(Priority.ALWAYS);

        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(20);
        col3.setHgrow(Priority.ALWAYS);

        table.getColumnConstraints().addAll(col1, col2, col3);

        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color:transparent;");
        scrollPane.setPrefHeight(220);

        VBox leftPane = new VBox(20, titleLabel, filterBox, scrollPane);
        leftPane.setPadding(new Insets(20));
        leftPane.setPrefWidth(800);
        HBox.setHgrow(leftPane, Priority.ALWAYS);
        VBox.setVgrow(leftPane, Priority.ALWAYS);

        Button goBackBtn = createSidebarButton("Go Back");
        goBackBtn.setOnAction(e -> {
            CaregiverView caregiverView = new CaregiverView(stage, conn, caregiver);
            stage.setScene(caregiverView.getScene());
        });

        VBox rightPane = new VBox();
        rightPane.setPadding(new Insets(30));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setAlignment(Pos.BOTTOM_CENTER);
        rightPane.setPrefWidth(250);

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        rightPane.getChildren().addAll(spacer, goBackBtn);

        HBox root = new HBox(20, leftPane, rightPane);
        root.setPadding(new Insets(20));
        HBox.setHgrow(leftPane, Priority.ALWAYS);

        this.scene = new Scene(root, 1100, 600);
        stage.setTitle("Your Elders");
        stage.setScene(scene);
        stage.show();

        populateTable();
        stage.setResizable(true);
        makeLayoutResponsive(root);
    }

    private void makeLayoutResponsive(HBox root) {
        scene.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double width = newWidth.doubleValue();
            VBox leftPane = (VBox) root.getChildren().get(0);
            VBox rightPane = (VBox) root.getChildren().get(1);

            if (width < 800) {
                leftPane.setPrefWidth(width * 0.9);
                rightPane.setPrefWidth(width * 0.9);
            } else {
                leftPane.setPrefWidth(width * 0.7);
                rightPane.setPrefWidth(250);
            }
            //make the table also resize
            table.requestLayout();
        });
    }

    private void populateTable() {
        table.getChildren().clear();

        Label eldersHeader = new Label("Elders");
        eldersHeader.setStyle("-fx-font-weight: bold; -fx-background-color: #E2C8FD; -fx-padding: 10;");
        eldersHeader.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(eldersHeader, Priority.ALWAYS);

        Label detailsHeader = new Label("Service Details");
        detailsHeader.setStyle("-fx-font-weight: bold; -fx-background-color: #E2C8FD; -fx-padding: 10;");
        detailsHeader.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(detailsHeader, Priority.ALWAYS);

        Label actionHeader = new Label("Actions");
        actionHeader.setStyle("-fx-font-weight: bold; -fx-background-color: #E2C8FD; -fx-padding: 10;");
        actionHeader.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(actionHeader, Priority.ALWAYS);
        actionHeader.setAlignment(Pos.CENTER);

        table.add(eldersHeader, 0, 0);
        table.add(detailsHeader, 1, 0);
        table.add(actionHeader, 2, 0);

        String selectedStatus = sortBox.getValue();
        List<Appointment> appointments = appointmentController.getAllAppointmentsByCaregiver(caregiver.getCaregiverID());

        if (!"ALL".equals(selectedStatus)) { // Only filter if a status is selected
            Appointment.AppointmentStatus filterStatus = Appointment.AppointmentStatus.valueOf(selectedStatus);
            appointments = appointments.stream()
                    .filter(a -> a.getStatus() == filterStatus)
                    .toList();
        }

        int rowIndex = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        for (Appointment appointment : appointments) {
            Elder elder = elderController.getElderById(appointment.getElderID());
            List<Service> services = serviceController.getAllServicesByAppointmentId(appointment.getAppointmentID());

            int age = Period.between(elder.getDateOfBirth().toLocalDate(), LocalDate.now()).getYears();
            String elderInfo = elder.getFirstName() + " " + elder.getLastName() +
                    "\nAge: " + age +
                    "\nPhone: " + elder.getContactNumber() +
                    "\nEmail: " + elder.getEmail() +
                    "\nAddress: " + elder.getAddress();

            StringBuilder details = new StringBuilder();
            details.append("Created: ").append(formatter.format(appointment.getCreatedDate()))
                    .append("\nScheduled: ").append(formatter.format(appointment.getAppointmentDate()))
                    .append("\nDuration: ").append(appointment.getDuration() / 60).append(" hours")
                    .append("\nStatus: ").append(appointment.getStatus().name())
                    .append("\n\nServices:");
            for (Service service : services) {
                details.append("\n- ").append(service.getServiceName())
                        .append(" (Type: ").append(service.getCategory()).append(")");
            }

            Label elderLabel = new Label(elderInfo);
            elderLabel.setWrapText(true);
            elderLabel.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(elderLabel, Priority.ALWAYS);

            Label detailsLabel = new Label(details.toString());
            detailsLabel.setWrapText(true);
            detailsLabel.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(detailsLabel, Priority.ALWAYS);

            Button markDoneBtn = createBigGreenButton("Mark as done");
            Button seeRecordBtn = createBigGreenButton("See Medical Record");
            Button activityButton = createBigGreenButton("Activity Log");

            markDoneBtn.setOnAction(e -> {
                appointment.setStatus(Appointment.AppointmentStatus.FINISHED);
                appointmentController.updateAppointment(appointment);
                populateTable();
            });

            seeRecordBtn.setOnAction(e -> {
                MedicalRecordView medicalRecordView = new MedicalRecordView(stage, conn, elder, caregiver);
                stage.setScene(medicalRecordView.getScene());
            });

            activityButton.setOnAction(event -> {
                ActivityView activityView = new ActivityView(stage, conn, appointment, caregiver);
                stage.setScene(activityView.getScene());
            });

            HBox buttonBox = new HBox(10, markDoneBtn, seeRecordBtn, activityButton);
            buttonBox.setAlignment(Pos.CENTER_RIGHT);
            buttonBox.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(buttonBox, Priority.ALWAYS);

            table.add(elderLabel, 0, rowIndex);
            table.add(detailsLabel, 1, rowIndex);
            table.add(buttonBox, 2, rowIndex);
            rowIndex++;
        }
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