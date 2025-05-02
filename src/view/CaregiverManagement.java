package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CaregiverManagement {

    private Stage stage;

    public CaregiverManagement(Stage primaryStage) {
        this.stage = primaryStage;
        setupUI();
    }

    private void setupUI() {
        // Title label
        Label titleLabel = new Label("Caregiver Management");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold;");
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setPadding(new Insets(10));

        // TableView setup
        TableView<Caregiver> table = new TableView<>();
        table.setPlaceholder(new Label("No content in table"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(200);

        TableColumn<Caregiver, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> data.getValue().nameProperty());

        TableColumn<Caregiver, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> data.getValue().emailProperty());

        TableColumn<Caregiver, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(data -> data.getValue().phoneProperty());

        table.getColumns().addAll(nameCol, emailCol, phoneCol);

        // Form for input
        Label nameLabel = new Label("Full Name:");
        TextField nameField = new TextField();

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();

        Label phoneLabel = new Label("Phone:");
        TextField phoneField = new TextField();

        VBox formBox = new VBox(10,
                new VBox(5, nameLabel, nameField),
                new VBox(5, emailLabel, emailField),
                new VBox(5, phoneLabel, phoneField)
        );
        formBox.setPadding(new Insets(15));
        formBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        formBox.setEffect(new DropShadow(3, Color.LIGHTGRAY));

        // Buttons
        Button addBtn = new Button("Add Caregiver");
        styleActionButton(addBtn, "#4CAF50", "#45a049");
        Button updateBtn = new Button("Update Caregiver");
        styleActionButton(updateBtn, "#2196F3", "#1976D2");
        Button deleteBtn = new Button("Delete Caregiver");
        styleActionButton(deleteBtn, "#F44336", "#d32f2f");

        VBox buttonsBox = new VBox(10, addBtn, updateBtn, deleteBtn);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setPadding(new Insets(10));

        // Left content VBox
        VBox leftContent = new VBox(15, titleLabel, table, formBox, buttonsBox);
        leftContent.setPadding(new Insets(20));
        leftContent.setStyle("-fx-background-color: #F0F0F0;");
        VBox.setVgrow(table, Priority.ALWAYS);

        // Right green layer (proportional width)
        VBox rightPane = new VBox();
        rightPane.setStyle("-fx-background-color: #3BB49C; -fx-background-radius: 10;");

        // Main container
        HBox mainContainer = new HBox(10, leftContent, rightPane);
        mainContainer.setPadding(new Insets(20));

        // Scene creation
        Scene scene = new Scene(mainContainer, 1000, 700);

        // Set proportional widths (e.g., 30% for right pane)
        double rightPanelProportion = 0.3; // 30%
        double leftPanelProportion = 1 - rightPanelProportion; // 70%

        rightPane.prefWidthProperty().bind(scene.widthProperty().multiply(rightPanelProportion));
        leftContent.prefWidthProperty().bind(scene.widthProperty().multiply(leftPanelProportion));

        stage.setTitle("Caregiver Management");
        stage.setScene(scene);
        stage.show();
    }

    private void styleActionButton(Button btn, String color, String hoverColor) {
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 8;");
        btn.setPrefWidth(180);
        btn.setPrefHeight(40);
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: " + hoverColor + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 8;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 8;"));
    }

    // Model class
    public static class Caregiver {
        private final javafx.beans.property.SimpleStringProperty name;
        private final javafx.beans.property.SimpleStringProperty email;
        private final javafx.beans.property.SimpleStringProperty phone;

        public Caregiver(String name, String email, String phone) {
            this.name = new javafx.beans.property.SimpleStringProperty(name);
            this.email = new javafx.beans.property.SimpleStringProperty(email);
            this.phone = new javafx.beans.property.SimpleStringProperty(phone);
        }

        public javafx.beans.property.StringProperty nameProperty() { return name; }
        public javafx.beans.property.StringProperty emailProperty() { return email; }
        public javafx.beans.property.StringProperty phoneProperty() { return phone; }
    }
}