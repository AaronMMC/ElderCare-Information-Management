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

public class GuardianElderView {

    private final Scene scene;

    public GuardianElderView(Stage stage, Connection conn, Guardian guardian) {
        Label titleLabel = new Label("Your Elders");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold;");

        // === Elders Table ===
        GridPane table = new GridPane();
        table.setHgap(20);
        table.setVgap(20);
        table.setPadding(new Insets(20));
        table.setStyle("-fx-border-color: #B891F1; -fx-border-width: 2; -fx-background-color: #F9F9F9;");
        table.setPrefWidth(750);

        // Header row
        Label elderHeader = new Label("Elders");
        elderHeader.setStyle("-fx-font-weight: bold; -fx-background-color: #E2C8FD; -fx-padding: 10;");
        Label detailsHeader = new Label("Elder Details");
        detailsHeader.setStyle("-fx-font-weight: bold; -fx-background-color: #E2C8FD; -fx-padding: 10;");
        Label actionHeader = new Label("");

        elderHeader.setPrefWidth(150);
        detailsHeader.setPrefWidth(400);

        table.add(elderHeader, 0, 0);
        table.add(detailsHeader, 1, 0);
        table.add(actionHeader, 2, 0);

        // Example data rows
        addElderRow(table, 1, "Jose", "(Name, gender, age, contact info,\nguardianElder_relationship)");
        addElderRow(table, 2, "Maria", "(Name, gender, age, contact info,\nguardianElder_relationship)");
        addElderRow(table, 3, "Pedro", "(Name, gender, age, contact info,\nguardianElder_relationship)");

        VBox leftPane = new VBox(20, titleLabel, table);
        leftPane.setPadding(new Insets(20));
        leftPane.setPrefWidth(800);

        // === Right Sidebar ===
        Button addElderBtn = createSidebarButton("Add an Elder");
        Button backButton = createSidebarButton("Back");

        addElderBtn.setOnAction(e -> {
            ElderView elderView = new ElderView(stage, conn, guardian);
            stage.setScene(elderView.getScene());
        });

        backButton.setOnAction(e -> {
            GuardianView guardianView = new GuardianView(stage,conn,guardian);
            stage.setScene(guardianView.getScene());
        });

        // Right pane setup
        VBox rightPane = new VBox(30, addElderBtn);
        rightPane.setPadding(new Insets(30));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setAlignment(Pos.TOP_CENTER);
        rightPane.setPrefWidth(250);

        // Add the Back button at the bottom of the right pane
        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        rightPane.getChildren().addAll(spacer, backButton);

        // === Root Layout ===
        HBox root = new HBox(20, leftPane, rightPane);
        root.setPadding(new Insets(20));

        this.scene = new Scene(root, 1100, 600);
        stage.setTitle("Your Elders");
        stage.setScene(scene);
        stage.show();
    }

    private void addElderRow(GridPane table, int rowIndex, String name, String details) {
        Label nameLabel = new Label(name);
        nameLabel.setPrefWidth(150);
        nameLabel.setWrapText(true);

        Label detailsLabel = new Label(details);
        detailsLabel.setPrefWidth(400);
        detailsLabel.setWrapText(true);

        Button editBtn = createBigGreenButton("Edit");
        Button removeBtn = createBigGreenButton("Remove");

        HBox actionBox = new HBox(10, editBtn, removeBtn);
        actionBox.setAlignment(Pos.CENTER_RIGHT);
        actionBox.setPrefWidth(150);

        table.add(nameLabel, 0, rowIndex);
        table.add(detailsLabel, 1, rowIndex);
        table.add(actionBox, 2, rowIndex);
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
            -fx-padding: 10 20;
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
