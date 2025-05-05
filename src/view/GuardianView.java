////package view;
////
////import controller.GuardianController;
////import controller.GuardianElderController;
////import javafx.geometry.Insets;
////import javafx.scene.Scene;
////import javafx.scene.layout.VBox;
////import javafx.stage.Stage;
////import model.Guardian;
////
////import java.sql.Connection;
////
////public class GuardianView {
////
////    private final GuardianController guardianController;
////    private final GuardianElderController guardianElderController;
////    private final Scene scene;
////
////    public GuardianView(Stage stage, Connection conn, Guardian guardian) {
////        this.guardianController = new GuardianController(conn);
////        this.guardianElderController = new GuardianElderController(conn);
////
////        // GUI Layout
////        VBox root = new VBox(10);
////        root.setPadding(new Insets(20));
////
////        // TODO: Add GUI components to root
////
////        this.scene = new Scene(root, 500, 400);
////        stage.setTitle("Guardian Dashboard");
////        stage.setScene(scene);
////        stage.show();
////    }
////
////    public Scene getScene() {
////        return scene;
////    }
////}
//
//
//package view;
//
//import controller.GuardianController;
//import controller.GuardianElderController;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.*;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//import javafx.stage.Stage;
//import model.Guardian;
//
//import java.sql.Connection;
//
//public class GuardianView {
//
//    private final GuardianController guardianController;
//    private final GuardianElderController guardianElderController;
//    private final Scene scene;
//
//    public GuardianView(Stage stage, Connection conn, Guardian guardian) {
//        this.guardianController = new GuardianController(conn);
//        this.guardianElderController = new GuardianElderController(conn);
//
//        // === Left Pane: Appointment Form ===
//        Label titleLabel = new Label("Guardian Homepage");
//        titleLabel.setFont(Font.font("Arial", 24));
//        titleLabel.setStyle("-fx-font-weight: bold;");
//
//        VBox formBox = new VBox(15,
//                createLabeledField("Caregiver:"),
//                createLabeledField("Service:"),
//                createLabeledDatePicker("Date:"),
//                new Label("Total Appointment:"),
//                createGreenButton("Submit Appointment")
//        );
//        formBox.setAlignment(Pos.TOP_LEFT);
//
//        VBox leftPane = new VBox(30, titleLabel, formBox);
//        leftPane.setPadding(new Insets(40));
//        leftPane.setStyle("-fx-background-color: white;");
//        leftPane.setPrefWidth(500);
//
//        // === Right Pane: Elders List ===
//        VBox rightPane = new VBox(20);
//        rightPane.setPadding(new Insets(30));
//        rightPane.setStyle("-fx-background-color: #3BB49C;");
//        rightPane.setPrefWidth(300);
//
//        Label eldersTitle = new Label("ELDERS:");
//        eldersTitle.setFont(Font.font("Arial", 20));
//        eldersTitle.setStyle("-fx-font-weight: bold;");
//        eldersTitle.setTextFill(Color.WHITE);
//
//        VBox elder1 = createElderCard("Juan Dela Cruz");
//        VBox elder2 = createElderCard("Maria Santos");
//
//        HBox bottomButtons = new HBox(10,
//                createWhiteButton("Add Elder"),
//                createWhiteButton("View All appointments")
//        );
//        bottomButtons.setAlignment(Pos.CENTER);
//
//        rightPane.getChildren().addAll(eldersTitle, elder1, elder2, bottomButtons);
//        rightPane.setAlignment(Pos.TOP_CENTER);
//
//        // === Combine Panes ===
//        HBox root = new HBox(leftPane, rightPane);
//
//        this.scene = new Scene(root, 800, 500);
//        stage.setTitle("Guardian Dashboard");
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    // === Styled Component Helpers ===
//
//    private VBox createLabeledField(String labelText) {
//        Label label = new Label(labelText);
//        TextField textField = new TextField();
//        textField.setStyle("-fx-background-color: #d9d9d9; -fx-background-radius: 20; -fx-padding: 10 20;");
//        textField.setPrefWidth(300);
//        return new VBox(label, textField);
//    }
//
//    private VBox createLabeledDatePicker(String labelText) {
//        Label label = new Label(labelText);
//        DatePicker datePicker = new DatePicker();
//        datePicker.setStyle("-fx-background-color: #d9d9d9; -fx-background-radius: 20; -fx-padding: 10 20;");
//        datePicker.setPrefWidth(300);
//        return new VBox(label, datePicker);
//    }
//
//    private Button createGreenButton(String text) {
//        Button btn = new Button(text);
//        btn.setStyle("-fx-background-color: #3BB49C; -fx-text-fill: black; -fx-font-size: 16px; -fx-background-radius: 15;");
//        btn.setPrefWidth(220);
//        btn.setPrefHeight(40);
//        return btn;
//    }
//
//    private Button createWhiteButton(String text) {
//        Button btn = new Button(text);
//        btn.setStyle("""
//            -fx-background-color: white;
//            -fx-text-fill: black;
//            -fx-font-size: 14px;
//            -fx-background-radius: 15;
//            -fx-border-color: #ccc;
//            -fx-border-radius: 15;
//            -fx-cursor: hand;
//            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 1);
//        """);
//        btn.setPrefHeight(35);
//        return btn;
//    }
//
//
//
//    private VBox createElderCard(String name) {
//        VBox card = new VBox(8);
//        card.setPadding(new Insets(10));
//        card.setStyle("-fx-background-color: white; -fx-background-radius: 15;");
//        card.setMaxWidth(220);
//        card.setAlignment(Pos.CENTER);
//
//        Label nameLabel = new Label(name);
//        nameLabel.setFont(Font.font("Arial", 16));
//        nameLabel.setStyle("-fx-font-weight: bold;");
//
//        Button editButton = createWhiteButton("Edit");
//        Button deleteButton = createWhiteButton("Delete");
//
//        // === Add click handlers ===
//        editButton.setOnAction(e -> {
//            System.out.println("Edit clicked for " + name);
//            // TODO: Replace with actual edit logic
//        });
//
//        deleteButton.setOnAction(e -> {
//            System.out.println("Delete clicked for " + name);
//            // TODO: Replace with actual delete logic
//        });
//
//        HBox buttons = new HBox(10, editButton, deleteButton);
//        buttons.setAlignment(Pos.CENTER);
//
//        card.getChildren().addAll(nameLabel, buttons);
//        return card;
//    }
//
//
//    public Scene getScene() {
//        return scene;
//    }
//}


