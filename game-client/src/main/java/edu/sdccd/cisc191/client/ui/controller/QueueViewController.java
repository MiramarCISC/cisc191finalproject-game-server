package edu.sdccd.cisc191.client.ui.controller;

import edu.sdccd.cisc191.client.net.dto.QueueEntryResponse;
import edu.sdccd.cisc191.client.ui.node.QueueEntryCell;
import edu.sdccd.cisc191.client.ui.util.WindowManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class QueueViewController {
    @FXML private ListView<QueueEntryResponse> listView;

    private final ObservableList<QueueEntryResponse> queue = FXCollections.observableArrayList();

    @FXML private void initialize() {
        listView.setItems(this.queue);
        listView.setCellFactory(QueueEntryCell::new);
    }

    @FXML
    private void closeAction(ActionEvent event) {
        WindowManager.closeWindow(event);
    }

    public void setQueue(List<QueueEntryResponse> queue) {
        this.queue.setAll(queue);
    }
}
