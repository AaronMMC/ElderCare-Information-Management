//import controller.LoginController;
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public class ElderCareMain extends Application {
//
//    private Connection conn;
//
//    @Override
//    public void start(Stage primaryStage) {
//        try {
//            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/eldercare_db", "yourUsername", "yourPassword");
//
//            LoginController loginController = new LoginController(primaryStage, conn);
//            Scene loginScene = loginController.getLoginScene();
//
//            primaryStage.setTitle("ElderCare Login");
//            primaryStage.setScene(loginScene);
//            primaryStage.show();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.err.println("Failed to connect to the database.");
//        }
//    }
//
//    @Override
//    public void stop() throws Exception {
//        super.stop();
//        if (conn != null && !conn.isClosed()) {
//            conn.close();
//        }
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}


//Caregiver view
//import javafx.application.Application;
//import javafx.stage.Stage;
//import model.Caregiver;
//import view.CaregiverView;
//
//import java.sql.Connection;
//
//public class ElderCareMain extends Application {
//
//    @Override
//    public void start(Stage primaryStage) {
//        // Dummy connection and caregiver (can be null for now)
//        Connection conn = null;
//        Caregiver dummyCaregiver = new Caregiver();  // Make sure you have a no-arg constructor in Caregiver
//
//        // Launch the CaregiverView
//        new CaregiverView(primaryStage, conn, dummyCaregiver);
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}

////Appointment
import javafx.application.Application;
import javafx.stage.Stage;
import view.*;
import view.CaregiverAppointmentView;

public class ElderCareMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        new CaregiverScheduleView(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

//import javafx.application.Application;
//import javafx.stage.Stage;
//import model.Guardian;
//import view.GuardianView;
//
//import java.sql.Connection;
//
//public class ElderCareMain extends Application {
//
//    @Override
//    public void start(Stage primaryStage) {
//        Connection conn = null;
//        Guardian guardian = null;
//        new GuardianView(primaryStage, conn, guardian);
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
//




