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

        loginView.getSignInButton().setOnAction(e -> handleLogin());
        loginView.getRegisterAsGuardianButton().setOnAction(e -> switchToGuardianRegistration());
        loginView.getRegisterAsCaregiverButton().setOnAction(e -> switchToCaregiverRegistration());
    }

    public Scene getLoginScene() {
        return new Scene(loginView.getView(), 1000, 700);
    }

    private void handleLogin() {
        String username = loginView.getUsernameField().getText();
        String password = loginView.getPasswordField().getText();

        Guardian guardian = guardianDAO.findByUsernameAndPassword(username,password);
        Caregiver caregiver = caregiverDAO.findByUsernameAndPassword(username,password);
        Admin admin = adminDAO.findByUsernameAndPassword(username,password);

        System.out.println(caregiver.getBackgroundCheckStatus());
        System.out.println(caregiver.getMedicalClearanceStatus());
        if (guardian == null && caregiver == null && admin == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Username does not exist.");
            alert.showAndWait();
            return;
        }

        if (guardian != null && guardian.getPassword().equals(password)) {
            GuardianView guardianView = new GuardianView(stage, conn, guardian);
            stage.setScene(guardianView.getScene());
            return;
        }
        if (caregiver != null && caregiver.getPassword().equals(password)) {
            if (caregiver.getBackgroundCheckStatus() == Caregiver.BackgroundCheckStatus.Pending  && caregiver.getMedicalClearanceStatus() == Caregiver.MedicalClearanceStatus.Pending ){
                CaregiverPendingView pendingView = new CaregiverPendingView(stage, conn);
                stage.setScene(pendingView.getScene());
                return;
            }
            if (caregiver.getBackgroundCheckStatus() == Caregiver.BackgroundCheckStatus.Passed && caregiver.getMedicalClearanceStatus() == Caregiver.MedicalClearanceStatus.Cleared) {
                CaregiverView caregiverView = new CaregiverView(stage,conn, caregiver);
                stage.setScene(caregiverView.getScene());
                return;
            }
            if (caregiver.getBackgroundCheckStatus() == Caregiver.BackgroundCheckStatus.Failed || caregiver.getMedicalClearanceStatus() == Caregiver.MedicalClearanceStatus.Not_Cleared) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed.");
                alert.setHeaderText(null);
                alert.setContentText("Your Account was not approve. Please register again.");
                alert.showAndWait();
                caregiverDAO.deleteCaregiver(caregiver.getCaregiverID());
                return;
            }
        }
        if (admin != null && admin.getPassword().equals(password)) {
            AdminView adminView = new AdminView(stage, conn, admin);
            stage.hide();
            adminView.start();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Failed");
        alert.setHeaderText(null);
        alert.setContentText("Incorrect password.");
        alert.showAndWait();

    }

    private void switchToGuardianRegistration() {
        GuardianRegisterView guardianRegisterView = new GuardianRegisterView(stage, conn);
        stage.setScene(guardianRegisterView.getScene());
    }

    private void switchToCaregiverRegistration() {
        CaregiverRegisterView caregiverRegisterView = new CaregiverRegisterView(stage, conn);
        stage.setScene(caregiverRegisterView.getScene());
    }

}
