package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Caregiver;

import java.sql.Connection;

public class CaregiverElderView {

    private final Scene scene;

    public CaregiverElderView(Stage stage, Connection conn, Caregiver caregiver) {
        // === Title and Search/Sort ===
        Label titleLabel = new Label("Your Elders");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold;");

        TextField searchField = createRoundedTextField("(Searchfield)");
        ComboBox<String> sortBox = createRoundedComboBox("(Dropdown)");

        HBox searchSortBox = new HBox(20,
                new Label("Search:"), searchField,
                new Label("Sort by:"), sortBox);
        searchSortBox.setAlignment(Pos.CENTER_RIGHT);

        // === Elders Table ===
        GridPane table = new GridPane();
        table.setHgap(20);
        table.setVgap(20);
        table.setPadding(new Insets(20));
        table.setStyle("-fx-border-color: #B891F1; -fx-border-width: 2; -fx-background-color: #F9F9F9;");
        table.setPrefWidth(750);

        // Header row
        Label eldersHeader = new Label("Elders");
        eldersHeader.setStyle("-fx-font-weight: bold; -fx-background-color: #E2C8FD; -fx-padding: 10;");
        Label detailsHeader = new Label("Service Details");
        detailsHeader.setStyle("-fx-font-weight: bold; -fx-background-color: #E2C8FD; -fx-padding: 10;");
        Label actionHeader = new Label(""); // Empty header

        eldersHeader.setPrefWidth(150);
        detailsHeader.setPrefWidth(400);

        table.add(eldersHeader, 0, 0);
        table.add(detailsHeader, 1, 0);
        table.add(actionHeader, 2, 0);

        // Example data rows
        addElderRow(table, 1, "Jose, Judas, & Joseph",
                "Type of Services: Physical Therapy\nService Names: FEMDOM");
        addElderRow(table, 2, "Maria",
                "Type of Service: Physical Therapy\nService Name: FEMDOM");
        addElderRow(table, 3, "Pedro",
                "Type of Service: Physical Therapy\nService Name: FEMDOM");

        VBox leftPane = new VBox(20, titleLabel, searchSortBox, table);
        leftPane.setPadding(new Insets(20));
        leftPane.setPrefWidth(800);

        // === Right Sidebar ===
        Button goBackBtn = createSidebarButton("Go Back");

        VBox rightPane = new VBox();
        rightPane.setPadding(new Insets(30));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setAlignment(Pos.BOTTOM_CENTER);
        rightPane.setPrefWidth(250);

        // Push Go Back button to bottom
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
    }

    private void addElderRow(GridPane table, int rowIndex, String elderName, String details) {
        Label elderLabel = new Label(elderName);
        elderLabel.setPrefWidth(150);
        elderLabel.setWrapText(true);

        Label detailsLabel = new Label(details);
        detailsLabel.setPrefWidth(400);
        detailsLabel.setWrapText(true);

        Button markDoneBtn = createBigGreenButton("Mark as done");

        // Align button to the right using HBox
        HBox buttonBox = new HBox(markDoneBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPrefWidth(150);

        table.add(elderLabel, 0, rowIndex);
        table.add(detailsLabel, 1, rowIndex);
        table.add(buttonBox, 2, rowIndex);
    }

    private TextField createRoundedTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle("""
            -fx-background-radius: 15;
            -fx-border-radius: 15;
            -fx-background-color: #D9D9D9;
            -fx-border-color: transparent;
            -fx-padding: 8 12;
        """);
        return tf;
    }

    private ComboBox<String> createRoundedComboBox(String prompt) {
        ComboBox<String> cb = new ComboBox<>();
        cb.setPromptText(prompt);
        cb.setStyle("""
            -fx-background-radius: 15;
            -fx-border-radius: 15;
            -fx-background-color: #D9D9D9;
            -fx-border-color: transparent;
            -fx-padding: 4 8;
        """);
        return cb;
    }

    private Button createBigGreenButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("""
            -fx-background-color: #3BB49C;
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-font-weight: bold;
            -fx-background-radius: 20;
            -fx-cursor: hand;
            -fx-padding: 10 30;
        """);
        return btn;
    }

    private Button createSidebarButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("""
            -fx-background-color: white;
            -fx-text-fill: black;
            -fx-font-size: 14px;
            -fx-background-radius: 15;
            -fx-cursor: hand;
            -fx-padding: 10 20;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 1);
        """);
        btn.setPrefWidth(200);
        return btn;
    }

    public Scene getScene() {
        return scene;
    }
}
