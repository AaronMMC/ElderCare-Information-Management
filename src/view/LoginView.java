//package view;
//
//import javafx.application.Application;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//
//public class LoginView extends Application {
//
//    @Override
//    public void start(Stage primaryStage) {
//        VBox root = new VBox(20);
//        root.setPadding(new Insets(20));
//        root.setAlignment(Pos.CENTER);
//        root.setStyle("-fx-background-color: #f0f4f7;");
//
//        // Title
//        Label title = new Label("Healthcare Management System");
//        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #333333;");
//
//        // Sign In Section
//        Label signInLabel = new Label("Sign In");
//        signInLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333333;");
//
//        // Username and Password fields
//        GridPane signInForm = new GridPane();
//        signInForm.setVgap(10);
//        signInForm.setHgap(10);
//        signInForm.setPadding(new Insets(10));
//        signInForm.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 8px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 8, 0, 0, 4);");
//
//        Label usernameLabel = new Label("Username:");
//        TextField usernameField = new TextField();
//
//        Label passwordLabel = new Label("Password:");
//        PasswordField passwordField = new PasswordField();
//
//        Button signInButton = new Button("Sign In");
//        signInButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-border-radius: 5px; -fx-padding: 10 20;");
//
//        signInForm.addColumn(0, usernameLabel, passwordLabel);
//        signInForm.addColumn(1, usernameField, passwordField);
//
//        // Register Section
//        Label registerLabel = new Label("Don't have an account?");
//        Button registerButton = new Button("Register");
//        registerButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16px; -fx-border-radius: 5px; -fx-padding: 10 20;");
//
//        // Action for Register button
//        registerButton.setOnAction(event -> {
//            // Redirect to the Registration page
//            showRegistrationPage(primaryStage);
//        });
//
//        // Action for Sign In button
//        signInButton.setOnAction(event -> {
//            // Handle Sign In functionality (e.g., validate credentials)
//            String username = usernameField.getText();
//            String password = passwordField.getText();
//
//            // Simulate user authentication (this can be replaced with actual logic)
//            if (username.equals("admin") && password.equals("admin123")) {
//                showAlert("Sign In Successful", "Welcome Admin!", Alert.AlertType.INFORMATION);
//            } else if (username.equals("guardian") && password.equals("guardian123")) {
//                showAlert("Sign In Successful", "Welcome Guardian!", Alert.AlertType.INFORMATION);
//            } else if (username.equals("caregiver") && password.equals("caregiver123")) {
//                showAlert("Sign In Successful", "Welcome Caregiver!", Alert.AlertType.INFORMATION);
//            } else {
//                showAlert("Sign In Failed", "Incorrect username or password.", Alert.AlertType.ERROR);
//            }
//        });
//
//        // Layout for Sign In
//        VBox signInBox = new VBox(10);
//        signInBox.setAlignment(Pos.CENTER);
//        signInBox.getChildren().addAll(signInLabel, signInForm, signInButton);
//
//        // Layout for Register Section
//        VBox registerBox = new VBox(10);
//        registerBox.setAlignment(Pos.CENTER);
//        registerBox.getChildren().addAll(registerLabel, registerButton);
//
//        // Adding Title, SignIn and Register sections to the root container
//        root.getChildren().addAll(title, signInBox, registerBox);
//
//        // Scene Setup
//        Scene scene = new Scene(root, 600, 500);
//        primaryStage.setTitle("Healthcare System - Landing Page");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    // Helper method to show alerts
//    private void showAlert(String title, String message, Alert.AlertType type) {
//        Alert alert = new Alert(type);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    // Helper method to show Registration page (dummy implementation for now)
//    private void showRegistrationPage(Stage primaryStage) {
//        VBox registrationPage = new VBox(20);
//        registrationPage.setPadding(new Insets(20));
//        registrationPage.setAlignment(Pos.CENTER);
//        registrationPage.setStyle("-fx-background-color: #f0f4f7;");
//
//        Label registrationTitle = new Label("Register New User");
//        registrationTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");
//
//        // Registration Form
//        GridPane registrationForm = new GridPane();
//        registrationForm.setVgap(10);
//        registrationForm.setHgap(10);
//        registrationForm.setPadding(new Insets(10));
//        registrationForm.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 8px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 8, 0, 0, 4);");
//
//        Label regUsernameLabel = new Label("Username:");
//        TextField regUsernameField = new TextField();
//
//        Label regPasswordLabel = new Label("Password:");
//        PasswordField regPasswordField = new PasswordField();
//
//        Label regRoleLabel = new Label("Role:");
//        ComboBox<String> roleComboBox = new ComboBox<>();
//        roleComboBox.getItems().addAll("Administrator", "Guardian", "Caregiver");
//
//        Button registerBtn = new Button("Register");
//        registerBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-border-radius: 5px; -fx-padding: 10 20;");
//
//        registrationForm.addColumn(0, regUsernameLabel, regPasswordLabel, regRoleLabel);
//        registrationForm.addColumn(1, regUsernameField, regPasswordField, roleComboBox);
//
//        // Action for Registration Button
//        registerBtn.setOnAction(event -> {
//            String regUsername = regUsernameField.getText();
//            String regPassword = regPasswordField.getText();
//            String role = roleComboBox.getValue();
//
//            if (regUsername.isEmpty() || regPassword.isEmpty() || role == null) {
//                showAlert("Registration Failed", "Please fill out all fields.", Alert.AlertType.ERROR);
//            } else {
//                showAlert("Registration Successful", "User " + regUsername + " registered as " + role + ".", Alert.AlertType.INFORMATION);
//            }
//        });
//
//        // Adding Registration Form to the page
//        registrationPage.getChildren().addAll(registrationTitle, registrationForm, registerBtn);
//
//        // Switch Scene to Registration Page
//        Scene registrationScene = new Scene(registrationPage, 600, 500);
//        primaryStage.setScene(registrationScene);
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
