//package view;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.CheckBoxTableCell;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.effect.DropShadow;
//import javafx.scene.layout.*;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//import javafx.scene.text.Text;
//import javafx.util.Callback;
//import model.AppointmentEntry;
//
//public class CaregiverAppointmentView {
//    private final VBox rootLayout;
//    private final TableView<AppointmentEntry> appointmentTable;
//    private final Button acceptBtn;
//    private final Button closeBtn;
//    private final Button declineBtn;
//
//    public CaregiverAppointmentView(Button acceptBtn, Button closeBtn, Button declineBtn) {
//        this.acceptBtn = acceptBtn;
//        this.closeBtn = closeBtn;
//        this.declineBtn = declineBtn;
//
//        rootLayout = new VBox();
//        rootLayout.setStyle("-fx-background-color: #F5F7FB;");
//        rootLayout.setPadding(new Insets(30));
//        rootLayout.setSpacing(20);
//
//        Text title = new Text("Appointments Dashboard");
//        title.setFont(Font.font("Segoe UI", 26));
//        title.setFill(Color.web("#333333"));
//
//        VBox card = new VBox(15);
//        card.setPadding(new Insets(20));
//        card.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(12), Insets.EMPTY)));
//        card.setEffect(new DropShadow(8, Color.rgb(0, 0, 0, 0.1)));
//        card.setStyle("-fx-border-color: #E0E0E0;");
//
//        appointmentTable = new TableView<>();
//        appointmentTable.setStyle("-fx-background-color: white; -fx-font-size: 13px;");
//        appointmentTable.setMinHeight(240);
//        appointmentTable.setMaxWidth(Double.MAX_VALUE);
//
//        TableColumn<AppointmentEntry, Boolean> selectCol = new TableColumn<>("Select");
//        selectCol.setCellValueFactory(new PropertyValueFactory<>("selected"));
//        selectCol.setCellFactory(CheckBoxTableCell.forTableColumn(selectCol)); // FIXED working checkbox
//
//        TableColumn<AppointmentEntry, String> guardianCol = createColumn("Guardian", "guardianName", 180);
//        TableColumn<AppointmentEntry, Integer> eldersCol = createColumn("Elders", "eldersCount", 80);
//        TableColumn<AppointmentEntry, String> timeCol = createColumn("Date & Time", "appointmentTime", 180);
//
//        appointmentTable.getColumns().addAll(selectCol, guardianCol, eldersCol, timeCol);
//        appointmentTable.setItems(getDummyAppointments());
//
//        // Set button styles
//        styleActionButton(acceptBtn, "#4CAF50");   // green
//        styleActionButton(declineBtn, "#E53935");  // red
//        styleCloseButton(closeBtn);                // no background
//
//        // Buttons: left side (accept, decline), right side (close)
//        HBox leftButtons = new HBox(15, acceptBtn, declineBtn);
//        leftButtons.setAlignment(Pos.CENTER_LEFT);
//
//        HBox rightButton = new HBox(closeBtn);
//        rightButton.setAlignment(Pos.CENTER_RIGHT);
//
//        BorderPane buttonBar = new BorderPane();
//        buttonBar.setLeft(leftButtons);
//        buttonBar.setRight(rightButton);
//        buttonBar.setPadding(new Insets(10, 0, 0, 0));
//
//        card.getChildren().addAll(new Text("Appointment Requests"), appointmentTable, buttonBar);
//        rootLayout.getChildren().addAll(title, card);
//    }
//
//    private <T> TableColumn<AppointmentEntry, T> createColumn(String title, String property, int width) {
//        TableColumn<AppointmentEntry, T> col = new TableColumn<>(title);
//        col.setCellValueFactory(new PropertyValueFactory<>(property));
//        col.setPrefWidth(width);
//        return col;
//    }
//
//    private void styleActionButton(Button btn, String colorHex) {
//        btn.setStyle(
//                "-fx-background-color: " + colorHex + ";" +
//                        "-fx-text-fill: white;" +
//                        "-fx-font-weight: bold;" +
//                        "-fx-font-size: 14px;" +
//                        "-fx-background-radius: 8px;" +
//                        "-fx-cursor: hand;"
//        );
//        btn.setMinHeight(36);
//        btn.setMinWidth(120);
//
//        btn.setOnMouseEntered(e -> {
//            btn.setScaleX(1.05);
//            btn.setScaleY(1.05);
//        });
//        btn.setOnMouseExited(e -> {
//            btn.setScaleX(1.0);
//            btn.setScaleY(1.0);
//        });
//    }
//
//    private void styleCloseButton(Button btn) {
//        btn.setStyle(
//                "-fx-background-color: transparent;" +
//                        "-fx-text-fill: #5E35B1;" +
//                        "-fx-font-weight: bold;" +
//                        "-fx-font-size: 14px;" +
//                        "-fx-cursor: hand;"
//        );
//        btn.setMinHeight(36);
//        btn.setMinWidth(120);
//    }
//
//    private ObservableList<AppointmentEntry> getDummyAppointments() {
//        return FXCollections.observableArrayList(
//                new AppointmentEntry("Alice Johnson", 2, "2025-04-10 10:00 AM"),
//                new AppointmentEntry("Bob Smith", 1, "2025-04-12 09:20 AM"),
//                new AppointmentEntry("Catherine Lee", 3, "2025-04-15 08:00 AM")
//        );
//    }
//
//    public VBox getRoot() {
//        return rootLayout;
//    }
//
//    public Scene getScene() {
//        return new Scene(rootLayout, 1000, 600);
//    }
//
//    public TableView<AppointmentEntry> getAppointmentTable() {
//        return appointmentTable;
//    }
//
//    public Button getAcceptButton() {
//        return acceptBtn;
//    }
//
//    public Button getCloseButton() {
//        return closeBtn;
//    }
//
//    public Button getDeclineButton() {
//        return declineBtn;
//    }
//}


