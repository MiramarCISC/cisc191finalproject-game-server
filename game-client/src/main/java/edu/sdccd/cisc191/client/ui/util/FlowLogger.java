package edu.sdccd.cisc191.client.ui.util;

import edu.sdccd.cisc191.util.Logger;
import edu.sdccd.cisc191.util.LogLevel;
import javafx.application.Platform;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.stream.Collectors;

public class FlowLogger extends Logger {
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
    public void clear() {
        this.textFlow.getChildren().clear();
    }

    @Override
    public String getText() {
        return textFlow.getChildren().stream()
            .filter(Text.class::isInstance)
            .map(node -> {
                LogLevel level = (LogLevel) node.getProperties().get("logLevel");
                String prefix = level != null ? level.getPrefix() : "";

                return prefix + ((Text) node).getText();
            }).collect(Collectors.joining());
    }

    protected void append(String message, LogLevel level, Object... args) {
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