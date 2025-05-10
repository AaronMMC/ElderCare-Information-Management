package view;

import controller.LoginController;
import dao.CaregiverDAO;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Admin;
import model.Caregiver;

import java.awt.Desktop;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.List;
import java.util.Base64;

public class AdminView {

    private final Admin admin;
    private final CaregiverDAO caregiverDAO;
    private final Stage primaryStage;
    private final Connection conn;

    private BorderPane mainLayout; // The root layout for the single window
    private TableView<Caregiver> caregiverTable;
    private final ObservableList<Caregiver> caregiverData = FXCollections.observableArrayList();
    private Node manageCaregiversViewNode; // Cached node for the caregiver table view

    public AdminView(Stage parent, Connection conn, Admin admin) {
        this.admin = admin;
        this.caregiverDAO = new CaregiverDAO(conn);
        this.primaryStage = parent;
        this.conn = conn;
        // Initialize caregiverTable here to be reused
        this.caregiverTable = new TableView<>();
        setupCaregiverTableColumns();
    }

    public Scene createAdminScene() {
        mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(10));

        // Top Navigation Bar
        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 0, 10, 0));
        Label titleApp = new Label("Admin Dashboard");
        titleApp.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button showCaregiversButton = new Button("Manage Caregivers");
        showCaregiversButton.setOnAction(e -> mainLayout.setCenter(getManageCaregiversNode()));

        Region spacer = new Region(); // Pushes logout to the right
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> logout());

        topBar.getChildren().addAll(titleApp, new Label(" :: "), showCaregiversButton, spacer, logoutButton);
        mainLayout.setTop(topBar);

        // Set initial view in the center
        mainLayout.setCenter(getManageCaregiversNode()); // Default to caregiver management

        return new Scene(mainLayout, 950, 700); // Adjusted size
    }

    // --- Node creation for Manage Caregivers View ---
    private Node getManageCaregiversNode() {
        if (manageCaregiversViewNode == null) {
            manageCaregiversViewNode = createManageCaregiversNodeInternal();
        }
        loadCaregiverData(); // Always refresh data when showing this view
        return manageCaregiversViewNode;
    }

    private Node createManageCaregiversNodeInternal() {
        BorderPane caregiverManagementLayout = new BorderPane();
        caregiverManagementLayout.setPadding(new Insets(10));

        // Center: TableView (already initialized and configured in constructor)
        caregiverManagementLayout.setCenter(caregiverTable);

        // Bottom: Buttons
        Button refreshButton = new Button("Refresh List");
        refreshButton.setOnAction(e -> loadCaregiverData());

        Button viewDetailsButton = new Button("View/Update Selected Details");
        viewDetailsButton.setOnAction(e -> {
            Caregiver selectedCaregiver = caregiverTable.getSelectionModel().getSelectedItem();
            if (selectedCaregiver != null) {
                mainLayout.setCenter(createCaregiverDetailNode(selectedCaregiver));
            } else {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a caregiver from the table.");
            }
        });
        viewDetailsButton.disableProperty().bind(caregiverTable.getSelectionModel().selectedItemProperty().isNull());

        Label caregiverAmount = new Label("Caregiver Amount" + caregiverDAO.countAllCaregivers());

        HBox buttonBox = new HBox(10, refreshButton, viewDetailsButton, caregiverAmount);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        caregiverManagementLayout.setBottom(buttonBox);

        loadCaregiverData(); // Initial data load for the table
        return caregiverManagementLayout;
    }

    @SuppressWarnings("unchecked")
    private void setupCaregiverTableColumns() {
        TableColumn<Caregiver, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("caregiverID"));

        TableColumn<Caregiver, String> firstNameColumn = new TableColumn<>("First Name");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Caregiver, String> lastNameColumn = new TableColumn<>("Last Name");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Caregiver, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailColumn.setMinWidth(180);

        TableColumn<Caregiver, Caregiver.BackgroundCheckStatus> bgStatusColumn = new TableColumn<>("BG Check");
        bgStatusColumn.setCellValueFactory(new PropertyValueFactory<>("backgroundCheckStatus"));

        TableColumn<Caregiver, Caregiver.MedicalClearanceStatus> medStatusColumn = new TableColumn<>("Med Clearance");
        medStatusColumn.setCellValueFactory(new PropertyValueFactory<>("medicalClearanceStatus"));

        TableColumn<Caregiver, String> employmentColumn = new TableColumn<>("Employment");
        employmentColumn.setCellValueFactory(new PropertyValueFactory<>("employmentType"));

        TableColumn<Caregiver, String> certsColumn = new TableColumn<>("Certs");
        certsColumn.setCellValueFactory(cellData -> {
            int count = (cellData.getValue().getCertifications() != null) ? cellData.getValue().getCertifications().size() : 0;
            return new SimpleStringProperty(count + " file(s)");
        });

        caregiverTable.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, emailColumn, bgStatusColumn, medStatusColumn, employmentColumn, certsColumn);
        caregiverTable.setItems(caregiverData); // Link data list to table
    }

    private void loadCaregiverData() {
        try {
            List<Caregiver> caregivers = caregiverDAO.getAllCaregivers();
            caregiverData.setAll(caregivers); // Update the observable list
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Load Error", "Failed to load caregiver data: " + e.getMessage());
        }
    }

    // --- Node creation for Caregiver Detail and Update View ---
    private Node createCaregiverDetailNode(Caregiver caregiver) {
        VBox detailRootLayout = new VBox(15);
        detailRootLayout.setPadding(new Insets(20));
        detailRootLayout.setAlignment(Pos.TOP_LEFT);

        // Back Button
        Button backButton = new Button("← Back to List");
        backButton.setOnAction(e -> mainLayout.setCenter(getManageCaregiversNode()));
        HBox backButtonBox = new HBox(backButton);
        backButtonBox.setPadding(new Insets(0,0,10,0));

        // Display caregiver details
        Label nameLabel = new Label("Name: " + caregiver.getFirstName() + " " + caregiver.getLastName());
        nameLabel.setStyle("-fx-font-weight: bold;");
        Label emailLabel = new Label("Email: " + caregiver.getEmail());
        Label employmentLabel = new Label("Employment Type: " + caregiver.getEmploymentType());

        // Status Update Section
        GridPane statusGrid = new GridPane();
        statusGrid.setHgap(10);
        statusGrid.setVgap(10);
        statusGrid.setPadding(new Insets(10, 0, 10, 0));

        Label bgLabel = new Label("Background Check Status:");
        ComboBox<Caregiver.BackgroundCheckStatus> bgStatusCombo = new ComboBox<>();
        bgStatusCombo.getItems().setAll(Caregiver.BackgroundCheckStatus.values());
        bgStatusCombo.setValue(caregiver.getBackgroundCheckStatus());

        Label medLabel = new Label("Medical Clearance Status:");
        ComboBox<Caregiver.MedicalClearanceStatus> medStatusCombo = new ComboBox<>();
        medStatusCombo.getItems().setAll(Caregiver.MedicalClearanceStatus.values());
        medStatusCombo.setValue(caregiver.getMedicalClearanceStatus());

        statusGrid.add(bgLabel, 0, 0);
        statusGrid.add(bgStatusCombo, 1, 0);
        statusGrid.add(medLabel, 0, 1);
        statusGrid.add(medStatusCombo, 1, 1);

        Button saveStatusButton = new Button("Save Status Changes");
        saveStatusButton.setOnAction(e -> {
            boolean changed = false;
            Caregiver.BackgroundCheckStatus newBgStatus = bgStatusCombo.getValue();
            Caregiver.MedicalClearanceStatus newMedStatus = medStatusCombo.getValue();

            if (newBgStatus != caregiver.getBackgroundCheckStatus()) {
                caregiver.setBackgroundCheckStatus(newBgStatus);
                caregiverDAO.updateCaregiverBackgroundStatus(caregiver);
                changed = true;
            }
            if (newMedStatus != caregiver.getMedicalClearanceStatus()) {
                caregiver.setMedicalClearanceStatus(newMedStatus);
                caregiverDAO.updateCaregiverMedicalClearanceStatus(caregiver);
                changed = true;
            }

            if (changed) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Statuses updated successfully.");
                // No need to close a stage, just go back to the list
                mainLayout.setCenter(getManageCaregiversNode()); // This will also trigger data reload
            } else {
                showAlert(Alert.AlertType.INFORMATION, "No Change", "No changes were made to the statuses.");
            }
        });

        // Certifications Section
        Label certsTitle = new Label("Certifications:");
        certsTitle.setStyle("-fx-font-weight: bold; -fx-padding: 10 0 5 0;");

        ListView<String> certListView = new ListView<>();
        certListView.setPrefHeight(120);
        List<String> base64CertStrings = caregiver.getCertifications();
        final int[] certIndexCounter = {0}; // For naming in list view

        if (base64CertStrings != null && !base64CertStrings.isEmpty()) {
            base64CertStrings.forEach(certStr -> {
                // Attempt to identify type for display name, very basic
                String displayCertName = "Certification " + (certIndexCounter[0] + 1);
                try {
                    byte[] tempBytes = Base64.getDecoder().decode(certStr);
                    String ext = determineFileExtension(tempBytes).toUpperCase();
                    if (!ext.equals("DAT")) {
                        displayCertName += " (." + ext + ")";
                    }
                } catch (Exception ignored) {}

                certListView.getItems().add(displayCertName);
                certIndexCounter[0]++;
            });
        } else {
            certListView.getItems().add("No certifications available.");
            certListView.setDisable(true);
        }

        Button viewCertButton = new Button("View Selected Certification");
        if (certListView.isDisabled() || certListView.getItems().isEmpty()) {
            viewCertButton.setDisable(true);
        }
        viewCertButton.setOnAction(e -> {
            int selectedIndex = certListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0 && base64CertStrings != null && selectedIndex < base64CertStrings.size()) {
                String selectedBase64 = base64CertStrings.get(selectedIndex);
                // Pass caregiver, base64 string, and the original index for naming
                handleViewCertification(caregiver, selectedBase64, selectedIndex);
            } else if (!certListView.isDisabled()) {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a certification to view.");
            }
        });

        Separator separator1 = new Separator();
        separator1.setPadding(new Insets(10,0,5,0));
        Separator separator2 = new Separator();
        separator2.setPadding(new Insets(10,0,5,0));

        detailRootLayout.getChildren().addAll(
                backButtonBox, nameLabel, emailLabel, employmentLabel,
                separator1,
                new Label("Update Statuses:"), statusGrid, saveStatusButton,
                separator2,
                certsTitle, certListView, viewCertButton
        );

        ScrollPane scrollPane = new ScrollPane(detailRootLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true); // Allow vertical scroll if content exceeds view
        return scrollPane;
    }

    // --- Certification Handling ---
    private String determineFileExtension(byte[] data) {
        if (data == null || data.length < 4) return "dat";
        if (data[0] == (byte)0x89 && data[1] == (byte)0x50 && data[2] == (byte)0x4E && data[3] == (byte)0x47) return "png";
        if (data.length >= 3 && data[0] == (byte)0xFF && data[1] == (byte)0xD8 && data[2] == (byte)0xFF) return "jpg";
        if (data[0] == (byte)0x25 && data[1] == (byte)0x50 && data[2] == (byte)0x44 && data[3] == (byte)0x46) return "pdf";
        if (data[0] == (byte)'G' && data[1] == (byte)'I' && data[2] == (byte)'F' && data[3] == (byte)'8') return "gif";
        if (data[0] == (byte)0x50 && data[1] == (byte)0x4B && data[2] == (byte)0x03 && data[3] == (byte)0x04) return "zip"; // General for PKZip archives (docx, xlsx, etc.)
        return "dat";
    }

    private void handleViewCertification(Caregiver caregiver, String base64String, int certIndex) {
        if (base64String == null || base64String.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Data", "Selected certification has no data.");
            return;
        }
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64String);
            String fileExtension = determineFileExtension(decodedBytes);

            // Sanitize names for use in filenames
            String sanitizedFirstName = caregiver.getFirstName().replaceAll("[^a-zA-Z0-9_.-]", "_");
            String sanitizedLastName = caregiver.getLastName().replaceAll("[^a-zA-Z0-9_.-]", "_");
            String fileNamePrefix = sanitizedFirstName + "_" + sanitizedLastName + "_certification_" + (certIndex + 1);

            // Attempt to display as an image first
            if (fileExtension.equals("png") || fileExtension.equals("jpg") || fileExtension.equals("gif")) {
                try {
                    Image image = new Image(new ByteArrayInputStream(decodedBytes));
                    if (!image.isError()) {
                        ImageView imageView = new ImageView(image);
                        imageView.setFitHeight(600);
                        imageView.setPreserveRatio(true);
                        StackPane imagePane = new StackPane(imageView);
                        imagePane.setPadding(new Insets(10));
                        Scene imageScene = new Scene(imagePane);
                        Stage imageStage = new Stage();
                        imageStage.setTitle(fileNamePrefix + "." + fileExtension); // Use descriptive title
                        imageStage.initOwner(this.primaryStage); // Owner is the main application stage
                        imageStage.initModality(Modality.APPLICATION_MODAL);
                        imageStage.setScene(imageScene);
                        imageStage.sizeToScene();
                        imageStage.showAndWait();
                        return;
                    }
                } catch (Exception imgEx) {
                    System.err.println("Tried to load as image based on extension '" + fileExtension + "' but failed: " + imgEx.getMessage());
                }
            }

            // If not displayed as an image, open with Desktop
            Path tempFilePath = Files.createTempFile(fileNamePrefix + "_", "." + fileExtension);
            Files.write(tempFilePath, decodedBytes);
            File tempFile = tempFilePath.toFile();

            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                Desktop.getDesktop().open(tempFile);
            } else {
                showAlert(Alert.AlertType.ERROR, "Opening Failed", "Desktop operations not supported.");
            }
            // tempFile.deleteOnExit(); // Consider this for cleanup

        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Decoding Error", "Certification data is corrupt.");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "File Error", "Could not create/write temp file: " + e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Unexpected Error", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Utility Methods ---
    private void logout() {
        Platform.runLater(() -> {
            LoginController loginController = new LoginController(primaryStage, conn);
            Scene loginScene = loginController.getLoginScene();
            primaryStage.setScene(loginScene);
            primaryStage.setTitle("Login");
            primaryStage.show();
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        if (Platform.isFxApplicationThread()) {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.initOwner(primaryStage);
            alert.showAndWait();
        } else {
            Platform.runLater(() -> {
                Alert alert = new Alert(alertType);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.initOwner(primaryStage);
                alert.showAndWait();
            });
        }
    }
}