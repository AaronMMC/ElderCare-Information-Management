package view;

import controller.LoginController;
import dao.CaregiverDAO;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Admin;
import model.Caregiver;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class AdminView {

    private final Admin admin;
    private final CaregiverDAO caregiverDAO;
    private final Scanner scanner;
    private final Stage stage;
    private final Connection conn;

    public AdminView(Stage parent, Connection conn, Admin admin) {
        this.admin = admin;
        this.caregiverDAO = new CaregiverDAO(conn);
        this.scanner = new Scanner(System.in);
        this.stage = parent;
        this.conn = conn;
    }

    public void start() {
        while (true) {
            System.out.println("\n=== Admin Panel ===");
            System.out.println("1. View All Caregivers");
            System.out.println("2. Update Caregiver");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    viewAllCaregivers();
                    break;
                case "2":
                    updateCaregiver();
                    break;
                case "3":
                    System.out.println("Logging out...");
                    logout();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void logout() {
        System.out.println("Logging out...");
        Platform.runLater(() -> {
            LoginController loginController = new LoginController(stage, conn);
            Scene loginScene = loginController.getLoginScene();
            stage.setScene(loginScene); // Now the scene will be shown again
        });
    }

    private void viewAllCaregivers() {
        List<Caregiver> caregivers = caregiverDAO.getAllCaregivers();
        System.out.println("\n--- All Caregivers ---");
        for (Caregiver cg : caregivers) {
            System.out.printf(
                    "ID: %d | Name: %s %s | Email: %s | Background Check Status: %s | Medical Clearance Status: %s | Employment: %s | Certifications: %s\n",
                    cg.getCaregiverID(),
                    cg.getFirstName(),
                    cg.getLastName(),
                    cg.getEmail(),
                    cg.isBackgroundCheckStatus() ? "Passed" : "Failed",
                    cg.isMedicalClearanceStatus() ? "Cleared" : "Not Cleared",
                    cg.getEmploymentType(),
                    cg.getCertifications()
            );
        }
    }

    private void updateCaregiver() {
        try {
            System.out.print("Enter caregiver ID to update: ");
            int id = Integer.parseInt(scanner.nextLine());

            Caregiver existing = caregiverDAO.getCaregiverById(id);
            if (existing == null) {
                System.out.println("Caregiver not found.");
                return;
            }

            // Prompt admin for status updates
            System.out.print("Update background check status (true/false) [" + existing.isBackgroundCheckStatus() + "]: ");
            String bgStatusInput = scanner.nextLine();
            if (!bgStatusInput.isEmpty()) {
                existing.setBackgroundCheckStatus(Boolean.parseBoolean(bgStatusInput));
            }

            System.out.print("Update medical clearance status (true/false) [" + existing.isMedicalClearanceStatus() + "]: ");
            String medStatusInput = scanner.nextLine();
            if (!medStatusInput.isEmpty()) {
                existing.setMedicalClearanceStatus(Boolean.parseBoolean(medStatusInput));
            }

            caregiverDAO.updateCaregiverStatus(existing);
            System.out.println("Caregiver status updated.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid caregiver ID format.");
        } catch (Exception e) {
            System.out.println("An error occurred while updating caregiver.");
            e.printStackTrace();
        }
    }
}
