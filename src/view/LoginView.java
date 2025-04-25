package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class LoginView {

    private final TextField usernameField = createStyledTextField("Username");
    private final PasswordField passwordField = createStyledPasswordField("Password");
    private final Button signInButton = createStyledButton("Sign In");
    private final Button registerButton = createStyledRegisterButton("Register");
    private final VBox rootLayout;

    public LoginView() {
        Label title = new Label("Healthcare Management System");
        title.setFont(new Font("Arial", 28));
        title.setTextFill(Color.web("#333333"));

        Label signInLabel = new Label("Sign In");
        signInLabel.setFont(new Font("Arial", 20));
        signInLabel.setTextFill(Color.web("#333333"));

        // Form Grid
        GridPane formGrid = new GridPane();
        formGrid.setVgap(10);
        formGrid.setHgap(10);
        formGrid.setPadding(new Insets(10));
        formGrid.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(8), Insets.EMPTY)));
        formGrid.setEffect(createShadowEffect());

        formGrid.add(new Label("Username:"), 0, 0);
        formGrid.add(usernameField, 1, 0);
        formGrid.add(new Label("Password:"), 0, 1);
        formGrid.add(passwordField, 1, 1);

        VBox signInBox = new VBox(10, signInLabel, formGrid, signInButton);
        signInBox.setAlignment(Pos.CENTER);

        Label registerLabel = new Label("Don't have an account?");
        VBox registerBox = new VBox(10, registerLabel, registerButton);
        registerBox.setAlignment(Pos.CENTER);

        rootLayout = new VBox(20, title, signInBox, registerBox);
        rootLayout.setPadding(new Insets(20));
        rootLayout.setAlignment(Pos.CENTER);
        rootLayout.setStyle("-fx-background-color: #f0f4f7;");
    }

    private TextField createStyledTextField(String promptText) {
        TextField tf = new TextField();
        tf.setPromptText(promptText);
        tf.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black; -fx-background-radius: 5; -fx-padding: 8;");
        return tf;
    }

    private PasswordField createStyledPasswordField(String promptText) {
        PasswordField pf = new PasswordField();
        pf.setPromptText(promptText);
        pf.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black; -fx-background-radius: 5; -fx-padding: 8;");
        return pf;
    }

    private Button createStyledButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 5;");
        btn.setMinWidth(200);
        return btn;
    }

    private Button createStyledRegisterButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 5;");
        btn.setMinWidth(200);
        return btn;
    }

    private DropShadow createShadowEffect() {
        DropShadow shadow = new DropShadow();
        shadow.setRadius(8);
        shadow.setOffsetX(0);
        shadow.setOffsetY(4);
        shadow.setColor(Color.rgb(0, 0, 0, 0.2));
        return shadow;
    }

    public VBox getView() {
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

    public Button getRegisterButton() {
        return registerButton;
    }
}
