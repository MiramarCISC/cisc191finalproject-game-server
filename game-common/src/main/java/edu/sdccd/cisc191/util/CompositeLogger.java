package edu.sdccd.cisc191.util;

import java.util.List;

public class CompositeLogger extends Logger {
    private final List<Logger> loggers;

    public CompositeLogger(Logger... loggers) {
        this.loggers = List.of(loggers);
    }

    @Override
    public void clear() {
        for (Logger logger : loggers) {
            logger.clear();
        }
    }

    @Override
    public String getText() {
        if (loggers.isEmpty()) return "";
        return loggers.getFirst().getText();
    }

    @Override
    protected void append(String message, LogLevel level, Object... args) {
        for (Logger logger : loggers) {
            logger.append(message, level, args);
        }
    }
}
