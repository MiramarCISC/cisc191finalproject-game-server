package edu.sdccd.cisc191.client.ui.controller;

import edu.sdccd.cisc191.client.net.GameHttpService;
import edu.sdccd.cisc191.client.net.HttpRequestExecutor;
import edu.sdccd.cisc191.client.net.exception.InvalidPlayerException;
import edu.sdccd.cisc191.client.ui.util.WindowManager;
import edu.sdccd.cisc191.client.util.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class RegisterPlayerController {
    @FXML private TextField nameField;
    @FXML private Spinner<Integer> ratingSpinner;

    private final Logger logger;
    private final GameHttpService gameHttpService;

    public RegisterPlayerController(GameController controller, GameHttpService gameHttpService) {
        this.logger = controller.getLogger();
        this.gameHttpService = gameHttpService;
    }

    @FXML
    private void okAction(ActionEvent event) {
        logger.debug("Requesting Player Register...");

        HttpRequestExecutor.tryRequest(() -> gameHttpService.registerPlayer(nameField.getText().trim(), ratingSpinner.getValue()), logger)
            .onFailure(InvalidPlayerException.class, (e) -> {
                logger.error("Player already exists or rating is invalid!", e);
            }).onSuccess((response) -> {
                logger.info(
                    "Registered ID %d: {username: '%s', rating: %d}",
                    response.id(), response.username(), response.rating()
                );
            });

        WindowManager.closeWindow(event);
    }

    @FXML
    private void closeAction(ActionEvent event) {
        WindowManager.closeWindow(event);
    }
}