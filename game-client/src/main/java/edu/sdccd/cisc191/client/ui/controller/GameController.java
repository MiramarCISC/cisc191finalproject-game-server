package edu.sdccd.cisc191.client.ui.controller;

import edu.sdccd.cisc191.client.net.GameHttpService;
import edu.sdccd.cisc191.client.net.HttpRequestExecutor;
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
    private final GameHttpService gameHttpService;

    @FXML private TextFlow logFlow;
    private FlowLogger logger;

    public GameController(GameHttpService gameHttpService) {
        this.gameHttpService = gameHttpService;
    }

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
    private void requestQueue() {
        logger.debug("Requesting Queue...");

        HttpRequestExecutor.tryRequest(gameHttpService::fetchQueue, logger)
            .onSuccess(queue -> {
                queue.stream();
                //TODO: Finish this
            });
    }

    @FXML
    private void requestNewMatch() {
        WindowManager.spawnWindow(ViewType.CREATE_MATCH, true);
    }

    @FXML
    private void requestEndMatch() {
       WindowManager.spawnWindow(ViewType.END_MATCH, true);
    }

    @FXML
    private void requestPlayerMatches() {
        WindowManager.spawnWindow(ViewType.REQUEST_MATCHES, true);
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
