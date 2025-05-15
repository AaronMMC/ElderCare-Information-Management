package view;

import controller.MedicalRecordController;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Caregiver;
import model.Elder;
import model.MedicalRecord;

import java.sql.Connection;
import java.time.format.DateTimeFormatter;

public class MedicalRecordView {

    private Scene scene;
    private final Connection conn;
    private final MedicalRecordController medicalRecordController;
    private MedicalRecord currentMedicalRecord;
    private TextArea diagnosisTextArea;
    private TextArea medicationTextArea;
    private TextArea treatmentTextArea;
    private ComboBox<MedicalRecord.Status> medicationStatusComboBox;
    private ComboBox<MedicalRecord.Status> treatmentStatusComboBox;
    private Label lastModifiedLabel;

    public MedicalRecordView(Stage stage, Connection conn, Elder elder, Caregiver caregiver) {
        this.conn = conn;
        this.medicalRecordController = new MedicalRecordController(conn);
        this.currentMedicalRecord = medicalRecordController.getMedicalRecordByElderId(elder.getElderID());

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f4f4f4;");

        // Top Section - Medical Record Details
        GridPane detailsPane = new GridPane();
        detailsPane.setAlignment(Pos.TOP_LEFT);
        detailsPane.setPadding(new Insets(20));
        detailsPane.setHgap(10);
        detailsPane.setVgap(8); // Slightly reduce vertical gap
        detailsPane.setStyle("-fx-border-color: #d3d3d3; -fx-border-radius: 5; -fx-background-color: white;");

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setMinWidth(120); // Give more width to labels
        col1.setPrefWidth(150);
        col1.setHgrow(Priority.SOMETIMES);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);

        detailsPane.getColumnConstraints().addAll(col1, col2);

        Label medicalRecordDetailsLabel = new Label("Medical Record Details:");
        medicalRecordDetailsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        GridPane.setColumnSpan(medicalRecordDetailsLabel, 2);
        detailsPane.add(medicalRecordDetailsLabel, 0, 0);

        Label diagnosisLabel = new Label("Diagnosis:");
        diagnosisTextArea = new TextArea(currentMedicalRecord != null ? currentMedicalRecord.getDiagnosis() : "");
        GridPane.setHgrow(diagnosisTextArea, Priority.ALWAYS);
        diagnosisTextArea.setMaxHeight(100); // Limit vertical growth
        detailsPane.add(diagnosisLabel, 0, 1);
        detailsPane.add(diagnosisTextArea, 1, 1);

        Label medicationLabel = new Label("Medication:");
        medicationTextArea = new TextArea(currentMedicalRecord != null ? currentMedicalRecord.getMedications() : "");
        GridPane.setHgrow(medicationTextArea, Priority.ALWAYS);
        medicationTextArea.setMaxHeight(100); // Limit vertical growth
        detailsPane.add(medicationLabel, 0, 2);
        detailsPane.add(medicationTextArea, 1, 2);

        Label treatmentLabel = new Label("Treatment Plan:");
        treatmentTextArea = new TextArea(currentMedicalRecord != null ? currentMedicalRecord.getTreatmentPlan() : "");
        GridPane.setHgrow(treatmentTextArea, Priority.ALWAYS);
        treatmentTextArea.setMaxHeight(100); // Limit vertical growth
        detailsPane.add(treatmentLabel, 0, 3);
        detailsPane.add(treatmentTextArea, 1, 3);

        Label medicationStatusLabel = new Label("Medication Status:");
        medicationStatusComboBox = new ComboBox<>(FXCollections.observableArrayList(MedicalRecord.Status.values()));
        medicationStatusComboBox.setValue(currentMedicalRecord != null ? currentMedicalRecord.getMedicationStatus() : MedicalRecord.Status.NOT_APPLICABLE);
        detailsPane.add(medicationStatusLabel, 0, 4);
        detailsPane.add(medicationStatusComboBox, 1, 4);

        Label treatmentStatusLabel = new Label("Treatment Status:");
        treatmentStatusComboBox = new ComboBox<>(FXCollections.observableArrayList(MedicalRecord.Status.values()));
        treatmentStatusComboBox.setValue(currentMedicalRecord != null ? currentMedicalRecord.getTreatmentStatus() : MedicalRecord.Status.NOT_APPLICABLE);
        detailsPane.add(treatmentStatusLabel, 0, 5);
        detailsPane.add(treatmentStatusComboBox, 1, 5);

        root.setTop(detailsPane);

