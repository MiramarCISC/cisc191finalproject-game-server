package edu.sdccd.cisc191.client.ui.node.controlller;

import edu.sdccd.cisc191.client.net.dto.MatchResponse;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class MatchEntryCellController {
    @FXML private Node cellContainer;

    @FXML private Label matchIdLabel;
    @FXML private Label matchStatusLabel;
    @FXML private Label arenaLabel;

    @FXML private Label playerOneNameLabel;
    @FXML private Label playerOneIdLabel;
    @FXML private Label playerOneWinnerLabel;
    @FXML private HBox playerOneNameContainer;

    @FXML private Label playerTwoNameLabel;
    @FXML private Label playerTwoIdLabel;
    @FXML private Label playerTwoWinnerLabel;
    @FXML private HBox playerTwoNameContainer;

    private final SimpleBooleanProperty isPlayerOneWinner = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty isPlayerTwoWinner = new SimpleBooleanProperty(false);

    @FXML
    private void initialize() {
        isPlayerOneWinner.addListener((ignoredObservable, ignoredOld, isWinner) -> {
            if (isWinner) {
                playerOneNameContainer.getStyleClass().add("inline-title");
            } else {
                playerOneNameContainer.getStyleClass().remove("inline-title");
            }
        });
        playerOneWinnerLabel.visibleProperty().bind(isPlayerOneWinner);
        playerOneWinnerLabel.managedProperty().bind(isPlayerOneWinner);

        isPlayerTwoWinner.addListener((ignoredObservable, ignoredOld, isWinner) -> {
            if (isWinner) {
                playerTwoNameContainer.getStyleClass().add("inline-title");
            } else {
                playerTwoNameContainer.getStyleClass().remove("inline-title");
            }
        });
        playerTwoWinnerLabel.visibleProperty().bind(isPlayerTwoWinner);
        playerTwoWinnerLabel.managedProperty().bind(isPlayerTwoWinner);
    }

    public void setMatch(MatchResponse match) {
        matchIdLabel.setText(match.id().toString());
        matchStatusLabel.setText(match.status());
        arenaLabel.setText(match.arenaName());

        playerOneNameLabel.setText(match.playerOneUsername());
        playerOneIdLabel.setText(match.playerOneId().toString());

        playerTwoNameLabel.setText(match.playerTwoUsername());
        playerTwoIdLabel.setText(match.playerTwoId().toString());

        isPlayerOneWinner.set(false);
        isPlayerTwoWinner.set(false);

        if (match.winnerId() != null) {
            if (match.winnerId().equals(match.playerOneId())) {
                isPlayerOneWinner.set(true);
            } else {
                isPlayerTwoWinner.set(true);
            }
        }
    }

    public Node getCellContainer() {
        return cellContainer;
    }
}
