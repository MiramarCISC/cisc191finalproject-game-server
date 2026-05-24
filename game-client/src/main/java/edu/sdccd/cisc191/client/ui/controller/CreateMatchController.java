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
public class CreateMatchController {
    @FXML private TextField player1IdField;
    @FXML private TextField player2IdField;
    @FXML private TextField arenaField;

    private final Logger logger;
    private final GameHttpService gameHttpService;

    public CreateMatchController(GameController controller, GameHttpService gameHttpService) {
        this.logger = controller.getLogger();
        this.gameHttpService = gameHttpService;
    }

    @FXML
    private void initialize() {
        player1IdField.setTextFormatter(NumberHelper.numberFormatter());
        player2IdField.setTextFormatter(NumberHelper.numberFormatter());
    }

    @FXML
    private void okAction(ActionEvent event) {
        long player1Id, player2Id;
        try {
            player1Id = NumberHelper.parseLongOrAlert(player1IdField.getText(), "player 1 ID");
            player2Id = NumberHelper.parseLongOrAlert(player2IdField.getText(), "player 2 ID");
        } catch (NumberFormatException e) { return; }

        logger.debug("Requesting Match Creation...");

        HttpRequestExecutor.tryRequest(() -> gameHttpService.createMatch(player1Id, player2Id, arenaField.getText()), logger)
            .onFailure(InvalidMatchException.class, (e) -> {
                logger.error("IDs are invalid, or match could not be created", e);
            }).onSuccess(request -> {
                logger.info(
                    "Created Match ID %s between %s and %s on arena %s",
                    request.id(), request.playerOneUsername(), request.playerTwoUsername(), request.arenaName()
                );
            });

        WindowManager.closeWindow(event);
    }

    @FXML
    private void closeAction(ActionEvent event) {
        WindowManager.closeWindow(event);
    }
}