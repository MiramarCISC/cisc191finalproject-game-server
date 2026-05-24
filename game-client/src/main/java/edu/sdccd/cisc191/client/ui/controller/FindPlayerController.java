package edu.sdccd.cisc191.client.ui.controller;

import edu.sdccd.cisc191.client.net.GameHttpService;
import edu.sdccd.cisc191.client.net.HttpRequestExecutor;
import edu.sdccd.cisc191.client.net.exception.InvalidPlayerException;
import edu.sdccd.cisc191.client.ui.util.WindowManager;
import edu.sdccd.cisc191.util.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class FindPlayerController {
    @FXML private TextField nameField;

    private final Logger logger;
    private final GameHttpService gameHttpService;

    public FindPlayerController(GameController controller, GameHttpService gameHttpService) {
        this.logger = controller.getLogger();
        this.gameHttpService = gameHttpService;
    }

    @FXML
    private void okAction(ActionEvent event) {
        logger.debug("Requesting Player Search...");

        HttpRequestExecutor.tryRequest(() -> gameHttpService.getPlayerByUsername(nameField.getText().trim()), logger)
            .onFailure(InvalidPlayerException.class, (e) -> {
                logger.error("Player does not exist!", e);
            })
            .onSuccess(response -> {
                logger.info(
                    "Player: %s/#%d | Rating: %d",
                    response.username(), response.id(), response.rating()
                );
            });

        WindowManager.closeWindow(event);
    }

    @FXML
    private void closeAction(ActionEvent event) {
        WindowManager.closeWindow(event);
    }
}
