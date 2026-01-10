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
package me.webhead1104.towncraft.commands.arguments;

import me.webhead1104.towncraft.dataLoaders.DataLoader;
import me.webhead1104.towncraft.features.factories.FactoryType;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.command.CommandActor;

public final class FactoryTypeArgument<A extends CommandActor> extends TowncraftDataLoaderArgument<A, FactoryType.Factory> {
    @Override
    public @NotNull Class<? extends DataLoader.KeyBasedDataLoader<?>> getDataLoaderClass() {
        return FactoryType.class;
    }

    @Override
    public @NotNull Class<FactoryType.Factory> resultClass() {
        return FactoryType.Factory.class;
    }
}
