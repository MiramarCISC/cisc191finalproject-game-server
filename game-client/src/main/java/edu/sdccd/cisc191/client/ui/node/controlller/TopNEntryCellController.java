package edu.sdccd.cisc191.client.ui.node.controlller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class TopNEntryCellController {
    @FXML private Node cellContainer;

    @FXML private Label playerNameLabel;
    @FXML private Label ratingLabel;

    public void setEntry(String[] entry) {
        playerNameLabel.setText(entry[0]);
        ratingLabel.setText(entry[1]);
    }

    public Node getCellContainer() {
        return cellContainer;
    }
}
