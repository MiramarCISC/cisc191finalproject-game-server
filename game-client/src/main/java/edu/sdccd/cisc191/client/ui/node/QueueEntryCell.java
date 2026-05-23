package edu.sdccd.cisc191.client.ui.node;

import edu.sdccd.cisc191.client.GameClientApplication;
import edu.sdccd.cisc191.client.net.dto.QueueEntryResponse;
import edu.sdccd.cisc191.client.ui.node.controlller.QueueEntryCellController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.io.IOException;

public class QueueEntryCell extends ListCell<QueueEntryResponse> {
    private FXMLLoader loader;
    private QueueEntryCellController controller;

    public QueueEntryCell(ListView<QueueEntryResponse> ignored) {
        super();
    }

    @Override
    protected void updateItem(QueueEntryResponse entry, boolean empty) {
        super.updateItem(entry, empty);

        if (empty || entry == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(GameClientApplication.class.getResource("/view/node/queue-entry-cell.fxml"));
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
