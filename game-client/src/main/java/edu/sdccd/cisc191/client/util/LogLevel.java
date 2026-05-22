package edu.sdccd.cisc191.client.util;

import javafx.scene.paint.Color;

public enum LogLevel {
    INFO("[INFO] ", Color.GREEN),
    WARN("[WARN] ", Color.ORANGE),
    ERROR("[ERROR] ", Color.RED),
    DEBUG("[DEBUG] ", Color.GRAY);

    private final String prefix;
    private final Color color;

    LogLevel(String prefix, Color color) {
        this.prefix = prefix;
        this.color = color;
    }

    public String getPrefix() { return prefix; }
    public Color getColor() { return color; }
}