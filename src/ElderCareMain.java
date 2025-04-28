import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.LoginView;

public class ElderCareMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        LoginView loginView = new LoginView(); // Create instance of your custom view

        Scene scene = new Scene(loginView.getView(), 600, 500); // Embed the view into the scene

        primaryStage.setTitle("ElderCare Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Launch JavaFX application
    }
}
