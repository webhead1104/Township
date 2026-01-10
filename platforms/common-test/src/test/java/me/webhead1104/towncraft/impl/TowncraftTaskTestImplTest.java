package me.webhead1104.towncraft.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class TowncraftTaskTestImplTest {
    private ScheduledExecutorService scheduler;

    @BeforeEach
    void setUp() {
        scheduler = Executors.newScheduledThreadPool(1);
    }

    @Test
    void testIsCancelled_InitiallyFalse_TrueAfterCancel() {
        ScheduledFuture<?> future = scheduler.schedule(() -> {
        }, 1000, TimeUnit.MILLISECONDS);
        TowncraftTaskTestImpl task = new TowncraftTaskTestImpl(future);

        assertFalse(task.isCancelled());

        task.cancel();

        assertTrue(task.isCancelled());
    }

    @Test
    @Timeout(value = 2, unit = TimeUnit.SECONDS)
    void testCancel_PreventsExecution() throws InterruptedException {
        AtomicInteger executionCount = new AtomicInteger(0);

        ScheduledFuture<?> future = scheduler.schedule(
                executionCount::incrementAndGet, 200, TimeUnit.MILLISECONDS);

        TowncraftTaskTestImpl task = new TowncraftTaskTestImpl(future);
        task.cancel();

        Thread.sleep(300);

        assertEquals(0, executionCount.get());
    }

    @Test
    @Timeout(value = 2, unit = TimeUnit.SECONDS)
    void testCancel_StopsRepeatingTask() throws InterruptedException {
        AtomicInteger executionCount = new AtomicInteger(0);
        CountDownLatch firstExecution = new CountDownLatch(1);

        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> {
            executionCount.incrementAndGet();
            firstExecution.countDown();
        }, 50, 100, TimeUnit.MILLISECONDS);

        TowncraftTaskTestImpl task = new TowncraftTaskTestImpl(future);

        assertTrue(firstExecution.await(500, TimeUnit.MILLISECONDS));
        task.cancel();

        int countAtCancel = executionCount.get();
        Thread.sleep(300);

        assertEquals(countAtCancel, executionCount.get());
    }
}