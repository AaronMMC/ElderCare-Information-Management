package view;

import controller.ActivityController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Activity;
import model.Appointment;
import model.Caregiver;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ActivityView {

    private final Scene scene;
    private final ActivityController activityController;
    private final List<Activity> activityList;
    private final TableView<Activity> activityTableView;
    private final Button saveBtn;
    private final Button cancelBtn;
    private Activity currentlyEditing;

    public ActivityView(Stage stage, Connection conn, Appointment appointment, Caregiver caregiver) {
        this.activityController = new ActivityController(conn);
        this.activityList = activityController.getAllActivitiesByAppointment(appointment);
        this.activityTableView = new TableView<>(FXCollections.observableList(activityList));
        this.saveBtn = createRoundedGreenButton("Save Changes");
        this.cancelBtn = createRoundedGreenButton("Cancel");
        saveBtn.setDisable(true);
        cancelBtn.setDisable(true);
        this.currentlyEditing = null;

        // === Title ===
        Label titleLabel = new Label("Activity Log");
        titleLabel.setFont(Font.font("Arial", 26));
        titleLabel.setStyle("-fx-font-weight: bold;");

        // === Table Columns ===
        TableColumn<Activity, String> titleColumn = new TableColumn<>("Activity Title");
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        titleColumn.setPrefWidth(200);
        titleColumn.setCellFactory(tc -> new EditingCell());
        titleColumn.setOnEditCommit(event -> {
            if (currentlyEditing != null && currentlyEditing == event.getRowValue()) {
                currentlyEditing.setTitle(event.getNewValue());
                saveBtn.setDisable(false);
                cancelBtn.setDisable(false);
            }
        });

        TableColumn<Activity, String> detailsColumn = new TableColumn<>("Activity Details");
        detailsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        detailsColumn.setPrefWidth(400);
        detailsColumn.setCellFactory(tc -> new EditingCell());
        detailsColumn.setOnEditCommit(event -> {
            if (currentlyEditing != null && currentlyEditing == event.getRowValue()) {
                currentlyEditing.setDescription(event.getNewValue());
                saveBtn.setDisable(false);
                cancelBtn.setDisable(false);
            }
        });

        TableColumn<Activity, String> timestampColumn = new TableColumn<>("Timestamp");
        timestampColumn.setCellValueFactory(cellData -> {
            Timestamp timestamp = cellData.getValue().getTimestamp();
            if (timestamp != null) {
                LocalDateTime localDateTime = timestamp.toLocalDateTime();
                return new SimpleStringProperty(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            } else {
                return new SimpleStringProperty(""); // Or handle null timestamp as needed
            }
        });
        timestampColumn.setPrefWidth(150);

        TableColumn<Activity, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setPrefWidth(100);
        actionsColumn.setCellFactory(param -> new TableCell<Activity, Void>() {
            private final Button removeBtn = createRoundedGreenButton("Remove");

            {
                removeBtn.setOnAction(event -> {
                    Activity activity = getTableRow().getItem();
                    if (activity != null) {
                        activityController.deleteActivity(activity);
                        activityList.remove(activity);
                        activityTableView.refresh();
                    }
                });
                HBox actionsBox = new HBox(10, removeBtn);
                actionsBox.setAlignment(Pos.CENTER);
                setGraphic(actionsBox);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(10, removeBtn));
                }
            }
        });

        activityTableView.getColumns().addAll(titleColumn, detailsColumn, timestampColumn, actionsColumn);
        activityTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        activityTableView.setPrefWidth(750);
        activityTableView.setEditable(true); // Make the table editable
        VBox.setVgrow(activityTableView, Priority.ALWAYS);

        // Add listener to handle row selection for editing
        activityTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                if (currentlyEditing == null) {
                    currentlyEditing = newSelection;
                    saveBtn.setDisable(false);
                    cancelBtn.setDisable(false);
                } else if (currentlyEditing != newSelection) {
                    // If another row is selected before saving/canceling
                    saveBtn.setDisable(false);
                    cancelBtn.setDisable(false);
                }
            } else {
                // No row selected
                if (currentlyEditing != null && saveBtn.isDisabled()) {
                    // If we were editing and then deselected without saving/canceling
                    saveBtn.setDisable(false);
                    cancelBtn.setDisable(false);
                }
            }
        });

        // === Action Buttons ===
        cancelBtn.setOnAction(e -> {
            activityTableView.getItems().clear();
            activityTableView.getItems().addAll(activityController.getAllActivitiesByAppointment(appointment));
            activityTableView.refresh();
            saveBtn.setDisable(true);
            cancelBtn.setDisable(true);
            currentlyEditing = null;
            activityTableView.getSelectionModel().clearSelection();
        });

        saveBtn.setOnAction(e -> {
            if (currentlyEditing != null) {
                activityController.updateActivity(currentlyEditing);
                activityTableView.refresh();
                saveBtn.setDisable(true);
                cancelBtn.setDisable(true);
                currentlyEditing = null;
                activityTableView.getSelectionModel().clearSelection();
            }
        });

        HBox actionBox = new HBox(20, cancelBtn, saveBtn);
        actionBox.setAlignment(Pos.CENTER_LEFT);

        VBox leftPane = new VBox(20, titleLabel, activityTableView, actionBox);
        leftPane.setPadding(new Insets(20));
        leftPane.setPrefWidth(800);

        // === Right Sidebar ===
        Button addActivityBtn = createSidebarButton("Add Activity");
        Button goBackBtn = createSidebarButton("Go Back");

        addActivityBtn.setOnAction(e -> {
            Dialog<Activity> dialog = new Dialog<>();
            dialog.setTitle("Add New Activity");
            dialog.setHeaderText("Enter details for the new activity:");

            // Set the button types
            ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

            // Create the labels and fields
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField titleInput = new TextField();
            titleInput.setPromptText("Activity Title");
            TextArea descriptionInput = new TextArea();
            descriptionInput.setPromptText("Activity Details");
            descriptionInput.setPrefRowCount(3);

            grid.add(new Label("Title:"), 0, 0);
            grid.add(titleInput, 1, 0);
            grid.add(new Label("Details:"), 0, 1);
            grid.add(descriptionInput, 1, 1);

            dialog.getDialogPane().setContent(grid);

            // Convert the result to an activity when the add button is clicked.
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == addButtonType) {
                    return new Activity(titleInput.getText(), descriptionInput.getText(), appointment.getAppointmentID());
                }
                return null;
            });

            dialog.showAndWait().ifPresent(newActivity -> {
                activityController.addActivity(newActivity);
                activityList.add(newActivity);
                activityTableView.refresh();
            });
        });

        goBackBtn.setOnAction(e -> {
            CaregiverElderView caregiverelderview = new CaregiverElderView(stage, conn, caregiver);
            stage.setScene(caregiverelderview.getScene());
        });

        addActivityBtn.setPrefWidth(200);
        goBackBtn.setPrefWidth(200);

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox rightPane = new VBox(30, addActivityBtn, spacer, goBackBtn);
        rightPane.setAlignment(Pos.TOP_CENTER);
        rightPane.setPadding(new Insets(30));
        rightPane.setStyle("-fx-background-color: #3BB49C;");
        rightPane.setPrefWidth(250);

        // === Main Layout ===
        HBox root = new HBox(20, leftPane, rightPane);
        root.setPadding(new Insets(20));

        this.scene = new Scene(root, 1100, 600);
        stage.setTitle("Activity Details");
        stage.setScene(scene);
        stage.show();
    }

    // Custom Editing Cell
    private static class EditingCell extends TableCell<Activity, String> {

        private TextField textField;

        public EditingCell() {
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createTextField();
                setText(null);
                setGraphic(textField);
                textField.selectAll();
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText(getItem());
            setGraphic(null);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    commitEdit(textField.getText());
                }
            });
            textField.setOnAction(event -> commitEdit(textField.getText()));
        }

        private String getString() {
            return getItem() == null ? "" : getItem();
        }
    }

    private Button createRoundedGreenButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("""
            -fx-background-color: #3BB49C;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-background-radius: 20;
            -fx-cursor: hand;
            -fx-padding: 8 20;
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
        return btn;
    }

    public Scene getScene() {
        return scene;
    }
}