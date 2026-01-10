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

import com.google.auto.service.AutoService;
import me.webhead1104.towncraft.TowncraftPlatform;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.TowncraftTask;
import net.cytonic.cytosis.Cytosis;
import net.cytonic.cytosis.player.CytosisPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.UUID;

@AutoService(TowncraftPlatform.class)
public class TowncraftCytosisPlatform implements TowncraftPlatform {
    private static final Logger LOGGER = LoggerFactory.getLogger("Towncraft");

    @Override
    @Nullable
    public TowncraftPlayer getPlayer(UUID uuid) {
        CytosisPlayer player = Cytosis.getPlayer(uuid).orElse(null);
        if (player == null) {
            return null;
        }
        return new TowncraftPlayerImpl(player);
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    public File getDataFolder() {
        return new File("Towncraft");
    }

    @Override
    public void runTaskNextTick(Runnable runnable) {
        MinecraftServer.getSchedulerManager().buildTask(runnable).delay(TaskSchedule.nextTick()).schedule();
    }

    @Override
    public TowncraftTask runTimer(Runnable runnable, long delay, long period) {
        Task task = MinecraftServer.getSchedulerManager().buildTask(runnable).delay(TaskSchedule.tick(Math.toIntExact(delay)))
                .repeat(TaskSchedule.tick(Math.toIntExact(period))).schedule();
        return new TowncraftTaskImpl(task);
    }

    @Override
    public void runTaskAsync(Runnable runnable) {
        MinecraftServer.getSchedulerManager().buildTask(runnable).schedule();
    }
}
