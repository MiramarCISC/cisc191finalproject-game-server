package edu.sdccd.cisc191.client.ui.util;

import edu.sdccd.cisc191.client.GameClientApplication;

import java.net.URL;

public enum ViewType {
    REGISTER_PLAYER("/view/register-player-view.fxml", "Register Player", 250, 160, false),
    ENQUEUE_PLAYER("/view/enqueue-player-view.fxml", "Add Player to Queue", 250, 100, false);

    private final String fxmlPath;
    private final String viewTitle;
    private final double width;
    private final double height;
    private final boolean isResizable;

    ViewType(String fxmlPath, String viewTitle, double width, double height, boolean isResizable) {
        this.fxmlPath = fxmlPath;
        this.viewTitle = viewTitle;
        this.width = width;
        this.height = height;
        this.isResizable = isResizable;
    }

    public URL getURL() {
        return GameClientApplication.class.getResource(fxmlPath);
    }

    public String getViewTitle() {
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
