package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Elder;

public class GuardianElderInfoView extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #f0f4f7;"); // Light background color

        // Title Styling
        Label title = new Label("Add or View Elder Information");
        title.getStyleClass().add("title");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        // Form Section
        GridPane formGrid = new GridPane();
        formGrid.setVgap(15);
        formGrid.setHgap(10);
        formGrid.setPadding(new Insets(10));
        formGrid.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 8px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 8, 0, 0, 4);");

        // Labels and Inputs
        Label nameLabel = new Label("Full Name:");
        TextField nameField = new TextField();
        nameField.setStyle("-fx-background-color: #fafafa; -fx-border-radius: 5px; -fx-border-color: #ccc;");

        Label ageLabel = new Label("Age:");
        TextField ageField = new TextField();
        ageField.setStyle("-fx-background-color: #fafafa; -fx-border-radius: 5px; -fx-border-color: #ccc;");

        Label sexLabel = new Label("Sex:");
        ComboBox<String> sexCombo = new ComboBox<>();
        sexCombo.getItems().addAll("Male", "Female", "Other");
        sexCombo.setValue("Male");
        sexCombo.setStyle("-fx-background-color: #fafafa; -fx-border-radius: 5px; -fx-border-color: #ccc;");

        Label healthLabel = new Label("Health Condition:");
        TextField healthField = new TextField();
        healthField.setStyle("-fx-background-color: #fafafa; -fx-border-radius: 5px; -fx-border-color: #ccc;");

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        emailField.setStyle("-fx-background-color: #fafafa; -fx-border-radius: 5px; -fx-border-color: #ccc;");

        Label contactLabel = new Label("Contact Number:");
        TextField contactField = new TextField();
        contactField.setStyle("-fx-background-color: #fafafa; -fx-border-radius: 5px; -fx-border-color: #ccc;");

        formGrid.addColumn(0, nameLabel, ageLabel, sexLabel, healthLabel, emailLabel, contactLabel);
        formGrid.addColumn(1, nameField, ageField, sexCombo, healthField, emailField, contactField);

        // Drag-and-drop area
        Label dropLabel = new Label("Drag and drop a medical record (PDF) here");
        dropLabel.setStyle("-fx-border-color: #ccc; -fx-border-style: dashed; -fx-padding: 20; -fx-background-color: #f8f8f8; -fx-font-style: italic;");
        dropLabel.setAlignment(Pos.CENTER);

        // Add Elder Button
        Button addElderBtn = new Button("Add Elder");
        addElderBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-border-radius: 5px; -fx-padding: 10 20;");

        // Table View Styling
        TableView<Elder> elderTable = new TableView<>();
        elderTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Columns Setup
        TableColumn<Elder, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cell -> cell.getValue().nameProperty());

        TableColumn<Elder, String> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(cell -> cell.getValue().ageProperty());

        TableColumn<Elder, String> sexCol = new TableColumn<>("Sex");
        sexCol.setCellValueFactory(cell -> cell.getValue().sexProperty());

        TableColumn<Elder, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(cell -> cell.getValue().emailProperty());

        TableColumn<Elder, String> contactCol = new TableColumn<>("Contact");
        contactCol.setCellValueFactory(cell -> cell.getValue().contactProperty());

        TableColumn<Elder, String> healthCol = new TableColumn<>("Medical");
        healthCol.setCellValueFactory(cell -> cell.getValue().healthConditionProperty());

        elderTable.getColumns().addAll(nameCol, ageCol, sexCol, emailCol, contactCol, healthCol);
        elderTable.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 8px;");

        // Adding Components to Root
        root.getChildren().addAll(title, formGrid, dropLabel, addElderBtn, elderTable);

        // Scene Setup
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setTitle("Guardian - Elder Info");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
