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
import me.webhead1104.towncraft.TowncraftPaper;
import me.webhead1104.towncraft.TowncraftPlatform;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.TowncraftTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.util.UUID;

@AutoService(TowncraftPlatform.class)
public class TowncraftPaperPlatform implements TowncraftPlatform {

    @Override
    @Nullable
    public TowncraftPlayer getPlayer(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return null;
        }
        return new TowncraftPlayerPaperImpl(Bukkit.getPlayer(uuid));
    }

    @Override
    public Logger getLogger() {
        return TowncraftPaper.getInstance().getComponentLogger();
    }

    @Override
    public File getDataFolder() {
        return TowncraftPaper.getInstance().getDataFolder();
    }

    @Override
    public void runTaskNextTick(Runnable runnable) {
        Bukkit.getScheduler().runTask(TowncraftPaper.getInstance(), runnable);
    }

    @Override
    public TowncraftTask runTimer(Runnable runnable, long delay, long period) {
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(TowncraftPaper.getInstance(), runnable, delay, period);
        return new TowncraftTaskPaperImpl(task);
    }

    @Override
    public void runTaskAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(TowncraftPaper.getInstance(), runnable);
    }
}
