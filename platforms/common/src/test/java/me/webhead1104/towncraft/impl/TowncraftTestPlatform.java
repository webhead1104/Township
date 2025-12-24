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
package me.webhead1104.towncraft.impl;

import me.webhead1104.towncraft.TowncraftPlatform;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.TowncraftTask;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TowncraftTestPlatform implements TowncraftPlatform {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public @Nullable TowncraftPlayer getPlayer(UUID uuid) {
        return null;
    }

    @Override
    public Logger getLogger() {
        return LoggerFactory.getLogger("Towncraft");
    }

    @Override
    public File getDataFolder() {
        return new File("build/config/config");
    }

    @Override
    public void runTaskNextTick(Runnable runnable) {
        scheduler.schedule(runnable, 50, TimeUnit.MILLISECONDS);
    }

    @Override
    public TowncraftTask runTimer(Runnable runnable, long delay, long period) {
        return null;
    }

    @Override
    public TowncraftTask runTaskAsync(Runnable runnable) {
        return null;
    }
}
