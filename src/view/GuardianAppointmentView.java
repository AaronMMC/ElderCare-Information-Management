package view;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GuardianAppointmentView extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("All Appointments");
        title.getStyleClass().add("title");

        TableView<Appointment> appointmentTable = new TableView<>();
        appointmentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        appointmentTable.setPrefHeight(300);

        TableColumn<Appointment, String> caregiverCol = new TableColumn<>("Caregiver Name");
        caregiverCol.setCellValueFactory(new PropertyValueFactory<>("caregiverName"));

        TableColumn<Appointment, String> dateTimeCol = new TableColumn<>("Appointment Date & Time");
        dateTimeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentDateTime"));

        TableColumn<Appointment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Appointment, String> balanceCol = new TableColumn<>("Balance");
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));

        TableColumn<Appointment, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button payBtn = new Button("Pay Balance");

            {
                payBtn.getStyleClass().add("btn-success");
                payBtn.setOnAction(e -> {
                    Appointment a = getTableView().getItems().get(getIndex());
                    System.out.println("Paying balance for: " + a.getCaregiverName());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : payBtn);
            }
        });

        appointmentTable.getColumns().addAll(caregiverCol, dateTimeCol, statusCol, balanceCol, actionCol);

        // Sample data
        appointmentTable.getItems().addAll(
                new Appointment("John Doe", "2025-04-10 10:00 AM", "Pending Payment", "PHP 1500"),
                new Appointment("Jane Smith", "2025-04-12 2:00 PM", "Paid", "PHP 0"),
                new Appointment("Emily Davis", "2025-04-15 9:00 AM", "Pending Payment", "PHP 1700")
        );

        Button backBtn = new Button("Back to Dashboard");
        backBtn.getStyleClass().add("btn-primary");
        backBtn.setOnAction(e -> System.out.println("Returning to dashboard..."));

        root.getChildren().addAll(title, appointmentTable, backBtn);

        Scene scene = new Scene(root, 800, 500);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setTitle("Guardian Appointments View");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static class Appointment {
        private final String caregiverName;
        private final String appointmentDateTime;
        private final String status;
        private final String balance;

        public Appointment(String caregiverName, String appointmentDateTime, String status, String balance) {
            this.caregiverName = caregiverName;
            this.appointmentDateTime = appointmentDateTime;
            this.status = status;
            this.balance = balance;
        }

        public String getCaregiverName() {
            return caregiverName;
        }

        public String getAppointmentDateTime() {
            return appointmentDateTime;
        }

        public String getStatus() {
            return status;
        }

        public String getBalance() {
            return balance;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
