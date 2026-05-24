package edu.sdccd.cisc191.client.ui.controller;

import edu.sdccd.cisc191.client.ui.node.TopNEntryCell;
import edu.sdccd.cisc191.client.ui.util.WindowManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Scope("prototype")
public class TopNViewController {
    @FXML private ListView<String[]> listView;

    private final ObservableList<String[]> queue = FXCollections.observableArrayList();

    @FXML private void initialize() {
        listView.setItems(this.queue);
        listView.setCellFactory(TopNEntryCell::new);
    }

    @FXML
    private void closeAction(ActionEvent event) {
        WindowManager.closeWindow(event);
    }

    public void setPlayers(String[][] players) {
        this.queue.setAll(Arrays.stream(players).toList());

        Stage stage = (Stage) listView.getScene().getWindow();
        stage.setTitle("Top " + players.length + " Players");
    }
}
