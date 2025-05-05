package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CaregiverServiceView {

    private final Scene scene;

    public CaregiverServiceView(Stage stage) {
        Label titleLabel = new Label("Your Services");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold;");

        TextField searchField = createRoundedTextField("(Searchfield)");
        ComboBox<String> sortBox = createRoundedComboBox("(Dropdown)");

        HBox searchSortBox = new HBox(20,
                new Label("Search:"), searchField,
                new Label("Sort by:"), sortBox);
        searchSortBox.setAlignment(Pos.CENTER_RIGHT);

        GridPane table = new GridPane();
        table.setHgap(20);
        table.setVgap(20);
        table.setPadding(new Insets(20));
        table.setStyle("-fx-border-color: #B891F1; -fx-border-width: 2; -fx-background-color: #F9F9F9;");
        table.setPrefWidth(750);

        Label serviceHeader = new Label("Services");
        serviceHeader.setStyle("-fx-font-weight: bold; -fx-background-color: #E2C8FD; -fx-padding: 10;");
        Label detailsHeader = new Label("Service Details");
        detailsHeader.setStyle("-fx-font-weight: bold; -fx-background-color: #E2C8FD; -fx-padding: 10;");
        Label actionHeader = new Label("");

        serviceHeader.setPrefWidth(150);
        detailsHeader.setPrefWidth(400);

        table.add(serviceHeader, 0, 0);
        table.add(detailsHeader, 1, 0);
        table.add(actionHeader, 2, 0);

        addServiceRow(table, 1, "FEMDOM", "(Category, hourly rate, years of experience & description)");
        addServiceRow(table, 2, "69", "(Category, hourly rate, years of experience & description)");
        addServiceRow(table, 3, "SCAT PILGRIM", "(Category, hourly rate, years of experience & description)");

        VBox leftPane = new VBox(20, titleLabel, searchSortBox, table);
        leftPane.setPadding(new Insets(20));
        leftPane.setPrefWidth(800);

        Button scheduleBtn = createSidebarButton("Your Schedule");
        Button goBackBtn = createSidebarButton("Go Back");

        VBox rightPane = new VBox(30, scheduleBtn);
        rightPane.setPadding(new Insets(30));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setAlignment(Pos.TOP_CENTER);
        rightPane.setPrefWidth(250);

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        rightPane.getChildren().addAll(spacer, goBackBtn);

        Button cancelButton = createBigGreenButton("Cancel");
        Button saveButton = createBigGreenButton("Save Changes");

        HBox bottomButtons = new HBox(30, cancelButton, saveButton);
        bottomButtons.setAlignment(Pos.CENTER);
        bottomButtons.setPadding(new Insets(20));

        VBox leftWithBottom = new VBox(20, leftPane, bottomButtons);

        HBox root = new HBox(20, leftWithBottom, rightPane);
        root.setPadding(new Insets(20));

        this.scene = new Scene(root, 1100, 650);
        stage.setTitle("Services");
        stage.setScene(scene);
        stage.show();
    }

    private void addServiceRow(GridPane table, int rowIndex, String service, String details) {
        Label serviceLabel = new Label(service);
        serviceLabel.setPrefWidth(150);
        serviceLabel.setWrapText(true);

        Label detailsLabel = new Label(details);
        detailsLabel.setPrefWidth(400);
        detailsLabel.setWrapText(true);

        Button editBtn = createBigGreenButton("Edit");
        Button removeBtn = createBigGreenButton("Remove");

        HBox actionBox = new HBox(10, editBtn, removeBtn);
        actionBox.setAlignment(Pos.CENTER_RIGHT);
        actionBox.setPrefWidth(150);

        table.add(serviceLabel, 0, rowIndex);
        table.add(detailsLabel, 1, rowIndex);
        table.add(actionBox, 2, rowIndex);
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