package edu.sdccd.cisc191.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

public class GameClientApplication extends Application {
    private ConfigurableApplicationContext springContext;

    @Override
    public void init() {
        this.springContext = new SpringApplicationBuilder()
            .sources(SpringBootConfiguration.class)
            .run(getParameters().getRaw().toArray(new String[0]));

        SpringContext.setContext(this.springContext);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
            GameClientApplication.class.getResource("/view/game-client.fxml")
        );
        loader.setControllerFactory(springContext::getBean);

        Scene scene = new Scene(loader.load(), 700, 480);

        stage.setTitle("JavaFX REST Game Dashboard");
        stage.setScene(scene);
        stage.show();

        stage.setMinWidth(500);
        stage.setMinHeight(350);
    }

    @Override
    public void stop() {
        springContext.close();
        Platform.exit();
    }
}
