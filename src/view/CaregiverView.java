package view;

import controller.CaregiverController;
import controller.LoginController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
// import javafx.scene.paint.Color; // Not directly used in this version
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Caregiver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.HashMap;


public class CaregiverView {

    private final CaregiverController caregiverController;
    private final Scene scene;
    private final ObservableList<String> certificationFileNames = FXCollections.observableArrayList();
    private final List<String> currentCertificationsBase64 = new ArrayList<>();

    // Regex Patterns for validation
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static final String CONTACT_NUMBER_REGEX = "^\\+?[0-9]{7,15}$";
    private static final Pattern CONTACT_NUMBER_PATTERN = Pattern.compile(CONTACT_NUMBER_REGEX);

    private static final String NAME_REGEX = "^[a-zA-ZÀ-ÿ\\s.'-]+$";
    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);

    // Style constants for input fields
    private static final String NORMAL_STYLE_DEFAULT = "-fx-background-radius: 15; -fx-border-radius: 15; -fx-background-color: #D9D9D9; -fx-border-color: transparent; -fx-padding: 8 12;";
    private static final String ERROR_STYLE_DEFAULT = "-fx-background-radius: 15; -fx-border-radius: 15; -fx-background-color: #D9D9D9; -fx-border-color: red; -fx-border-width: 1.5px; -fx-padding: 8 12;";

    private static final String NORMAL_STYLE_COMBO_DATEPICKER = "-fx-background-radius: 15; -fx-border-radius: 15; -fx-background-color: #D9D9D9; -fx-border-color: transparent; -fx-padding: 4 8;";
    private static final String ERROR_STYLE_COMBO_DATEPICKER = "-fx-background-radius: 15; -fx-border-radius: 15; -fx-background-color: #D9D9D9; -fx-border-color: red; -fx-border-width: 1.5px; -fx-padding: 4 8;";
    private static final String NORMAL_STYLE_LISTVIEW = "-fx-background-color: #D9D9D9; -fx-background-radius: 10; -fx-border-color: #B0B0B0; -fx-border-radius: 10;";

    // Mapping for Employment Type display
    private final Map<String, Caregiver.EmploymentType> employmentDisplayToEnum = new HashMap<>();
    private final Map<Caregiver.EmploymentType, String> employmentEnumToDisplay = new HashMap<>();


    public CaregiverView(Stage stage, Connection conn, Caregiver caregiver) {
        this.caregiverController = new CaregiverController(conn);

        // Initialize employment type mappings
        // Assuming Enum names are Full_Time, Part_Time, Retired
        employmentDisplayToEnum.put("Full Time", Caregiver.EmploymentType.Full_time);
        employmentDisplayToEnum.put("Part Time", Caregiver.EmploymentType.Part_time);
        employmentDisplayToEnum.put("Retired", Caregiver.EmploymentType.Retired);

        employmentEnumToDisplay.put(Caregiver.EmploymentType.Full_time, "Full Time");
        employmentEnumToDisplay.put(Caregiver.EmploymentType.Part_time, "Part Time");
        employmentEnumToDisplay.put(Caregiver.EmploymentType.Retired, "Retired");


        Label titleLabel = new Label("Caregiver Profile");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold;");

        TextField firstNameField = createRoundedTextField("First Name");
        addTextValidationListener(firstNameField, NAME_PATTERN, "First name contains invalid characters or is empty.");

        TextField lastNameField = createRoundedTextField("Last Name");
        addTextValidationListener(lastNameField, NAME_PATTERN, "Last name contains invalid characters or is empty.");

        DatePicker birthdayPicker = new DatePicker();
        birthdayPicker.setPromptText("Select Birthday");
        styleDatePicker(birthdayPicker, false);
        final LocalDate today = LocalDate.of(2025, 5, 8);
        birthdayPicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(today));
            }
        });
        addDatePickerValidationListener(birthdayPicker);


        ComboBox<String> genderBox = createRoundedComboBox("Gender");
        addComboBoxValidationListener(genderBox);

        TextField contactField = createRoundedTextField("Contact Number");
        addTextValidationListener(contactField, CONTACT_NUMBER_PATTERN, "Invalid contact number format (e.g., +639xxxxxxxxx or 09xxxxxxxxx).");

        TextField emailField = createRoundedTextField("Email");
        addTextValidationListener(emailField, EMAIL_PATTERN, "Invalid email address format.");

        TextField addressField = createRoundedTextField("Address");
        addTextValidationListener(addressField, s -> !s.trim().isEmpty(), "Address cannot be empty.");

        ComboBox<String> employmentBox = createRoundedComboBox("Employment Type");
        addComboBoxValidationListener(employmentBox);


        Label certificationsTitleLabel = new Label("Certifications (PDF only):");
        certificationsTitleLabel.setFont(Font.font("Arial", Font.getDefault().getSize()));

        ListView<String> certificationsListView = new ListView<>(certificationFileNames);
        certificationsListView.setPrefHeight(100);
        styleListView(certificationsListView, false);

        Button uploadCertificationButton = createSmallGreenButton("Upload PDF");
        Button removeCertificationButton = createSmallRedButton("Remove Selected");

        HBox certificationButtonsBox = new HBox(10, uploadCertificationButton, removeCertificationButton);
        certificationButtonsBox.setAlignment(Pos.CENTER_LEFT);

        // Populate fields
        firstNameField.setText(caregiver.getFirstName());
        lastNameField.setText(caregiver.getLastName());
        if (caregiver.getDateOfBirth() != null) {
            birthdayPicker.setValue(caregiver.getDateOfBirth().toLocalDate());
        }
        // Assuming Gender enum constants are MALE, FEMALE, OTHER
        genderBox.getItems().addAll("Male", "Female", "Other");
        if (caregiver.getGender() != null) {
            // This assumes caregiver.getGender().toString() returns "MALE", "FEMALE", or "OTHER"
            genderBox.setValue(caregiver.getGender().toString());
        }
        contactField.setText(caregiver.getContactNumber());
        emailField.setText(caregiver.getEmail());
        addressField.setText(caregiver.getAddress());

        employmentBox.getItems().addAll(employmentDisplayToEnum.keySet());
        if (caregiver.getEmploymentType() != null) {
            employmentBox.setValue(employmentEnumToDisplay.get(caregiver.getEmploymentType()));
        }


        currentCertificationsBase64.clear();
        certificationFileNames.clear();
        if (caregiver.getCertifications() != null) {
            int certCounter = 1;
            for (String base64Cert : caregiver.getCertifications()) {
                if (base64Cert != null && !base64Cert.isEmpty()) {
                    currentCertificationsBase64.add(base64Cert);
                    certificationFileNames.add("Stored Certification " + certCounter++ + ".pdf");
                }
            }
        }

        final String originalFirstName = caregiver.getFirstName();
        final String originalLastName = caregiver.getLastName();
        final LocalDate originalDOB = (caregiver.getDateOfBirth() != null) ? caregiver.getDateOfBirth().toLocalDate() : null;
        final String originalGender = (caregiver.getGender() != null) ? caregiver.getGender().toString() : null;
        final String originalContact = caregiver.getContactNumber();
        final String originalEmail = caregiver.getEmail();
        final String originalAddress = caregiver.getAddress();
        final String originalEmploymentDisplay = (caregiver.getEmploymentType() != null) ? employmentEnumToDisplay.get(caregiver.getEmploymentType()) : null;
        final List<String> originalCertificationsBase64 = new ArrayList<>(currentCertificationsBase64);
        final List<String> originalCertificationFileNames = new ArrayList<>(certificationFileNames);


        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(15);
        formGrid.add(new Label("First Name:"), 0, 0); formGrid.add(firstNameField, 1, 0);
        formGrid.add(new Label("Last Name:"), 2, 0); formGrid.add(lastNameField, 3, 0);
        formGrid.add(new Label("Birthday:"), 0, 1); formGrid.add(birthdayPicker, 1, 1);
        formGrid.add(new Label("Gender:"), 0, 2); formGrid.add(genderBox, 1, 2);
        formGrid.add(new Label("Contact Number:"), 2, 2); formGrid.add(contactField, 3, 2);
        formGrid.add(new Label("Email:"), 0, 3); formGrid.add(emailField, 1, 3);
        formGrid.add(new Label("Address:"), 0, 4); formGrid.add(addressField, 1, 4, 3, 1);
        formGrid.add(new Label("Employment Type:"), 0, 5); formGrid.add(employmentBox, 1, 5);

        VBox certificationsLayout = new VBox(5, certificationsTitleLabel, certificationsListView, certificationButtonsBox);
        VBox leftPane = new VBox(20, titleLabel, formGrid, certificationsLayout);
        leftPane.setPadding(new Insets(20));
        leftPane.setPrefWidth(650);

        Button cancelBtn = createMainActionButton("Cancel");
        Button saveBtn = createMainActionButton("Save Changes");

        uploadCertificationButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Upload PDF Certification");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                try {
                    byte[] fileContent = Files.readAllBytes(selectedFile.toPath());
                    String base64EncodedString = Base64.getEncoder().encodeToString(fileContent);
                    if (certificationFileNames.stream().anyMatch(name -> name.equalsIgnoreCase(selectedFile.getName()))) {
                        showAlert(Alert.AlertType.WARNING, "Duplicate File", "A file named '" + selectedFile.getName() + "' already exists in the list.");
                        return;
                    }
                    currentCertificationsBase64.add(base64EncodedString);
                    certificationFileNames.add(selectedFile.getName());
                    styleListView(certificationsListView, false);
                } catch (IOException ex) {
                    showAlert(Alert.AlertType.ERROR, "File Read Error", "Could not read the selected file: " + ex.getMessage());
                }
            }
        });

        removeCertificationButton.setOnAction(e -> {
            int selectedIndex = certificationsListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex != -1) {
                currentCertificationsBase64.remove(selectedIndex);
                certificationFileNames.remove(selectedIndex);
            } else {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a certification to remove.");
            }
        });

        cancelBtn.setOnAction(e -> {
            firstNameField.setText(originalFirstName); styleTextField(firstNameField, false);
            lastNameField.setText(originalLastName); styleTextField(lastNameField, false);
            birthdayPicker.setValue(originalDOB); styleDatePicker(birthdayPicker, false);
            genderBox.setValue(originalGender); styleComboBox(genderBox, originalGender == null);
            contactField.setText(originalContact); styleTextField(contactField, false);
            emailField.setText(originalEmail); styleTextField(emailField, false);
            addressField.setText(originalAddress); styleTextField(addressField, false);
            employmentBox.setValue(originalEmploymentDisplay); styleComboBox(employmentBox, originalEmploymentDisplay == null);

            currentCertificationsBase64.clear();
            currentCertificationsBase64.addAll(originalCertificationsBase64);
            certificationFileNames.setAll(originalCertificationFileNames);
            styleListView(certificationsListView, false);
        });

        saveBtn.setOnAction(e -> {
            boolean isValid = true;
            StringBuilder errors = new StringBuilder("Please correct the following issues:\n");
            Caregiver.Gender validatedGender = null; // Temp variable for validated gender

            if (!NAME_PATTERN.matcher(firstNameField.getText().trim()).matches() || firstNameField.getText().trim().isEmpty()) {
                errors.append("- First name is invalid or empty.\n"); styleTextField(firstNameField, true); isValid = false;
            } else { styleTextField(firstNameField, false); }

            if (!NAME_PATTERN.matcher(lastNameField.getText().trim()).matches() || lastNameField.getText().trim().isEmpty()) {
                errors.append("- Last name is invalid or empty.\n"); styleTextField(lastNameField, true); isValid = false;
            } else { styleTextField(lastNameField, false); }

            LocalDate parsedDob = birthdayPicker.getValue();
            if (parsedDob == null) {
                errors.append("- Birthday is required.\n"); styleDatePicker(birthdayPicker, true); isValid = false;
            } else if (parsedDob.isAfter(today)) {
                errors.append("- Birthday cannot be in the future.\n"); styleDatePicker(birthdayPicker, true); isValid = false;
            } else { styleDatePicker(birthdayPicker, false); }

            String genderStringFromBox = genderBox.getValue();
            if (genderStringFromBox == null || genderStringFromBox.trim().isEmpty()) {
                errors.append("- Gender is required.\n"); styleComboBox(genderBox, true); isValid = false;
            } else {
                try {
                    // Assuming ComboBox stores "MALE", "FEMALE", "OTHER" and enum is defined with these exact uppercase names
                    validatedGender = Caregiver.Gender.valueOf(genderStringFromBox.trim());
                    styleComboBox(genderBox, false);
                } catch (IllegalArgumentException ex) {
                    errors.append("- Invalid gender value. Check ComboBox items and Gender enum definitions (expected MALE, FEMALE, or OTHER).\n");
                    styleComboBox(genderBox, true);
                    isValid = false;
                }
            }

            if (!CONTACT_NUMBER_PATTERN.matcher(contactField.getText().trim()).matches()) {
                errors.append("- Contact number is invalid.\n"); styleTextField(contactField, true); isValid = false;
            } else { styleTextField(contactField, false); }

            if (!EMAIL_PATTERN.matcher(emailField.getText().trim()).matches()) {
                errors.append("- Email is invalid.\n"); styleTextField(emailField, true); isValid = false;
            } else { styleTextField(emailField, false); }

            if (addressField.getText().trim().isEmpty()) {
                errors.append("- Address is required.\n"); styleTextField(addressField, true); isValid = false;
            } else { styleTextField(addressField, false); }

            String selectedEmploymentDisplay = employmentBox.getValue();
            Caregiver.EmploymentType employmentEnumValue = null;
            if (selectedEmploymentDisplay == null || selectedEmploymentDisplay.trim().isEmpty()) {
                errors.append("- Employment type is required.\n"); styleComboBox(employmentBox, true); isValid = false;
            } else {
                employmentEnumValue = employmentDisplayToEnum.get(selectedEmploymentDisplay);
                if(employmentEnumValue == null){
                    errors.append("- Invalid employment type selection.\n"); styleComboBox(employmentBox, true); isValid = false;
                } else {
                    styleComboBox(employmentBox, false);
                }
            }

            if (!isValid) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", errors.toString());
                return;
            }

            try {
                caregiver.setFirstName(firstNameField.getText().trim());
                caregiver.setLastName(lastNameField.getText().trim());
                if (parsedDob != null) {
                    caregiver.setDateOfBirth(parsedDob.atStartOfDay());
                }
                if(validatedGender != null) { // Set gender if it was validated successfully
                    caregiver.setGender(validatedGender);
                }
                caregiver.setContactNumber(contactField.getText().trim());
                caregiver.setEmail(emailField.getText().trim());
                caregiver.setAddress(addressField.getText().trim());

                if (employmentEnumValue != null) { // Ensure employmentEnumValue is not null before setting
                    caregiver.setEmploymentType(employmentEnumValue);
                }
                caregiver.setCertifications(new ArrayList<>(currentCertificationsBase64));

                caregiverController.updateCaregiver(caregiver);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully.");

            } catch (IllegalArgumentException ex) {
                showAlert(Alert.AlertType.ERROR, "Update Error", "Invalid data provided: " + ex.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Update Error", "An unexpected error occurred: " + ex.getMessage());
            }
        });

        HBox mainButtonBox = new HBox(20, cancelBtn, saveBtn);
        mainButtonBox.setAlignment(Pos.CENTER_LEFT);
        leftPane.getChildren().add(mainButtonBox);

        Button appointmentsBtn = createSidebarButton("Your Appointments");
        Button eldersBtn = createSidebarButton("Your Elders");
        Button servicesBtn = createSidebarButton("Your Services");
        Button logoutBtn = createSidebarButton("Log Out");
        appointmentsBtn.setOnAction(e -> navigateToView(stage, conn, caregiver, "appointments"));
        eldersBtn.setOnAction(e -> navigateToView(stage, conn, caregiver, "elders"));
        servicesBtn.setOnAction(e -> navigateToView(stage, conn, caregiver, "services"));
        logoutBtn.setOnAction(e -> {
            System.out.println("Logging out...");
            Platform.runLater(() -> {
                LoginController loginController = new LoginController(stage, conn);
                Scene loginScene = loginController.getLoginScene();
                stage.setScene(loginScene);
            });
        });
        VBox topSidebarButtons = new VBox(20, appointmentsBtn, eldersBtn, servicesBtn);
        topSidebarButtons.setAlignment(Pos.TOP_CENTER);
        VBox rightPane = new VBox();
        rightPane.setPadding(new Insets(30));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setAlignment(Pos.TOP_CENTER);
        rightPane.setPrefWidth(250);
        rightPane.setMinWidth(230);
        VBox.setVgrow(topSidebarButtons, Priority.ALWAYS);
        rightPane.getChildren().addAll(topSidebarButtons, logoutBtn);
        HBox root = new HBox(leftPane, rightPane);
        HBox.setHgrow(leftPane, Priority.ALWAYS);
        this.scene = new Scene(root, 950, 700);
    }

    private void navigateToView(Stage stage, Connection conn, Caregiver caregiver, String viewType) {
        Platform.runLater(() -> {
            try {
                Scene nextScene = null;
                switch (viewType) {
                    case "appointments":
                        System.out.println("Switching to CaregiverAppointmentView...");
                        CaregiverAppointmentView cav = new CaregiverAppointmentView(stage, conn, caregiver);
                        nextScene = cav.getScene();
                        break;
                    case "elders":
                        System.out.println("Switching to CaregiverElderView...");
                        CaregiverElderView cev = new CaregiverElderView(stage, conn, caregiver);
                        nextScene = cev.getScene();
                        break;
                    case "services":
                        System.out.println("Switching to CaregiverServicesView...");
                        CaregiverServiceView csv = new CaregiverServiceView(stage, conn, caregiver);
                        nextScene = csv.getScene();
                        break;
                }
                if (nextScene != null) {
                    stage.setScene(nextScene);
                }
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Navigation Error", "Database error navigating to " + viewType + ": " + ex.getMessage());
            } catch (Exception ex) { // Catch other potential runtime exceptions from view constructors
                showAlert(Alert.AlertType.ERROR, "Navigation Error", "Error navigating to " + viewType + ": " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }


    private TextField createRoundedTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        styleTextField(tf, false);
        return tf;
    }

    private ComboBox<String> createRoundedComboBox(String prompt) {
        ComboBox<String> cb = new ComboBox<>();
        cb.setPromptText(prompt);
        styleComboBox(cb, false);
        cb.setMinWidth(180);
        cb.setPrefWidth(ComboBox.USE_COMPUTED_SIZE);
        return cb;
    }

    private void styleTextField(TextField field, boolean isError) {
        field.setStyle(isError ? ERROR_STYLE_DEFAULT : NORMAL_STYLE_DEFAULT);
    }

    private void styleComboBox(ComboBox<String> comboBox, boolean isError) {
        comboBox.setStyle(isError ? ERROR_STYLE_COMBO_DATEPICKER : NORMAL_STYLE_COMBO_DATEPICKER);
    }

    private void styleDatePicker(DatePicker datePicker, boolean isError) {
        datePicker.setStyle(isError ? ERROR_STYLE_COMBO_DATEPICKER : NORMAL_STYLE_COMBO_DATEPICKER);
        datePicker.setPrefWidth(190); // Adjusted for better fit
    }

    private void styleListView(ListView<String> listView, boolean isError) {
        listView.setStyle(isError ? ERROR_STYLE_COMBO_DATEPICKER : NORMAL_STYLE_LISTVIEW);
    }

    private void addTextValidationListener(TextField field, Pattern pattern, String errorMessageIfInvalid) {
        field.focusedProperty().addListener((obs, oldValFocus, newValFocus) -> {
            if (!newValFocus) {
                boolean isValid = field.getText() != null && pattern.matcher(field.getText().trim()).matches();
                boolean isEmptyAndNotRequired = field.getText().trim().isEmpty() && pattern.matcher("").matches();

                if(field.getText().trim().isEmpty() && !pattern.matcher("").matches() && (pattern.toString().contains("+") || (pattern.toString().contains("*") && !pattern.toString().contains("?")) ) ){
                    styleTextField(field, true);
                } else {
                    styleTextField(field, !isValid && !isEmptyAndNotRequired);
                }
            }
        });
    }

    private void addTextValidationListener(TextField field, java.util.function.Predicate<String> validator, String errorMessageIfInvalid) {
        field.focusedProperty().addListener((obs, oldValFocus, newValFocus) -> {
            if (!newValFocus) {
                boolean isValid = field.getText() != null && validator.test(field.getText().trim());
                styleTextField(field, !isValid);
            }
        });
    }

    private void addDatePickerValidationListener(DatePicker datePicker) {
        datePicker.focusedProperty().addListener((obs, oldValFocus, newValFocus) -> {
            if (!newValFocus) {
                validateDatePicker(datePicker);
            }
        });
        datePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            validateDatePicker(datePicker);
        });
    }

    private void validateDatePicker(DatePicker datePicker){
        LocalDate value = datePicker.getValue();
        final LocalDate today = LocalDate.of(2025, 5, 8);
        boolean isError = (value == null || value.isAfter(today)); // Error if null or future date
        styleDatePicker(datePicker, isError);
    }


    private void addComboBoxValidationListener(ComboBox<String> comboBox) {
        comboBox.focusedProperty().addListener((obs, oldValFocus, newValFocus) -> {
            if (!newValFocus) {
                styleComboBox(comboBox, comboBox.getValue() == null || comboBox.getValue().trim().isEmpty());
            }
        });
        comboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            styleComboBox(comboBox, newVal == null || newVal.trim().isEmpty());
        });
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Button createMainActionButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: #3BB49C; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand; -fx-padding: 10 25;");
        return btn;
    }

    private Button createSmallGreenButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 12px; -fx-background-radius: 15; -fx-cursor: hand; -fx-padding: 6 15;");
        return btn;
    }

    private Button createSmallRedButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-size: 12px; -fx-background-radius: 15; -fx-cursor: hand; -fx-padding: 6 15;");
        return btn;
    }

    private Button createSidebarButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 14px; -fx-background-radius: 15; -fx-cursor: hand; -fx-padding: 10 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 1);");
        btn.setPrefWidth(200);
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }

    public Scene getScene() {
        return scene;
    }
}