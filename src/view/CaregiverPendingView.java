package view;

import controller.LoginController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.sql.Connection;


public class CaregiverPendingView {

    private Scene scene;
    private Stage mainStage;
    private Connection dbConnection;

    public CaregiverPendingView(Stage stage, Connection conn) {
        this.mainStage = stage;
        this.dbConnection = conn;
        buildUI();
    }

    private void buildUI() {


        Label pendingLabel = new Label("Account is\nPending...");
        pendingLabel.setFont(new Font("Arial", 48));
        pendingLabel.setStyle("-fx-font-weight: bold;");
        pendingLabel.setTextAlignment(TextAlignment.CENTER);

        VBox leftContent = new VBox(pendingLabel);
        leftContent.setAlignment(Pos.CENTER);


        VBox leftPane = new VBox(leftContent);
        leftPane.setAlignment(Pos.CENTER);
        leftPane.setPadding(new Insets(40));
        leftPane.setStyle("-fx-background-color: #FFFFFF;");
        leftPane.setPrefWidth(650);
        VBox.setVgrow(leftContent, Priority.ALWAYS);


        Label rightMessage = new Label("Your account is being verified at the moment. Come back again.");
        rightMessage.setFont(new Font("Arial", 18));
        rightMessage.setTextFill(Color.WHITE);
        rightMessage.setWrapText(true);
        rightMessage.setTextAlignment(TextAlignment.CENTER);
        rightMessage.setMaxWidth(250);

        Button goBackButton = new Button("Go Back");
        styleGoBackButton(goBackButton);

        goBackButton.setOnAction(e -> { LoginController loginController = new LoginController(mainStage, dbConnection);
            Scene loginScene = loginController.getLoginScene();
            mainStage.setScene(loginScene);
        });

        VBox rightPane = new VBox(30, rightMessage, goBackButton);
        rightPane.setAlignment(Pos.CENTER);
        rightPane.setPadding(new Insets(60));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setPrefWidth(350);


        HBox rootLayout = new HBox(leftPane, rightPane);


        double prefWidth = 1000;
        double prefHeight = 700;
        if (mainStage != null && mainStage.getScene() != null) {
            prefWidth = mainStage.getScene().getWidth();
            prefHeight = mainStage.getScene().getHeight();
        }
        this.scene = new Scene(rootLayout, prefWidth, prefHeight);
    }


    private void styleGoBackButton(Button button) {
        String baseStyle = "-fx-background-color: white; -fx-text-fill: #3BB49C; -fx-font-size: 14px; -fx-background-radius: 20; -fx-border-color: white; -fx-border-width: 1; -fx-border-radius: 20;";
        String hoverStyle = "-fx-background-color: #e0e0e0; -fx-text-fill: #3BB49C; -fx-font-size: 14px; -fx-background-radius: 20; -fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 20;";
        button.setStyle(baseStyle);
        button.setPrefWidth(150);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
    }

    public Scene getScene() {
        return this.scene;
    }
}