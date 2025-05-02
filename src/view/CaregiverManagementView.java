package view;//package view;
//
//import javafx.application.Application;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.*;
//import javafx.stage.Stage;
//import model.Caregiver;
//
//public class CaregiverManagementView extends Application {
//
//    @Override
//    public void start(Stage primaryStage) {
//        VBox root = new VBox(20);
//        root.setPadding(new Insets(20));
//        root.setAlignment(Pos.TOP_CENTER);
//        root.setStyle("-fx-background-color: #f0f4f7;");
//
//        // Title Section
//        Label title = new Label("Caregiver Management");
//        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");
//
//        // Caregiver Selection Table
//        TableView<Caregiver> caregiverTable = new TableView<>();
//        caregiverTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//        caregiverTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//
//        // Caregiver Columns
//        TableColumn<Caregiver, String> nameCol = new TableColumn<>("Name");
//        nameCol.setCellValueFactory(cell -> cell.getValue().nameProperty());
//
//        TableColumn<Caregiver, String> emailCol = new TableColumn<>("Email");
//        emailCol.setCellValueFactory(cell -> cell.getValue().emailProperty());
//
//        TableColumn<Caregiver, String> phoneCol = new TableColumn<>("Phone");
//        phoneCol.setCellValueFactory(cell -> cell.getValue().phoneProperty());
//
//        caregiverTable.getColumns().addAll(nameCol, emailCol, phoneCol);
//
//        // Form for Adding/Updating Caregiver
//        GridPane formGrid = new GridPane();
//        formGrid.setVgap(15);
//        formGrid.setHgap(10);
//        formGrid.setPadding(new Insets(10));
//        formGrid.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 8px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 8, 0, 0, 4);");
//
//        Label nameLabel = new Label("Full Name:");
//        TextField nameField = new TextField();
//
//        Label emailLabel = new Label("Email:");
//        TextField emailField = new TextField();
//
//        Label phoneLabel = new Label("Phone:");
//        TextField phoneField = new TextField();
//
//        formGrid.addColumn(0, nameLabel, emailLabel, phoneLabel);
//        formGrid.addColumn(1, nameField, emailField, phoneField);
//
//        // Buttons for Actions
//        Button addBtn = new Button("Add Caregiver");
//        addBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-border-radius: 5px; -fx-padding: 10 20;");
//
//        Button updateBtn = new Button("Update Caregiver");
//        updateBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16px; -fx-border-radius: 5px; -fx-padding: 10 20;");
//        updateBtn.setDisable(true);  // Initially disabled
//
//        Button deleteBtn = new Button("Delete Caregiver");
//        deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 16px; -fx-border-radius: 5px; -fx-padding: 10 20;");
//        deleteBtn.setDisable(true);  // Initially disabled
//
//        // Action for adding a new caregiver
//        addBtn.setOnAction(event -> {
//            // Create a new caregiver object
//            Caregiver newCaregiver = new Caregiver(nameField.getText(), emailField.getText(), phoneField.getText());
//            // In a real scenario, you would save this to the database
//            caregiverTable.getItems().add(newCaregiver);
//
//            // Clear the form fields
//            nameField.clear();
//            emailField.clear();
//            phoneField.clear();
//        });
//
//        // Action for updating an existing caregiver
//        updateBtn.setOnAction(event -> {
//            Caregiver selectedCaregiver = caregiverTable.getSelectionModel().getSelectedItem();
//            if (selectedCaregiver != null) {
//                selectedCaregiver.setName(nameField.getText());
//                selectedCaregiver.setEmail(emailField.getText());
//                selectedCaregiver.setPhone(phoneField.getText());
//
//                // In a real scenario, save the updated caregiver to the database
//
//                caregiverTable.refresh();  // Refresh the table to show the updated data
//            }
//        });
//
//        // Action for deleting a caregiver
//        deleteBtn.setOnAction(event -> {
//            Caregiver selectedCaregiver = caregiverTable.getSelectionModel().getSelectedItem();
//            if (selectedCaregiver != null) {
//                // In a real scenario, delete the caregiver from the database
//                caregiverTable.getItems().remove(selectedCaregiver);
//            }
//        });
//
//        // Handle selection from table to populate the form
//        caregiverTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                nameField.setText(newValue.getName());
//                emailField.setText(newValue.getEmail());
//                phoneField.setText(newValue.getPhone());
//
//                updateBtn.setDisable(false);  // Enable the update button
//                deleteBtn.setDisable(false);  // Enable the delete button
//            }
//        });
//
//        // Add components to the root layout
//        root.getChildren().addAll(title, caregiverTable, formGrid, addBtn, updateBtn, deleteBtn);
//
//        // Scene Setup
//        Scene scene = new Scene(root, 900, 600);
//        primaryStage.setTitle("Administrator - Caregiver Management");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CaregiverManagementView {

    private final TableView<Service> tableView = new TableView<>();
    private final TextField nameField = new TextField();
    private final TextArea descArea = new TextArea();

    private final Button addButton = new Button("Add");
    private final Button updateButton = new Button("Update");
    private final Button deleteButton = new Button("Delete");

    private final Scene scene;
    private final Stage stage;

    public CaregiverManagementView(Stage stage) {
        this.stage = stage;

        // Title
        Label titleLabel = new Label("Caregiver Service Management");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold;");

        // TableView setup
        setupTableView();

        // Input section
        VBox inputSection = createInputSection();

        // Buttons
        VBox buttonsBox = createButtonsBox();

        // Left content container
        VBox leftContent = new VBox(20, titleLabel, tableView, inputSection, buttonsBox);
        leftContent.setPadding(new Insets(20));
        leftContent.setStyle("-fx-background-color: #F0F0F0;");
        // Make the left content expand to fill available space
        HBox.setHgrow(leftContent, Priority.ALWAYS);

        // Right green layer
        VBox rightPane = new VBox();
        rightPane.setPrefWidth(200);
        rightPane.setStyle("-fx-background-color: #3BB49C; -fx-background-radius: 10;");

        // Main HBox container
        HBox mainContainer = new HBox();
        mainContainer.getChildren().addAll(leftContent, rightPane);
        mainContainer.setSpacing(10);
        mainContainer.setPadding(new Insets(20));

        scene = new Scene(mainContainer, 1000, 700);
        stage.setTitle("Caregiver Service Management");
        stage.setScene(scene);
        stage.show();
    }

    private void setupTableView() {
        tableView.setPrefHeight(250);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPlaceholder(new Label("No services added"));

        TableColumn<Service, String> nameCol = new TableColumn<>("Service Name");
        nameCol.setCellValueFactory(param -> param.getValue().nameProperty());

        TableColumn<Service, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(param -> param.getValue().descriptionProperty());

        tableView.getColumns().addAll(nameCol, descCol);
    }

    private VBox createInputSection() {
        Label nameLabel = new Label("Service Name:");
        Label descLabel = new Label("Description:");

        styleInputField(nameField);
        styleInputArea(descArea);

        VBox nameBox = new VBox(5, nameLabel, nameField);
        VBox descBox = new VBox(5, descLabel, descArea);

        VBox inputBox = new VBox(10, nameBox, descBox);
        inputBox.setPadding(new Insets(15));
        inputBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        inputBox.setEffect(new DropShadow(5, Color.GRAY));

        return inputBox;
    }

    private VBox createButtonsBox() {
        String buttonStyle = """
            -fx-background-radius: 8;
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-cursor: hand;
        """;

        styleActionButton(addButton, "#4CAF50", "#45a049", buttonStyle, true);
        styleActionButton(updateButton, "#2196F3", "#1976D2", buttonStyle, true);
        styleActionButton(deleteButton, "#F44336", "#d32f2f", buttonStyle, true);

        VBox buttonsBox = new VBox(12, addButton, updateButton, deleteButton);
        buttonsBox.setAlignment(Pos.CENTER);
        return buttonsBox;
    }

    private void styleInputField(TextField tf) {
        tf.setStyle("-fx-background-color: #d9d9d9; -fx-background-radius: 20; -fx-padding: 10 20;");
        tf.setPrefWidth(300);
    }

    private void styleInputArea(TextArea ta) {
        ta.setStyle("-fx-background-color: #d9d9d9; -fx-background-radius: 20; -fx-padding: 10 20;");
        ta.setPrefWidth(300);
    }

    private void styleActionButton(Button btn, String color, String hoverColor, String baseStyle, boolean smaller) {
        btn.setStyle("-fx-background-color: " + color + "; " + baseStyle + " -fx-text-fill: white;");
        if (smaller) {
            btn.setPrefWidth(130);
            btn.setPrefHeight(40);
        } else {
            btn.setPrefWidth(150);
            btn.setPrefHeight(40);
        }
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: " + hoverColor + "; " + baseStyle + " -fx-text-fill: white;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: " + color + "; " + baseStyle + " -fx-text-fill: white;"));
    }

    // Getters if needed
    public TableView<Service> getTableView() { return tableView; }
    public TextField getNameField() { return nameField; }
    public TextArea getDescArea() { return descArea; }
    public Button getAddButton() { return addButton; }
    public Button getUpdateButton() { return updateButton; }
    public Button getDeleteButton() { return deleteButton; }

    // Service class
    public static class Service {
        private final javafx.beans.property.SimpleStringProperty name;
        private final javafx.beans.property.SimpleStringProperty description;

        public Service(String name, String description) {
            this.name = new javafx.beans.property.SimpleStringProperty(name);
            this.description = new javafx.beans.property.SimpleStringProperty(description);
        }

        public javafx.beans.property.StringProperty nameProperty() { return name; }
        public javafx.beans.property.StringProperty descriptionProperty() { return description; }
    }
}