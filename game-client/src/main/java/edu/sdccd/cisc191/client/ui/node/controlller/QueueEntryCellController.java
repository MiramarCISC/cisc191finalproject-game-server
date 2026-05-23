package edu.sdccd.cisc191.client.ui.node.controlller;

import edu.sdccd.cisc191.client.net.dto.QueueEntryResponse;
import edu.sdccd.cisc191.client.util.DateHelper;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class QueueEntryCellController {
    @FXML private Node cellContainer;

    @FXML private Label positionLabel;
    @FXML private Label playerNameLabel;
    @FXML private Label playerIdLabel;
    @FXML private Label instantLabel;

    public void setEntry(QueueEntryResponse entry) {
        positionLabel.setText(entry.id().toString());
        playerNameLabel.setText(entry.username());
        playerIdLabel.setText(entry.playerId().toString());
        instantLabel.setText(DateHelper.formatInstant(entry.joinedAt()));
    }

    public Node getCellContainer() {
        return cellContainer;
    }
}
