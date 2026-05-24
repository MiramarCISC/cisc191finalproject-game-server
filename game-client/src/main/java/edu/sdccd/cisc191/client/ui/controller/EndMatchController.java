package edu.sdccd.cisc191.client.ui.controller;

import edu.sdccd.cisc191.client.net.GameHttpService;
import edu.sdccd.cisc191.client.net.HttpRequestExecutor;
import edu.sdccd.cisc191.client.net.exception.InvalidMatchException;
import edu.sdccd.cisc191.client.ui.util.NumberHelper;
import edu.sdccd.cisc191.client.ui.util.WindowManager;
import edu.sdccd.cisc191.util.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class EndMatchController {
    @FXML private TextField matchIdField;
    @FXML private TextField winnerIdField;

    private final Logger logger;
    private final GameHttpService gameHttpService;

    public EndMatchController(GameController controller, GameHttpService gameHttpService) {
        this.logger = controller.getLogger();
        this.gameHttpService = gameHttpService;
    }

    @FXML
    private void initialize() {
        matchIdField.setTextFormatter(NumberHelper.numberFormatter());
        winnerIdField.setTextFormatter(NumberHelper.numberFormatter());
    }

    @FXML
    private void okAction(ActionEvent event) {
        long matchId, winnerId;
        try {
            matchId = NumberHelper.parseLongOrAlert(matchIdField.getText(), "match ID");
            winnerId = NumberHelper.parseLongOrAlert(winnerIdField.getText(), "player ID");
        } catch (NumberFormatException e) { return; }

        logger.debug("Requesting End of Match...");

        HttpRequestExecutor.tryRequest(() -> gameHttpService.finishMatch(matchId, winnerId), logger)
            .onFailure(InvalidMatchException.class, (e) -> {
                logger.error("Match ID invalid or player not in match", e);
            }).onSuccess(response -> {
                logger.info(
                    "Successfully ended Match ID %s with winner %s",
                    response.id(),
                    response.winnerId().equals(response.playerOneId())?
                        response.playerOneUsername() : response.playerTwoUsername()
                );
            });

        WindowManager.closeWindow(event);
    }

    @FXML
    private void closeAction(ActionEvent event) {
        WindowManager.closeWindow(event);
    }
}