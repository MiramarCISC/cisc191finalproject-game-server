package edu.sdccd.cisc191.client.net;

import edu.sdccd.cisc191.util.Logger;

import java.net.ConnectException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class HttpRequestExecutor<T> {
    private static final ExecutorService executor = Executors.newFixedThreadPool(1);
    private final Logger logger;

    private CompletableFuture<ResultWrapper<T>> futureResult;

    // Internal state wrapper
    private static class ResultWrapper<V> {
        V value;
        Throwable exception;
        boolean isHandled = false;

        ResultWrapper(V value, Throwable exception) {
            this.value = value;
            this.exception = exception;
        }
    }

    private HttpRequestExecutor(Supplier<T> task, Logger logger) {
        this.logger = logger;

        this.futureResult = CompletableFuture.supplyAsync(() -> {
            try {
                return new ResultWrapper<>(task.get(), null);
            } catch (Throwable e) {
                Throwable caught = e.getCause() != null ? e.getCause() : e;

                if (caught instanceof ConnectException) {
                    logger.error("Could not reach server! Is server up?", e);
                    ResultWrapper<T> wrapper = new ResultWrapper<>(null, caught);
                    wrapper.isHandled = true;
                    return wrapper;
                }

                return new ResultWrapper<>(null, caught);
            }
        }, executor);
    }

    public static <T> HttpRequestExecutor<T> tryRequest(Supplier<T> task, Logger logger) {
        return new HttpRequestExecutor<>(task, logger);
    }

    public <U extends Exception> HttpRequestExecutor<T> onFailure(Class<U> exception, Consumer<? super U> callback) {
        this.futureResult = this.futureResult.thenApply(wrapper -> {
            if (wrapper.exception != null && !wrapper.isHandled) {
                if (exception.isInstance(wrapper.exception)) {
                    callback.accept(exception.cast(wrapper.exception));
                    wrapper.isHandled = true;
                }
            }
            return wrapper;
        });

        return this;
    }

    public void onSuccess(Consumer<T> callback) {
        this.futureResult.thenAccept(wrapper -> {
            if (wrapper.exception != null) {
                if (!wrapper.isHandled) {
                    logger.error("Error during HTTP Request", wrapper.exception);
                }
            } else {
                callback.accept(wrapper.value);
            }
        });
    }
}