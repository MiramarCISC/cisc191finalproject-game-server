package edu.sdccd.cisc191.client.util;

public enum LogLevel {
    INFO("[INFO] "),
    WARN("[WARN] "),
    ERROR("[ERROR] "),
    DEBUG("[DEBUG] ");

    private final String prefix;

    LogLevel(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() { return prefix; }
}