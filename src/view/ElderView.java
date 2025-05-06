package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Guardian;

import java.sql.Connection;

public class ElderView {

    private final Scene scene;

    public ElderView(Stage stage, Connection conn, Guardian guardian) {
        // === Title ===
        Label title = new Label("Add an Elder");
        title.setFont(new Font("Arial", 24));
        title.setStyle("-fx-font-weight: bold;");

        // === Elder Form ===
        GridPane formGrid = new GridPane();
        formGrid.setHgap(20);
        formGrid.setVgap(20);
        formGrid.setAlignment(Pos.CENTER_LEFT);

        TextField firstNameField = createRoundedField("First Name");
        TextField lastNameField = createRoundedField("Last Name");
        TextField birthdayField = createRoundedField("Birthday");
        TextField contactField = createRoundedField("Contact Number");
        TextField addressField = createRoundedField("Address");
        TextField emailField = createRoundedField("Email");
        TextField relationshipField = createRoundedField("Relationship");

        formGrid.add(new Label("First Name:"), 0, 0);
        formGrid.add(firstNameField, 0, 1);
        formGrid.add(new Label("Last Name:"), 1, 0);
        formGrid.add(lastNameField, 1, 1);
        formGrid.add(new Label("Birthday:"), 0, 2);
        formGrid.add(birthdayField, 0, 3);
        formGrid.add(new Label("Contact Number:"), 1, 2);
        formGrid.add(contactField, 1, 3);
        formGrid.add(new Label("Address:"), 0, 4);
        formGrid.add(addressField, 0, 5);
        formGrid.add(new Label("Email:"), 1, 4);
        formGrid.add(emailField, 1, 5);
        formGrid.add(new Label("Relationship:"), 0, 6);
        formGrid.add(relationshipField, 0, 7);

        // === Left Buttons ===
        Button cancelButton = createMainButton("Cancel");
        Button addButton = createMainButton("Add");

        HBox buttonBox = new HBox(20, cancelButton, addButton);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        VBox leftPane = new VBox(20, title, formGrid, buttonBox);
        leftPane.setPadding(new Insets(40));
        leftPane.setPrefWidth(800);
        leftPane.setAlignment(Pos.TOP_CENTER);

        // === Right Sidebar ===
        Button goBackBtn = createSidebarButton("Go Back");

        VBox rightPane = new VBox();
        rightPane.setPadding(new Insets(30));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setAlignment(Pos.BOTTOM_CENTER);
        rightPane.setPrefWidth(250);

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        rightPane.getChildren().addAll(spacer, goBackBtn);

        // === Root Layout ===
        HBox root = new HBox(20, leftPane, rightPane);
        root.setPadding(new Insets(20));

        this.scene = new Scene(root, 1100, 600);
        stage.setTitle("Your Elders");
        stage.setScene(scene);
        stage.show();

        // === Actions ===
        cancelButton.setOnAction(e -> stage.close());
        addButton.setOnAction(e -> System.out.println("Elder added."));
        goBackBtn.setOnAction(e -> System.out.println("Back button clicked."));
    }

    private TextField createRoundedField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle("-fx-background-color: lightgray; -fx-background-radius: 20; -fx-padding: 8 16;");
        field.setPrefWidth(250);
        return field;
    }

    private Button createMainButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #3BB49C; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20;");
        button.setPrefWidth(120);
        return button;
    }

    private Button createSidebarButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 14px; -fx-background-radius: 20;");
        button.setPrefWidth(120);
        return button;
    }

    public Scene getScene() {
        return scene;
    }
}
