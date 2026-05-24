package edu.sdccd.cisc191.util;

import java.io.PrintWriter;
import java.io.StringWriter;

// Decoupled Logger
public abstract class Logger {
    public void info(String message, Object... args) {
        append(message, LogLevel.INFO, args);
    }

    public void warn(String message, Object... args) {
        append(message, LogLevel.WARN, args);
    }

    public void debug(String message, Object... args) {
        append(message, LogLevel.DEBUG, args);
    }

    public void error(String message, Object... args) {
        append(message, LogLevel.ERROR, args);
    }

    public void error(String message, Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        e.printStackTrace(pw);

        this.append(message + '\n' + sw, LogLevel.ERROR);
    }

    public abstract void clear();

    public abstract String getText();

    protected abstract void append(String message, LogLevel level, Object... args);
}