/*
 * MIT License
 *
 * Copyright (c) 2025 Webhead1104
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
package me.webhead1104.towncraft.menus.internal;

import me.devnatan.inventoryframework.internal.Job;
import me.webhead1104.towncraft.Scheduler;
import me.webhead1104.towncraft.TowncraftTask;

class TowncraftTaskJobImpl implements Job {

    private final Scheduler scheduler;
    private final long intervalInTicks;
    private final Runnable execution;
    private TowncraftTask task;

    public TowncraftTaskJobImpl(Scheduler scheduler, long intervalInTicks, Runnable execution) {
        this.scheduler = scheduler;
        this.intervalInTicks = intervalInTicks;
        this.execution = execution;
    }

    @Override
    public boolean isStarted() {
        return task != null && !task.isCancelled();
    }

    @Override
    public void start() {
        if (isStarted()) return;

        task = scheduler.runTimer(this::loop, intervalInTicks, intervalInTicks);
    }

    @Override
    public void cancel() {
        if (!isStarted()) return;
        task.cancel();
        task = null;
    }

    @Override
    public void loop() {
        execution.run();
    }
}
