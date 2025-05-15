package view;

import controller.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class AppointmentView {

    private final Scene scene;
    private final ComboBox<Caregiver> caregiverDropdown = new ComboBox<>();
    private final VBox certBox = new VBox(5);
    private final Label amountLabel = new Label("Amount To Be Paid: 0.00");
    private final VBox serviceCheckboxContainer = new VBox(10);
    private final ComboBox<String> filterDropdown = new ComboBox<>();
    private final Connection conn;
    private final AppointmentController appointmentController;
    private final CaregiverController caregiverController;
    private final CaregiverServiceController caregiverServiceController;
    private final ElderController elderController;
    private final ServiceController serviceController;
    private final PaymentController paymentController;
    private Service selectedService;
    private Elder selectedElder;
    private ComboBox<String> durationBox;
    private final Stage primaryStage;


    public AppointmentView(Stage stage, Connection conn, Guardian guardian) {
        this.primaryStage = stage;
        this.conn = conn;

        this.appointmentController = new AppointmentController(conn);
        this.caregiverController = new CaregiverController(conn);
        this.caregiverServiceController = new CaregiverServiceController(conn);
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
        updateAvailableCaregivers();
        updateCaregiverInfo();
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

        final Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        };
        datePicker.setDayCellFactory(dayCellFactory);
        datePicker.setValue(LocalDate.now());


        durationBox = createRoundedComboBox("Duration (hrs)");
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

        durationBox.valueProperty().addListener((obs, oldVal, newVal) -> updateTotalAmount());
        datePicker.valueProperty().addListener((obs, oldVal, newVal) -> updateTotalAmount());

        return leftPane;
    }

    private VBox createRightPane() {
        Label caregiverLabel = new Label("Select a Caregiver:");
        caregiverLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        caregiverLabel.setStyle("-fx-text-fill: white;");

        caregiverDropdown.setPromptText("Select a service first");
        caregiverDropdown.setStyle(getInputFieldStyle() + "-fx-pref-width: 280px;");
        caregiverDropdown.setCellFactory(lv -> new ListCell<>() {
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
        certsLabel.setStyle("-fx-text-fill: white;");

        certBox.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: #bdc3c7; -fx-border-radius: 10;");
        certBox.setPadding(new Insets(12));
        certBox.setPrefHeight(180);

        ScrollPane certScrollPane = new ScrollPane(certBox);
        certScrollPane.setFitToWidth(true);
        certScrollPane.setPrefHeight(180);
        certScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        VBox rightPane = new VBox(15, caregiverLabel, caregiverDropdown, certsLabel, certScrollPane);
        rightPane.setPadding(new Insets(30));
        rightPane.setStyle("-fx-background-color: #3BB49C; -fx-background-radius: 15;");
        rightPane.setPrefWidth(380);
        rightPane.setAlignment(Pos.TOP_CENTER);

        return rightPane;
    }

    private void loadInitialData(Guardian guardian) {
        List<Service> allServices = serviceController.getAllServices();
        Set<String> categories = allServices.stream().map(Service::getCategory).collect(Collectors.toCollection(TreeSet::new));
        filterDropdown.getItems().add("All Categories");
        filterDropdown.getItems().addAll(categories);
        filterDropdown.setValue("All Categories");

        populateServiceCheckboxes(allServices, "All Categories");

        filterDropdown.setOnAction(f -> {
            String selectedCategory = filterDropdown.getValue();
            populateServiceCheckboxes(allServices, selectedCategory);
        });
    }

    private void populateServiceCheckboxes(List<Service> services, String categoryFilter) {
        serviceCheckboxContainer.getChildren().clear();
        Service oldSelectedService = selectedService;
        selectedService = null;

        Caregiver currentCaregiver = caregiverDropdown.getValue();

        for (Service service : services) {
            if (!"All Categories".equals(categoryFilter) && !service.getCategory().equals(categoryFilter)) {
                continue;
            }

            String serviceDisplayName = service.getServiceName();
            if (currentCaregiver != null) {
                CaregiverService cs = caregiverServiceController.getCaregiverService(currentCaregiver.getCaregiverID(), service.getServiceID());
                if (cs != null) {
                    serviceDisplayName += String.format(" (Php %.2f/hr)", cs.getHourlyRate());
                } else {
                    serviceDisplayName += " (Rate N/A)";
                }
            } else {
                serviceDisplayName += " (Select caregiver for rate)";
            }

            CheckBox cb = new CheckBox(serviceDisplayName);
            cb.setWrapText(true);
            cb.setUserData(service);

            if (service.equals(oldSelectedService)) {
                cb.setSelected(true);
                selectedService = service;
            }

            cb.setOnAction(e -> {
                Service newlySelectedService = (Service) cb.getUserData();
                if (cb.isSelected()) {
                    if (selectedService != null && !selectedService.equals(newlySelectedService)) {
                        for (Node node : serviceCheckboxContainer.getChildren()) {
                            if (node instanceof CheckBox otherCb && node != cb) {
                                if (otherCb.getUserData().equals(selectedService)) {
                                    otherCb.setSelected(false);
                                    break;
                                }
                            }
                        }
                    }
                    selectedService = newlySelectedService;
                } else {
                    if (selectedService != null && selectedService.equals(newlySelectedService)) {
                        selectedService = null;
                    }
                }
                updateAvailableCaregivers();
            });
            serviceCheckboxContainer.getChildren().add(cb);
        }
        if (selectedService == null) {
            updateAvailableCaregivers();
        }
        updateTotalAmount();
    }


    private void updateAvailableCaregivers() {
        Caregiver previouslySelectedCaregiver = caregiverDropdown.getValue();
        caregiverDropdown.getItems().clear();

        if (selectedService != null) {
            List<Caregiver> availableCaregivers = caregiverController.getAllCaregiversByService(selectedService);
            if (availableCaregivers.isEmpty()) {
                caregiverDropdown.setPromptText("No caregivers for this service");
                if (previouslySelectedCaregiver != null) {
                    caregiverDropdown.setValue(null);
                } else {
                    updateCaregiverInfo();
                }
            } else {
                caregiverDropdown.getItems().addAll(availableCaregivers);
                caregiverDropdown.setPromptText("Select a caregiver");
                if (previouslySelectedCaregiver != null && availableCaregivers.contains(previouslySelectedCaregiver)) {
                    caregiverDropdown.setValue(previouslySelectedCaregiver);
                } else {
                    caregiverDropdown.setValue(null);
                }
            }
        } else {
            caregiverDropdown.setPromptText("Select a service first");
            if (previouslySelectedCaregiver != null) {
                caregiverDropdown.setValue(null);
            } else {
                updateCaregiverInfo();
            }
        }
    }

    private void updateTotalAmount() {
        double totalAmountValue = 0.0;
        int durationValue = 0;

        if (durationBox.getValue() != null && !durationBox.getValue().trim().isEmpty()) {
            try {
                durationValue = Integer.parseInt(durationBox.getValue().trim());
                if (durationValue <= 0) {
                    amountLabel.setText("Duration must be positive");
                    return;
                }
            } catch (NumberFormatException e) {
                amountLabel.setText("Invalid duration format");
                return;
            }
        } else {
            amountLabel.setText("Amount To Be Paid: 0.00");
            return;
        }

        Caregiver currentCaregiver = caregiverDropdown.getValue();
        if (selectedService != null && currentCaregiver != null) {
            if (durationValue < selectedService.getMinimumHourDuration()) {
                amountLabel.setText("Min. duration: " + selectedService.getMinimumHourDuration() + "hr(s)");
                return;
            }
            CaregiverService cs = caregiverServiceController.getCaregiverService(currentCaregiver.getCaregiverID(), selectedService.getServiceID());

            if (cs != null) {
                totalAmountValue = cs.getHourlyRate() * durationValue;
                amountLabel.setText(String.format("Amount To Be Paid: %.2f", totalAmountValue));
            } else {
                amountLabel.setText("Rate N/A for this combination");
            }
        } else {
            amountLabel.setText("Amount To Be Paid: 0.00");
        }
    }

    private String determineFileExtension(byte[] data) {
        if (data == null || data.length < 4) return "dat";
        if (data[0] == (byte) 0x89 && data[1] == (byte) 0x50 && data[2] == (byte) 0x4E && data[3] == (byte) 0x47)
            return "png";
        if (data.length >= 3 && data[0] == (byte) 0xFF && data[1] == (byte) 0xD8 && data[2] == (byte) 0xFF)
            return "jpg";
        if (data[0] == (byte) 0x25 && data[1] == (byte) 0x50 && data[2] == (byte) 0x44 && data[3] == (byte) 0x46)
            return "pdf";
        if (data[0] == (byte) 'G' && data[1] == (byte) 'I' && data[2] == (byte) 'F' && data[3] == (byte) '8')
            return "gif";
        if (data[0] == (byte) 0x50 && data[1] == (byte) 0x4B && data[2] == (byte) 0x03 && data[3] == (byte) 0x04)
            return "zip";
        return "dat";
    }

    private void refreshServiceDisplayRates() {
        Caregiver currentCaregiver = caregiverDropdown.getValue();
        for (Node node : serviceCheckboxContainer.getChildren()) {
            if (node instanceof CheckBox cb) {
                Service service = (Service) cb.getUserData();
                if (service == null) continue;

                String serviceDisplayName = service.getServiceName();
                if (currentCaregiver != null) {
                    CaregiverService cs = caregiverServiceController.getCaregiverService(currentCaregiver.getCaregiverID(), service.getServiceID());
                    if (cs != null) {
                        serviceDisplayName += String.format(" (Php %.2f/hr)", cs.getHourlyRate());
                    } else {
                        serviceDisplayName += " (Rate N/A)";
                    }
                } else {
                    serviceDisplayName += " (Select caregiver for rate)";
                }
                cb.setText(serviceDisplayName);
            }
        }
    }

    private void updateCaregiverInfo() {
        certBox.getChildren().clear();
        Caregiver selectedCaregiver = caregiverDropdown.getValue();

        if (selectedCaregiver != null) {
            List<String> certStrings = selectedCaregiver.getCertifications();

            if (certStrings == null || certStrings.isEmpty()) {
                Label messageLabel = new Label("No certifications listed for this caregiver.");
                messageLabel.setStyle("-fx-text-fill: #555555; -fx-font-style: italic; -fx-padding: 5px;");
                certBox.getChildren().add(messageLabel);
            } else {
                boolean contentAdded = false;
                int certIndex = 1;
                for (String certStr : certStrings) {
                    if (certStr != null && !certStr.trim().isEmpty()) {
                        try {
                            byte[] decodedBytes = Base64.getDecoder().decode(certStr);
                            String extension = determineFileExtension(decodedBytes).toLowerCase();

                            String certDisplayNameText = "• View Certification " + certIndex;
                            if (!"dat".equals(extension)) {
                                certDisplayNameText += " (." + extension + ")";
                            } else {
                                certDisplayNameText += " (File)";
                            }

                            Hyperlink certLink = new Hyperlink(certDisplayNameText);
                            certLink.setWrapText(true);
                            certLink.setStyle("-fx-text-fill: #0000EE; -fx-padding: 2px 0;");

                            final String currentCertStr = certStr;
                            final int currentIndex = certIndex - 1;

                            certLink.setOnAction(event -> {

                                viewCertificationFile(selectedCaregiver, currentCertStr, currentIndex, extension);
                            });

                            certBox.getChildren().add(certLink);
                            contentAdded = true;
                        } catch (IllegalArgumentException e) {
                            Label errorLabel = new Label("• Certification " + certIndex + " (Error processing data)");
                            errorLabel.setStyle("-fx-text-fill: #D32F2F; -fx-font-style: italic; -fx-padding: 2px 0;");
                            certBox.getChildren().add(errorLabel);
                            contentAdded = true;
                        }
                        certIndex++;
                    }
                }
                if (!contentAdded) {
                    Label messageLabel = new Label("Certification entries are present but contain no displayable data.");
                    messageLabel.setStyle("-fx-text-fill: #555555; -fx-font-style: italic; -fx-padding: 5px;");
                    certBox.getChildren().add(messageLabel);
                }
            }
        } else {
            Label messageLabel = new Label("No caregiver selected.");
            if (selectedService == null) {
                messageLabel.setText("Select a service and caregiver.");
            }
            messageLabel.setStyle("-fx-text-fill: #555555; -fx-font-style: italic; -fx-padding: 5px;");
            certBox.getChildren().add(messageLabel);
        }
        refreshServiceDisplayRates();
        updateTotalAmount();
    }

    private void viewCertificationFile(Caregiver caregiver, String base64String, int certListIndex, String identifiedExtension) {
        if (base64String == null || base64String.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Data", "Selected certification has no data.");
            return;
        }
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64String);

            String fileExtension = (identifiedExtension != null && !identifiedExtension.isEmpty()) ? identifiedExtension : determineFileExtension(decodedBytes);


            String sanitizedFirstName = caregiver.getFirstName().replaceAll("[^a-zA-Z0-9_.-]", "_");
            String sanitizedLastName = caregiver.getLastName().replaceAll("[^a-zA-Z0-9_.-]", "_");
            String fileNamePrefix = sanitizedFirstName + "_" + sanitizedLastName + "_certification_" + (certListIndex + 1);


            if (fileExtension.equalsIgnoreCase("png") || fileExtension.equalsIgnoreCase("jpg") || fileExtension.equalsIgnoreCase("jpeg") || fileExtension.equalsIgnoreCase("gif")) {
                try {
                    Image image = new Image(new ByteArrayInputStream(decodedBytes));
                    if (!image.isError()) {
                        ImageView imageView = new ImageView(image);
                        imageView.setPreserveRatio(true);

                        double maxWidth = 600;
                        double maxHeight = 400;
                        if (image.getWidth() > maxWidth || image.getHeight() > maxHeight) {
                            imageView.setFitWidth(maxWidth);
                            imageView.setFitHeight(maxHeight);
                        }

                        StackPane imagePane = new StackPane(imageView);
                        imagePane.setPadding(new Insets(10));
                        Scene imageScene = new Scene(imagePane);
                        Stage imageStage = new Stage();
                        imageStage.setTitle(fileNamePrefix + "." + fileExtension);
                        imageStage.initOwner(this.primaryStage);
                        imageStage.initModality(Modality.APPLICATION_MODAL);
                        imageStage.setScene(imageScene);
                        imageStage.sizeToScene();
                        imageStage.showAndWait();
                        return;
                    }
                } catch (Exception imgEx) {
                    System.err.println("Could not display certification as image directly: " + imgEx.getMessage());

                }
            }


            Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "eldercare_certs_guardian");
            if (!Files.exists(tempDir)) {
                Files.createDirectories(tempDir);
            }


            Path tempFilePath = Files.createTempFile(tempDir, fileNamePrefix + "_", "." + fileExtension);
            Files.write(tempFilePath, decodedBytes);
            File tempFile = tempFilePath.toFile();
            tempFile.deleteOnExit();

            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                Desktop.getDesktop().open(tempFile);
            } else {
                showAlert(Alert.AlertType.ERROR, "Opening Failed", "Desktop operations not supported to open the file type: " + fileExtension);
            }

        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Decoding Error", "Certification data appears to be corrupt or not valid Base64.");
            e.printStackTrace();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "File Error", "Could not create, write, or open temporary file: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Unexpected Error", "An error occurred while trying to view the file: " + e.getMessage());
            e.printStackTrace();
        }
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
            for (Elder elder : elders) {
                CheckBox cb = new CheckBox(elder.getFirstName() + " " + elder.getLastName());
                cb.setUserData(elder);
                cb.setFont(Font.font("Arial", 13));
                cb.setOnAction(e -> {
                    if (cb.isSelected()) {
                        if (selectedElder != null && selectedElder != elder) {
                            for (Node node : elderListBox.getChildren()) {
                                if (node instanceof CheckBox otherCb) {
                                    if (otherCb.getUserData() == selectedElder) {
                                        otherCb.setSelected(false);
                                        break;
                                    }
                                }
                            }
                        }
                        selectedElder = elder;
                    } else {
                        if (selectedElder == elder) {
                            selectedElder = null;
                        }
                    }
                });
                elderListBox.getChildren().add(cb);
            }
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
        LocalDate selectedDateValue = datePicker.getValue();
        String durationText = this.durationBox.getValue();
        Caregiver currentSelectedCaregiver = caregiverDropdown.getValue();

        if (selectedElder == null) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please select an elder.");
            return;
        }
        if (selectedService == null) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please select a service.");
            return;
        }
        if (selectedDateValue == null) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please select an appointment date.");
            return;
        }
        if (durationText == null || durationText.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please specify the duration.");
            return;
        }
        if (currentSelectedCaregiver == null) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please select a caregiver.");
            return;
        }

        int durationInHours;
        try {
            durationInHours = Integer.parseInt(durationText.trim());
            if (durationInHours <= 0) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Duration must be a positive number.");
                return;
            }
            if (durationInHours < selectedService.getMinimumHourDuration()) {
                showAlert(Alert.AlertType.WARNING, "Invalid Duration", "Duration must be at least " + selectedService.getMinimumHourDuration() + " hour(s) for " + selectedService.getServiceName() + ".");
                return;
            }
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Duration must be a valid number.");
            return;
        }

        LocalDateTime appointmentDateTime = selectedDateValue.atTime(9, 0);

        CaregiverService cs = caregiverServiceController.getCaregiverService(currentSelectedCaregiver.getCaregiverID(), selectedService.getServiceID());

        if (cs == null) {
            showAlert(Alert.AlertType.ERROR, "Rate Error", "Could not determine the hourly rate for the selected caregiver and service. Please ensure the caregiver offers this service at a set rate.");
            return;
        }
        double totalAmountCalculated = cs.getHourlyRate() * durationInHours;

        try {
            Appointment appointment = new Appointment(appointmentDateTime, Appointment.AppointmentStatus.PENDING, durationInHours, currentSelectedCaregiver.getCaregiverID(), selectedElder.getElderID(), selectedService.getServiceID(), totalAmountCalculated);
            int appointmentId = appointmentController.addAppointment(appointment);
            if (appointmentId > 0) {
                showAlert(Alert.AlertType.CONFIRMATION, "Success", "Appointment submitted successfully! ID: " + appointmentId);
            } else {
                showAlert(Alert.AlertType.ERROR, "Submission Error", "Failed to submit appointment. No ID was returned from the database operation.");
            }
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Submission Error", "An unexpected error occurred: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        if (primaryStage != null) {
            alert.initOwner(primaryStage);
        }
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