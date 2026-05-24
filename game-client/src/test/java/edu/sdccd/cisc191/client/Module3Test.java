package edu.sdccd.cisc191.client;

import edu.sdccd.cisc191.client.ui.util.FlowLogger;
import edu.sdccd.cisc191.util.CompositeLogger;
import edu.sdccd.cisc191.util.ConsoleLogger;
import edu.sdccd.cisc191.util.Logger;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

// GitHub Actions cannot do UI.
@DisabledIfEnvironmentVariable(named = "GITHUB_ACTIONS", matches = "true")
public class Module3Test extends ApplicationTest {
    private TextFlow textFlow;

    @Override
    public void start(Stage stage) {
        textFlow = new TextFlow();
    }

    @Test
    public void polymorphismTest() {
        List<Logger> loggers = List.of(
            new FlowLogger(textFlow, 100),
            new ConsoleLogger(100),
            new CompositeLogger(new ConsoleLogger(100))
        );

        Throwable e;
        try {
            throw new IllegalArgumentException();
        } catch (IllegalArgumentException ex) {
            e = ex;
        }

        for (Logger logger : loggers) {
            logger.info("Info thing");
            logger.warn("Warning!");
            logger.error("Substitution! 1+2=%d", 3);
            logger.error("Error!", e);
        }

        WaitForAsyncUtils.waitForFxEvents();

        // Logger implementations work very differently, but must produce same result when queried.
        assertEquals(loggers.get(0).getText(), loggers.get(1).getText(), "Loggers did not produce equal output");
        assertEquals(loggers.get(0).getText(), loggers.get(2).getText(), "Loggers did not produce equal output");
    }
}
