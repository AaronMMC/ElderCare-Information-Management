package controller;

import dao.AdminDAO;
import dao.CaregiverDAO;
import dao.GuardianDAO;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.Admin;
import model.Caregiver;
import model.Guardian;
import view.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class LoginController {

    private final Stage stage;
    private final Connection conn;
    private final LoginView loginView;
    private final GuardianDAO guardianDAO;
    private final CaregiverDAO caregiverDAO;
    private final AdminDAO adminDAO;

    public LoginController(Stage stage, Connection conn) {
        this.stage = stage;
        this.conn = conn;
        this.loginView = new LoginView();
        this.guardianDAO = new GuardianDAO(conn);
        this.caregiverDAO = new CaregiverDAO(conn);
        this.adminDAO = new AdminDAO(conn);

        // wire buttons
        loginView.getSignInButton().setOnAction(e -> handleLogin());
        loginView.getRegisterAsGuardianButton().setOnAction(e -> switchToGuardianRegistration());
        loginView.getRegisterAsCaregiverButton().setOnAction(e -> switchToCaregiverRegistration());
    }

    public Scene getLoginScene() {
        return new Scene(loginView.getView(), 400, 500);
    }

    private void handleLogin() {
        String username = loginView.getUsernameField().getText();
        String password = loginView.getPasswordField().getText();

        Guardian guardian = guardianDAO.findByUsernameAndPassword(username, password);
        Caregiver caregiver = caregiverDAO.findByUsernameAndPassword(username, password);
        Admin admin = adminDAO.findByUsernameAndPassword(username, password);

        List<String> matchedRoles = new ArrayList<>();
        if (guardian != null) matchedRoles.add("Guardian");
        if (caregiver != null) matchedRoles.add("Caregiver");
        if (admin != null) matchedRoles.add("Admin");

        if (matchedRoles.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Invalid username or password").showAndWait();
            return;
        }

        if (matchedRoles.size() == 1) {
            String role = matchedRoles.getFirst();
            switch (role) {
                case "Guardian":
                    GuardianView guardianView = new GuardianView(stage, conn, guardian);
                    stage.setScene(guardianView.getScene());
                    break;
                case "Caregiver":
                    CaregiverView caregiverView = new CaregiverView(stage, conn, caregiver);
                    stage.setScene(caregiverView.getScene());
                    break;
                case "Admin":
                    AdminView adminView = new AdminView(stage, conn, admin);
                    adminView.start();
                    break;
            }
        } else {
            RoleSelectionView roleSelectionView = new RoleSelectionView(stage, matchedRoles, selectedRole -> {
                switch (selectedRole) {
                    case "Guardian":
                        GuardianView guardianView = new GuardianView(stage, conn, guardian);
                        stage.setScene(guardianView.getScene());
                        break;
                    case "Caregiver":
                        CaregiverView caregiverView = new CaregiverView(stage, conn, caregiver);
                        stage.setScene(caregiverView.getScene());
                        break;
                    case "Admin":
                        stage.hide();
                        AdminView adminView = new AdminView(stage, conn, admin);
                        adminView.start();
                        break;
                }
            });
            stage.setScene(roleSelectionView.getScene());
        }
    }


    private void switchToGuardianRegistration() {
        GuardianRegisterView guardianRegisterView = new GuardianRegisterView(stage, conn);
        // stage.setScene(guardianRegisterView.getScene());
    }

    private void switchToCaregiverRegistration() {
        CaregiverRegisterView caregiverRegisterView = new CaregiverRegisterView(stage, conn);
        // stage.setScene(caregiverRegisterView.getScene());
    }
}
