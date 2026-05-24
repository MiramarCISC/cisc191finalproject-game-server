package edu.sdccd.cisc191.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ConsoleLogger extends Logger {
    private final List<LogEntry> backLog = Collections.synchronizedList(new ArrayList<>());
    private final int maxLines;

    public ConsoleLogger() {
        this(200);
    }

    public ConsoleLogger(int maxLines) {
        this.maxLines = maxLines;
    }

    @Override
    public String getText() {
        return backLog.stream()
            .map(entry -> {
                LogLevel level = entry.level;
                String prefix = level != null ? level.getPrefix() : "";

                return prefix + entry.message + '\n';
            }).collect(Collectors.joining());
    }

    @Override
    public void clear() {
        backLog.clear();
    }

    @Override
    protected void append(String message, LogLevel level, Object... args) {
        String formattedMessage = String.format(message, args);
        String[] lines = formattedMessage.split("\n");

        for (String line : lines) {
            System.out.println(level.getPrefix() + line);
            backLog.add(new LogEntry(line, level));

            if (backLog.size() > maxLines) {
                backLog.removeFirst();
            }
        }
    }

    private record LogEntry(String message, LogLevel level) {}
}
