package view;

import controller.GuardianController;
import controller.LoginController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Guardian;

import java.sql.Connection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuardianRegisterView {

    private final Scene scene;
    private final Connection dbConnection;
    private final Stage mainStage;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static final String NUMBER_REGEX = "\\d+";
    private static final Pattern NUMBER_PATTERN = Pattern.compile(NUMBER_REGEX);

    public GuardianRegisterView(Stage stage, Connection conn) {
        this.mainStage = stage;
        this.dbConnection = conn;

        Label personalInfoTitle = new Label("Fill up Personal Information");
        personalInfoTitle.setFont(new Font("Arial", 24));
        personalInfoTitle.setStyle("-fx-font-weight: bold;");

        GridPane personalInfoGrid = new GridPane();
        personalInfoGrid.setHgap(20);
        personalInfoGrid.setVgap(15);
        personalInfoGrid.setAlignment(Pos.CENTER_LEFT);

        TextField firstNameField = createRoundedField("First Name");
        TextField lastNameField = createRoundedField("Last Name");
        TextField contactNumberField = createRoundedField("Contact Number");
        TextField emailField = createRoundedField("Email Address");
        TextField addressField = createRoundedField("Address");

        int row = 0;
        personalInfoGrid.add(new Label("First Name:"), 0, row);
        personalInfoGrid.add(firstNameField, 1, row);
        row++;
        personalInfoGrid.add(new Label("Last Name:"), 0, row);
        personalInfoGrid.add(lastNameField, 1, row);
        row++;
        personalInfoGrid.add(new Label("Contact Number:"), 0, row);
        personalInfoGrid.add(contactNumberField, 1, row);
        row++;
        personalInfoGrid.add(new Label("Email Address:"), 0, row);
        personalInfoGrid.add(emailField, 1, row);
        row++;
        personalInfoGrid.add(new Label("Address:"), 0, row);
        personalInfoGrid.add(addressField, 1, row);
        row++;

        VBox leftPane = new VBox(20, personalInfoTitle, personalInfoGrid);
        leftPane.setPadding(new Insets(40));
        leftPane.setPrefWidth(600);
        leftPane.setAlignment(Pos.TOP_CENTER);

        Label registerLabel = new Label("Register");
        registerLabel.setFont(new Font("Arial", 24));
        registerLabel.setTextFill(Color.WHITE);
        registerLabel.setStyle("-fx-font-weight: bold;");

        TextField usernameField = createRoundedField("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        styleRoundedField(passwordField);

        Button registerButton = new Button("Register");
        styleRegisterButton(registerButton);

        Button backButton = new Button("Back to Login");
        styleStandardButton(backButton);

        VBox rightPane = new VBox(20, registerLabel, usernameField, passwordField, registerButton, backButton);
        rightPane.setPadding(new Insets(60));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setAlignment(Pos.CENTER);
        rightPane.setPrefWidth(400);

        HBox rootLayout = new HBox(leftPane, rightPane);
        this.scene = new Scene(rootLayout, 1000, 700);

        registerButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String contactNumber = contactNumberField.getText().trim();
            String email = emailField.getText().trim();
            String address = addressField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || contactNumber.isEmpty() || email.isEmpty() || address.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Missing Information", "Please fill in all required fields.");
                return;
            }

            Matcher emailMatcher = EMAIL_PATTERN.matcher(email);
            if (!emailMatcher.matches()) {
                showAlert(Alert.AlertType.WARNING, "Invalid Email Format", "Please enter a valid email address.");
                return;
            }

            Matcher numberMatcher = NUMBER_PATTERN.matcher(contactNumber);
            if (!numberMatcher.matches()) {
                showAlert(Alert.AlertType.WARNING, "Invalid Contact Number", "Contact number should only contain digits.");
                return;
            }

            Guardian guardian = new Guardian(username, password, firstName, lastName, contactNumber, email, address);

            try {
                GuardianController guardianController = new GuardianController(dbConnection);
                guardianController.addGuardian(guardian);

                showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "Your account has been created.");

                Guardian registeredGuardian = guardianController.findByUsername(username);

                if (registeredGuardian != null) {
                    // Assuming GuardianView constructor is GuardianView(Stage stage, Connection conn, Guardian guardian)
                    GuardianView guardianView = new GuardianView(mainStage, dbConnection, registeredGuardian);
                    mainStage.setScene(guardianView.getScene());
                } else {
                    showAlert(Alert.AlertType.ERROR, "Navigation Error", "Registration succeeded, but could not load guardian profile.");
                    LoginView loginView = new LoginView();
                    mainStage.setScene(new Scene(loginView.getView(), mainStage.getScene().getWidth(), mainStage.getScene().getHeight()));
                }

            } catch (RuntimeException ex) {
                showAlert(Alert.AlertType.WARNING, "Registration Failed", ex.getMessage());
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", "An error occurred during registration: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        backButton.setOnAction(e -> {
                LoginController loginController = new LoginController(mainStage, dbConnection);
                Scene loginScene = loginController.getLoginScene();
                mainStage.setTitle("ElderCare");
                mainStage.setScene(loginScene);
                mainStage.show();
                });

        stage.setScene(scene);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private TextField createRoundedField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        styleRoundedField(field);
        return field;
    }

    private void styleRoundedField(TextField field) {
        field.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 15; -fx-padding: 8 12; -fx-border-color: #cccccc; -fx-border-radius: 15;");
        field.setPrefWidth(250);
    }

    private void styleStandardButton(Button button) {
        String baseStyle = "-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 14px; -fx-background-radius: 20; -fx-border-color: #3BB49C; -fx-border-width: 1; -fx-border-radius: 20;";
        String hoverStyle = "-fx-background-color: #e0e0e0; -fx-text-fill: black; -fx-font-size: 14px; -fx-background-radius: 20; -fx-border-color: #3BB49C; -fx-border-width: 1; -fx-border-radius: 20;";
        button.setStyle(baseStyle);
        button.setPrefWidth(150);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
    }

    private void styleRegisterButton(Button button) {
        String baseStyle = "-fx-background-color: #2a8c79; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 20;";
        String hoverStyle = "-fx-background-color: #1f6f5f; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 20;";
        button.setStyle(baseStyle);
        button.setPrefWidth(150);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
    }

    public Scene getScene() {
        return scene;
    }
}
