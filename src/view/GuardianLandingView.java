//package view;
//
//import javafx.application.Application;
//import javafx.geometry.Insets;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.layout.*;
//import javafx.stage.Stage;
//
//public class GuardianLandingView extends Application {
//
//
//    public void start(Stage primaryStage) {
//
//        ComboBox<String> caregiverComboBox = new javafx.scene.control.ComboBox<String>();
//        caregiverComboBox.setPromptText("Select Caregiver");
//
//
//        ComboBox<String> serviceComboBox = new ComboBox<>();
//        serviceComboBox.setPromptText("Select Service");
//
//
//        DatePicker datePicker = new DatePicker();
//
//
//        Label totalPaymentLabel = new Label("Total Payment: PHP 0");
//
//
//        Button submitButton = new Button("Submit Appointment");
//        submitButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
//
//        // Form Layout
//        VBox formLayout = new VBox(10,
//                new Label("Caregiver:"), caregiverComboBox,
//                new Label("Service:"), serviceComboBox,
//                new Label("Date:"), datePicker,
//                totalPaymentLabel,
//                submitButton
//        );
//        formLayout.setPadding(new Insets(10));
//        formLayout.setMaxWidth(300);
//
//
//        TableView<Elder> elderTable = new TableView<>();
//
//        TableColumn<Elder, String> nameColumn = new TableColumn<>("Elder Name");
//        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
//        nameColumn.setMinWidth(200);
//
//        TableColumn<Elder, Void> actionColumn = new TableColumn<>("Actions");
//        actionColumn.setCellFactory(param -> new TableCell<>() {
//            private final Button editBtn = new Button("Edit");
//            private final Button deleteBtn = new Button("Delete");
//            private final HBox hbox = new HBox(10, editBtn, deleteBtn);
//
//            {
//                editBtn.setStyle("-fx-background-color: #FFC107;");
//                deleteBtn.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");
//            }
//
//            @Override
//            protected void updateItem(Void item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty) {
//                    setGraphic(null);
//                } else {
//                    setGraphic(hbox);
//                }
//            }
//        });
//
//        elderTable.getColumns().addAll(nameColumn, actionColumn);
//        elderTable.getItems().addAll(
//                new Elder("Juan Dela Cruz"),
//                new Elder("Maria Santos")
//        );
//
//
//        Button addElderBtn = new Button("Add Elder");
//        Button viewAppointmentsBtn = new Button("View All Appointments");
//        addElderBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
//        viewAppointmentsBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
//
//        HBox bottomButtons = new HBox(10, addElderBtn, viewAppointmentsBtn);
//        bottomButtons.setPadding(new Insets(10));
//
//        VBox rightLayout = new VBox(10,
//                new Label("Elders:"),
//                elderTable,
//                bottomButtons
//        );
//        rightLayout.setPadding(new Insets(10));
//
//
//        HBox root = new HBox(20, formLayout, new Separator(), rightLayout);
//        root.setPadding(new Insets(20));
//
//        Scene scene = new Scene(root, 800, 400);
//        primaryStage.setTitle("Guardian Dashboard");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//
//    public static class Elder {
//        private final String name;
//
//        public Elder(String name) {
//            this.name = name;
//        }
//
//        public String getName() {
//            return name;
//        }
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}