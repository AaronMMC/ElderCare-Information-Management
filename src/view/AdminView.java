package view;

import controller.LoginController;
import dao.CaregiverDAO;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
import java.util.Scanner;
import java.util.Base64;
import java.util.concurrent.CountDownLatch; // To wait for FX thread

// Add imports for JavaFX components you'll use in the new window

public class AdminView {

    private final Admin admin;
    private final CaregiverDAO caregiverDAO;
    private final Scanner scanner;
    private final Stage stage; // This is the main stage, likely for the login view
    private final Connection conn;

    public AdminView(Stage parent, Connection conn, Admin admin) {
        this.admin = admin;
        this.caregiverDAO = new CaregiverDAO(conn);
        this.scanner = new Scanner(System.in);
        this.stage = parent;
        this.conn = conn;
    }

    public void start() {
        // The main loop runs on the non-JavaFX thread (usually main thread)
        while (true) {
            System.out.println("\n=== Admin Panel ===");
            System.out.println("1. View All Caregivers (Console)");
            System.out.println("2. Update Caregiver (Console)");
            System.out.println("3. Logout");
            System.out.println("4. View Caregiver Details & Certifications (GUI)"); // New GUI Option
            System.out.print("Choose an option: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    viewAllCaregiversConsole(); // Renamed to indicate console output
                    break;
                case "2":
                    updateCaregiverConsole(); // Renamed to indicate console input/output
                    break;
                case "3":
                    logout();
                    return; // Exit start() method
                case "4":
                    viewCaregiverDetailsGUI(); // Call method to launch GUI
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void logout() {
        System.out.println("Logging out...");
        // Use Platform.runLater to switch back to the login view on the FX thread
        Platform.runLater(() -> {
            LoginController loginController = new LoginController(stage, conn);
            Scene loginScene = loginController.getLoginScene(); // Assuming LoginController provides the scene
            stage.setScene(loginScene);
            stage.setTitle("Login"); // Set title if needed
            stage.show(); // Make sure the main stage is visible
        });
    }

    private void viewAllCaregiversConsole() { // Renamed
        List<Caregiver> caregivers = caregiverDAO.getAllCaregivers();
        System.out.println("\n--- All Caregivers ---");
        if (caregivers.isEmpty()) {
            System.out.println("No caregivers found.");
            return;
        }
        for (Caregiver cg : caregivers) {
            // Printing Base64 here is impractical, just indicate presence
            String certStatus = (cg.getCertifications() != null && !cg.getCertifications().isEmpty())
                    ? cg.getCertifications().size() + " files(s)" : "None";
            System.out.printf(
                    "ID: %d | Name: %s %s | Email: %s | Statuses: BG=%s, Med=%s | Employment: %s | Certifications: %s\n",
                    cg.getCaregiverID(),
                    cg.getFirstName(),
                    cg.getLastName(),
                    cg.getEmail(),
                    cg.getBackgroundCheckStatus(),
                    cg.getMedicalClearanceStatus(),
                    cg.getEmploymentType(),
                    certStatus // Show count instead of raw Base64
            );
        }
    }

    private void updateCaregiverConsole() { // Renamed
        try {
            System.out.print("Enter caregiver ID to update statuses: ");
            int id = Integer.parseInt(scanner.nextLine());

            Caregiver existing = caregiverDAO.getCaregiverById(id);
            if (existing == null) {
                System.out.println("Caregiver not found.");
                return;
            }

            // Background Check Status
            System.out.println("\nUpdate Background Check Status:");
            Caregiver.BackgroundCheckStatus[] bgOptions = Caregiver.BackgroundCheckStatus.values();
            for (int i = 0; i < bgOptions.length; i++) {
                System.out.printf("%d. %s\n", i + 1, bgOptions[i]);
            }
            System.out.print("Choose an option (or press Enter to skip): ");
            String bgInput = scanner.nextLine().trim();
            if (!bgInput.isEmpty()) {
                int bgChoice = Integer.parseInt(bgInput);
                if (bgChoice >= 1 && bgChoice <= bgOptions.length) {
                    existing.setBackgroundCheckStatus(bgOptions[bgChoice - 1]);
                } else {
                    System.out.println("Invalid choice. Background check status not changed.");
                }
            }

            // Medical Clearance Status
            System.out.println("\nUpdate Medical Clearance Status:");
            Caregiver.MedicalClearanceStatus[] medOptions = Caregiver.MedicalClearanceStatus.values();
            for (int i = 0; i < medOptions.length; i++) {
                System.out.printf("%d. %s\n", i + 1, medOptions[i]);
            }
            System.out.print("Choose an option (or press Enter to skip): ");
            String medInput = scanner.nextLine().trim();
            if (!medInput.isEmpty()) {
                int medChoice = Integer.parseInt(medInput);
                if (medChoice >= 1 && medChoice <= medOptions.length) {
                    existing.setMedicalClearanceStatus(medOptions[medChoice - 1]);
                } else {
                    System.out.println("Invalid choice. Medical clearance status not changed.");
                }
            }

            // Assuming your DAO methods handle partial updates or you combine changes
            caregiverDAO.updateCaregiverBackgroundStatus(existing);
            caregiverDAO.updateCaregiverMedicalClearanceStatus(existing);
            System.out.println("\nCaregiver status updated.");

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Operation cancelled.");
        } catch (Exception e) {
            System.out.println("An error occurred while updating caregiver: " + e.getMessage());
            //e.printStackTrace(); // Keep for debugging if needed
        }
    }


    // --- GUI Methods (Run on FX Thread) ---

    private void viewCaregiverDetailsGUI() {
        System.out.print("Enter caregiver ID to view details (GUI): ");
        String idInput = scanner.nextLine();
        try {
            int caregiverId = Integer.parseInt(idInput);

            // Fetch caregiver from DB on the current thread
            Caregiver caregiverToView = caregiverDAO.getCaregiverById(caregiverId);

            if (caregiverToView == null) {
                System.out.println("Caregiver not found.");
                return; // Exit the console method
            }

            // Use Platform.runLater to create and show the GUI on the JavaFX Application Thread
            // Use CountDownLatch to pause the console thread until the GUI is ready or closed
            CountDownLatch latch = new CountDownLatch(1);

            Platform.runLater(() -> {
                try {
                    Stage detailStage = new Stage();
                    detailStage.initOwner(stage); // Optional: Set main stage as owner
                    detailStage.initModality(Modality.APPLICATION_MODAL); // Optional: Block main window
                    detailStage.setTitle("Caregiver Details: " + caregiverToView.getFirstName() + " " + caregiverToView.getLastName());

                    // --- Build the GUI Layout ---
                    VBox rootLayout = new VBox(20);
                    rootLayout.setPadding(new Insets(20));
                    rootLayout.setAlignment(Pos.TOP_LEFT);

                    Label nameLabel = new Label("Name: " + caregiverToView.getFirstName() + " " + caregiverToView.getLastName());
                    Label emailLabel = new Label("Email: " + caregiverToView.getEmail());
                    Label statusLabel = new Label("Statuses: Background=" + caregiverToView.getBackgroundCheckStatus() + ", Medical=" + caregiverToView.getMedicalClearanceStatus());
                    Label employmentLabel = new Label("Employment Type: " + caregiverToView.getEmploymentType());


                    Label certsTitle = new Label("Certifications:");
                    certsTitle.setStyle("-fx-font-weight: bold; -fx-underline: true;");

                    ListView<String> certListView = new ListView<>();
                    certListView.setPrefHeight(150); // Give it some height

                    List<String> base64CertStrings = caregiverToView.getCertifications();
                    List<String> certNames = new java.util.ArrayList<>();
                    if (base64CertStrings != null && !base64CertStrings.isEmpty()) {
                        for (int i = 0; i < base64CertStrings.size(); i++) {
                            certNames.add("Certification " + (i + 1));
                        }
                        certListView.getItems().addAll(certNames);
                    } else {
                        certListView.getItems().add("No certifications available.");
                        certListView.setDisable(true); // Disable list if empty
                    }

                    Button viewCertButton = new Button("View Selected Certification");
                    if (certListView.getItems().isEmpty() || certListView.getItems().get(0).equals("No certifications available.")) {
                        viewCertButton.setDisable(true);
                    }

                    // Add action to view button
                    viewCertButton.setOnAction(e -> {
                        int selectedIndex = certListView.getSelectionModel().getSelectedIndex();
                        if (selectedIndex >= 0 && selectedIndex < base64CertStrings.size()) {
                            String selectedBase64 = base64CertStrings.get(selectedIndex);
                            // Call the method to decode and display/open
                            handleViewCertification(selectedBase64, "caregiver_" + caregiverToView.getCaregiverID() + "_cert_" + (selectedIndex + 1));
                        } else {
                            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a certification to view.");
                        }
                    });


                    rootLayout.getChildren().addAll(nameLabel, emailLabel, statusLabel, employmentLabel, certsTitle, certListView, viewCertButton);

                    Scene detailScene = new Scene(rootLayout, 600, 500); // Adjust size as needed
                    detailStage.setScene(detailScene);

                    // Release the latch when the detail stage is closed
                    detailStage.setOnHidden(e -> latch.countDown());

                    detailStage.showAndWait(); // Show the window and wait for it to close
                    // This makes the console loop wait

                } catch (Exception e) {
                    // Handle errors during GUI creation on FX thread
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "GUI Error", "An error occurred displaying details: " + e.getMessage());
                    latch.countDown(); // Release latch even on error
                }
            });

            // Wait on the console thread until the GUI stage is closed
            try {
                latch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Admin view interrupted while waiting for GUI.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid caregiver ID input.");
        } catch (Exception e) {
            System.out.println("An error occurred while fetching caregiver details: " + e.getMessage());
            //e.printStackTrace(); // Keep for debugging if needed
        }
    }


    // Helper method to handle decoding and displaying/opening a single certification
    // This method runs on the JavaFX Application Thread
    private void handleViewCertification(String base64String, String tempFileNameBase) {
        if (base64String == null || base64String.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Data", "Selected certification has no data.");
            return;
        }

        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64String);

            // --- Attempt to display as Image ---
            try {
                // Try loading as an image. This is a common format.
                Image image = new Image(new ByteArrayInputStream(decodedBytes));
                if (!image.isError()) {
                    // It's an image! Display it in a new window.
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(600); // Set appropriate size for image viewer
                    imageView.setPreserveRatio(true);

                    StackPane imagePane = new StackPane(imageView);
                    imagePane.setPadding(new Insets(10)); // Add some padding

                    Scene imageScene = new Scene(imagePane);
                    Stage imageStage = new Stage(); // Use a new stage for the image
                    imageStage.setTitle("Certification Image");
                    imageStage.setScene(imageScene);
                    imageStage.sizeToScene(); // Adjust stage size to fit image
                    imageStage.show();
                    return; // Exit the method if successfully displayed as image

                }
                // If image.isError(), fall through to the file opening part

            } catch (IllegalArgumentException e) {
                // This specific exception might occur if bytes are not even close to image format header
                System.err.println("Attempted image decode failed: " + e.getMessage());
                // Fall through to file opening
            }


            // --- If not an image (or image decode failed), save to temp file and open ---
            System.out.println("Not an image, attempting to open as file via Desktop...");

            // Create a temporary file. Using a common extension like .tmp or none
            // might work, but knowing the original extension is best.
            // Here we just use a generic .file extension.
            Path tempFilePath = Files.createTempFile(tempFileNameBase + "_", ".file");

            try {
                Files.write(tempFilePath, decodedBytes);

                File tempFile = tempFilePath.toFile();
                // Use Desktop to open the file with the default system application (e.g., PDF reader, Word)
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                    // Desktop.open() is generally safe off the FX thread, but running it on Platform.runLater
                    // can sometimes prevent unexpected interactions depending on the OS/JRE.
                    // However, since handleViewCertification is already on the FX thread, we can call it directly.
                    try {
                        Desktop.getDesktop().open(tempFile);
                        // Optional: Add a shutdown hook to delete the temp file when the application exits
                        tempFile.deleteOnExit();
                    } catch (IOException ex) {
                        showAlert(Alert.AlertType.ERROR, "File Open Error", "Could not open the certification file: " + ex.getMessage());
                        try { Files.deleteIfExists(tempFilePath); } catch (IOException cleanupEx) {} // Clean up failed temp file
                    }

                } else {
                    showAlert(Alert.AlertType.ERROR, "Opening Failed", "Desktop operations not supported on this system.");
                    // Manually clean up if Desktop is not supported
                    try { Files.deleteIfExists(tempFilePath); } catch (IOException cleanupEx) {}
                }

            } catch (IOException fileWriteError) {
                showAlert(Alert.AlertType.ERROR, "File Save Error", "Could not save the certification file: " + fileWriteError.getMessage());
                // Clean up the failed temp file if it was created
                try { Files.deleteIfExists(tempFilePath); } catch (IOException cleanupEx) {}
            }

        } catch (IllegalArgumentException e) {
            // The Base64 string itself is malformed
            showAlert(Alert.AlertType.ERROR, "Decoding Error", "The selected certification data is corrupt (invalid Base64).");
            System.err.println("Base64 decoding failed: " + e.getMessage());
        } catch (IOException e) {
            // Error during temp file creation
            showAlert(Alert.AlertType.ERROR, "File Error", "Could not create temporary file: " + e.getMessage());
            System.err.println("Temp file creation failed: " + e.getMessage());
        } catch (Exception e) {
            // Catch any other unexpected errors during the process
            showAlert(Alert.AlertType.ERROR, "Unexpected Error", "An unexpected error occurred: " + e.getMessage());
            e.printStackTrace(); // Print full stack trace for unexpected errors
        }
    }

    // Helper to show alerts on the JavaFX Application Thread
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        // Ensure alerts are shown on the FX thread
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}