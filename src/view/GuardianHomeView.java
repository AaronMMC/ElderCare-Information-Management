//package view;
//
//
//import javafx.application.Application;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.layout.*;
//import javafx.stage.Stage;
//
//public class GuardianHomeView extends Application {
//
//    @Override
//    public void start(Stage primaryStage) {
//        VBox root = new VBox(20);
//        root.setPadding(new Insets(20));
//        root.setAlignment(Pos.TOP_CENTER);
//
//        Label title = new Label("Guardian Home");
//        title.getStyleClass().add("title");
//
//        // ---------------------- Appointment Form ---------------------- //
//        GridPane form = new GridPane();
//        form.setVgap(10);
//        form.setHgap(10);
//        form.setAlignment(Pos.CENTER_LEFT);
//
//        ComboBox<String> caregiverBox = new ComboBox<>();
//        caregiverBox.setPromptText("Select Caregiver");
//
//        ComboBox<String> serviceBox = new ComboBox<>();
//        serviceBox.setPromptText("Select Service");
//
//        DatePicker appointmentDate = new DatePicker();
//
//        Label totalPaymentLabel = new Label("Total Payment: PHP 0");
//
//        Button submitBtn = new Button("Submit Appointment");
//        submitBtn.getStyleClass().add("btn-primary");
//
//        form.add(new Label("Caregiver:"), 0, 0);
//        form.add(caregiverBox, 1, 0);
//        form.add(new Label("Service:"), 0, 1);
//        form.add(serviceBox, 1, 1);
//        form.add(new Label("Date:"), 0, 2);
//        form.add(appointmentDate, 1, 2);
//        form.add(totalPaymentLabel, 0, 3, 2, 1);
//        form.add(submitBtn, 0, 4, 2, 1);
//
//        // ---------------------- Elders Table ---------------------- //
//        TableView<Elder> elderTable = new TableView<>();
//        elderTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//
//        TableColumn<Elder, String> nameCol = new TableColumn<>("Elder Name");
//        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
//
//        TableColumn<Elder, Void> actionCol = new TableColumn<>("Actions");
//        actionCol.setCellFactory(param -> new TableCell<>() {
//            private final Button editBtn = new Button("Edit");
//            private final Button deleteBtn = new Button("Delete");
//            {
//                editBtn.getStyleClass().add("btn-action");
//                deleteBtn.getStyleClass().add("btn-danger");
//
//                editBtn.setOnAction(e -> {
//                    Elder elder = getTableView().getItems().get(getIndex());
//                    // TODO: Redirect to edit screen
//                    System.out.println("Editing: " + elder.getName());
//                });
//
//                deleteBtn.setOnAction(e -> {
//                    Elder elder = getTableView().getItems().get(getIndex());
//                    getTableView().getItems().remove(elder);
//                    // TODO: Also delete from database if needed
//                    System.out.println("Deleted: " + elder.getName());
//                });
//            }
//
//            private final HBox pane = new HBox(5, editBtn, deleteBtn);
//            {
//                pane.setAlignment(Pos.CENTER);
//            }
//
//            @Override
//            protected void updateItem(Void item, boolean empty) {
//                super.updateItem(item, empty);
//                setGraphic(empty ? null : pane);
//            }
//        });
//
//        elderTable.getColumns().addAll(nameCol, actionCol);
//        elderTable.getItems().addAll(new Elder("Juan Dela Cruz"), new Elder("Maria Santos"));
//
//        elderTable.setPrefHeight(200);
//        elderTable.setPlaceholder(new Label("No elders to display."));
//
//        Button addElderBtn = new Button("Add Elder");
//        addElderBtn.getStyleClass().add("btn-secondary");
//
//        // ---------------------- View Appointments ---------------------- //
//        Button viewAppointmentsBtn = new Button("View All Appointments");
//        viewAppointmentsBtn.getStyleClass().add("btn-secondary");
//        viewAppointmentsBtn.setOnAction(e -> {
//            // TODO: Replace with actual scene navigation
//            System.out.println("Redirecting to Appointments View...");
//        });
//
//        root.getChildren().addAll(
//                title,
//                form,
//                new Separator(),
//                new Label("Elders:"),
//                elderTable,
//                new HBox(10, addElderBtn, viewAppointmentsBtn)
//        );
//
//        Scene scene = new Scene(root, 850, 650);
//        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
//
//        primaryStage.setTitle("Guardian Dashboard");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    // Dummy Elder class
//    public static class Elder {
//        private final String name;
//        public Elder(String name) { this.name = name; }
//        public String getName() { return name; }
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
//
