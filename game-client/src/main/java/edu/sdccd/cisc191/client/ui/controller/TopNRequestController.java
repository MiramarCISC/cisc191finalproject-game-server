package edu.sdccd.cisc191.client.ui.controller;

import edu.sdccd.cisc191.client.net.GameHttpService;
import edu.sdccd.cisc191.client.net.HttpRequestExecutor;
import edu.sdccd.cisc191.client.ui.util.NumberHelper;
import edu.sdccd.cisc191.client.ui.util.ViewType;
import edu.sdccd.cisc191.client.ui.util.WindowManager;
import edu.sdccd.cisc191.util.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class TopNRequestController {
    @FXML private TextField numberField;

    private final Logger logger;
    private final GameHttpService gameHttpService;

    public TopNRequestController(GameController controller, GameHttpService gameHttpService) {
        this.logger = controller.getLogger();
        this.gameHttpService = gameHttpService;
    }

    @FXML
    private void initialize() {
        numberField.setTextFormatter(NumberHelper.numberFormatter());
    }

    @FXML
    private void okAction(ActionEvent event) {
        int number;
        try {
            number = NumberHelper.parseIntOrAlert(numberField.getText(), "number");
        } catch (NumberFormatException e) { return; }

        logger.debug("Requesting Top %d players...",  number);

        HttpRequestExecutor.tryRequest(() -> gameHttpService.getTopNPlayersSortedAlpha(number), logger)
            .onSuccess(response -> {
                logger.info("Successfully fetched truncated leaderboard!");

                Platform.runLater(() -> {
                    WindowManager.spawnWindow(ViewType.TOP_N_VIEW, false, controller -> {
                        controller.setPlayers(response);
                    });
                });
            });

        WindowManager.closeWindow(event);
    }

    @FXML
    private void closeAction(ActionEvent event) {
        WindowManager.closeWindow(event);
    }
}