package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CaregiverAppointmentView {

    private final Scene scene;

    public CaregiverAppointmentView(Stage stage) {

        // === Title and Search/Sort ===
        Label titleLabel = new Label("Your Appointments");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold;");

        TextField searchField = createRoundedTextField("(Searchfield)");
        ComboBox<String> sortBox = createRoundedComboBox("(Dropdown)");

        HBox searchSortBox = new HBox(20,
                new Label("Search:"), searchField,
                new Label("Sort by:"), sortBox);
        searchSortBox.setAlignment(Pos.CENTER_RIGHT);

        // === Appointments Table ===
        GridPane table = new GridPane();
        table.setHgap(20);
        table.setVgap(20);
        table.setPadding(new Insets(20));
        table.setStyle("-fx-border-color: #B891F1; -fx-border-width: 2; -fx-background-color: #F9F9F9;");
        table.setPrefWidth(750);

        // Header row
        Label guardiansHeader = new Label("Guardians");
        guardiansHeader.setStyle("-fx-font-weight: bold; -fx-background-color: #E2C8FD; -fx-padding: 10;");
        Label detailsHeader = new Label("Appointment Details");
        detailsHeader.setStyle("-fx-font-weight: bold; -fx-background-color: #E2C8FD; -fx-padding: 10;");
        Label actionHeader = new Label(""); // Empty header

        guardiansHeader.setPrefWidth(150);
        detailsHeader.setPrefWidth(400);

        table.add(guardiansHeader, 0, 0);
        table.add(detailsHeader, 1, 0);
        table.add(actionHeader, 2, 0);

        // Example data rows
        addAppointmentRow(table, 1, "Jose", "Date posted: 01/13/2025\nAppointment On: 02/02/2025");
        addAppointmentRow(table, 2, "Maria", "Date posted: 01/13/2025\nAppointment On: 02/02/2025");
        addAppointmentRow(table, 3, "Pedro", "Date posted: 01/13/2025\nAppointment On: 02/02/2025");

        VBox leftPane = new VBox(20, titleLabel, searchSortBox, table);
        leftPane.setPadding(new Insets(20));
        leftPane.setPrefWidth(800);

        // === Right Sidebar ===
        Button scheduleBtn = createSidebarButton("Your Schedule");
        Button goBackBtn = createSidebarButton("Go Back");

        VBox rightPane = new VBox(30, scheduleBtn);
        rightPane.setPadding(new Insets(30));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setAlignment(Pos.TOP_CENTER);
        rightPane.setPrefWidth(250);

        // Push Go Back button to bottom
        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        rightPane.getChildren().addAll(spacer, goBackBtn);

        // === Root Layout ===
        HBox root = new HBox(20, leftPane, rightPane);
        root.setPadding(new Insets(20));

        this.scene = new Scene(root, 1100, 600);
        stage.setTitle("Appointments");
        stage.setScene(scene);
        stage.show();
    }

    private void addAppointmentRow(GridPane table, int rowIndex, String guardian, String details) {
        Label guardianLabel = new Label(guardian);
        guardianLabel.setPrefWidth(150);
        guardianLabel.setWrapText(true);

        Label detailsLabel = new Label(details);
        detailsLabel.setPrefWidth(400);
        detailsLabel.setWrapText(true);

        Button approveBtn = createBigGreenButton("Approve");

        // Align button to the right using HBox
        HBox buttonBox = new HBox(approveBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPrefWidth(150);

        table.add(guardianLabel, 0, rowIndex);
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
