package edu.sdccd.cisc191.client.ui.controller;

import edu.sdccd.cisc191.client.ui.util.FlowLogger;
import edu.sdccd.cisc191.client.ui.util.ViewType;
import edu.sdccd.cisc191.client.ui.util.WindowManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class GameController {
    @FXML private TextFlow logFlow;
    private FlowLogger logger;

    @FXML
    private void initialize() {
        assert(logFlow != null);
        logger = new FlowLogger(logFlow);
    }

    @FXML
    private void requestRegisterWindow() {
        WindowManager.spawnWindow(ViewType.REGISTER_PLAYER, true);
    }

    @FXML
    private void enqueuePlayerWindow() {
        WindowManager.spawnWindow(ViewType.ENQUEUE_PLAYER, true);
    }

    @FXML
    private void clearLogs() {
        logger.clear();
    }

    @FXML
    private void saveLogs() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Server Logs");

        chooser.setInitialFileName("logs.log");
        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Log files (*.log)", "*.log")
        );

        File file = chooser.showSaveDialog(logFlow.getScene().getWindow());

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.append(logger.getText());
            } catch (IOException e) {
                e.printStackTrace();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Dashboard could not save your logs due to a filesystem error.\nSorry!");

                alert.showAndWait();
            }
        }
    }

    public FlowLogger getLogger() {
        return logger;
    }
}
