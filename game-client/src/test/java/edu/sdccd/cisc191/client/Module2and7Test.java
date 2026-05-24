package edu.sdccd.cisc191.client;

import edu.sdccd.cisc191.client.net.HttpRequestExecutor;
import edu.sdccd.cisc191.client.net.exception.InvalidPlayerException;
import edu.sdccd.cisc191.util.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class Module2and7Test {

    @Mock
    private Logger logger;

    @Test // Module 2
    public void httpRequestExecutor_ParallelExecutionTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger testInt = new AtomicInteger(1);

        // Runs, waits 20 millis, returns int 3.
        Supplier<Integer> fakeRequest = () -> {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 3;
        };

        // Uses int from Supplier.
        Consumer<Integer> fakeRequestProcessor = number -> {
            testInt.addAndGet(number);
            latch.countDown();
        };


        HttpRequestExecutor.tryRequest(fakeRequest, logger)
            .onSuccess(fakeRequestProcessor);

        testInt.updateAndGet(v -> v * 2);

        // Waits for HttpRequestExecutor.onSuccess() to finish, or 1 second, whichever is faster.
        boolean completedInTime = latch.await(1, TimeUnit.SECONDS);


        assertTrue(completedInTime, "Timed out waiting for request.");
        // Supposed to trigger a race condition, resulting in: (1×2)+3=5.
        // If it was run synchronously, it would result in: (1+3)×2=8.
        assertEquals(5, testInt.get(), "Callback did not run asynchronously.");
    }

    @Test // Module 7; HttpRequestExecutor is vital to the client.
    public void httpRequestExecutor_successCallbackTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<String> result = new AtomicReference<>();

        HttpRequestExecutor.tryRequest(() -> "success", logger)
            .onSuccess(response -> {
                result.set(response);
                latch.countDown();
            });

        boolean completedInTime = latch.await(200, TimeUnit.MILLISECONDS);

        assertTrue(completedInTime, "Timed out waiting for request.");
        assertEquals("success", result.get());
    }

    @Test // Module 7; HttpRequestExecutor is vital to the client.
    public void httpRequestExecutor_failureCallbackTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<String> result = new AtomicReference<>();

        HttpRequestExecutor.tryRequest(() -> { throw new InvalidPlayerException("failure"); }, logger)
            .onFailure(InvalidPlayerException.class, (e) -> {
                result.set(e.getMessage());
                latch.countDown();
            })
            .onSuccess(response -> {
                fail("Should not be called");
            });

        boolean completedInTime = latch.await(200, TimeUnit.MILLISECONDS);

        assertTrue(completedInTime, "Timed out waiting for request.");
        assertEquals("failure", result.get());
    }
}
