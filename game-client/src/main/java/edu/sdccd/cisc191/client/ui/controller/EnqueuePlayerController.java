package edu.sdccd.cisc191.client.ui.controller;

import edu.sdccd.cisc191.client.net.GameHttpService;
import edu.sdccd.cisc191.client.net.HttpRequestExecutor;
import edu.sdccd.cisc191.client.net.exception.InvalidPlayerException;
import edu.sdccd.cisc191.client.ui.util.DateHelper;
import edu.sdccd.cisc191.client.ui.util.WindowManager;
import edu.sdccd.cisc191.client.util.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class EnqueuePlayerController {
    @FXML private TextField idField;

    private final Logger logger;
    private final GameHttpService gameHttpService;

    public EnqueuePlayerController(GameController controller, GameHttpService gameHttpService) {
        this.logger = controller.getLogger();
        this.gameHttpService = gameHttpService;
    }

    @FXML
    private void initialize() {
        idField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                return change;
            } else {
                return null;
            }
        }));
    }

    @FXML
    private void okAction(ActionEvent event) {
        logger.debug("Requesting Player Enqueueing...");

        long playerId;
        try {
            playerId = Long.parseLong(idField.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("The player ID provided is invalid.");
            alert.showAndWait();
            return;
        }

        HttpRequestExecutor.tryRequest(() -> gameHttpService.enqueuePlayer(playerId), logger)
            .onFailure(InvalidPlayerException.class, (e) -> {
                logger.error("Player does not exist or is already enqueued!", e);
            })
            .onSuccess(response -> {
                logger.info(
                    "Successfully assigned player %s/#%d to queue position %d at %s",
                    response.username(), response.playerId(), response.id(),
                    DateHelper.formatInstant(response.joinedAt())
                );
            });

        WindowManager.closeWindow(event);
    }

    @FXML
    private void closeAction(ActionEvent event) {
        WindowManager.closeWindow(event);
    }
}