//package view;
//
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.*;
//import javafx.scene.text.Font;
//import javafx.stage.Stage;
//import model.Guardian;
//
//import java.sql.Connection;
//
//public class GuardianView {
//
//    private final Scene scene;
//
//    public GuardianView(Stage stage, Connection conn, Guardian guardian) {
//        // === Left Form Layout ===
//        Label titleLabel = new Label("Guardian Homepage");
//        titleLabel.setFont(Font.font("Arial", 28));
//        titleLabel.setStyle("-fx-font-weight: bold;");
//
//        GridPane formGrid = new GridPane();
//        formGrid.setHgap(20);
//        formGrid.setVgap(15);
//        formGrid.setPadding(new Insets(20));
//
//        formGrid.add(createFormField("First Name:"), 0, 0);
//        formGrid.add(createFormField("Last Name:"), 1, 0);
//        formGrid.add(createFormField("Birthday:"), 2, 0);
//        formGrid.add(createDropdownField("Gender:"), 0, 1);
//        formGrid.add(createFormField("Contact Number:"), 1, 1);
//        formGrid.add(createFormField("Email:"), 2, 1);
//        formGrid.add(createFormField("Address:"), 0, 2);
//
//        HBox actionButtons = new HBox(20,
//                createTealButton("Cancel"),
//                createTealButton("Save Changes")
//        );
//        actionButtons.setAlignment(Pos.CENTER_LEFT);
//        actionButtons.setPadding(new Insets(30, 0, 0, 0));
//
//        VBox leftContent = new VBox(20, titleLabel, formGrid, actionButtons);
//        leftContent.setPadding(new Insets(40));
//        leftContent.setPrefWidth(800);
//        leftContent.setStyle("-fx-background-color: white;");
//
//        // === Right Menu Layout ===
//        VBox rightPane = new VBox(40,
//                createMenuButton("Your Appointments"),
//                createMenuButton("Your Elders")
//        );
//        Button logOutButton = createMenuButton("Log Out");
//        VBox.setVgrow(logOutButton, Priority.ALWAYS);  // This makes sure the log out button goes to the bottom
//
//        rightPane.getChildren().add(logOutButton);  // Add "Log Out" at the end
//        rightPane.setAlignment(Pos.TOP_CENTER);
//        rightPane.setPadding(new Insets(60, 20, 20, 20));
//        rightPane.setStyle("-fx-background-color: #3BB49C;");
//        rightPane.setPrefWidth(300);
//
//        // === Root Layout ===
//        HBox root = new HBox(leftContent, rightPane);
//        this.scene = new Scene(root, 1100, 600);
//        stage.setTitle("Guardian Homepage");
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    private VBox createFormField(String labelText) {
//        Label label = new Label(labelText);
//        TextField field = new TextField();
//        field.setStyle("-fx-background-color: #d9d9d9; -fx-background-radius: 20;");
//        field.setPrefWidth(200);
//        return new VBox(5, label, field);
//    }
//
//    private VBox createDropdownField(String labelText) {
//        Label label = new Label(labelText);
//        ComboBox<String> comboBox = new ComboBox<>();
//        comboBox.getItems().addAll("Male", "Female", "Other");
//        comboBox.setStyle("-fx-background-color: #d9d9d9; -fx-background-radius: 20;");
//        comboBox.setPrefWidth(200);
//        return new VBox(5, label, comboBox);
//    }
//
//    private Button createTealButton(String text) {
//        Button btn = new Button(text);
//        btn.setStyle("""
//        -fx-background-color: #3BB49C;
//        -fx-text-fill: white;
//        -fx-font-size: 18px;  /* Increased font size */
//        -fx-background-radius: 20;
//    """);
//        btn.setPrefHeight(50);  // Increased button height
//        btn.setPrefWidth(200);  // Increased button width
//        return btn;
//    }
//
//
//    private Button createMenuButton(String text) {
//        Button btn = new Button(text);
//        btn.setStyle("""
//            -fx-background-color: white;
//            -fx-text-fill: black;
//            -fx-font-size: 16px;
//            -fx-background-radius: 20;
//        """);
//        btn.setPrefWidth(240);
//        btn.setPrefHeight(60);
//        return btn;
//    }
//
//    public Scene getScene() {
//        return scene;
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
import javafx.scene.layout.Priority;  // Import for VBox.setVgrow
import model.Guardian;

import java.sql.Connection;

public class GuardianView {

    private final Scene scene;

    public GuardianView(Stage stage, Connection conn, Guardian guardian) {
        // === Left Form Layout ===
        Label titleLabel = new Label("Guardian Homepage");
        titleLabel.setFont(Font.font("Arial", 28));
        titleLabel.setStyle("-fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(20);
        formGrid.setVgap(15);
        formGrid.setPadding(new Insets(20));

        formGrid.add(createFormField("First Name:"), 0, 0);
        formGrid.add(createFormField("Last Name:"), 1, 0);
        formGrid.add(createFormField("Birthday:"), 2, 0);
        formGrid.add(createDropdownField("Gender:"), 0, 1);
        formGrid.add(createFormField("Contact Number:"), 1, 1);
        formGrid.add(createFormField("Email:"), 2, 1);
        formGrid.add(createFormField("Address:"), 0, 2);

        HBox actionButtons = new HBox(20,
                createTealButton("Cancel"),
                createTealButton("Save Changes")
        );
        actionButtons.setAlignment(Pos.CENTER_LEFT);
        actionButtons.setPadding(new Insets(30, 0, 0, 0));

        VBox leftContent = new VBox(20, titleLabel, formGrid, actionButtons);
        leftContent.setPadding(new Insets(40));
        leftContent.setPrefWidth(800);
        leftContent.setStyle("-fx-background-color: white;");

        // === Right Menu Layout ===
        VBox rightPane = new VBox(40,
                createMenuButton("Your Appointments"),
                createMenuButton("Your Elders")
        );
        Button logOutButton = createMenuButton("Log Out");

        // Push Log Out button to the bottom
        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);  // This makes sure the Log Out button goes to the bottom
        rightPane.getChildren().addAll(spacer, logOutButton);

        rightPane.setAlignment(Pos.TOP_CENTER);
        rightPane.setPadding(new Insets(60, 20, 20, 20));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setPrefWidth(300);

        // === Root Layout ===
        HBox root = new HBox(leftContent, rightPane);
        this.scene = new Scene(root, 1100, 600);
        stage.setTitle("Guardian Homepage");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createFormField(String labelText) {
        Label label = new Label(labelText);
        TextField field = new TextField();
        field.setStyle("-fx-background-color: #d9d9d9; -fx-background-radius: 20;");
        field.setPrefWidth(200);
        return new VBox(5, label, field);
    }

    private VBox createDropdownField(String labelText) {
        Label label = new Label(labelText);
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Male", "Female", "Other");
        comboBox.setStyle("-fx-background-color: #d9d9d9; -fx-background-radius: 20;");
        comboBox.setPrefWidth(200);
        return new VBox(5, label, comboBox);
    }

    private Button createTealButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("""
            -fx-background-color: #3BB49C;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-background-radius: 20;
        """);
        btn.setPrefHeight(40);
        btn.setPrefWidth(150);
        return btn;
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("""
            -fx-background-color: white;
            -fx-text-fill: black;
            -fx-font-size: 16px;
            -fx-background-radius: 20;
        """);
        btn.setPrefWidth(240);
        btn.setPrefHeight(60);
        return btn;
    }

    public Scene getScene() {
        return scene;
    }
}
