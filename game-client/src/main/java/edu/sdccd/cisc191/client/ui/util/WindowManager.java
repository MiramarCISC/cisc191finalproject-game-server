package edu.sdccd.cisc191.client.ui.util;

import edu.sdccd.cisc191.client.SpringContext;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowManager {
    public static void spawnWindow(ViewType vt, boolean isModal) {
        try {
            FXMLLoader loader = new FXMLLoader(vt.getURL());
            loader.setControllerFactory(SpringContext::getBean);
            Scene scene = new Scene(loader.load(), vt.width(), vt.height());

            Stage stage = new Stage();
            stage.setMinWidth(vt.width());
            stage.setMinHeight(vt.height());
            stage.setScene(scene);
            stage.setTitle(vt.getViewTitle());
            stage.setResizable(vt.isResizable());

            if (isModal) {
                stage.showAndWait();
            } else {
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeWindow(Event event) {
        final Node source = (Node) event.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();

        stage.close();
    }
}