        // Center Section - Last Modified
        HBox lastModifiedBox = new HBox(10);
        lastModifiedBox.setAlignment(Pos.CENTER_LEFT);
        lastModifiedBox.setPadding(new Insets(10, 20, 10, 20)); // Reduce top and bottom padding
        Label lastModifiedTitleLabel = new Label("Last Modified:");
        lastModifiedTitleLabel.setStyle("-fx-font-weight: bold;");
        lastModifiedLabel = new Label(currentMedicalRecord != null && currentMedicalRecord.getLastModified() != null ?
                currentMedicalRecord.getLastModified().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A");
        HBox.setHgrow(lastModifiedLabel, Priority.SOMETIMES);
        lastModifiedBox.getChildren().addAll(lastModifiedTitleLabel, lastModifiedLabel);
        root.setCenter(lastModifiedBox);

        // Bottom Section - Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT); // Align buttons to the right
        buttonBox.setPadding(new Insets(10, 20, 20, 20)); // Add some top padding
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS); // Push buttons to the right

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: #a9a9a9; -fx-text-fill: white; -fx-padding: 8px 15px; -fx-background-radius: 5;");
        cancelButton.setOnAction(e -> {
            if (currentMedicalRecord != null) {
                diagnosisTextArea.setText(currentMedicalRecord.getDiagnosis());
                medicationTextArea.setText(currentMedicalRecord.getMedications());
                treatmentTextArea.setText(currentMedicalRecord.getTreatmentPlan());
                medicationStatusComboBox.setValue(currentMedicalRecord.getMedicationStatus());
                treatmentStatusComboBox.setValue(currentMedicalRecord.getTreatmentStatus());
                lastModifiedLabel.setText(currentMedicalRecord.getLastModified() != null ?
                        currentMedicalRecord.getLastModified().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A");
            } else {
                diagnosisTextArea.clear();
                medicationTextArea.clear();
                treatmentTextArea.clear();
                medicationStatusComboBox.setValue(MedicalRecord.Status.NOT_APPLICABLE);
                treatmentStatusComboBox.setValue(MedicalRecord.Status.NOT_APPLICABLE);
                lastModifiedLabel.setText("N/A");
            }
        });

        Button saveButton = new Button("Save Changes");
        saveButton.setStyle("-fx-background-color: #20b2aa; -fx-text-fill: white; -fx-padding: 8px 15px; -fx-background-radius: 5;");
        saveButton.setOnAction(e -> {
            if (currentMedicalRecord != null) {
                currentMedicalRecord.setDiagnosis(diagnosisTextArea.getText());
                currentMedicalRecord.setMedications(medicationTextArea.getText());
                currentMedicalRecord.setTreatmentPlan(treatmentTextArea.getText());
                currentMedicalRecord.setMedicationStatus(medicationStatusComboBox.getValue());
                currentMedicalRecord.setTreatmentStatus(treatmentStatusComboBox.getValue());
                medicalRecordController.updateMedicalRecord(currentMedicalRecord);
                lastModifiedLabel.setText(currentMedicalRecord.getLastModified().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            } else {
                MedicalRecord updatedMedicalRecord = new MedicalRecord();
                updatedMedicalRecord.setElderID(elder.getElderID());
                updatedMedicalRecord.setDiagnosis(diagnosisTextArea.getText());
                updatedMedicalRecord.setMedications(medicationTextArea.getText());
                updatedMedicalRecord.setTreatmentPlan(treatmentTextArea.getText());
                updatedMedicalRecord.setMedicationStatus(medicationStatusComboBox.getValue());
                updatedMedicalRecord.setTreatmentStatus(treatmentStatusComboBox.getValue());
                medicalRecordController.updateMedicalRecord(updatedMedicalRecord);
                currentMedicalRecord = medicalRecordController.getMedicalRecordByElderId(elder.getElderID());
                if (currentMedicalRecord != null && currentMedicalRecord.getLastModified() != null) {
                    lastModifiedLabel.setText(currentMedicalRecord.getLastModified().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                }
            }
        });

        buttonBox.getChildren().addAll(spacer, cancelButton, saveButton); // Add spacer
        root.setBottom(buttonBox);

        // Right Section - Go Back Button
        VBox rightPane = new VBox();
        rightPane.setAlignment(Pos.TOP_RIGHT); // Align to top right
        rightPane.setPadding(new Insets(20));
        rightPane.setStyle("-fx-background-color: #e0f2f7;");
        VBox.setVgrow(rightPane, Priority.ALWAYS); // Allow right pane to grow vertically

        Button goBackButton = new Button("Go Back");
        goBackButton.setStyle("-fx-background-color: white; -fx-text-fill: #20b2aa; -fx-padding: 8px 15px; -fx-background-radius: 5; -fx-border-color: #20b2aa; -fx-border-radius: 5;");
        goBackButton.setOnAction(e -> {
            CaregiverElderView caregiverElderView = new CaregiverElderView(stage, conn, caregiver);
            stage.setScene(caregiverElderView.getScene());
        });
        rightPane.getChildren().add(goBackButton);
        BorderPane.setAlignment(rightPane, Pos.TOP_RIGHT); // Align the right pane itself
        root.setRight(rightPane);

        this.scene = new Scene(root);
        stage.setTitle("Medical Record");
        stage.setScene(scene);
    }

    public Scene getScene() {
        return scene;
    }
}