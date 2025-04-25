
import controller.CareGiverAppointmentController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import view.CaregiverAppointmentView;

public class ElderCareMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Buttons passed to the view (as required by your constructor)
        Button acceptBtn = new Button();
        Button closeBtn = new Button();
        Button declineBtn = new Button();

        // Create the view and controller
        CaregiverAppointmentView view = new CaregiverAppointmentView(acceptBtn, closeBtn, declineBtn);
        new CareGiverAppointmentController(view);

        // Set up the stage
        Scene scene = view.getScene();
        primaryStage.setTitle("Caregiver Appointment Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
