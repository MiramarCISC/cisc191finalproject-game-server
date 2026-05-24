package edu.sdccd.cisc191.client.ui.node;

import edu.sdccd.cisc191.client.GameClientApplication;
import edu.sdccd.cisc191.client.ui.node.controlller.TopNEntryCellController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.io.IOException;

public class TopNEntryCell extends ListCell<String[]> {
    private FXMLLoader loader;
    private TopNEntryCellController controller;

    public TopNEntryCell(ListView<String[]> ignored) {
        super();
    }

    @Override
    protected void updateItem(String[] entry, boolean empty) {
        super.updateItem(entry, empty);

        if (empty || entry == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(GameClientApplication.class.getResource("/view/node/top-n-entry-cell.fxml"));
                try {
                    loader.load();
                    controller = loader.getController();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            controller.setEntry(entry);

            setGraphic(controller.getCellContainer());
        }
    }
}
