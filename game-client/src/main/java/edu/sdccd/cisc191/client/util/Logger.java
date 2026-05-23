package edu.sdccd.cisc191.client.util;

// Exists in order to have client.net NOT depend on client.ui.util
public interface Logger {
    void info(String message, Object... args);
    void warn(String message, Object... args);
    void debug(String message, Object... args);

    void error(String message, Object... args);
    void error(String message, Throwable throwable);

    String getText();
}