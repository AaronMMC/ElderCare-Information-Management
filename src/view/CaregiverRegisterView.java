package view;

import controller.CaregiverController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Caregiver; 

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;


public class CaregiverRegisterView {

    private final Scene scene;
    private final List<File> selectedCertFiles = new ArrayList<>();
    private final ListView<String> certificationsListView = new ListView<>();
    private final Connection dbConnection;
    private final Stage mainStage;

    public CaregiverRegisterView(Stage stage, Connection conn) {
        this.mainStage = stage;
        this.dbConnection = conn;

        Label personalInfoTitle = new Label("Fill up Personal Information");
        personalInfoTitle.setFont(new Font("Arial", 24));
        personalInfoTitle.setStyle("-fx-font-weight: bold;");

        GridPane personalInfoGrid = new GridPane();
        personalInfoGrid.setHgap(20);
        personalInfoGrid.setVgap(15);
        personalInfoGrid.setAlignment(Pos.CENTER_LEFT);

        TextField firstNameField = createRoundedField("First Name");
        TextField lastNameField = createRoundedField("Last Name");
        DatePicker birthdayPicker = new DatePicker();
        birthdayPicker.setPromptText("Select Birthday");
        styleDatePicker(birthdayPicker);

        ComboBox<Caregiver.Gender> genderComboBox = new ComboBox<>();
        genderComboBox.getItems().addAll(Caregiver.Gender.values());
        genderComboBox.setPromptText("Select Gender");
        styleComboBox(genderComboBox);

        TextField contactNumberField = createRoundedField("Contact Number");
        TextField emailField = createRoundedField("Email Address");
        TextField addressField = createRoundedField("Address");

        
        ToggleGroup employmentToggleGroup = new ToggleGroup();

        RadioButton rbFullTime = new RadioButton("Full Time");
        rbFullTime.setToggleGroup(employmentToggleGroup);
        rbFullTime.setUserData(Caregiver.EmploymentType.FULL_TIME);

        RadioButton rbPartTime = new RadioButton("Part Time");
        rbPartTime.setToggleGroup(employmentToggleGroup);
        rbPartTime.setUserData(Caregiver.EmploymentType.PART_TIME);

        RadioButton rbRetired = new RadioButton("Retired");
        rbRetired.setToggleGroup(employmentToggleGroup);
        rbRetired.setUserData(Caregiver.EmploymentType.RETIRED);

        HBox employmentBox = new HBox(15, rbFullTime, rbPartTime, rbRetired); 
        employmentBox.setAlignment(Pos.CENTER_LEFT);
        


        Label certificationsLabel = new Label("Upload Certifications");
        Button uploadCertButton = new Button("Add Files...");
        styleStandardButton(uploadCertButton);

        certificationsListView.setPrefHeight(100);
        certificationsListView.setStyle("-fx-background-color: lightgray; -fx-border-color: gray; -fx-border-radius: 5;");

        Button removeCertButton = new Button("Remove Selected");
        styleStandardButton(removeCertButton);
        removeCertButton.setDisable(true);

        HBox certButtonsBox = new HBox(10, removeCertButton);
        certButtonsBox.setAlignment(Pos.CENTER_LEFT);

        VBox certificationsBox = new VBox(5, uploadCertButton, certificationsListView, certButtonsBox);
        certificationsBox.setAlignment(Pos.CENTER_LEFT);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Certification Files (PDF, Images, Word Docs)");
        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("PDF Documents (*.pdf)", "*.pdf");
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files (*.png, *.jpg, *.jpeg)", "*.png", "*.jpg", "*.jpeg");
        FileChooser.ExtensionFilter wordFilter = new FileChooser.ExtensionFilter("Word Documents (*.doc, *.docx)", "*.doc", "*.docx");
        FileChooser.ExtensionFilter combinedFilter = new FileChooser.ExtensionFilter("Supported Documents & Images", "*.pdf", "*.png", "*.jpg", "*.jpeg", "*.doc", "*.docx");
        fileChooser.getExtensionFilters().addAll(combinedFilter, pdfFilter, imageFilter, wordFilter);
        fileChooser.setSelectedExtensionFilter(combinedFilter);

        certificationsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            removeCertButton.setDisable(newSelection == null);
        });

        uploadCertButton.setOnAction(e -> {
            List<File> chosenFiles = fileChooser.showOpenMultipleDialog(stage);
            if (chosenFiles != null) {
                for (File newFile : chosenFiles) {
                    if (!selectedCertFiles.contains(newFile)) {
                        selectedCertFiles.add(newFile);
                    }
                }
                refreshListView();
            }
        });

        removeCertButton.setOnAction(e -> {
            int selectedIndex = certificationsListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex != -1) {
                selectedCertFiles.remove(selectedIndex);
                refreshListView();
            }
        });

        int row = 0;
        personalInfoGrid.add(new Label("First Name:"), 0, row);
        personalInfoGrid.add(firstNameField, 1, row);
        row++;
        personalInfoGrid.add(new Label("Last Name:"), 0, row);
        personalInfoGrid.add(lastNameField, 1, row);
        row++;
        personalInfoGrid.add(new Label("Birthday:"), 0, row);
        personalInfoGrid.add(birthdayPicker, 1, row);
        row++;
        personalInfoGrid.add(new Label("Gender:"), 0, row);
        personalInfoGrid.add(genderComboBox, 1, row);
        row++;
        personalInfoGrid.add(new Label("Contact Number:"), 0, row);
        personalInfoGrid.add(contactNumberField, 1, row);
        row++;
        personalInfoGrid.add(new Label("Email Address:"), 0, row);
        personalInfoGrid.add(emailField, 1, row);
        row++;
        personalInfoGrid.add(new Label("Address:"), 0, row);
        personalInfoGrid.add(addressField, 1, row);
        row++;
        
        personalInfoGrid.add(new Label("Employment Type:"), 0, row);
        personalInfoGrid.add(employmentBox, 1, row);
        row++;
        
        personalInfoGrid.add(certificationsLabel, 0, row, 2, 1);
        row++;
        personalInfoGrid.add(certificationsBox, 0, row, 2, 1);
        row++;

        VBox leftPane = new VBox(20, personalInfoTitle, personalInfoGrid);
        leftPane.setPadding(new Insets(40));
        leftPane.setPrefWidth(600);
        leftPane.setAlignment(Pos.TOP_CENTER);

        Label registerLabel = new Label("Register");
        registerLabel.setFont(new Font("Arial", 24));
        registerLabel.setTextFill(Color.WHITE);
        registerLabel.setStyle("-fx-font-weight: bold;");
        TextField usernameField = createRoundedField("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        styleRoundedField(passwordField);
        Button registerButton = new Button("Register");
        styleRegisterButton(registerButton);
        Button backButton = new Button("Back to Login");
        styleStandardButton(backButton);
        VBox rightPane = new VBox(20, registerLabel, usernameField, passwordField, registerButton, backButton);
        rightPane.setPadding(new Insets(60));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setAlignment(Pos.CENTER);
        rightPane.setPrefWidth(400);

        HBox rootLayout = new HBox(leftPane, rightPane);
        this.scene = new Scene(rootLayout, 1000, 700);

        registerButton.setOnAction(e -> {

            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            LocalDate selectedBirthday = birthdayPicker.getValue();
            Caregiver.Gender selectedGender = genderComboBox.getValue();
            String contactNumber = contactNumberField.getText().trim();
            String email = emailField.getText().trim();
            String address = addressField.getText().trim();
            
            Toggle selectedToggle = employmentToggleGroup.getSelectedToggle();
            Caregiver.EmploymentType selectedEmploymentType = null;
            if (selectedToggle != null) {
                selectedEmploymentType = (Caregiver.EmploymentType) selectedToggle.getUserData();
            }
            
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            
            if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty() || selectedBirthday == null
            || selectedGender == null || contactNumber.isEmpty() || address.isEmpty() || selectedEmploymentType == null || selectedCertFiles.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Missing Information", "Please fill in all required fields.");
                return;
            }

            List<String> base64CertStrings = new ArrayList<>();
            boolean encodingError = false;
            for (File file : selectedCertFiles) {
                try {
                    byte[] fileContent = Files.readAllBytes(file.toPath());
                    String encodedString = Base64.getEncoder().encodeToString(fileContent);
                    base64CertStrings.add(encodedString);
                } catch (IOException ioException) {
                    encodingError = true;
                    System.err.println("Error reading file: " + file.getName() + " - " + ioException.getMessage());
                }
            }

            if (encodingError) {
                showAlert(Alert.AlertType.ERROR, "File Read Error", "Could not read one or more certification files. Please check the files and try again.");
                return;
            }

            System.out.println("Data gathered by View. Passing control to Controller (simulated)...");

            Caregiver newCaregiver = new Caregiver(username,password,firstName,lastName, selectedBirthday.atStartOfDay(),selectedGender,contactNumber,email,address,base64CertStrings,selectedEmploymentType);
            boolean submissionSuccess = false;
            try {
                CaregiverController caregiverController = new CaregiverController(conn);
                caregiverController.addCaregiver(newCaregiver);
                submissionSuccess = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (submissionSuccess) {
                System.out.println("Submission successful. Switching to Pending View...");
                CaregiverPendingView pendingView = new CaregiverPendingView(mainStage, dbConnection);
                mainStage.setScene(pendingView.getScene());
            } else {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", "Could not submit registration data. Please try again later.");
            }
        });

        backButton.setOnAction(e -> {
            
        });

        stage.setScene(scene);
    }

    private void refreshListView() {
        List<String> fileNames = selectedCertFiles.stream()
                .map(File::getName)
                .collect(Collectors.toList());
        certificationsListView.getItems().setAll(fileNames);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private TextField createRoundedField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        styleRoundedField(field);
        return field;
    }

    private void styleRoundedField(TextField field) {
        field.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 15; -fx-padding: 8 12; -fx-border-color: #cccccc; -fx-border-radius: 15;");
        field.setPrefWidth(250);
    }

    private void styleComboBox(ComboBox<?> comboBox) {
        comboBox.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 15; -fx-border-color: #cccccc; -fx-border-radius: 15; -fx-padding: 4 8;");
        comboBox.setPrefWidth(250);
    }

    private void styleDatePicker(DatePicker datePicker) {
        datePicker.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 15; -fx-border-color: #cccccc; -fx-border-radius: 15; -fx-padding: 4 8;");
        datePicker.setPrefWidth(250);
    }

    private void styleStandardButton(Button button) {
        String baseStyle = "-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 14px; -fx-background-radius: 20; -fx-border-color: #3BB49C; -fx-border-width: 1; -fx-border-radius: 20;";
        String hoverStyle = "-fx-background-color: #e0e0e0; -fx-text-fill: black; -fx-font-size: 14px; -fx-background-radius: 20; -fx-border-color: #3BB49C; -fx-border-width: 1; -fx-border-radius: 20;";
        button.setStyle(baseStyle);
        button.setPrefWidth(150);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
    }

    private void styleRegisterButton(Button button) {
        String baseStyle = "-fx-background-color: #2a8c79; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 20;";
        String hoverStyle = "-fx-background-color: #1f6f5f; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 20;";
        button.setStyle(baseStyle);
        button.setPrefWidth(150);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
    }

    public Scene getScene() {
        return scene;
    }
}