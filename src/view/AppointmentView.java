package view;

import controller.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class AppointmentView {

    private final Scene scene;
    private final List<Service> selectedServices = new ArrayList<>();
    private final List<Elder> selectedElders = new ArrayList<>();
    private final ComboBox<Caregiver> caregiverDropdown = new ComboBox<>();
    private final VBox certBox = new VBox(5);
    // private final VBox infoBox = new VBox(5); // Removed as per request
    private final Label amountLabel = new Label("Amount To Be Paid: 0.00");
    private final VBox serviceCheckboxContainer = new VBox(10);
    private final ComboBox<String> filterDropdown = new ComboBox<>();

    private final Connection conn;
    private final AppointmentController appointmentController;
    private final CaregiverController caregiverController;
    private final GuardianController guardianController;
    private final ElderController elderController;
    private final ServiceController serviceController;
    private final PaymentController paymentController;

    private Payment payment = new Payment();
    private final Appointment appointment = new Appointment();

    public AppointmentView(Stage stage, Connection conn, Guardian guardian) {
        this.conn = conn;
        this.appointmentController = new AppointmentController(conn);
        this.caregiverController = new CaregiverController(conn);
        this.guardianController = new GuardianController(conn);
        this.elderController = new ElderController(conn);
        this.serviceController = new ServiceController(conn);
        this.paymentController = new PaymentController(conn);

        BorderPane rootLayout = new BorderPane();
        rootLayout.setPadding(new Insets(20));

        VBox leftPane = createLeftPane(stage, guardian);
        VBox rightPane = createRightPane();

        HBox mainContent = new HBox(30, leftPane, rightPane);
        HBox.setHgrow(leftPane, Priority.ALWAYS);
        mainContent.setAlignment(Pos.TOP_CENTER);

        rootLayout.setCenter(mainContent);

        this.scene = new Scene(rootLayout, 1250, 700);
        stage.setTitle("Submit New Appointment");
        stage.setScene(scene);
        stage.show();

        loadInitialData(guardian);
    }

    private VBox createLeftPane(Stage stage, Guardian guardian) {
        Label titleLabel = new Label("Submit an Appointment");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        VBox elderSelectionBox = createElderCheckboxSection(guardian);
        Label selectEldersLabel = new Label("Select Elders:");
        selectEldersLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        VBox elderSection = new VBox(10, selectEldersLabel, elderSelectionBox);

        Label serviceLabel = new Label("Services to Avail:");
        serviceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        Label filterLabel = new Label("Filter by Category:");
        filterDropdown.setPromptText("All Categories");
        filterDropdown.setStyle(getInputFieldStyle() + "-fx-pref-width: 200px;");
        HBox filterBox = new HBox(10, filterLabel, filterDropdown);
        filterBox.setAlignment(Pos.CENTER_LEFT);

        ScrollPane serviceScrollPane = new ScrollPane(serviceCheckboxContainer);
        serviceScrollPane.setFitToWidth(true);
        serviceScrollPane.setPrefHeight(180);
        serviceScrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: #bdc3c7; -fx-border-radius: 10; -fx-background-radius: 10;");
        VBox serviceBoxContainer = new VBox(serviceScrollPane);
        serviceBoxContainer.setStyle("-fx-background-color: #ecf0f1; -fx-background-radius: 15; -fx-padding: 15px;");
        serviceCheckboxContainer.setPadding(new Insets(5));

        VBox serviceSection = new VBox(10, serviceLabel, filterBox, serviceBoxContainer);

        Label dateTimeLabel = new Label("Choose Date & Duration:");
        dateTimeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select Date");
        datePicker.setStyle(getInputFieldStyle());
        datePicker.setPrefWidth(180);

        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item.isBefore(LocalDate.now())) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                            }
                        };
                    }
                };
        datePicker.setDayCellFactory(dayCellFactory);
        datePicker.setValue(LocalDate.now());


        ComboBox<String> durationBox = createRoundedComboBox("Duration (hrs)");
        durationBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
        durationBox.setEditable(true);
        durationBox.setPrefWidth(150);

        HBox dateTimeBox = new HBox(20, datePicker, durationBox);
        dateTimeBox.setAlignment(Pos.CENTER_LEFT);

        amountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        amountLabel.setStyle("-fx-text-fill: #27ae60;");
        VBox centerContent = new VBox(20, serviceSection, dateTimeLabel, dateTimeBox, amountLabel);

        Button cancelButton = createStyledButton("Cancel", "#e74c3c", "#c0392b");
        cancelButton.setOnAction(e -> {
            GuardianAppointmentView guardianAppointmentView = new GuardianAppointmentView(stage, conn, guardian);
            stage.setScene(guardianAppointmentView.getScene());
        });

        Button submitButton = createStyledButton("Submit Appointment", "#2ecc71", "#27ae60");
        submitButton.setOnAction(e -> handleSubmitAppointment(datePicker, durationBox, guardian));

        HBox actionButtons = new HBox(20, cancelButton, submitButton);
        actionButtons.setAlignment(Pos.CENTER_LEFT);
        actionButtons.setPadding(new Insets(20, 0, 0, 0));

        VBox leftPane = new VBox(25, titleLabel, elderSection, centerContent, actionButtons);
        leftPane.setPadding(new Insets(20, 30, 20, 20));
        leftPane.setAlignment(Pos.TOP_LEFT);

        return leftPane;
    }

    private VBox createRightPane() {
        Label caregiverLabel = new Label("Select a Caregiver:");
        caregiverLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        caregiverLabel.setStyle("-fx-text-fill: white;"); // Ensure text is visible on new background
        caregiverDropdown.setPromptText("Select a caregiver");
        caregiverDropdown.setStyle(getInputFieldStyle() + "-fx-pref-width: 280px;");
        caregiverDropdown.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Caregiver caregiver, boolean empty) {
                super.updateItem(caregiver, empty);
                setText(empty || caregiver == null ? null : caregiver.getFirstName() + " " + caregiver.getLastName());
            }
        });
        caregiverDropdown.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Caregiver caregiver, boolean empty) {
                super.updateItem(caregiver, empty);
                setText(empty || caregiver == null ? null : caregiver.getFirstName() + " " + caregiver.getLastName());
            }
        });
        caregiverDropdown.setOnAction(e -> updateCaregiverInfo());

        Label certsLabel = new Label("Caregiver Certifications:");
        certsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        certsLabel.setStyle("-fx-text-fill: white;"); // Ensure text is visible
        certBox.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: #bdc3c7; -fx-border-radius: 10;");
        certBox.setPadding(new Insets(12));
        certBox.setPrefHeight(180);
        ScrollPane certScrollPane = new ScrollPane(certBox);
        certScrollPane.setFitToWidth(true);
        certScrollPane.setPrefHeight(180);
        certScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        VBox rightPane = new VBox(15, caregiverLabel, caregiverDropdown, certsLabel, certScrollPane);
        rightPane.setPadding(new Insets(30));
        rightPane.setStyle("-fx-background-color: #3BB49C; -fx-background-radius: 15;"); // Reverted color
        rightPane.setPrefWidth(380);
        rightPane.setAlignment(Pos.TOP_CENTER);

        return rightPane;
    }


    private void loadInitialData(Guardian guardian) {
        List<Service> allServices = serviceController.getAllServices();
        Set<String> categories = allServices.stream()
                .map(Service::getCategory)
                .collect(Collectors.toCollection(TreeSet::new));
        categories.add("All Categories");
        filterDropdown.getItems().setAll(categories);
        filterDropdown.setValue("All Categories");
        populateServiceCheckboxes(allServices, "All Categories");

        filterDropdown.setOnAction(f -> {
            String selectedCategory = filterDropdown.getValue();
            populateServiceCheckboxes(allServices, selectedCategory);
            updateAvailableCaregivers();
            updateTotalAmount();
        });
    }


    private void populateServiceCheckboxes(List<Service> services, String categoryFilter) {
        serviceCheckboxContainer.getChildren().clear();
        List<Service> currentSelected = new ArrayList<>(selectedServices);
        selectedServices.clear();

        for (Service service : services) {
            if (!"All Categories".equals(categoryFilter) && !service.getCategory().equals(categoryFilter)) {
                continue;
            }

            CheckBox cb = new CheckBox(service.getServiceName() + " - Php " + String.format("%.2f", service.getPrice()));
            cb.setWrapText(true);
            cb.setFont(Font.font("Arial", 13));

            if (currentSelected.stream().anyMatch(s -> s.getServiceID() == service.getServiceID())) {
                cb.setSelected(true);
                selectedServices.add(service);
            }

            cb.setOnAction(e -> {
                if (cb.isSelected()) {
                    selectedServices.add(service);
                } else {
                    selectedServices.removeIf(s -> s.getServiceID() == service.getServiceID());
                }
                updateAvailableCaregivers();
                updateTotalAmount();
            });
            serviceCheckboxContainer.getChildren().add(cb);
        }
        if (selectedServices.isEmpty()) {
            updateAvailableCaregivers();
        }
        updateTotalAmount();
    }

    private void updateAvailableCaregivers() {
        Caregiver currentlySelectedCaregiver = caregiverDropdown.getValue();
        caregiverDropdown.getItems().clear();
        if (!selectedServices.isEmpty()) {
            Set<Caregiver> availableCaregivers = selectedServices.stream()
                    .flatMap(service -> caregiverController.getAllCaregiversByService(service).stream())
                    .filter(cg -> cg.getBackgroundCheckStatus() == Caregiver.BackgroundCheckStatus.Passed &&
                            cg.getMedicalClearanceStatus() == Caregiver.MedicalClearanceStatus.Cleared)
                    .collect(Collectors.toSet());
            caregiverDropdown.getItems().addAll(availableCaregivers);

            if (currentlySelectedCaregiver != null && availableCaregivers.contains(currentlySelectedCaregiver)) {
                caregiverDropdown.setValue(currentlySelectedCaregiver);
            } else {
                caregiverDropdown.setValue(null);
                clearCaregiverDetails();
            }
        } else {
            caregiverDropdown.setValue(null);
            clearCaregiverDetails();
        }
        if(caregiverDropdown.getValue() == null) {
            clearCaregiverDetails();
        }
    }

    private void clearCaregiverDetails() {
        certBox.getChildren().clear();
        // infoBox.getChildren().clear(); // Removed as per request
        Label noCertsLabel = new Label("No certifications to display.");
        noCertsLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #7f8c8d;");
        certBox.getChildren().add(noCertsLabel);
        // Label noInfoLabel = new Label("No caregiver details to display."); // Removed
        // noInfoLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #7f8c8d;"); // Removed
        // infoBox.getChildren().add(noInfoLabel); // Removed
    }


    private void updateTotalAmount() {
        if (!selectedServices.isEmpty()) {
            List<CaregiverService> caregiverServices = selectedServices.stream()
                    .map(service -> new CaregiverService(service.getServiceID(), (caregiverDropdown.getValue() != null) ? caregiverDropdown.getValue().getCaregiverID() : -1))
                    .toList();
            payment = paymentController.getPaymentByAllServices(appointment, caregiverServices, selectedServices);
            amountLabel.setText("Amount To Be Paid: Php " + String.format("%.2f", payment.getTotalAmount()));
        } else {
            amountLabel.setText("Amount To Be Paid: Php 0.00");
        }
    }

    private String determineFileExtension(byte[] data) {
        if (data == null || data.length < 4) return "dat";
        if (data[0] == (byte)0x89 && data[1] == (byte)0x50 && data[2] == (byte)0x4E && data[3] == (byte)0x47) return "png";
        if (data.length >= 3 && data[0] == (byte)0xFF && data[1] == (byte)0xD8 && data[2] == (byte)0xFF) return "jpg";
        if (data[0] == (byte)0x25 && data[1] == (byte)0x50 && data[2] == (byte)0x44 && data[3] == (byte)0x46) return "pdf";
        if (data[0] == (byte)'G' && data[1] == (byte)'I' && data[2] == (byte)'F' && data[3] == (byte)'8') return "gif";
        if (data[0] == (byte)0x50 && data[1] == (byte)0x4B && data[2] == (byte)0x03 && data[3] == (byte)0x04) return "zip";
        return "dat";
    }

    private void updateCaregiverInfo() {
        certBox.getChildren().clear();
        // infoBox.getChildren().clear(); // Removed as per request
        Caregiver selected = caregiverDropdown.getValue();

        if (selected != null) {
            List<String> base64CertStrings = selected.getCertifications();
            if (base64CertStrings != null && !base64CertStrings.isEmpty()) {
                int certIndex = 1;
                for (String certStr : base64CertStrings) {
                    if (certStr != null && !certStr.trim().isEmpty()) {
                        try {
                            byte[] decodedBytes = Base64.getDecoder().decode(certStr);
                            String extension = determineFileExtension(decodedBytes).toUpperCase();
                            String displayName = "Certification " + certIndex + (!"DAT".equals(extension) ? " (." + extension + ")" : " (File)");
                            Label certLabel = new Label("• " + displayName);
                            certLabel.setWrapText(true);
                            certBox.getChildren().add(certLabel);
                            certIndex++;
                        } catch (IllegalArgumentException e) {
                            Label errorLabel = new Label("• Certification " + certIndex + " (Error decoding)");
                            certBox.getChildren().add(errorLabel);
                            certIndex++;
                        }
                    }
                }
                if (certBox.getChildren().isEmpty()){
                    Label noCertsLabel = new Label("No valid certifications found.");
                    noCertsLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #7f8c8d;");
                    certBox.getChildren().add(noCertsLabel);
                }
            } else {
                Label noCertsLabel = new Label("No certifications available for this caregiver.");
                noCertsLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #7f8c8d;");
                certBox.getChildren().add(noCertsLabel);
            }

            // Caregiver details section removed from display
            // int age = (selected.getDateOfBirth() != null) ? getAge(selected.getDateOfBirth().toLocalDate()) : 0;
            // infoBox.getChildren().addAll(
            //         new Label("Age: " + (age > 0 ? age : "N/A")),
            //         new Label("Gender: " + (selected.getGender() != null ? selected.getGender() : "N/A")),
            //         new Label("Employment: " + (selected.getEmploymentType() != null ? selected.getEmploymentType() : "N/A")),
            //         new Label("Contact: " + (selected.getContactNumber() != null ? selected.getContactNumber() : "N/A")),
            //         new Label("Email: " + (selected.getEmail() != null ? selected.getEmail() : "N/A"))
            // );

        } else {
            clearCaregiverDetails();
        }
        updateTotalAmount();
    }

    private VBox createElderCheckboxSection(Guardian guardian) {
        VBox elderListBox = new VBox(8);
        elderListBox.setPadding(new Insets(10));

        List<Elder> elders = elderController.getAllEldersByGuardianId(guardian.getGuardianID());
        if (elders.isEmpty()) {
            Label noEldersLabel = new Label("No elders registered for this guardian.");
            noEldersLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #7f8c8d;");
            elderListBox.getChildren().add(noEldersLabel);
        } else {
            elders.forEach(elder -> {
                CheckBox cb = new CheckBox(elder.getFirstName() + " " + elder.getLastName());
                cb.setUserData(elder);
                cb.setFont(Font.font("Arial", 13));
                cb.setOnAction(e -> {
                    if (cb.isSelected())
                        selectedElders.add(elder);
                    else
                        selectedElders.remove(elder);
                });
                elderListBox.getChildren().add(cb);
            });
        }

        ScrollPane scrollPane = new ScrollPane(elderListBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(150);
        scrollPane.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #bdc3c7; -fx-background-color: transparent;");

        VBox wrapper = new VBox(scrollPane);
        wrapper.setStyle("-fx-background-color: #ecf0f1; -fx-background-radius: 15; -fx-padding: 10px;");
        wrapper.setPrefWidth(300);
        return wrapper;
    }

    private void handleSubmitAppointment(DatePicker datePicker, ComboBox<String> durationBox, Guardian guardian) {
        LocalDate selectedDate = datePicker.getValue();
        String durationText = durationBox.getValue();
        Caregiver selectedCaregiver = caregiverDropdown.getValue();

        if (selectedElders.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please select at least one elder.");
            return;
        }
        if (selectedServices.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please select at least one service.");
            return;
        }
        if (selectedDate == null) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please select an appointment date.");
            return;
        }
        if (durationText == null || durationText.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please specify the duration.");
            return;
        }
        if (selectedCaregiver == null) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please select a caregiver.");
            return;
        }


        try {
            int durationInHours = Integer.parseInt(durationText.replaceAll("[^0-9]", ""));
            if (durationInHours <=0) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Duration must be a positive number of hours.");
                return;
            }
            int durationInSeconds = durationInHours * 3600;
            LocalDateTime appointmentDateTime = selectedDate.atTime(9, 0);

            List<Integer> elderIds = selectedElders.stream()
                    .map(Elder::getElderID)
                    .collect(Collectors.toList());

            List<CaregiverService> caregiverServices = selectedServices.stream()
                    .map(service -> new CaregiverService(service.getServiceID(), selectedCaregiver.getCaregiverID()))
                    .toList();

            payment = paymentController.getPaymentByAllServices(appointment, caregiverServices, selectedServices);

            appointment.setAppointmentDate(appointmentDateTime);
            appointment.setStatus(Appointment.AppointmentStatus.UNPAID);
            appointment.setDuration(durationInSeconds);
            appointment.setCreatedDate(LocalDateTime.now());
            appointment.setCaregiverID(selectedCaregiver.getCaregiverID());
            appointment.setGuardianID(guardian.getGuardianID());
            appointment.setElderIDs(elderIds);

            if (payment.getPaymentID() == 0) {
                paymentController.addPayment(payment);
            }
            appointment.setPaymentID(payment.getPaymentID());


            appointmentController.addAppointment(appointment);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment submitted successfully!");

            Stage stage = (Stage) scene.getWindow();
            GuardianAppointmentView guardianAppointmentView = new GuardianAppointmentView(stage, conn, guardian);
            stage.setScene(guardianAppointmentView.getScene());


        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Duration must be a valid number.");
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Submission Error", "An unexpected error occurred: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private int getAge(LocalDate birthDate) {
        if (birthDate == null) return 0;
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(scene.getWindow());
        alert.showAndWait();
    }

    private ComboBox<String> createRoundedComboBox(String prompt) {
        ComboBox<String> cb = new ComboBox<>();
        cb.setPromptText(prompt);
        cb.setStyle(getInputFieldStyle());
        return cb;
    }

    private String getInputFieldStyle() {
        return """
                -fx-background-radius: 10;
                -fx-border-radius: 10;
                -fx-background-color: #ffffff;
                -fx-border-color: #bdc3c7;
                -fx-font-size: 13px;
                -fx-padding: 8 12;
                """;
    }

    private Button createStyledButton(String text, String baseColor, String hoverColor) {
        Button btn = new Button(text);
        String baseStyle = String.format("-fx-background-color: %s; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 10 25; -fx-cursor: hand;", baseColor);
        String hoverStyle = String.format("-fx-background-color: %s; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 10 25; -fx-cursor: hand;", hoverColor);

        btn.setStyle(baseStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));
        return btn;
    }

    public Scene getScene() {
        return scene;
    }
}
