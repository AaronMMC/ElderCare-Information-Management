package view;

import controller.GuardianElderController;
import dao.ElderDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Elder;
import model.Guardian;
import model.GuardianElder;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GuardianElderView {

    private final Scene scene;
    private final ObservableList<Elder> allElders = FXCollections.observableArrayList();
    private final VBox elderListContainer = new VBox(20);
    private final TextField searchField = new TextField();
    private final ComboBox<String> sortComboBox = new ComboBox<>();

    public GuardianElderView(Stage stage, Connection conn, Guardian guardian) {
        Label titleLabel = new Label("Your Elders");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold;");

        searchField.setPromptText("Search elder by name...");
        searchField.textProperty().addListener((obs, oldVal, newVal) -> refreshElderList(conn, guardian));

        sortComboBox.getItems().addAll(
                "Age Ascending", "Age Descending",
                "Name Ascending", "Name Descending"
        );
        sortComboBox.setValue("Name Ascending");
        sortComboBox.setOnAction(e -> refreshElderList(conn, guardian));

        HBox filterBar = new HBox(10, searchField, sortComboBox);
        filterBar.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(elderListContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);

        VBox leftPane = new VBox(20, titleLabel, filterBar, scrollPane);
        leftPane.setPadding(new Insets(20));
        leftPane.setPrefWidth(800);

        Button addElderBtn = createSidebarButton("Add an Elder");
        Button backButton = createSidebarButton("Back");

        addElderBtn.setOnAction(e -> stage.setScene(new ElderView(stage, conn, guardian).getScene()));
        backButton.setOnAction(e -> stage.setScene(new GuardianView(stage, conn, guardian).getScene()));

        VBox rightPane = new VBox(30, addElderBtn);
        rightPane.setPadding(new Insets(30));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setAlignment(Pos.TOP_CENTER);
        rightPane.setPrefWidth(250);
        VBox.setVgrow(rightPane, Priority.ALWAYS);
        rightPane.getChildren().add(backButton);

        HBox root = new HBox(20, leftPane, rightPane);
        root.setPadding(new Insets(20));

        this.scene = new Scene(root, 1100, 600);
        stage.setTitle("Your Elders");
        stage.setScene(scene);
        stage.show();

        refreshElderList(conn, guardian);
    }

    private void refreshElderList(Connection conn, Guardian guardian) {
        GuardianElderController guardianElderController = new GuardianElderController(conn);

        // Already returns List<Elder> — no need to map GuardianElder
        List<Elder> elders = guardianElderController.getAllEldersByGuardianId(guardian.getGuardianID());
        allElders.setAll(elders);

        String searchText = searchField.getText().toLowerCase();
        List<Elder> filtered = allElders.stream()
                .filter(e -> (e.getFirstName() + " " + e.getLastName()).toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        Comparator<Elder> comparator;
        switch (sortComboBox.getValue()) {
            case "Age Ascending" -> comparator = Comparator.comparing(e -> getAge(e.getDateOfBirth().toLocalDate()));
            case "Age Descending" -> comparator = Comparator.comparing((Elder e) -> getAge(e.getDateOfBirth().toLocalDate())).reversed();
            case "Name Descending" -> comparator = Comparator.comparing(Elder::getFirstName)
                    .thenComparing(Elder::getLastName)
                    .reversed();
            default -> comparator = Comparator.comparing(Elder::getFirstName)
                    .thenComparing(Elder::getLastName);
        }

        filtered.sort(comparator);

        elderListContainer.getChildren().clear();
        for (Elder elder : filtered) {
            elderListContainer.getChildren().add(createElderRow(elder, guardian, conn)); // pass guardian to get relationship
        }
    }


    private HBox createElderRow(Elder elder, Guardian guardian, Connection conn) {
        GuardianElderController guardianElderController = new GuardianElderController(conn);
        GuardianElder relationship = guardianElderController.getGuardianElderRelationshipByIds(guardian.getGuardianID(), elder.getElderID());
        String relationshipType = (relationship != null) ? relationship.getRelationshipType() : "Unknown";

        Label nameLabel = new Label(elder.getFirstName() + " " + elder.getLastName());
        nameLabel.setPrefWidth(150);

        TextField firstNameField = new TextField(elder.getFirstName());
        TextField lastNameField = new TextField(elder.getLastName());
        TextField contactField = new TextField(elder.getContactNumber());
        TextField emailField = new TextField(elder.getEmail());
        TextField addressField = new TextField(elder.getAddress());
        DatePicker dobPicker = new DatePicker(elder.getDateOfBirth().toLocalDate());
        Label ageLabel = new Label("Age: " + getAge(elder.getDateOfBirth().toLocalDate()));
        TextField relationshipField = new TextField(relationshipType); // Editable relationship

        VBox detailBox = new VBox(5, firstNameField, lastNameField, dobPicker, ageLabel, contactField, emailField, addressField, relationshipField);
        detailBox.setPrefWidth(400);

        Button editBtn = createBigGreenButton("Edit");
        Button saveBtn = createBigGreenButton("Save");
        Button cancelBtn = createBigGreenButton("Cancel");
        Button deleteBtn = createBigGreenButton("Delete");

        saveBtn.setDisable(true);
        cancelBtn.setDisable(true);

        editBtn.setOnAction(e -> {
            setEditable(true, firstNameField, lastNameField, contactField, emailField, addressField, dobPicker, relationshipField);
            saveBtn.setDisable(false);
            cancelBtn.setDisable(false);
        });

        cancelBtn.setOnAction(e -> {
            firstNameField.setText(elder.getFirstName());
            lastNameField.setText(elder.getLastName());
            contactField.setText(elder.getContactNumber());
            emailField.setText(elder.getEmail());
            addressField.setText(elder.getAddress());
            dobPicker.setValue(elder.getDateOfBirth().toLocalDate());
            relationshipField.setText(relationshipType);
            setEditable(false, firstNameField, lastNameField, contactField, emailField, addressField, dobPicker, relationshipField);
            saveBtn.setDisable(true);
            cancelBtn.setDisable(true);
        });

        saveBtn.setOnAction(e -> {
            elder.setFirstName(firstNameField.getText());
            elder.setLastName(lastNameField.getText());
            elder.setContactNumber(contactField.getText());
            elder.setEmail(emailField.getText());
            elder.setAddress(addressField.getText());
            elder.setDateOfBirth(dobPicker.getValue().atStartOfDay());
            new ElderDAO(conn).updateElder(elder);

            // Update GuardianElder relationship
            GuardianElder updatedGE = new GuardianElder(guardian.getGuardianID(), elder.getElderID(), relationshipField.getText());
            guardianElderController.updateGuardianElderRelationship(updatedGE);

            refreshElderList(conn, guardian);
        });

        deleteBtn.setOnAction(e -> {
            new ElderDAO(conn).deleteElder(elder.getElderID());
            refreshElderList(conn, guardian);
        });

        VBox buttonBox = new VBox(10, editBtn, saveBtn, cancelBtn, deleteBtn);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPrefWidth(150);

        return new HBox(20, nameLabel, detailBox, buttonBox);
    }

    private void setEditable(boolean editable, Object... controls) {
        for (Object control : controls) {
            if (control instanceof TextInputControl textField) {
                textField.setEditable(editable);
            } else if (control instanceof DatePicker datePicker) {
                datePicker.setDisable(!editable); // Enable if editing, disable otherwise
            }
        }
    }

    private int getAge(LocalDate dob) {
        return Period.between(dob, LocalDate.now()).getYears();
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
            -fx-padding: 10 20;
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