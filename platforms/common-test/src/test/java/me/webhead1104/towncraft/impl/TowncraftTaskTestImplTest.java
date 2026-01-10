/*
 * MIT License
 *
 * Copyright (c) 2026 Webhead1104
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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