//package view;
//import javafx.application.Application;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.*;
//import javafx.stage.Stage;
//import model.Elder;
//
//public class UpdateElderMedicalRecordView extends Application {
//
//    @Override
//    public void start(Stage primaryStage) {
//        VBox root = new VBox(20);
//        root.setPadding(new Insets(20));
//        root.setAlignment(Pos.TOP_CENTER);
//        root.setStyle("-fx-background-color: #f0f4f7;");
//
//        // Title Section
//        Label title = new Label("Update Medical Record for Elder");
//        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");
//
//        // Elder Selection Table
//        TableView<Elder> elderTable = new TableView<>();
//        elderTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//        elderTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//
//        // Elder Columns
//        TableColumn<Elder, String> nameCol = new TableColumn<>("Name");
//        nameCol.setCellValueFactory(cell -> cell.getValue().nameProperty());
//
//        TableColumn<Elder, String> ageCol = new TableColumn<>("Age");
//        ageCol.setCellValueFactory(cell -> cell.getValue().ageProperty());
//
//        TableColumn<Elder, String> healthCol = new TableColumn<>("Medical Record");
//        healthCol.setCellValueFactory(cell -> cell.getValue().healthConditionProperty());
//
//        elderTable.getColumns().addAll(nameCol, ageCol, healthCol);
//
//        // Form for Medical Record Update
//        GridPane formGrid = new GridPane();
//        formGrid.setVgap(15);
//        formGrid.setHgap(10);
//        formGrid.setPadding(new Insets(10));
//        formGrid.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 8px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 8, 0, 0, 4);");
//
//        Label nameLabel = new Label("Full Name:");
//        TextField nameField = new TextField();
//        nameField.setEditable(false);
//
//        Label ageLabel = new Label("Age:");
//        TextField ageField = new TextField();
//        ageField.setEditable(false);
//
//        Label healthLabel = new Label("Current Medical Record:");
//        TextArea healthField = new TextArea();
//        healthField.setPromptText("Update medical details here...");
//        healthField.setWrapText(true);
//        healthField.setPrefRowCount(4);
//
//        Label dropLabel = new Label("Drag and drop a new medical record (PDF) here");
//        dropLabel.setStyle("-fx-border-color: #ccc; -fx-border-style: dashed; -fx-padding: 20; -fx-background-color: #f8f8f8; -fx-font-style: italic;");
//        dropLabel.setAlignment(Pos.CENTER);
//
//        formGrid.addColumn(0, nameLabel, ageLabel, healthLabel);
//        formGrid.addColumn(1, nameField, ageField, healthField);
//
//        // Update Medical Record Button
//        Button updateBtn = new Button("Update Medical Record");
//        updateBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-border-radius: 5px; -fx-padding: 10 20;");
//        updateBtn.setDisable(true);  // Initially disable the button until an elder is selected
//
//        // Handle selection from table to populate the form
//        elderTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                nameField.setText(newValue.getName());
//                ageField.setText(String.valueOf(newValue.getAge()));
//                healthField.setText(newValue.getHealthCondition());
//                updateBtn.setDisable(false);  // Enable the update button
//            }
//        });
//
//        // Button Action for updating the record
//        updateBtn.setOnAction(event -> {
//            Elder selectedElder = elderTable.getSelectionModel().getSelectedItem();
//            if (selectedElder != null) {
//                // Update the medical record with the new value from the form
//                selectedElder.setHealthCondition(healthField.getText());
//                // In a real scenario, this would also update the database or the backend system
//                System.out.println("Updated Medical Record for: " + selectedElder.getName());
//            }
//        });
//
//        // Add components to the root layout
//        root.getChildren().addAll(title, elderTable, formGrid, dropLabel, updateBtn);
//
//        // Scene Setup
//        Scene scene = new Scene(root, 900, 600);
//        primaryStage.setTitle("Caregiver - Update Medical Record");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
