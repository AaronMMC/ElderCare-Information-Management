package view;

import dao.AppointmentDAO;
import dao.ElderDAO;
import dao.ServiceDAO;
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

    public CaregiverElderView(Stage stage, Connection conn, Caregiver caregiver) {
        Label titleLabel = new Label("Your Elders");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold;");

        TextField searchField = createRoundedTextField("(Searchfield)");
        ComboBox<String> sortBox = createRoundedComboBox("(Dropdown)");

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

        Label eldersHeader = new Label("Elders");
        eldersHeader.setStyle("-fx-font-weight: bold; -fx-background-color: #E2C8FD; -fx-padding: 10;");
        Label detailsHeader = new Label("Service Details");
        detailsHeader.setStyle("-fx-font-weight: bold; -fx-background-color: #E2C8FD; -fx-padding: 10;");
        Label actionHeader = new Label("");

        eldersHeader.setPrefWidth(150);
        detailsHeader.setPrefWidth(400);

        table.add(eldersHeader, 0, 0);
        table.add(detailsHeader, 1, 0);
        table.add(actionHeader, 2, 0);

        ElderDAO elderDAO = new ElderDAO(conn);
        ServiceDAO serviceDAO = new ServiceDAO(conn);
        AppointmentDAO appointmentDAO = new AppointmentDAO(conn);
        List<Appointment> appointments = appointmentDAO.getAllAppointmentsByCaregiver(caregiver.getCaregiverID());

        int rowIndex = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        for (Appointment appointment : appointments) {
            List<Elder> elders = elderDAO.getAllEldersByAppointmentId(appointment.getAppointmentID());
            List<Service> services = serviceDAO.getAllServicesByAppointmentId(appointment.getAppointmentID());

            for (Elder elder : elders) {
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
                elderLabel.setPrefWidth(150);
                elderLabel.setWrapText(true);

                Label detailsLabel = new Label(details.toString());
                detailsLabel.setPrefWidth(400);
                detailsLabel.setWrapText(true);

                Button markDoneBtn = createBigGreenButton("Mark as done");
                markDoneBtn.setOnAction(e -> {
                    appointment.setStatus(Appointment.AppointmentStatus.FINISHED);
                    appointmentDAO.updateAppointment(appointment);
                    detailsLabel.setText(details.toString().replaceFirst("Status: \\w+", "Status: FINISHED"));
                });

                HBox buttonBox = new HBox(markDoneBtn);
                buttonBox.setAlignment(Pos.CENTER_RIGHT);
                buttonBox.setPrefWidth(150);

                table.add(elderLabel, 0, rowIndex);
                table.add(detailsLabel, 1, rowIndex);
                table.add(buttonBox, 2, rowIndex);
                rowIndex++;
            }
        }

        VBox leftPane = new VBox(20, titleLabel, searchSortBox, table);
        leftPane.setPadding(new Insets(20));
        leftPane.setPrefWidth(800);

        Button goBackBtn = createSidebarButton("Go Back");
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

        this.scene = new Scene(root, 1100, 600);
        stage.setTitle("Your Elders");
        stage.setScene(scene);
        stage.show();
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