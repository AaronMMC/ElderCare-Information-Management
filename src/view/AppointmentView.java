package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SubmitAppointmentView {

    private final Scene scene;

    public SubmitAppointmentView(Stage stage) {

        // === Title ===
        Label titleLabel = new Label("Submit an Appointment");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold;");

        // === Left Section: Elders Selection ===
        VBox elderBox = createRoundedSection("(Checkbox)");
        Label selectEldersLabel = new Label("Select Elders:");
        selectEldersLabel.setStyle("-fx-font-weight: bold;");
        VBox elderSelection = new VBox(10, selectEldersLabel, elderBox);

        // === Middle Section: Services & Time ===
        VBox serviceBox = createRoundedSection("(Checkbox)");
        Label serviceLabel = new Label("Service to avail:");
        Label filterLabel = new Label("Filter by:");
        ComboBox<String> filterDropdown = createRoundedComboBox("(Dropdown)");
        HBox filterBox = new HBox(10, filterLabel, filterDropdown);
        filterBox.setAlignment(Pos.CENTER_LEFT);

        VBox serviceSection = new VBox(10, serviceLabel, filterBox, serviceBox);

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("(Date)");
        datePicker.setStyle(getInputFieldStyle());

        ComboBox<String> durationBox = createRoundedComboBox("(Duration)");

        Label amountLabel = new Label("Amount To Be Paid: ....");

        HBox dateTimeBox = new HBox(20, datePicker, durationBox);
        dateTimeBox.setAlignment(Pos.CENTER_LEFT);

        VBox centerBox = new VBox(15, serviceSection, new Label("Choose a date & time:"), dateTimeBox, amountLabel);

        // === Buttons ===
        Button cancelBtn = createMainButton("Cancel");
        Button submitBtn = createMainButton("Submit");

        HBox actionButtons = new HBox(20, cancelBtn, submitBtn);
        actionButtons.setAlignment(Pos.CENTER_LEFT);

        VBox leftPane = new VBox(20, titleLabel, elderSelection, centerBox, actionButtons);
        leftPane.setPadding(new Insets(20));
        leftPane.setPrefWidth(800);

        // === Right Sidebar ===
        Label caregiverLabel = new Label("Select a Caregiver:");
        caregiverLabel.setStyle("-fx-font-weight: bold;");
        ComboBox<String> caregiverDropdown = createRoundedComboBox("(Dropdown)");

        Label certsLabel = new Label("Certifications:");
        certsLabel.setStyle("-fx-font-weight: bold;");
        VBox certBox = createRoundedSection("Scrollable by (5) list of certifications");
        certBox.setPrefHeight(150);

        Label infoLabel = new Label("More info:");
        infoLabel.setStyle("-fx-font-weight: bold;");
        VBox infoBox = createRoundedSection("(i.e. age, gender, employment type,\ncontact infos)");
        infoBox.setPrefHeight(100);

        VBox rightPane = new VBox(20, caregiverLabel, caregiverDropdown, certsLabel, certBox, infoLabel, infoBox);
        rightPane.setPadding(new Insets(30));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setPrefWidth(300);

        // === Root Layout ===
        HBox root = new HBox(30, leftPane, rightPane);
        root.setPadding(new Insets(20));

        this.scene = new Scene(root, 1200, 650);
        stage.setTitle("Submit Appointment");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createRoundedSection(String placeholderText) {
        Label label = new Label(placeholderText);
        label.setStyle("-fx-font-style: italic;");
        VBox box = new VBox(label);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: #D9D9D9; -fx-background-radius: 20;");
        box.setPadding(new Insets(40));
        box.setPrefWidth(250);
        return box;
    }

    private ComboBox<String> createRoundedComboBox(String prompt) {
        ComboBox<String> cb = new ComboBox<>();
        cb.setPromptText(prompt);
        cb.setStyle(getInputFieldStyle());
        return cb;
    }

    private String getInputFieldStyle() {
        return """
            -fx-background-radius: 15;
            -fx-border-radius: 15;
            -fx-background-color: #D9D9D9;
            -fx-border-color: transparent;
            -fx-padding: 8 12;
        """;
    }

    private Button createMainButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("""
            -fx-background-color: #3BB49C;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-background-radius: 20;
            -fx-padding: 10 30;
            -fx-cursor: hand;
        """);
        return btn;
    }

    public Scene getScene() {
        return scene;
    }
}
