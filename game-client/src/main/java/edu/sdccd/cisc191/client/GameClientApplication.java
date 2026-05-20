package edu.sdccd.cisc191.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GameClientApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
            GameClientApplication.class.getResource("/view/game-client.fxml")
        );

        Scene scene = new Scene(loader.load(), 760, 540);

        stage.setTitle("JavaFX gRPC 1v1 Game Client");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
