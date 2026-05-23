package edu.sdccd.cisc191.client.ui.node;

import edu.sdccd.cisc191.client.GameClientApplication;
import edu.sdccd.cisc191.client.net.dto.MatchResponse;
import edu.sdccd.cisc191.client.ui.node.controlller.MatchEntryCellController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.io.IOException;

public class MatchEntryCell extends ListCell<MatchResponse> {
    private FXMLLoader loader;
    private MatchEntryCellController controller;

    public MatchEntryCell(ListView<MatchResponse> ignored) {
        super();
    }

    @Override
    protected void updateItem(MatchResponse entry, boolean empty) {
        super.updateItem(entry, empty);

        if (empty || entry == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(GameClientApplication.class.getResource("/view/node/match-entry-cell.fxml"));
                try {
                    loader.load();
                    controller = loader.getController();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            controller.setMatch(entry);

            setGraphic(controller.getCellContainer());
        }
    }
}
