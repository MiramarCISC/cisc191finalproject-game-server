package edu.sdccd.cisc191.client.net;

import edu.sdccd.cisc191.client.util.Logger;
import org.springframework.web.client.ResourceAccessException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class HttpRequestExecutor<T> {
    private static final ExecutorService executor = Executors.newFixedThreadPool(1);

    private final Logger logger;

    private final CompletableFuture<T> futureResult;
    private Throwable caughtException;
    private boolean isHandledException = false;

    public HttpRequestExecutor(Supplier<T> task, Logger logger) {
        this.logger = logger;

        this.futureResult = CompletableFuture.supplyAsync(task, executor)
            .exceptionally(e -> {
                this.caughtException = e.getCause() != null ? e.getCause() : e;

                if (this.caughtException instanceof ResourceAccessException) {
                    this.isHandledException = true;
                    logger.error("Could not reach server! Is server up?", e);
                }

                return null;
            });
    }

    public static <T> HttpRequestExecutor<T> tryRequest(Supplier<T> task, Logger logger) {
        return new HttpRequestExecutor<>(task, logger);
    }

    public <U extends Exception> HttpRequestExecutor<T> onFailure(Class<U> exception, Consumer<? super U> callback) {
        futureResult.join();

        if (caughtException != null) {
            if (exception.isInstance(caughtException)) {
                callback.accept(exception.cast(caughtException));
                isHandledException = true;
            }
        }

        return this;
    }

    public void onSuccess(Consumer<T> callback) {
        futureResult.join();

        if (caughtException != null) {
            if (!isHandledException) {
                logger.error("Error during HTTP Request", caughtException);
            }
        } else {
            callback.accept(futureResult.join());
        }
    }
}
