package edu.sdccd.cisc191.client.ui.util;

import edu.sdccd.cisc191.client.GameClientApplication;
import edu.sdccd.cisc191.client.ui.controller.*;

import java.net.URL;

// NOTE: This is basically a fancy enum.
// Made into a class to allow for generics.
@SuppressWarnings("unused") // Phantom type, used in WindowManager
public final class ViewType<T> {
    public static final ViewType<RegisterPlayerController> REGISTER_PLAYER = new ViewType<>(
        "/view/register-player.fxml",
        "Register Player", 250, 160, false
    );

    public static final ViewType<EnqueuePlayerController> ENQUEUE_PLAYER = new ViewType<>(
        "/view/enqueue-player.fxml",
        "Add Player to Queue", 250, 100,
        false
    );

    public static final ViewType<CreateMatchController> CREATE_MATCH = new ViewType<>(
        "/view/create-match.fxml",
        "Create Match", 250, 215,
        false
    );

    public static final ViewType<EndMatchController> END_MATCH = new ViewType<>(
        "/view/end-match.fxml",
        "Finish Match", 250, 160,
        false
    );

    public static final ViewType<RequestMatchesController> REQUEST_MATCHES = new ViewType<>(
        "/view/request-matches.fxml",
        "Get Past Matches", 250, 100,
        false
    );

    public static final ViewType<QueueViewController> QUEUE_VIEW = new ViewType<>(
        "/view/queue-view.fxml",
        "Current Queue", 400, 250,
        true
    );

    public static final ViewType<PastMatchViewController> PAST_MATCH_VIEW = new ViewType<>(
        "/view/past-match-view.fxml",
        "Past Matches for Player", 400, 250,
        true
    );

    private final String fxmlPath;
    private final String viewTitle;
    private final double width;
    private final double height;
    private final boolean isResizable;

    private ViewType(String fxmlPath, String viewTitle, double width, double height, boolean isResizable) {
        this.fxmlPath = fxmlPath;
        this.viewTitle = viewTitle;
        this.width = width;
        this.height = height;
        this.isResizable = isResizable;
    }

    public URL getURL() {
        return GameClientApplication.class.getResource(fxmlPath);
    }

    public String fxmlPath() {
        return fxmlPath;
    }

    public String viewTitle() {
        return viewTitle;
    }

    public double width() {
        return width;
    }

    public double height() {
        return height;
    }

    public boolean isResizable() {
        return isResizable;
    }
}
