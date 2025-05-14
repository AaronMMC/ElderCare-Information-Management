package view;

import controller.CaregiverController;
import controller.CaregiverServiceController;
import controller.ServiceController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Caregiver;
import model.CaregiverService;
import model.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CaregiverServiceView {

    private final Scene scene;
    private final ObservableList<Service> allServices = FXCollections.observableArrayList();
    private final TextField searchField = new TextField();
    private final ComboBox<String> sortBox = new ComboBox<>();
    private final VBox serviceListContainer = new VBox(20);

    private final Connection conn;
    private final Caregiver caregiver;
    private final CaregiverController caregiverController;
    private final ServiceController serviceController;
    private final CaregiverServiceController caregiverServiceController;

    public CaregiverServiceView(Stage stage, Connection conn, Caregiver caregiver) throws SQLException {
        this.conn = conn;
        this.caregiver = caregiver;
        this.caregiverController = new CaregiverController(conn);
        this.serviceController = new ServiceController(conn);
        this.caregiverServiceController = new CaregiverServiceController(conn);

        Label titleLabel = new Label("Your Services");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold;");

        searchField.setPromptText("Search by Service Name");
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            refreshServiceList(conn, caregiver, caregiverServiceController);
        });

        sortBox.getItems().addAll("ASCENDING", "DESCENDING", "DEFAULT");
        sortBox.setValue("DEFAULT");
        sortBox.setOnAction(e -> {
            refreshServiceList(conn, caregiver, caregiverServiceController);
        });

        HBox filterBar = new HBox(10, searchField, sortBox);
        filterBar.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(serviceListContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);

        VBox leftPane = new VBox(20, titleLabel, filterBar, scrollPane);
        leftPane.setPadding(new Insets(20));
        leftPane.setPrefWidth(800);

        Button goBackBtn = createSidebarButton("Go Back");
        Button addService = createSidebarButton("Add Service");


        goBackBtn.setOnAction(e -> {
            CaregiverView caregiverView = new CaregiverView(stage, conn, caregiver);
            stage.setScene(caregiverView.getScene());
        });

        addService.setOnAction(e -> {
            showAddServicePanel();
        });

        VBox rightPane = new VBox(30);
        rightPane.setPadding(new Insets(30));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setAlignment(Pos.TOP_CENTER);
        rightPane.setPrefWidth(250);

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        rightPane.getChildren().addAll(addService, spacer, goBackBtn);

        Button cancelButton = createBigGreenButton("Cancel");
        Button saveButton = createBigGreenButton("Save Changes");

        cancelButton.setOnAction(e -> {
            CaregiverView caregiverView = new CaregiverView(stage,conn,caregiver);
            stage.setScene(caregiverView.getScene());
        });

        saveButton.setOnAction(e -> {
            showAddServicePanel();
        });

        HBox bottomButtons = new HBox(30, cancelButton, saveButton);
        bottomButtons.setAlignment(Pos.CENTER);
        bottomButtons.setPadding(new Insets(20));

        VBox leftWithBottom = new VBox(20, leftPane, bottomButtons);

        HBox root = new HBox(20, leftWithBottom, rightPane);
        root.setPadding(new Insets(20));

        this.scene = new Scene(root, 1100, 650);
        stage.setTitle("Services");
        stage.setScene(scene);
        stage.show();

        refreshServiceList(conn, caregiver, caregiverServiceController);
    }

    private void showAddServicePanel() {
        List<Service> availableServices = serviceController.getAllServices();

        Stage serviceStage = new Stage();
        serviceStage.setTitle("Add a Service");

        // Service dropdown
        ComboBox<Service> serviceDropdown = new ComboBox<>();
        serviceDropdown.getItems().addAll(availableServices);
        serviceDropdown.setPromptText("Select a service");

        // Labels to display selected service details
        Label serviceNameLabel = new Label("Service name: ");
        Label serviceCategoryLabel = new Label("Category: ");

        // Update labels when a service is selected
        serviceDropdown.setOnAction(e -> {
            Service selectedService = serviceDropdown.getValue();
            if (selectedService != null) {
                serviceNameLabel.setText("Service name: " + selectedService.getServiceName());
                serviceCategoryLabel.setText("Category: " + selectedService.getCategory());
            }
        });

        // Experience and hourly rate input
        Label experienceYearsLabel = new Label("Experience in years: ");
        TextField experienceYearsField = new TextField();
        experienceYearsField.setPromptText("e.g., 2");

        Label hourlyRateLabel = new Label("Rate per hour (Php): ");
        TextField hourlyRateField = new TextField();
        hourlyRateField.setPromptText("e.g., 300");

        Button add = new Button("Add");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        VBox layout = new VBox(10,
                serviceDropdown,
                serviceNameLabel,
                serviceCategoryLabel,
                experienceYearsLabel,
                experienceYearsField,
                hourlyRateLabel,
                hourlyRateField,
                errorLabel,
                add
        );

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 350, 400);
        serviceStage.setScene(scene);
        serviceStage.show();

        add.setOnAction(e -> {
            System.out.println("The add service button set on action is triggered ");
            Service selectedService = serviceDropdown.getValue();
            if (selectedService == null) {
                errorLabel.setText("Please select a service.");
                return;
            }

            try {
                int experienceYears = Integer.parseInt(experienceYearsField.getText());
                double hourlyRate = Double.parseDouble(hourlyRateField.getText());

                CaregiverService caregiverService = new CaregiverService(caregiver.getCaregiverID(), selectedService.getServiceID(), experienceYears, hourlyRate);
                caregiverServiceController.addCaregiverService(caregiverService);

                refreshServiceList(conn, caregiver, caregiverServiceController);
                serviceStage.close();
            } catch (NumberFormatException ex) {
                errorLabel.setText("Please enter valid numeric values.");
            }
        });
    }

    private void refreshServiceList(Connection conn, Caregiver caregiver, CaregiverServiceController caregiverServiceController) {
        List<Service> services = serviceController.getAllServicesByCaregiverId(caregiver.getCaregiverID());
        allServices.setAll(services);

        String searchText = searchField.getText().toLowerCase();
        List<Service> filteredServices = new ArrayList<>(allServices.stream()
                .filter(e -> (e.getServiceName().toLowerCase().contains(searchText))).toList());

        Comparator<Service> serviceComparator;
        if (sortBox.getValue().equals("DESCENDING")) {
            serviceComparator = Comparator.comparing(Service::getServiceName).reversed();
        } else {
            serviceComparator = Comparator.comparing(Service::getServiceName);
        }

        filteredServices.sort(serviceComparator);

        serviceListContainer.getChildren().clear();
        for (Service service : filteredServices) {
            serviceListContainer.getChildren().add(createServiceRow(service, caregiver, conn));
        }
    }

    private HBox createServiceRow(Service service, Caregiver caregiver, Connection conn) {
        CaregiverService caregiverService = caregiverServiceController.getCaregiverService(caregiver.getCaregiverID(),service.getServiceID());
        int experienceYears = (caregiverService != null) ? caregiverService.getExperienceYears() : 0;
        double hourlyRate = (caregiverService != null) ? caregiverService.getHourlyRate() : 0;

        Label nameLabel = new Label(service.getServiceName());
        nameLabel.setPrefWidth(150);

        TextField nameField = new TextField(service.getServiceName());
        TextField categoryField = new TextField(service.getCategory());
        TextField experienceYearsField = new TextField(String.valueOf(experienceYears));
        TextField hourlyRateField = new TextField(String.valueOf(hourlyRate));

        VBox detailBox = new VBox(5, nameField, categoryField, experienceYearsField, hourlyRateField);
        detailBox.setPrefWidth(400);

        Button editBtn = createBigGreenButton("Edit");
        Button saveBtn = createBigGreenButton("Save");
        Button cancelBtn = createBigGreenButton("Cancel");
        Button deleteBtn = createBigGreenButton("Delete");

        saveBtn.setDisable(true);
        cancelBtn.setDisable(true);

        editBtn.setOnAction(e -> {
            setEditable(true, nameField, categoryField, experienceYearsField, hourlyRateField);
            saveBtn.setDisable(false);
            cancelBtn.setDisable(false);
        });

        saveBtn.setOnAction(e -> {
            service.setServiceName(nameField.getText());
            service.setCategory(categoryField.getText());
            serviceController.updateService(service);

            CaregiverService updatedCaregiverService = new CaregiverService(caregiver.getCaregiverID(), service.getServiceID(), experienceYearsField.getLength(), hourlyRateField.getLength());
            caregiverServiceController.updateCaregiverService(updatedCaregiverService);

            refreshServiceList(conn, caregiver, caregiverServiceController);
        });

        cancelBtn.setOnAction(e -> {
            nameField.setText(service.getServiceName());
            categoryField.setText(service.getCategory());
            experienceYearsField.setText(String.valueOf(experienceYears));
            hourlyRateField.setText(String.valueOf(hourlyRate));
            setEditable(false, nameField, categoryField, experienceYearsField, hourlyRateField);
            saveBtn.setDisable(true);
            cancelBtn.setDisable(true);
        });
        deleteBtn.setOnAction(e -> {
            serviceController.deleteService(service.getServiceID());
            refreshServiceList(conn, caregiver, caregiverServiceController);
        });

        VBox buttonBox = new VBox(10, editBtn, saveBtn, cancelBtn, deleteBtn);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPrefWidth(150);

        return new HBox(20, nameLabel, detailBox, buttonBox);
    }

    private void setEditable(boolean editable, Object... controls) {
        for (Object control : controls) {
            if (control instanceof TextInputControl textField)
                textField.setEditable(editable);
        }
    }

    private Button createBigGreenButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("""
            -fx-background-color: #3BB49C;
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-font-weight: bold;
            -fx-background-radius: 20;
            -fx-cursor: hand;
            -fx-padding: 10 30;
        """);
        return btn;
    }

    private Button createSidebarButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("""
            -fx-background-color: white;
            -fx-text-fill: black;
            -fx-font-size: 14px;
            -fx-background-radius: 15;
            -fx-cursor: hand;
            -fx-padding: 10 20;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 1);
        """);
        btn.setPrefWidth(200);
        return btn;
    }

    public Scene getScene() {
        return scene;
    }
}