package edu.sdccd.cisc191.client.ui.controller;

import edu.sdccd.cisc191.client.net.dto.MatchResponse;
import edu.sdccd.cisc191.client.ui.node.MatchEntryCell;
import edu.sdccd.cisc191.client.ui.util.WindowManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class PastMatchViewController {
    @FXML private ListView<MatchResponse> listView;

    private final ObservableList<MatchResponse> matchList = FXCollections.observableArrayList();

    @FXML private void initialize() {
        listView.setItems(this.matchList);
        listView.setCellFactory(MatchEntryCell::new);
    }

    @FXML
    private void closeAction(ActionEvent event) {
        WindowManager.closeWindow(event);
    }

    public void setMatchList(List<MatchResponse> matchList) {
        this.matchList.setAll(matchList);
    }

    public void setPlayer(String name, long id) {
        Stage stage = (Stage) listView.getScene().getWindow();
        stage.setTitle("Past Matches for " + name + "/#" + id);
    }
}
