package view;

import controller.LoginController;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.layout.Priority;  // Import for VBox.setVgrow
import model.Guardian;

import java.sql.Connection;

public class GuardianView {

    private final Scene scene;

    public GuardianView(Stage stage, Connection conn, Guardian guardian) {
        // === Left Form Layout ===
        Label titleLabel = new Label("Guardian Homepage");
        titleLabel.setFont(Font.font("Arial", 28));
        titleLabel.setStyle("-fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(20);
        formGrid.setVgap(15);
        formGrid.setPadding(new Insets(20));

        formGrid.add(createFormField("First Name:"), 0, 0);
        formGrid.add(createFormField("Last Name:"), 1, 0);
        formGrid.add(createFormField("Birthday:"), 2, 0);
        formGrid.add(createDropdownField("Gender:"), 0, 1);
        formGrid.add(createFormField("Contact Number:"), 1, 1);
        formGrid.add(createFormField("Email:"), 2, 1);
        formGrid.add(createFormField("Address:"), 0, 2);

        HBox actionButtons = new HBox(20,
                createTealButton("Cancel"),
                createTealButton("Save Changes")
        );
        actionButtons.setAlignment(Pos.CENTER_LEFT);
        actionButtons.setPadding(new Insets(30, 0, 0, 0));

        VBox leftContent = new VBox(20, titleLabel, formGrid, actionButtons);
        leftContent.setPadding(new Insets(40));
        leftContent.setPrefWidth(800);
        leftContent.setStyle("-fx-background-color: white;");

        // === Right Menu Layout ===
        VBox rightPane = new VBox(40,
                createMenuButton("Your Appointments"),
                createMenuButton("Your Elders")
        );
        Button logOutButton = createMenuButton("Log Out");
        logOutButton.setOnAction(e -> {
            System.out.println("Logging out...");
            Platform.runLater(() -> {
                LoginController loginController = new LoginController(stage, conn);
                Scene loginScene = loginController.getLoginScene();
                stage.setScene(loginScene); // Now the scene will be shown again
            });
        });

        // Push Log Out button to the bottom
        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);  // This makes sure the Log Out button goes to the bottom
        rightPane.getChildren().addAll(spacer, logOutButton);

        rightPane.setAlignment(Pos.TOP_CENTER);
        rightPane.setPadding(new Insets(60, 20, 20, 20));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setPrefWidth(300);

        // === Root Layout ===
        HBox root = new HBox(leftContent, rightPane);
        this.scene = new Scene(root, 1100, 600);
        stage.setTitle("Guardian Homepage");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createFormField(String labelText) {
        Label label = new Label(labelText);
        TextField field = new TextField();
        field.setStyle("-fx-background-color: #d9d9d9; -fx-background-radius: 20;");
        field.setPrefWidth(200);
        return new VBox(5, label, field);
    }

    private VBox createDropdownField(String labelText) {
        Label label = new Label(labelText);
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Male", "Female", "Other");
        comboBox.setStyle("-fx-background-color: #d9d9d9; -fx-background-radius: 20;");
        comboBox.setPrefWidth(200);
        return new VBox(5, label, comboBox);
    }

    private Button createTealButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("""
            -fx-background-color: #3BB49C;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-background-radius: 20;
        """);
        btn.setPrefHeight(40);
        btn.setPrefWidth(150);
        return btn;
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("""
            -fx-background-color: white;
            -fx-text-fill: black;
            -fx-font-size: 16px;
            -fx-background-radius: 20;
        """);
        btn.setPrefWidth(240);
        btn.setPrefHeight(60);
        return btn;
    }

    public Scene getScene() {
        return scene;
    }
}
