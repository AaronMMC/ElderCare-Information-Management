package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Guardian;

import java.sql.Connection;

public class GuardianAppointmentView {

    private final Scene scene;

    public GuardianAppointmentView(Stage stage, Connection conn, Guardian guardian) {
        // === Logo ===
        ImageView logo = new ImageView(new Image("file:resources/logo.png"));
        logo.setFitWidth(150);
        logo.setPreserveRatio(true);

        // === Title ===
        Label titleLabel = new Label("Your Appointments");
        titleLabel.setFont(Font.font("Arial", 28));
        titleLabel.setStyle("-fx-font-weight: bold;");

        // === Search/Sort Controls ===
        TextField searchField = createRoundedTextField("(Searchfield)");
        ComboBox<String> sortBox = createRoundedComboBox("(Dropdown)");

        HBox searchSortBox = new HBox(20,
                new Label("Search:"), searchField,
                new Label("Sort by:"), sortBox);
        searchSortBox.setAlignment(Pos.CENTER_RIGHT);

        // === Header Section (Title + Controls) ===
        VBox headerBox = new VBox(10, titleLabel, searchSortBox);
        headerBox.setAlignment(Pos.TOP_LEFT);

        // === Appointments Table ===
        GridPane table = new GridPane();
        table.setHgap(10);
        table.setVgap(10);
        table.setPadding(new Insets(20));
        table.setStyle("-fx-border-color: #CBA5F5; -fx-border-width: 2; -fx-background-color: #FDFBFF;");

        // Header row
        addHeaderRow(table);

        // Data rows
        addAppointmentRow(table, 1, "Jose", "Date posted: 01/13/2025\nStatus: Pending\nAppointment On: 02/02/2025\nBalance: 0.00Php\nDue on: xxx");
        addAppointmentRow(table, 2, "Maria", "Date posted: 01/13/2025\nStatus: Pending\nAppointment On: 02/02/2025\nBalance: 0.00Php\nDue on: xxx");
        addAppointmentRow(table, 3, "Pedro", "Date posted: 01/13/2025\nStatus: Pending\nAppointment On: 02/02/2025\nBalance: 0.00Php\nDue on: xxx");

        VBox leftPane = new VBox(20, logo, headerBox, table);
        leftPane.setPadding(new Insets(20));
        leftPane.setPrefWidth(850);

        // === Sidebar ===
        Button submitBtn = createSidebarButton("Submit an Appointment");
        Button goBackBtn = createSidebarButton("Go Back");

        submitBtn.setOnAction(e -> {
            AppointmentView appointmentView = new AppointmentView(stage, conn, guardian);
            stage.setScene(appointmentView.getScene());
        });

        goBackBtn.setOnAction(e -> {
            GuardianView guardianView = new GuardianView(stage, conn, guardian);
            stage.setScene(guardianView.getScene());
        });

        VBox rightPane = new VBox(30, submitBtn);
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setPadding(new Insets(30));
        rightPane.setAlignment(Pos.TOP_CENTER);
        rightPane.setPrefWidth(250);

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        rightPane.getChildren().addAll(spacer, goBackBtn);

        // === Root Layout ===
        HBox root = new HBox(20, leftPane, rightPane);
        root.setPadding(new Insets(20));

        this.scene = new Scene(root, 1150, 650);
        stage.setTitle("Guardian Appointments");
        stage.setScene(scene);
        stage.show();
    }

    private void addHeaderRow(GridPane table) {
        Label caregiverHeader = new Label("Caregivers");
        caregiverHeader.setStyle("-fx-font-weight: bold; -fx-background-color: #E6D1FB; -fx-padding: 10;");
        caregiverHeader.setPrefWidth(150);

        Label detailsHeader = new Label("Appointment Details");
        detailsHeader.setStyle("-fx-font-weight: bold; -fx-background-color: #E6D1FB; -fx-padding: 10;");
        detailsHeader.setPrefWidth(400);

        Label payHeader = new Label();
        payHeader.setPrefWidth(150);

        table.add(caregiverHeader, 0, 0);
        table.add(detailsHeader, 1, 0);
        table.add(payHeader, 2, 0);
    }

    private void addAppointmentRow(GridPane table, int rowIndex, String caregiver, String details) {
        Label caregiverLabel = new Label(caregiver);
        caregiverLabel.setPrefWidth(150);

        Label detailsLabel = new Label(details);
        detailsLabel.setWrapText(true);
        detailsLabel.setPrefWidth(400);

        Button payBtn = createPayButton("Pay");
        HBox btnBox = new HBox(payBtn);
        btnBox.setAlignment(Pos.CENTER_RIGHT);
        btnBox.setPrefWidth(150);

        table.add(caregiverLabel, 0, rowIndex);
        table.add(detailsLabel, 1, rowIndex);
        table.add(btnBox, 2, rowIndex);
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

    private Button createPayButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("""
            -fx-background-color: #3BB49C;
            -fx-text-fill: white;
            -fx-font-size: 14px;
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
