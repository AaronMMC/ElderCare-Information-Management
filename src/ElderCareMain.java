import controller.LoginController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ElderCareMain extends Application {

    private Connection conn;

    @Override
    public void start(Stage primaryStage) {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lalakers2", "root", "");

            LoginController loginController = new LoginController(primaryStage, conn);
            Scene loginScene = loginController.getLoginScene();

            primaryStage.setTitle("ElderCare");
            primaryStage.setScene(loginScene);
            primaryStage.show();

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to connect to the database.");
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}