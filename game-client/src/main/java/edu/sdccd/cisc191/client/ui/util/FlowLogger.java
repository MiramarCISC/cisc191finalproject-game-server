package edu.sdccd.cisc191.client.ui.util;

import edu.sdccd.cisc191.client.util.Logger;
import edu.sdccd.cisc191.client.util.LogLevel;
import javafx.application.Platform;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.stream.Collectors;

public class FlowLogger implements Logger {
    private final TextFlow textFlow;
    private final int maxLines;

    public FlowLogger(TextFlow textFlow) {
        this(textFlow, 200);
    }

    public FlowLogger(TextFlow textFlow, int maxLines) {
        this.textFlow = textFlow;
        this.maxLines = maxLines;
    }

    @Override
    public void info(String message, Object... args) {

        this.append(message, LogLevel.INFO, args);
    }

    @Override
    public void debug(String message, Object... args) {
        this.append(message, LogLevel.DEBUG, args);
    }

    @Override
    public void warn(String message, Object... args) {
        this.append(message, LogLevel.WARN, args);
    }

    @Override
    public void error(String message, Object... args) {
        this.append(message, LogLevel.ERROR, args);
    }

    @Override
    public void error(String message, Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        e.printStackTrace(pw);

        this.append(message + '\n' + sw, LogLevel.ERROR);
    }

    public void clear() {
        this.textFlow.getChildren().clear();
    }

    public String getText() {
        return textFlow.getChildren().stream()
            .filter(Text.class::isInstance)
            .map(node -> {
                LogLevel level = (LogLevel) node.getProperties().get("logLevel");
                String prefix = level != null ? level.getPrefix() : "";

                return prefix + ((Text) node).getText();
            }).collect(Collectors.joining());
    }

    private void append(String message, LogLevel level, Object... args) {
        Platform.runLater(() -> {
            String formattedMessage = String.format(message, args);
            String[] lines = formattedMessage.split("\n");

            for (String line : lines) {
                Text text = new Text(line + '\n');
                text.getProperties().put("logLevel", level);
                text.getStyleClass().add("log-" + level.name().toLowerCase());

                textFlow.getChildren().add(text);

                if (textFlow.getChildren().size() > maxLines) {
                    textFlow.getChildren().removeFirst();
                }
            }
        });
    }
}