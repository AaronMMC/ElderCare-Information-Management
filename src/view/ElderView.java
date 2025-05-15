package view;

import controller.ElderController;
import controller.MedicalRecordController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Elder;
import model.Guardian;
import model.MedicalRecord;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class ElderView {

    private final Scene scene;
    private final ElderController elderController;
    private final MedicalRecordController medicalRecordController;
    private final Guardian guardian;

    private static final String NORMAL_STYLE_COMBO_DATEPICKER = "-fx-background-radius: 15; -fx-border-radius: 15; -fx-background-color: #D9D9D9; -fx-border-color: transparent; -fx-padding: 4 8;";
    private static final String ERROR_STYLE_COMBO_DATEPICKER = "-fx-background-radius: 15; -fx-border-radius: 15; -fx-background-color: #D9D9D9; -fx-border-color: red; -fx-border-width: 1.5px; -fx-padding: 4 8;";
    private final Map<String, MedicalRecord.Status> statusDisplayToEnum = new HashMap<>();
    private final Map<MedicalRecord.Status, String> statusEnumToDisplay = new HashMap<>();

    // UI elements
    private TextField firstNameField;
    private TextField lastNameField;
    private DatePicker birthdayPicker;
    private TextField contactField;
    private TextField addressField;
    private TextField emailField;
    private TextField relationshipField;
    private TextField diagnosisField;
    private TextField medicationsField;
    private TextField treatmentPlanField;
    private ComboBox<String> medicationStatusBox;
    private ComboBox<String> treatmentStatusBox;
    private Button cancelButton;
    private Button addButton;

    public ElderView(Stage stage, Connection conn, Guardian guardian) {
        this.elderController = new ElderController(conn);
        this.medicalRecordController = new MedicalRecordController(conn);
        this.guardian = guardian;

        statusDisplayToEnum.put("Ongoing", MedicalRecord.Status.ONGOING);
        statusDisplayToEnum.put("Completed", MedicalRecord.Status.COMPLETED);
        statusDisplayToEnum.put("Pending", MedicalRecord.Status.PENDING);
        statusDisplayToEnum.put("Not Applicable", MedicalRecord.Status.NOT_APPLICABLE);

        statusEnumToDisplay.put(MedicalRecord.Status.ONGOING, "Ongoing");
        statusEnumToDisplay.put(MedicalRecord.Status.COMPLETED, "Completed");
        statusEnumToDisplay.put(MedicalRecord.Status.PENDING, "Pending");
        statusEnumToDisplay.put(MedicalRecord.Status.NOT_APPLICABLE, "Not Applicable");

        if (guardian != null) {
            System.out.println("ElderView received Guardian ID: " + guardian.getGuardianID());
        } else {
            System.err.println("ElderView received a NULL Guardian object!");
            showAlert(Alert.AlertType.ERROR, "Initialization Error", "Guardian data not available. Cannot add elder.");
        }

        Label title = new Label("Add an Elder");
        title.setFont(new Font("Arial", 24));
        title.setStyle("-fx-font-weight: bold;");

        // === Elder Form ===
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10); // Reduced horizontal gap
        formGrid.setVgap(10); // Reduced vertical gap
        formGrid.setAlignment(Pos.CENTER_LEFT);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(50);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(50);
        formGrid.getColumnConstraints().addAll(column1, column2);

        firstNameField = createRoundedField("First Name");
        lastNameField = createRoundedField("Last Name");
        birthdayPicker = createRoundedDatePicker("Birthday");
        contactField = createRoundedField("Contact Number");
        addressField = createRoundedField("Address");
        emailField = createRoundedField("Email");
        relationshipField = createRoundedField("Relationship");

        birthdayPicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(LocalDate.now()));
            }
        });

        formGrid.add(new Label("First Name:"), 0, 0);
        formGrid.add(firstNameField, 0, 1);
        formGrid.add(new Label("Last Name:"), 1, 0);
        formGrid.add(lastNameField, 1, 1);
        formGrid.add(new Label("Birthday:"), 0, 2);
        formGrid.add(birthdayPicker, 0, 3);
        formGrid.add(new Label("Contact Number:"), 1, 2);
        formGrid.add(contactField, 1, 3);
        formGrid.add(new Label("Address:"), 0, 4);
        formGrid.add(addressField, 0, 5, 2, 1); // Span two columns
        formGrid.add(new Label("Email:"), 0, 6);
        formGrid.add(emailField, 0, 7);
        formGrid.add(new Label("Relationship:"), 1, 6);
        formGrid.add(relationshipField, 1, 7);

        // == Medical Record Grid ==
        GridPane medicalRecordGrid = new GridPane();
        medicalRecordGrid.setHgap(10); // Reduced horizontal gap
        medicalRecordGrid.setVgap(10); // Reduced vertical gap
        medicalRecordGrid.setAlignment(Pos.CENTER_RIGHT);

        ColumnConstraints medCol1 = new ColumnConstraints();
        medCol1.setPercentWidth(35);
        ColumnConstraints medCol2 = new ColumnConstraints();
        medCol2.setPercentWidth(65);
        medicalRecordGrid.getColumnConstraints().addAll(medCol1, medCol2);

        diagnosisField = createRoundedField("Diagnosis");
        medicationsField = createRoundedField("Medications");
        treatmentPlanField = createRoundedField("Treatment Plan");
        medicationStatusBox = createRoundedComboBox("Medication Status");
        addComboBoxValidationListener(medicationStatusBox);
        treatmentStatusBox = createRoundedComboBox("Treatment Status");
        addComboBoxValidationListener(treatmentStatusBox);

        medicalRecordGrid.add(new Label("Diagnosis:"), 0, 0);
        medicalRecordGrid.add(diagnosisField, 1, 0);
        medicalRecordGrid.add(new Label("Medications:"), 0, 1);
        medicalRecordGrid.add(medicationsField, 1, 1);
        medicalRecordGrid.add(new Label("Treatment Plan:"), 0, 2);
        medicalRecordGrid.add(treatmentPlanField, 1, 2);
        medicalRecordGrid.add(new Label("Medication Status:"), 0, 3);
        medicalRecordGrid.add(medicationStatusBox, 1, 3);
        medicalRecordGrid.add(new Label("Treatment Status:"), 0, 4);
        medicalRecordGrid.add(treatmentStatusBox, 1, 4);

        medicationStatusBox.getItems().addAll(statusDisplayToEnum.keySet());
        treatmentStatusBox.getItems().addAll(statusDisplayToEnum.keySet());

        // === Left Buttons ===
        cancelButton = createMainButton("Cancel");
        addButton = createMainButton("Add");
        addButton.setDisable(true); // Start with Add button disabled
        cancelButton.setDisable(true);

        HBox buttonBox = new HBox(20, cancelButton, addButton);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        VBox leftPane = new VBox(20, title, formGrid, medicalRecordGrid, buttonBox);
        leftPane.setPadding(new Insets(20)); // Reduced padding
        HBox.setHgrow(leftPane, Priority.ALWAYS); // Allow left pane to grow horizontally

        // === Right Sidebar ===
        Button goBackBtn = createSidebarButton("Go Back");
        goBackBtn.setOnAction(e -> {
            GuardianElderView guardianElderView = new GuardianElderView(stage, conn, guardian, elderController);
            stage.setScene(guardianElderView.getScene());
        });

        VBox rightPane = new VBox();
        rightPane.setPadding(new Insets(20)); // Reduced padding
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setAlignment(Pos.BOTTOM_CENTER);
        rightPane.setMinWidth(150); // Set a minimum width for the sidebar
        rightPane.setMaxWidth(200); // Set a maximum width for the sidebar

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        rightPane.getChildren().addAll(spacer, goBackBtn);

        HBox root = new HBox(20, leftPane, rightPane);
        root.setPadding(new Insets(10)); // Reduced root padding
        HBox.setHgrow(leftPane, Priority.ALWAYS);
        HBox.setHgrow(rightPane, Priority.NEVER);

        this.scene = new Scene(root);
        stage.setTitle("Add Elder");
        stage.setScene(scene);
        stage.show();

        // Add listeners after the scene is set
        addTextFieldListeners();
        // Make the scene resizable and call method.
        stage.setResizable(true);
        makeLayoutResponsive(root);
    }

    private void makeLayoutResponsive(HBox root) {
        scene.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double width = newWidth.doubleValue();
            // Adjust the widths of the left and right panes
            VBox leftPane = (VBox) root.getChildren().get(0);
            VBox rightPane = (VBox) root.getChildren().get(1);

            if (width < 800) { // Example breakpoint
                leftPane.setPrefWidth(width * 0.9);
                rightPane.setPrefWidth(width * 0.9);
            } else {
                leftPane.setPrefWidth(width * 0.7); // 70% of the width
                rightPane.setPrefWidth(250); // Fixed width
            }
        });
    }

    private void addTextFieldListeners() {
        // Listener for all text fields.
        firstNameField.textProperty().addListener((obs, oldVal, newVal) -> updateButtonState());
        lastNameField.textProperty().addListener((obs, oldVal, newVal) -> updateButtonState());
        contactField.textProperty().addListener((obs, oldVal, newVal) -> updateButtonState());
        addressField.textProperty().addListener((obs, oldVal, newVal) -> updateButtonState());
        emailField.textProperty().addListener((obs, oldVal, newVal) -> updateButtonState());
        relationshipField.textProperty().addListener((obs, oldVal, newVal) -> updateButtonState());
        diagnosisField.textProperty().addListener((obs, oldVal, newVal) -> updateButtonState());
        medicationsField.textProperty().addListener((obs, oldVal, newVal) -> updateButtonState());
        treatmentPlanField.textProperty().addListener((obs, oldVal, newVal) -> updateButtonState());
        birthdayPicker.valueProperty().addListener((obs, oldVal, newVal) -> updateButtonState());
        medicationStatusBox.valueProperty().addListener((obs, oldVal, newVal) -> updateButtonState());
        treatmentStatusBox.valueProperty().addListener((obs, oldVal, newVal) -> updateButtonState());
    }
    private void updateButtonState() {
        boolean allFieldsFilled = !firstNameField.getText().trim().isEmpty() &&
                !lastNameField.getText().trim().isEmpty() &&
                birthdayPicker.getValue() != null &&
                !contactField.getText().trim().isEmpty() &&
                !addressField.getText().trim().isEmpty() &&
                !emailField.getText().trim().isEmpty() &&
                !relationshipField.getText().trim().isEmpty() &&
                !diagnosisField.getText().trim().isEmpty() &&
                !medicationsField.getText().trim().isEmpty() &&
                !treatmentPlanField.getText().trim().isEmpty() &&
                medicationStatusBox.getValue() != null &&
                treatmentStatusBox.getValue() != null;

        boolean anyFieldFilled = !firstNameField.getText().trim().isEmpty() ||
                !lastNameField.getText().trim().isEmpty() ||
                birthdayPicker.getValue() != null ||
                !contactField.getText().trim().isEmpty() ||
                !addressField.getText().trim().isEmpty() ||
                !emailField.getText().trim().isEmpty() ||
                !relationshipField.getText().trim().isEmpty() ||
                !diagnosisField.getText().trim().isEmpty() ||
                !medicationsField.getText().trim().isEmpty() ||
                !treatmentPlanField.getText().trim().isEmpty() ||
                medicationStatusBox.getValue() != null ||
                treatmentStatusBox.getValue() != null;

        addButton.setDisable(!allFieldsFilled);
        cancelButton.setDisable(!anyFieldFilled);
    }

    private void addComboBoxValidationListener(ComboBox<String> comboBox) {
        comboBox.focusedProperty().addListener((obs, oldValFocus, newValFocus) -> {
            if (!newValFocus) {
                styleComboBox(comboBox, comboBox.getValue() == null || comboBox.getValue().trim().isEmpty());
            }
        });
        comboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            styleComboBox(comboBox, newVal == null || newVal.trim().isEmpty());
            updateButtonState(); // also update button state when combobox value changes.
        });
    }

    private TextField createRoundedField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle("-fx-background-color: lightgray; -fx-background-radius: 20; -fx-padding: 8 16;");
        HBox.setHgrow(field, Priority.ALWAYS); // Allow text fields to grow horizontally
        return field;
    }

    private ComboBox<String> createRoundedComboBox(String prompt) {
        ComboBox<String> cb = new ComboBox<>();
        cb.setPromptText(prompt);
        styleComboBox(cb, false);
        cb.setMinWidth(120);
        cb.setPrefWidth(ComboBox.USE_COMPUTED_SIZE);
        HBox.setHgrow(cb, Priority.ALWAYS); // Allow combo boxes to grow horizontally
        return cb;
    }

    private void styleComboBox(ComboBox<String> comboBox, boolean isError) {
        comboBox.setStyle(isError ? ERROR_STYLE_COMBO_DATEPICKER : NORMAL_STYLE_COMBO_DATEPICKER);
    }

    private DatePicker createRoundedDatePicker(String prompt) {
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText(prompt);
        datePicker.setStyle("-fx-background-color: lightgray; -fx-background-radius: 20; -fx-padding: 8 16;");
        HBox.setHgrow(datePicker, Priority.ALWAYS); // Allow date picker to grow horizontally
        return datePicker;
    }

    private Button createMainButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #3BB49C; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20;");
        button.setPrefWidth(120);
        return button;
    }

    private Button createSidebarButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 14px; -fx-background-radius: 20;");
        button.setPrefWidth(120);
        return button;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Scene getScene() {
        return scene;
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        birthdayPicker.setValue(null);
        contactField.clear();
        addressField.clear();
        emailField.clear();
        relationshipField.clear();
        diagnosisField.clear();
        medicationsField.clear();
        treatmentPlanField.clear();
        medicationStatusBox.setValue(null);
        treatmentStatusBox.setValue(null);
    }

    private void addElderAndMedicalRecord() {
        StringBuilder errors = new StringBuilder("Please correct the following issues:\n");

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        LocalDate birthdayDate = birthdayPicker.getValue();
        String contactNumber = contactField.getText();
        String address = addressField.getText();
        String email = emailField.getText();
        String relationship = relationshipField.getText();

        String diagnosis = diagnosisField.getText();
        String medications = medicationsField.getText();
        String treatmentPlan = treatmentPlanField.getText();

        String selectedMedicationStatus = medicationStatusBox.getValue();
        MedicalRecord.Status medicationStatusValue = null;
        if (selectedMedicationStatus == null || selectedMedicationStatus.trim().isEmpty()) {
            errors.append("- Medication status is required.\n");
            styleComboBox(medicationStatusBox, true); //
        } else {
            medicationStatusValue = statusDisplayToEnum.get(selectedMedicationStatus);
            if (medicationStatusValue == null) {
                errors.append("- Medication status is unknown.\n");
                styleComboBox(medicationStatusBox, true);
            } else {
                styleComboBox(medicationStatusBox, false);
            }
        }

        String selectedTreatmentStatus = treatmentStatusBox.getValue();
        MedicalRecord.Status treatmentStatusValue = null;
        if (selectedTreatmentStatus == null || selectedTreatmentStatus.trim().isEmpty()) {
            errors.append("- Treatment status is required.\n");
            styleComboBox(treatmentStatusBox, true);
        } else {
            treatmentStatusValue = statusDisplayToEnum.get(selectedTreatmentStatus);
            if (treatmentStatusValue == null) {
                errors.append("- Treatment status is unknown.\n");
                styleComboBox(treatmentStatusBox, true);
            } else {
                styleComboBox(treatmentStatusBox, false);
            }
        }


        if (firstName.isEmpty() || lastName.isEmpty() || birthdayDate == null ||
                contactNumber.isEmpty() || address.isEmpty() || email.isEmpty() || relationship.isEmpty() ||
                diagnosis.isEmpty() || medications.isEmpty() || treatmentPlan.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required.");
            return;
        }

        if (birthdayDate.isAfter(LocalDate.now())) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Birthday cannot be in the future.");
            return;
        }

        if (errors.length() > "Please correct the following issues:\n".length()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", errors.toString());
            return;
        }


        LocalDateTime birthdayDateTime = birthdayDate.atTime(LocalTime.MIDNIGHT);
        LocalDateTime currentDate = LocalDateTime.now();

        try {
            Elder newElder = new Elder(firstName, lastName, birthdayDateTime, contactNumber, email, address, guardian.getGuardianID(), relationship);
            elderController.addElder(newElder);

            MedicalRecord newMedicalRecord = new MedicalRecord(diagnosis, medications, treatmentPlan, medicationStatusValue, treatmentStatusValue, currentDate, newElder.getElderID());
            medicalRecordController.addMedicalRecord(newMedicalRecord);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Elder and Medical Record added successfully.");
            clearFields();
            cancelButton.setDisable(true);
            addButton.setDisable(true);

        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add elder and medical record: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}