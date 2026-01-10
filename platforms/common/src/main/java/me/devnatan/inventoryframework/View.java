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
package me.devnatan.inventoryframework;

import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.pipeline.Pipeline;
import me.devnatan.inventoryframework.pipeline.StandardPipelinePhases;
import me.webhead1104.towncraft.TowncraftPlatformManager;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.menus.component.TowncraftItemComponentBuilder;
import me.webhead1104.towncraft.menus.context.CloseContext;
import me.webhead1104.towncraft.menus.context.Context;
import me.webhead1104.towncraft.menus.context.SlotClickContext;
import me.webhead1104.towncraft.menus.pipeline.CancelledCloseInterceptor;
import me.webhead1104.towncraft.menus.pipeline.GlobalClickInterceptor;
import me.webhead1104.towncraft.menus.pipeline.ItemClickInterceptor;
import me.webhead1104.towncraft.menus.pipeline.ItemCloseOnClickInterceptor;
import org.jetbrains.annotations.ApiStatus;

/**
 * Towncraft platform {@link PlatformView} implementation.
 */
@ApiStatus.OverrideOnly
public class View
        extends PlatformView<
        ViewFrame,
        TowncraftPlayer,
        TowncraftItemComponentBuilder,
        Context,
        OpenContext,
        CloseContext,
        RenderContext,
        SlotClickContext> {

    @Override
    public void registerPlatformInterceptors() {
        final Pipeline<? super VirtualView> pipeline = getPipeline();
        pipeline.intercept(StandardPipelinePhases.CLICK, new ItemClickInterceptor());
        pipeline.intercept(StandardPipelinePhases.CLICK, new GlobalClickInterceptor());
        pipeline.intercept(StandardPipelinePhases.CLICK, new ItemCloseOnClickInterceptor());
        pipeline.intercept(StandardPipelinePhases.CLOSE, new CancelledCloseInterceptor());
    }

    @Override
    public final void nextTick(Runnable task) {
        TowncraftPlatformManager.getPlatform().runTaskNextTick(task);
    }
}
