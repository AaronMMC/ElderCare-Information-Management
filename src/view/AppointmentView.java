package view;

import controller.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.*;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AppointmentView {

    private final Scene scene;
    private final List<Service> selectedServices = new ArrayList<>();
    private final List<Elder> selectedElders = new ArrayList<>();
    private final ComboBox<Caregiver> caregiverDropdown = new ComboBox<>();

    private final Connection conn;
    private final AppointmentController appointmentController;
    private final CaregiverController caregiverController;
    private final GuardianController guardianController;
    private final ElderController elderController;
    private final GuardianElderController guardianElderController;
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
        this.guardianElderController = new GuardianElderController(conn);
        this.serviceController = new ServiceController(conn);
        this.paymentController = new PaymentController(conn);

        Label titleLabel = new Label("Submit an Appointment");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold;");

        VBox elderBox = createElderCheckboxSection(guardian);
        Label selectEldersLabel = new Label("Select Elders:");
        selectEldersLabel.setStyle("-fx-font-weight: bold;");
        VBox elderSelection = new VBox(10, selectEldersLabel, elderBox);

        Label caregiverLabel = new Label("Select a Caregiver:");
        caregiverLabel.setStyle("-fx-font-weight: bold;");
        caregiverDropdown.setPromptText("Select a caregiver");
        caregiverDropdown.setStyle(getInputFieldStyle());

        caregiverController.getAllCaregivers().forEach(caregiver -> caregiverDropdown.getItems().add(caregiver));

        caregiverDropdown.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(model.Caregiver caregiver, boolean empty) {
                super.updateItem(caregiver, empty);
                setText(empty || caregiver == null ? null : caregiver.getFirstName() + " " + caregiver.getLastName());
            }
        });
        caregiverDropdown.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(model.Caregiver caregiver, boolean empty) {
                super.updateItem(caregiver, empty);
                setText(empty || caregiver == null ? null : caregiver.getFirstName() + " " + caregiver.getLastName());
            }
        });

        Label certsLabel = new Label("Certifications:");
        certsLabel.setStyle("-fx-font-weight: bold;");
        VBox certBox = new VBox();
        certBox.setStyle("-fx-background-color: #D9D9D9; -fx-background-radius: 20;");
        certBox.setPadding(new Insets(10));
        certBox.setPrefHeight(150);
        ScrollPane certScrollPane = new ScrollPane(certBox);
        certScrollPane.setFitToWidth(true);
        certScrollPane.setPrefHeight(150);

        VBox serviceBox = createRoundedSection("(Checkbox)");
        Label serviceLabel = new Label("Service to avail:");
        Label filterLabel = new Label("Filter by:");
        ComboBox<String> filterDropdown = createRoundedComboBox("(Dropdown)");
        HBox filterBox = new HBox(10, filterLabel, filterDropdown);
        filterBox.setAlignment(Pos.CENTER_LEFT);

        Label infoLabel = new Label("More info:");
        infoLabel.setStyle("-fx-font-weight: bold;");
        VBox infoBox = new VBox();
        infoBox.setStyle("-fx-background-color: #D9D9D9; -fx-background-radius: 20;");
        infoBox.setPadding(new Insets(10));
        infoBox.setPrefHeight(100);

        caregiverDropdown.setOnAction(e -> {
            certBox.getChildren().clear();
            infoBox.getChildren().clear();
            model.Caregiver selected = caregiverDropdown.getValue();
            if (selected != null) {
                int count = 0;
                for (String cert : selected.getCertifications()) {
                    if (count >= 5) break;
                    Label certLabel = new Label("• " + cert);
                    certBox.getChildren().add(certLabel);
                    count++;
                }

                int age = getAge(selected.getDateOfBirth().toLocalDate());
                infoBox.getChildren().addAll(
                        new Label("Age: " + age),
                        new Label("Gender: " + selected.getGender()),
                        new Label("Employment: " + selected.getEmploymentType()),
                        new Label("Contact: " + selected.getContactNumber()),
                        new Label("Email: " + selected.getEmail())
                );

                List<Service> allServices = serviceController.getAllServicesByCaregiverId(selected.getCaregiverID());

                Set<String> categories = allServices.stream()
                        .map(Service::getCategory)
                        .collect(Collectors.toCollection(TreeSet::new));
                categories.add("All Categories");

                filterDropdown.getItems().setAll(categories);
                filterDropdown.setValue("All Categories");

                populateServiceCheckboxes(serviceBox, allServices, "All Categories", selectedServices);

                filterDropdown.setOnAction(f -> {
                    String selectedCategory = filterDropdown.getValue();
                    populateServiceCheckboxes(serviceBox, allServices, selectedCategory, selectedServices);
                });
            }
        });

        VBox serviceSection = new VBox(10, serviceLabel, filterBox, serviceBox);

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("(Date)");
        datePicker.setStyle(getInputFieldStyle());

        ComboBox<String> durationBox = createRoundedComboBox("Duration (hrs)");
        durationBox.getItems().addAll("1", "2", "3", "4", "5", "6", "8", "12");
        durationBox.setEditable(true);

        HBox dateTimeBox = new HBox(20, datePicker, durationBox);
        dateTimeBox.setAlignment(Pos.CENTER_LEFT);

        Label amountLabel = new Label("Amount To Be Paid: 0.00");
        VBox centerBox = new VBox(15, serviceSection, new Label("Choose a date & time:"), dateTimeBox, amountLabel);

        Button cancelBtn = createMainButton("Cancel");
        cancelBtn.setOnAction(e -> {
            GuardianAppointmentView guardianAppointmentView = new GuardianAppointmentView(stage,conn,guardian);
            stage.setScene(guardianAppointmentView.getScene());
        });

        Button submitBtn = createMainButton("Submit");
        submitBtn.setOnAction(e -> {
            LocalDate selectedDate = datePicker.getValue();
            String durationText = durationBox.getValue();
            Caregiver selectedCaregiver = caregiverDropdown.getValue();

            if (selectedDate == null || durationText == null || durationText.trim().isEmpty() || selectedCaregiver == null || selectedElders.isEmpty() || selectedServices.isEmpty()) {
                showAlert("Please fill in all fields: select elder(s), caregiver, service(s), date, and duration.");
                return;
            }

            try {
                int durationInHours = Integer.parseInt(durationText);
                int durationInSeconds = durationInHours * 3600;
                LocalDateTime appointmentDateTime = selectedDate.atTime(9, 0);

                List<Integer> elderIds = selectedElders.stream()
                        .map(Elder::getElderID)
                        .collect(Collectors.toList());

                List<CaregiverService> caregiverServices = selectedServices.stream()
                        .map(service -> new CaregiverService(service.getServiceID(), selectedCaregiver.getCaregiverID()))
                        .toList();

                payment = paymentController.getPaymentByAllServices(appointment, caregiverServices, selectedServices);
                amountLabel.setText("Amount To Be Paid: " + String.format("%.2f", payment.getTotalAmount()));


                appointment.setAppointmentDate(appointmentDateTime);
                appointment.setStatus(Appointment.AppointmentStatus.UNPAID);
                appointment.setDuration(durationInSeconds);
                appointment.setCreatedDate(LocalDateTime.now());
                appointment.setCaregiverID(selectedCaregiver.getCaregiverID());
                appointment.setGuardianID(guardian.getGuardianID());
                appointment.setElderIDs(elderIds);
                appointment.setPaymentID(payment.getPaymentID());

                appointmentController.addAppointment(appointment);
                showAlert("Appointment submitted successfully!");


            } catch (NumberFormatException ex) {
                showAlert("Duration must be a number.");
            }
        });

        HBox actionButtons = new HBox(20, cancelBtn, submitBtn);
        actionButtons.setAlignment(Pos.CENTER_LEFT);

        VBox leftPane = new VBox(20, titleLabel, elderSelection, centerBox, actionButtons);
        leftPane.setPadding(new Insets(20));
        leftPane.setPrefWidth(800);

        VBox rightPane = new VBox(20, caregiverLabel, caregiverDropdown, certsLabel, certBox, infoLabel, infoBox);
        rightPane.setPadding(new Insets(30));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setPrefWidth(300);

        HBox root = new HBox(30, leftPane, rightPane);
        root.setPadding(new Insets(20));

        this.scene = new Scene(root, 1200, 650);
        stage.setTitle("Submit Appointment");
        stage.setScene(scene);
        stage.show();
    }

    private void populateServiceCheckboxes(VBox container, List<Service> services, String categoryFilter, List<Service> localSelectedServices) {
        container.getChildren().clear();
        localSelectedServices.clear();
        for (Service service : services) {
            if (!"All Categories".equals(categoryFilter) && !service.getCategory().equals(categoryFilter)) {
                continue;
            }

            CheckBox cb = new CheckBox(service.getServiceName() + " - " + String.format("%.2f",service.getPrice()));
            cb.setWrapText(true);
            cb.setOnAction(e -> {
                if (cb.isSelected()) {
                    localSelectedServices.add(service);
                } else {
                    localSelectedServices.remove(service);
                }
            });
            container.getChildren().add(cb);
        }
    }

    private VBox createElderCheckboxSection(Guardian guardian) {
        VBox elderListBox = new VBox(10);
        elderListBox.setPadding(new Insets(10));

        guardianElderController.getAllEldersByGuardianId(guardian.getGuardianID()).forEach(elder -> {
            CheckBox cb = new CheckBox(elder.getFirstName() + " " + elder.getLastName());
            cb.setUserData(elder);
            cb.setOnAction(e -> {
                if (cb.isSelected()) selectedElders.add(elder);
                else selectedElders.remove(elder);
            });
            elderListBox.getChildren().add(cb);
        });

        ScrollPane scrollPane = new ScrollPane(elderListBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200);
        scrollPane.setStyle("-fx-background-radius: 20; -fx-background-color: transparent;");

        VBox wrapper = new VBox(scrollPane);
        wrapper.setStyle("-fx-background-color: #D9D9D9; -fx-background-radius: 20;");
        wrapper.setPadding(new Insets(10));
        wrapper.setPrefWidth(250);
        return wrapper;
    }

    private int getAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    private void showAlert(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, s, ButtonType.OK);
        alert.showAndWait();
    }

    private VBox createRoundedSection(String placeholderText) {
        Label label = new Label(placeholderText);
        label.setStyle("-fx-font-style: italic;");
        VBox box = new VBox(label);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: #D9D9D9; -fx-background-radius: 20;");
        box.setPadding(new Insets(40));
        box.setPrefWidth(250);
        return box;
    }

    private ComboBox<String> createRoundedComboBox(String prompt) {
        ComboBox<String> cb = new ComboBox<>();
        cb.setPromptText(prompt);
        cb.setStyle(getInputFieldStyle());
        return cb;
    }

    private String getInputFieldStyle() {
        return """
            -fx-background-radius: 15;
            -fx-border-radius: 15;
            -fx-background-color: #D9D9D9;
            -fx-border-color: transparent;
            -fx-padding: 8 12;
        """;
    }

    private Button createMainButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("""
            -fx-background-color: #3BB49C;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-background-radius: 20;
            -fx-padding: 10 30;
            -fx-cursor: hand;
        """);
        return btn;
    }

    public Scene getScene() {
        return scene;
    }
}