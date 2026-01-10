package me.webhead1104.towncraft.impl;

import me.webhead1104.towncraft.TowncraftTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("DataFlowIssue")
class TowncraftTestPlatformTest {
    private TowncraftTestPlatform platform;

    @BeforeEach
    void setUp() {
        platform = new TowncraftTestPlatform();
    }

    @Test
    void getLogger() {
        Logger expected = LoggerFactory.getLogger("Towncraft");
        assertEquals(expected, platform.getLogger());
    }

    @Test
    void getDataFolder() {
        assertEquals(new File("build/config"), platform.getDataFolder());
    }

    @Nested
    class Player {
        @Test
        void getPlayer() {
            UUID uuid = UUID.randomUUID();
            assertEquals(new TowncraftPlayerTestImpl(uuid), platform.getPlayer(uuid));
        }

        @Test
        void doesNotThrow() {
            assertDoesNotThrow(() -> platform.getPlayer(UUID.randomUUID()));
        }

        @Test
        void getPlayerNull() {
            NullPointerException exception =
                    assertThrows(NullPointerException.class, () -> platform.getPlayer(null));
            assertEquals("uuid cannot be null", exception.getMessage());
        }
    }

    @Nested
    class Tasks {
        @Test
        @Timeout(value = 2, unit = TimeUnit.SECONDS)
        void testRunTaskNextTick_ExecutesAfterDelay() throws InterruptedException {
            CountDownLatch latch = new CountDownLatch(1);
            AtomicInteger executionCount = new AtomicInteger(0);

            long startTime = System.currentTimeMillis();

            platform.runTaskNextTick(() -> {
                executionCount.incrementAndGet();
                latch.countDown();
            });

            assertTrue(latch.await(500, TimeUnit.MILLISECONDS),
                    "Task should execute within 500ms");

            long executionTime = System.currentTimeMillis() - startTime;

            assertEquals(1, executionCount.get(), "Task should execute exactly once");
            assertTrue(executionTime >= 40,
                    "Task should execute after approximately 50ms (allowing 10ms tolerance)");
        }

        @Test
        @Timeout(value = 2, unit = TimeUnit.SECONDS)
        void testRunTaskNextTick_ExecutesRunnable() throws InterruptedException {
            CountDownLatch latch = new CountDownLatch(1);
            AtomicBoolean executed = new AtomicBoolean(false);

            platform.runTaskNextTick(() -> {
                executed.set(true);
                latch.countDown();
            });

            assertTrue(latch.await(500, TimeUnit.MILLISECONDS));
            assertTrue(executed.get(), "Runnable should have been executed");
        }

        @Test
        @Timeout(value = 3, unit = TimeUnit.SECONDS)
        void testRunTimer_ExecutesMultipleTimes() throws InterruptedException {
            AtomicInteger executionCount = new AtomicInteger(0);
            CountDownLatch latch = new CountDownLatch(3);

            TowncraftTask task = platform.runTimer(() -> {
                executionCount.incrementAndGet();
                latch.countDown();
            }, 50, 100);

            assertTrue(latch.await(2, TimeUnit.SECONDS),
                    "Task should execute at least 3 times");

            task.cancel();

            int countAtCancel = executionCount.get();
            Thread.sleep(200); // Wait to ensure no more executions

            assertEquals(countAtCancel, executionCount.get(),
                    "Task should not execute after being cancelled");
            assertTrue(executionCount.get() >= 3,
                    "Task should have executed at least 3 times");
        }

        @Test
        @Timeout(value = 2, unit = TimeUnit.SECONDS)
        void testRunTimer_RespectsDelay() throws InterruptedException {
            AtomicInteger executionCount = new AtomicInteger(0);
            CountDownLatch latch = new CountDownLatch(1);
            long startTime = System.currentTimeMillis();

            TowncraftTask task = platform.runTimer(() -> {
                if (executionCount.incrementAndGet() == 1) {
                    latch.countDown();
                }
            }, 100, 100); // 100ms delay

            assertTrue(latch.await(500, TimeUnit.MILLISECONDS));
            long firstExecutionTime = System.currentTimeMillis() - startTime;

            task.cancel();

            assertTrue(firstExecutionTime >= 90,
                    "First execution should respect the initial delay (allowing 10ms tolerance)");
        }

        @Test
        @Timeout(value = 2, unit = TimeUnit.SECONDS)
        void testRunTimer_ReturnsCancellableTask() throws InterruptedException {
            AtomicInteger executionCount = new AtomicInteger(0);

            TowncraftTask task = platform.runTimer(executionCount::incrementAndGet, 50, 50);

            assertNotNull(task, "runTimer should return a non-null task");

            Thread.sleep(200); // Let it run a few times
            task.cancel();

            int countAfterCancel = executionCount.get();
            Thread.sleep(150); // Wait longer than the period

            assertEquals(countAfterCancel, executionCount.get(),
                    "Task should stop executing after cancel");
        }

        @Test
        @Timeout(value = 2, unit = TimeUnit.SECONDS)
        void testRunTaskAsync_ExecutesInBackground() throws InterruptedException {
            CountDownLatch latch = new CountDownLatch(1);
            String mainThreadName = Thread.currentThread().getName();
            AtomicReference<String> executionThreadName = new AtomicReference<>("");

            platform.runTaskAsync(() -> {
                executionThreadName.set(Thread.currentThread().getName());
                latch.countDown();
            });

            assertTrue(latch.await(1, TimeUnit.SECONDS),
                    "Async task should execute within 1 second");
            assertNotNull(executionThreadName.get(), "Task should have executed");
            assertNotEquals(mainThreadName, executionThreadName.get(),
                    "Async task should execute on a different thread");
        }

        @Test
        @Timeout(value = 2, unit = TimeUnit.SECONDS)
        void testRunTaskAsync_ExecutesRunnable() throws InterruptedException {
            CountDownLatch latch = new CountDownLatch(1);
            AtomicInteger value = new AtomicInteger(0);

            platform.runTaskAsync(() -> {
                value.set(42);
                latch.countDown();
            });

            assertTrue(latch.await(1, TimeUnit.SECONDS));
            assertEquals(42, value.get(), "Runnable should have modified the value");
        }

        @Test
        @Timeout(value = 2, unit = TimeUnit.SECONDS)
        void testRunTaskAsync_MultipleTasksExecute() throws InterruptedException {
            int taskCount = 5;
            CountDownLatch latch = new CountDownLatch(taskCount);
            AtomicInteger executionCount = new AtomicInteger(0);

            for (int i = 0; i < taskCount; i++) {
                platform.runTaskAsync(() -> {
                    executionCount.incrementAndGet();
                    latch.countDown();
                });
            }

            assertTrue(latch.await(2, TimeUnit.SECONDS),
                    "All async tasks should execute");
            assertEquals(taskCount, executionCount.get(),
                    "All tasks should have executed");
        }
    }
}