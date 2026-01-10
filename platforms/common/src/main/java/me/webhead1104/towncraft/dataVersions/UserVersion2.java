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
package me.webhead1104.towncraft.dataVersions;

import com.google.errorprone.annotations.Keep;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

import static org.spongepowered.configurate.transformation.ConfigurationTransformation.WILDCARD_OBJECT;

@Keep
public final class UserVersion2 implements DataVersion {

    @Override
    public ConfigurationTransformation getTransformation() {
        return rootNode -> runInChildren(rootNode, node -> {
            node.node("give-item-amount").raw(node.node("give-item", "amount").getInt());
            node.node("give-item-type").raw(node.node("give-item", "type").getInt());

            node.node("claim-item-amount").raw(node.node("claim-item", "amount").getInt());
            node.node("claim-item-type").raw(node.node("claim-item", "type").getInt());
        }, "trains", "trains", WILDCARD_OBJECT, "train-cars", WILDCARD_OBJECT);
    }

    @Override
    public int getVersion() {
        return 2;
    }
}
