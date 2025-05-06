package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CaregiverScheduleView {

    private final Scene scene;

    public CaregiverScheduleView(Stage stage) {

        // === Title ===
        Label titleLabel = new Label("Your Schedule");
        titleLabel.setFont(Font.font("Arial", 28));
        titleLabel.setStyle("-fx-font-weight: bold;");

        // === Schedule Grid ===
        GridPane scheduleGrid = new GridPane();
        scheduleGrid.setHgap(1);
        scheduleGrid.setVgap(1);
        scheduleGrid.setPadding(new Insets(20));
        scheduleGrid.setStyle("-fx-border-color: #C299FF; -fx-border-width: 2;");

        String[] days = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

        // Header row
        for (int col = 0; col < days.length; col++) {
            Label dayLabel = new Label(days[col]);
            dayLabel.setPrefWidth(120);
            dayLabel.setAlignment(Pos.CENTER);
            dayLabel.setStyle("-fx-font-weight: bold; -fx-background-color: #DEC5FF; -fx-padding: 10;");
            scheduleGrid.add(dayLabel, col, 0);
        }

        // Empty time slots (6 rows)
        for (int row = 1; row <= 6; row++) {
            for (int col = 0; col < days.length; col++) {
                Pane cell = new Pane();
                cell.setPrefSize(120, 60);
                cell.setStyle("-fx-border-color: #C299FF; -fx-background-color: white;");
                scheduleGrid.add(cell, col, row);
            }
        }

        VBox leftPane = new VBox(20, titleLabel, scheduleGrid);
        leftPane.setPadding(new Insets(20));
        leftPane.setPrefWidth(900);

        // === Right Sidebar ===
        Button goBackBtn = createSidebarButton("Go Back");

        VBox rightPane = new VBox();
        rightPane.setPadding(new Insets(30));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setAlignment(Pos.BOTTOM_CENTER);
        rightPane.setPrefWidth(250);

        rightPane.getChildren().add(goBackBtn);

        // === Root Layout ===
        HBox root = new HBox(20, leftPane, rightPane);
        root.setPadding(new Insets(20));

        this.scene = new Scene(root, 1200, 600);
        stage.setTitle("Schedule");
        stage.setScene(scene);
        stage.show();
    }

    private Button createSidebarButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("""
            -fx-background-color: white;
            -fx-text-fill: black;
            -fx-font-size: 14px;
            -fx-background-radius: 20;
            -fx-cursor: hand;
            -fx-padding: 10 30;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 1);
        """);
        btn.setPrefWidth(200);
        return btn;
    }

    public Scene getScene() {
        return scene;
    }
}
