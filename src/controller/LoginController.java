package controller;

import dao.CaregiverDAO;
import dao.GuardianDAO;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.Caregiver;
import model.Guardian;
import view.*;

import java.sql.Connection;

public class LoginController {

    private final Stage stage;
    private final Connection conn;
    private final LoginView loginView;
    private final GuardianDAO guardianDAO;
    private final CaregiverDAO caregiverDAO;

    public LoginController(Stage stage, Connection conn) {
        this.stage = stage;
        this.conn = conn;
        this.loginView = new LoginView();
        this.guardianDAO = new GuardianDAO(conn);
        this.caregiverDAO = new CaregiverDAO(conn);

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

        // 1) Try guardian
        Guardian guardian = guardianDAO.findByUsernameAndPassword(username, password);
        if (guardian != null) {
            // switch to guardian main menu
            GuardianView guardianView = new GuardianView(stage, conn, guardian);
            stage.setScene(guardianView.getScene());
            return;
        }

        // 2) Try caregiver
        Caregiver caregiver = caregiverDAO.findByUsernameAndPassword(username, password);
        if (caregiver != null) {
            // switch to caregiver main menu
            CaregiverView caregiverView = new CaregiverView(stage, conn, caregiver);
            stage.setScene(caregiverView.getScene());
            return;
        }

        // TODO: Admin authentication catcher.

        // Neither matched
        new Alert(Alert.AlertType.ERROR, "Invalid username or password").showAndWait();

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
