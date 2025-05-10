package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class LoginView {


    private final TextField usernameField = createRoundedField("Email");
    private final PasswordField passwordField = createRoundedPasswordField("Password");
    private final Button signInButton = createGreenButton("Sign in");
    private final Button registerAsGuardianButton = createWhiteButton("Sign up as Guardian");
    private final Button registerAsCaregiverButton = createWhiteButton("Sign up as CareGiver");


    private final HBox rootLayout;

    public LoginView() {

        Label titleLabel = new Label("Login to Your Account");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold;");

        VBox formBox = new VBox(15,
                new VBox(new Label("Username:"), usernameField),
                new VBox(new Label("Password:"), passwordField),
                signInButton
        );
        formBox.setAlignment(Pos.CENTER_LEFT);
        formBox.setMaxWidth(300);

        VBox leftPane = new VBox(30, titleLabel, formBox);
        leftPane.setPadding(new Insets(40));
        leftPane.setPrefWidth(600);
        leftPane.setAlignment(Pos.CENTER);
        leftPane.setStyle("-fx-background-color: white;");


        Label newHereLabel = new Label("New Here?");
        newHereLabel.setFont(Font.font("Arial", 22));
        newHereLabel.setStyle("-fx-font-weight: bold;");
        newHereLabel.setTextFill(Color.WHITE);

        Label infoLabel = new Label("Sign up here and discover great\namount of opportunities");
        infoLabel.setTextFill(Color.WHITE);
        infoLabel.setFont(Font.font(14));
        infoLabel.setWrapText(true);


        Label orLabel = new Label("OR");
        orLabel.setTextFill(Color.WHITE);
        orLabel.setStyle("-fx-font-weight: bold;");

        VBox rightPane = new VBox(20,
                newHereLabel,
                infoLabel,
                registerAsGuardianButton,
                orLabel,
                registerAsCaregiverButton
        );
        rightPane.setPadding(new Insets(60));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setAlignment(Pos.CENTER);
        rightPane.setPrefWidth(400);


        rootLayout = new HBox(leftPane, rightPane);
    }

    private TextField createRoundedField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle("-fx-background-color: #d9d9d9; -fx-background-radius: 20; -fx-padding: 10 20;");
        tf.setPrefWidth(300);
        tf.setMaxWidth(300);
        return tf;
    }

    private PasswordField createRoundedPasswordField(String prompt) {
        PasswordField pf = new PasswordField();
        pf.setPromptText(prompt);
        pf.setStyle("-fx-background-color: #d9d9d9; -fx-background-radius: 20; -fx-padding: 10 20;");
        pf.setPrefWidth(300);
        pf.setMaxWidth(300);
        return pf;
    }

    private Button createGreenButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: #3BB49C; -fx-text-fill: white; -fx-font-size: 18px; -fx-background-radius: 15; -fx-font-weight: bold;");
        btn.setPrefWidth(300);
        btn.setMaxWidth(300);
        btn.setPrefHeight(45);
        return btn;
    }

    private Button createWhiteButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: white; -fx-text-fill: #3BB49C; -fx-font-size: 16px; -fx-background-radius: 15; -fx-font-weight: bold;");
        btn.setPrefWidth(220);
        btn.setMaxWidth(220);
        btn.setPrefHeight(45);
        return btn;
    }


    public HBox getView() {
        return rootLayout;
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public Button getSignInButton() {
        return signInButton;
    }

    public Button getRegisterAsGuardianButton() {
        return registerAsGuardianButton;
    }

    public Button getRegisterAsCaregiverButton() {
        return registerAsCaregiverButton;
    }
}