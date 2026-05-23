package edu.sdccd.cisc191.client.ui.util;

import edu.sdccd.cisc191.client.SpringContext;
import jakarta.annotation.Nullable;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

public class WindowManager {
    public static void spawnWindow(ViewType<?> vt, boolean isModal) {
        spawnWindow(vt, isModal, null);
    }

    public static <T> void spawnWindow(ViewType<T> vt, boolean isModal, @Nullable Consumer<T> initializer) {
        try {
            FXMLLoader loader = new FXMLLoader(vt.getURL());
            loader.setControllerFactory(SpringContext::getBean);
            Scene scene = new Scene(loader.load(), vt.width(), vt.height());

            Stage stage = new Stage();
            stage.setMinWidth(vt.width());
            stage.setMinHeight(vt.height());
            stage.setScene(scene);
            stage.setTitle(vt.viewTitle());
            stage.setResizable(vt.isResizable());

            if (initializer != null) {
                initializer.accept(loader.getController());
            }

            if (isModal) {
                stage.showAndWait();
            } else {
                stage.show();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeWindow(Event event) {
        final Node source = (Node) event.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();

        stage.close();
    }
}
