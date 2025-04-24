package view;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class CaregiverAppointmentView extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Caregiver Home - Appointments");
        title.getStyleClass().add("title");

        TableView<AppointmentEntry> appointmentTable = new TableView<>();
        appointmentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<AppointmentEntry, Boolean> selectCol = new TableColumn<>("Select");
        selectCol.setCellFactory(tc -> new CheckBoxTableCell<>());

        TableColumn<AppointmentEntry, String> guardianCol = new TableColumn<>("Guardian Name");
        guardianCol.setCellValueFactory(new PropertyValueFactory<>("guardianName"));

        TableColumn<AppointmentEntry, Integer> elderCountCol = new TableColumn<>("Elders Count");
        elderCountCol.setCellValueFactory(new PropertyValueFactory<>("eldersCount"));

        TableColumn<AppointmentEntry, String> dateTimeCol = new TableColumn<>("Appointment Date & Time");
        dateTimeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentTime"));

        appointmentTable.getColumns().addAll(selectCol, guardianCol, elderCountCol, dateTimeCol);

        appointmentTable.getItems().addAll(
                new AppointmentEntry("Alice Johnson", 2, "2025-04-10 10:00 AM"),
                new AppointmentEntry("Bob Smith", 1, "2025-04-12 09:20 AM"),
                new AppointmentEntry("Catherine Lee", 3, "2025-04-15 08:00 AM")
        );

        appointmentTable.setPrefHeight(300);
        appointmentTable.setPlaceholder(new Label("No upcoming appointments."));

        // Action Buttons
        Button acceptBtn = new Button("Accept Appointment");
        Button closeBtn = new Button("Close Appointment");
        Button declineBtn = new Button("Decline Appointment");

        acceptBtn.getStyleClass().add("btn-primary");
        closeBtn.getStyleClass().add("btn-secondary");
        declineBtn.getStyleClass().add("btn-danger");

        acceptBtn.setOnAction(e -> System.out.println("Accepted selected appointments"));
        closeBtn.setOnAction(e -> System.out.println("Closed selected appointments"));
        declineBtn.setOnAction(e -> System.out.println("Declined selected appointments"));

        HBox actionBox = new HBox(10, acceptBtn, closeBtn, declineBtn);
        actionBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(title, appointmentTable, actionBox);

        Scene scene = new Scene(root, 850, 500);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setTitle("Caregiver Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static class AppointmentEntry {
        private final String guardianName;
        private final int eldersCount;
        private final String appointmentTime;

        public AppointmentEntry(String guardianName, int eldersCount, String appointmentTime) {
            this.guardianName = guardianName;
            this.eldersCount = eldersCount;
            this.appointmentTime = appointmentTime;
        }

        public String getGuardianName() { return guardianName; }
        public int getEldersCount() { return eldersCount; }
        public String getAppointmentTime() { return appointmentTime; }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
