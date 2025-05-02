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
package view;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class UpdateElderMedicalRecordView {

    private final TableView<MedicalRecord> elderTable = new TableView<>();
    private final TextField nameField = new TextField();
    private final TextField ageField = new TextField();
    private final TextArea medicalRecordArea = new TextArea();

    private final Button updateButton = new Button("Update Medical Record");

    private Scene scene;
    private final Stage stage;

    public UpdateElderMedicalRecordView(Stage stage) {
        this.scene = scene;
        this.stage = stage;
        buildUI();
    }

    private void buildUI() {
        // Main container
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f0f4f7;");

        // Title
        Label title = new Label("Update Medical Record for Elder");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        // Elder selection table with mock data
        setupElderTable();

        // Form section
        VBox formSection = createFormSection();

        // Drag-and-Drop section
        VBox dragDropSection = createDragDropSection();

        // Update button
        styleButton(updateButton, "#4CAF50", "#45a049");
        HBox buttonContainer = new HBox(updateButton);
        buttonContainer.setAlignment(Pos.CENTER);

        // Assemble all parts
        root.getChildren().addAll(title, elderTable, formSection, dragDropSection, buttonContainer);

        // Right styled panel
        VBox rightPane = new VBox();
        rightPane.setPrefWidth(200);
        rightPane.setStyle("-fx-background-color: #3BB49C; -fx-background-radius: 10;");

        // Main HBox
        HBox mainContainer = new HBox();
        mainContainer.getChildren().addAll(root, rightPane);
        mainContainer.setSpacing(10);

        // Scene setup
        scene = new Scene(mainContainer, 900, 700);
        stage.setTitle("Caregiver - Update Elder Medical Record");
        stage.setScene(scene);
        stage.show();

        // Populate with mock data
        populateMockData();
    }

    private void setupElderTable() {
        elderTable.setPrefHeight(200);
        elderTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        elderTable.setPlaceholder(new Label("Select an elder to view details"));

        // Define columns
        TableColumn<MedicalRecord, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cell -> cell.getValue().nameProperty());

        TableColumn<MedicalRecord, String> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(cell -> cell.getValue().ageProperty());

        TableColumn<MedicalRecord, String> recordCol = new TableColumn<>("Medical Record");
        recordCol.setCellValueFactory(cell -> cell.getValue().medicalRecordProperty());

        elderTable.getColumns().addAll(nameCol, ageCol, recordCol);

        // Handle selection to populate form
        elderTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                nameField.setText(newVal.getName());
                ageField.setText(newVal.getAge());
                medicalRecordArea.setText(newVal.getMedicalRecord());
            }
        });
    }

    private VBox createFormSection() {
        Label nameLabel = new Label("Full Name:");
        Label ageLabel = new Label("Age:");
        Label medicalRecordLabel = new Label("Medical Record:");

        styleInputField(nameField);
        styleInputField(ageField);
        styleInputArea(medicalRecordArea);

        VBox nameBox = new VBox(5, nameLabel, nameField);
        VBox ageBox = new VBox(5, ageLabel, ageField);
        VBox recordBox = new VBox(5, medicalRecordLabel, medicalRecordArea);

        VBox formBox = new VBox(10, nameBox, ageBox, recordBox);
        formBox.setPadding(new Insets(15));
        formBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        formBox.setEffect(new DropShadow(5, Color.GRAY));
        return formBox;
    }

    private VBox createDragDropSection() {
        Label dragDropLabel = new Label("Drag and drop a new medical record (PDF) here");
        dragDropLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #777;");
        StackPane dragDropPane = new StackPane();
        dragDropPane.setPrefHeight(150);
        dragDropPane.setStyle("-fx-border-color: #bbb; -fx-border-style: dashed; -fx-border-radius: 8; -fx-background-color: #f0f0f0;");
        dragDropPane.getChildren().add(dragDropLabel);
        StackPane.setAlignment(dragDropLabel, Pos.CENTER);

        VBox container = new VBox(dragDropPane);
        return container;
    }

    private void styleButton(Button btn, String color, String hoverColor) {
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 6;");
        btn.setPrefWidth(220);
        btn.setPrefHeight(40);
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: " + hoverColor + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 6;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 6;"));
    }

    private void styleInputField(TextField tf) {
        tf.setStyle("-fx-background-color: #d9d9d9; -fx-background-radius: 20; -fx-padding: 10 20;");
        tf.setPrefWidth(300);
    }

    private void styleInputArea(TextArea ta) {
        ta.setStyle("-fx-background-color: #d9d9d9; -fx-background-radius: 20; -fx-padding: 10 20;");
        ta.setPrefWidth(300);
    }

    private void populateMockData() {
        elderTable.getItems().addAll(
                new MedicalRecord("John Doe", "70", "Hypertension, Diabetes"),
                new MedicalRecord("Jane Smith", "68", "Arthritis"),
                new MedicalRecord("Albert Johnson", "75", "Heart Disease"),
                new MedicalRecord("Mary Williams", "80", "Alzheimer's")
        );
    }

    // Getters for interacting with UI components
    public TableView<MedicalRecord> getElderTable() { return elderTable; }
    public TextField getNameField() { return nameField; }
    public TextField getAgeField() { return ageField; }
    public TextArea getMedicalRecordArea() { return medicalRecordArea; }
    public Button getUpdateButton() { return updateButton; }

    // Data class
    public static class MedicalRecord {
        private final javafx.beans.property.SimpleStringProperty name;
        private final javafx.beans.property.SimpleStringProperty age;
        private final javafx.beans.property.SimpleStringProperty medicalRecord;

        public MedicalRecord(String name, String age, String medicalRecord) {
            this.name = new javafx.beans.property.SimpleStringProperty(name);
            this.age = new javafx.beans.property.SimpleStringProperty(age);
            this.medicalRecord = new javafx.beans.property.SimpleStringProperty(medicalRecord);
        }

        public javafx.beans.property.StringProperty getNameProperty() { return name; }
        public javafx.beans.property.StringProperty getAgeProperty() { return age; }
        public javafx.beans.property.StringProperty getMedicalRecordProperty() { return medicalRecord; }
        public String getName() { return name.get(); }
        public String getAge() { return age.get(); }
        public String getMedicalRecord() { return medicalRecord.get(); }
        public void setMedicalRecord(String record) { this.medicalRecord.set(record); }

        public ObservableValue<String> nameProperty() {
            return null;
        }

        public ObservableValue<String> ageProperty() {
            return null;
        }

        public ObservableValue<String> medicalRecordProperty() {
            return null;
        }
    }
}