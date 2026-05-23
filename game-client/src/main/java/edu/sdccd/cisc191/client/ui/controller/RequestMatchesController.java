package edu.sdccd.cisc191.client.ui.controller;

import edu.sdccd.cisc191.client.net.GameHttpService;
import edu.sdccd.cisc191.client.net.HttpRequestExecutor;
import edu.sdccd.cisc191.client.net.exception.InvalidPlayerException;
import edu.sdccd.cisc191.client.ui.util.NumberHelper;
import edu.sdccd.cisc191.client.ui.util.ViewType;
import edu.sdccd.cisc191.client.ui.util.WindowManager;
import edu.sdccd.cisc191.client.util.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class RequestMatchesController {
    @FXML private TextField idField;

    private final Logger logger;
    private final GameHttpService gameHttpService;

    public RequestMatchesController(GameController controller, GameHttpService gameHttpService) {
        this.logger = controller.getLogger();
        this.gameHttpService = gameHttpService;
    }

    @FXML
    private void initialize() {
        idField.setTextFormatter(NumberHelper.numberFormatter());
    }

    @FXML
    private void okAction(ActionEvent event) {
        long playerId;
        try {
            playerId = NumberHelper.parseLongOrAlert(idField.getText(), "player ID");
        } catch (NumberFormatException e) { return; }

        logger.debug("Requesting Matches for Player...");

        HttpRequestExecutor.tryRequest(() -> gameHttpService.fetchMatchesForPlayer(playerId), logger)
            .onFailure(InvalidPlayerException.class, (e) -> {
                logger.error("Player does not exist!", e);
            })
            .onSuccess(response -> {
                if (response.isEmpty()) {
                    logger.warn("Player has no associated match data.");
                } else {
                    logger.info("Successfully fetched match data");

                    WindowManager.spawnWindow(ViewType.PAST_MATCH_VIEW, false, controller -> {
                        controller.setMatchList(response);

                        String playerName = playerId == response.getFirst().playerOneId()
                            ? response.getFirst().playerOneUsername()
                            : response.getFirst().playerTwoUsername();

                        controller.setPlayer(playerName, playerId);
                    });
                }
            });

        WindowManager.closeWindow(event);
    }

    @FXML
    private void closeAction(ActionEvent event) {
        WindowManager.closeWindow(event);
    }
}
