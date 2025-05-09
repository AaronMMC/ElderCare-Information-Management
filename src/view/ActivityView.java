package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Appointment;

import java.sql.Connection;

public class ActivityView {

    private final Scene scene;

    public ActivityView(Stage stage, Connection conn, Appointment appointment) {
        // === Title ===
        Label titleLabel = new Label("Activity Log");
        titleLabel.setFont(Font.font("Arial", 26));
        titleLabel.setStyle("-fx-font-weight: bold;");

        // === Table Headers ===
        GridPane table = new GridPane();
        table.setHgap(10);
        table.setVgap(10);
        table.setPadding(new Insets(10));
        table.setStyle("-fx-border-color: #B891F1; -fx-border-width: 2; -fx-background-color: white;");
        table.setPrefWidth(750);

        Label titleHeader = new Label("Activity Title");
        titleHeader.setStyle("-fx-background-color: #E2C8FD; -fx-font-weight: bold; -fx-padding: 10;");
        titleHeader.setPrefWidth(200);
        Label detailsHeader = new Label("Activity Details");
        detailsHeader.setStyle("-fx-background-color: #E2C8FD; -fx-font-weight: bold; -fx-padding: 10;");
        detailsHeader.setPrefWidth(400);

        table.add(titleHeader, 0, 0);
        table.add(detailsHeader, 1, 0);

        // === Dummy Rows (Dynamic in future) ===
        int totalRows = 8; // Increased number of rows to fill space
        for (int i = 1; i <= totalRows; i++) {
            TextField titleField = new TextField("Activity " + i);
            titleField.setPrefWidth(200);

            TextField detailsField = new TextField("Details for activity " + i);
            detailsField.setPrefWidth(400);

            Button editBtn = createRoundedGreenButton("Edit");
            Button removeBtn = createRoundedGreenButton("Remove");

            HBox actionsBox = new HBox(10, editBtn, removeBtn);
            actionsBox.setAlignment(Pos.CENTER);

            table.add(titleField, 0, i);
            table.add(detailsField, 1, i);
            table.add(actionsBox, 2, i);
        }

        // === Action Buttons ===
        Button cancelBtn = createRoundedGreenButton("Cancel");
        Button saveBtn = createRoundedGreenButton("Save Changes");

        HBox actionBox = new HBox(20, cancelBtn, saveBtn);
        actionBox.setAlignment(Pos.CENTER_LEFT);

        VBox leftPane = new VBox(20, titleLabel, table, actionBox);
        leftPane.setPadding(new Insets(20));
        leftPane.setPrefWidth(800);
        VBox.setVgrow(table, Priority.ALWAYS);

        // === Right Sidebar ===
        Button addActivityBtn = createSidebarButton("Add Activity");
        Button goBackBtn = createSidebarButton("Go Back");

        addActivityBtn.setPrefWidth(200);
        goBackBtn.setPrefWidth(200);

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox rightPane = new VBox(30, addActivityBtn, spacer, goBackBtn);
        rightPane.setAlignment(Pos.TOP_CENTER);
        rightPane.setPadding(new Insets(30));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setPrefWidth(250);

        // === Main Layout ===
        HBox root = new HBox(20, leftPane, rightPane);
        root.setPadding(new Insets(20));

        this.scene = new Scene(root, 1100, 600);
        stage.setTitle("Activity Details");
        stage.setScene(scene);
        stage.show();
    }

    private Button createRoundedGreenButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("""
            -fx-background-color: #3BB49C;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-background-radius: 20;
            -fx-cursor: hand;
            -fx-padding: 8 20;
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
        return btn;
    }

    public Scene getScene() {
        return scene;
    }
}
