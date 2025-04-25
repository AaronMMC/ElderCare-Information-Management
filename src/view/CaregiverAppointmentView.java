package view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.AppointmentEntry;


public class CaregiverAppointmentView {
    private final VBox rootLayout;
    private final TableView<model.AppointmentEntry> appointmentTable;


    public CaregiverAppointmentView(Button acceptBtn, Button closeBtn, Button declineBtn) {
        this.acceptBtn = acceptBtn;
        this.closeBtn = closeBtn;
        this.declineBtn = declineBtn;
        rootLayout = new VBox(20);
        rootLayout.setPadding(new Insets(20));
        rootLayout.setAlignment(Pos.CENTER);
        rootLayout.setBackground(new Background(new BackgroundFill(Color.web("#0A3459"), CornerRadii.EMPTY, Insets.EMPTY)));
        rootLayout.setEffect(createShadowEffect());

        Text title = new Text("Caregiver Home - Appointments");
        title.setFont(new Font("Arial", 24));
        title.setFill(Color.web("#FFFFFF"));

        appointmentTable = new TableView<>();
        appointmentTable.setMaxWidth(700);
        appointmentTable.setMinHeight(240);
        appointmentTable.setStyle("-fx-text-fill: white; -fx-font-size: 14px; "
                + "-fx-font-weight: bold; -fx-background-radius: 10; "
                + "-fx-effect: dropshadow(gaussian, black, 5, 0.5, 2, 2);");

        TableColumn<AppointmentEntry, Boolean> selectCol = new TableColumn<>("Select");
        selectCol.setCellFactory(tc -> new CheckBoxTableCell<>());

        TableColumn<AppointmentEntry, String> guardianCol = createColumn("Guardian Name", "guardianName", 150);
        TableColumn<AppointmentEntry, Integer> elderCountCol = createColumn("Elders Count", "eldersCount", 100);
        TableColumn<AppointmentEntry, String> dateTimeCol = createColumn("Appointment Date & Time", "appointmentTime", 200);

        appointmentTable.getColumns().addAll(selectCol, guardianCol, elderCountCol, dateTimeCol);
        appointmentTable.setItems(FXCollections.observableArrayList(
                new AppointmentEntry("Alice Johnson", 2, "2025-04-10 10:00 AM"),
                new AppointmentEntry("Bob Smith", 1, "2025-04-12 09:20 AM"),
                new AppointmentEntry("Catherine Lee", 3, "2025-04-15 08:00 AM")
        ));

        acceptBtn = createStyledButton("Accept Appointment", "#3A8E3A");
        closeBtn = createStyledButton("Close Appointment", "#3A3456");
        declineBtn = createStyledButton("Decline Appointment", "#E53935");

        acceptBtn.setOnAction(e -> System.out.println("Accepted selected appointments"));
        closeBtn.setOnAction(e -> System.out.println("Closed selected appointments"));
        declineBtn.setOnAction(e -> System.out.println("Declined selected appointments"));

        HBox actionBox = new HBox(10, acceptBtn, closeBtn, declineBtn);
        actionBox.setAlignment(Pos.CENTER);

        rootLayout.getChildren().addAll(title, appointmentTable, actionBox);
    }

    private <T> TableColumn<AppointmentEntry, T> createColumn(String title, String property, int width) {
        TableColumn<AppointmentEntry, T> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        col.setPrefWidth(width);
        return col;
    }

    private Button createStyledButton(String text, String colorHex) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + colorHex + "; -fx-text-fill: white; -fx-font-size: 14px; "
                + "-fx-font-weight: bold; -fx-background-radius: 10; "
                + "-fx-effect: dropshadow(gaussian, black, 5, 0.5, 2, 2);");
        button.setMinWidth(200);
        button.setMinHeight(40);

        button.setOnMouseEntered(event -> {
            button.setScaleX(1.1);
            button.setScaleY(1.1);
        });
        button.setOnMouseExited(event -> {
            button.setScaleX(1.0);
            button.setScaleY(1.0);
        });
        button.setOnMousePressed(event -> {
            button.setScaleX(1.2);
            button.setScaleY(1.2);
        });
        button.setOnMouseReleased(event -> {
            button.setScaleX(1.1);
            button.setScaleY(1.1);
        });

        return button;
    }

    private DropShadow createShadowEffect() {
        DropShadow shadow = new DropShadow();
        shadow.setRadius(5);
        shadow.setOffsetX(4);
        shadow.setOffsetY(4);
        shadow.setColor(Color.BLACK);
        return shadow;
    }
    private final Button acceptBtn;
    private final Button closeBtn;
    private final Button declineBtn;


    public VBox getRoot() {
        return rootLayout;
    }
    public Button getAcceptButton() {
        return acceptBtn;
    }

    public Button getCloseButton() {
        return closeBtn;
    }

    public Button getDeclineButton() {
        return declineBtn;
    }

    public TableView<AppointmentEntry> getAppointmentTable() {
        return appointmentTable;
    }



    public Scene getScene() {
        return new Scene(rootLayout, 850, 500);
    }
}
