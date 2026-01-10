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
package me.webhead1104.towncraft.codegen;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Objects;

/**
 * THIS CODE HAS BEEN CREATED BY THE MINESTOM CONTRIBUTORS AND HAS BEEN MODIFIED FOR TOWNCRAFT. ALL CREDIT GOES TO THE
 * HARDING WORKING INDIVIDUALS THAT HAVE CONTRIBUTED TO MINESTOM.
 */
public final class Generators {

    static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: <target folder>");
            return;
        }
        Path outputFolder = Path.of(args[0]);

        RegistryGenerator generator = new RegistryGenerator(outputFolder);
        generator.generate(resource("item.json"), "me.webhead1104.towncraft.items", "TowncraftMaterial", "TowncraftMaterialFactory", "TowncraftMaterials");

        System.out.println("Finished generating code");
    }

    @SuppressWarnings("SameParameterValue")
    private static InputStream resource(String name) {
        return Objects.requireNonNull(Generators.class.getResourceAsStream("/" + name), "Cannot find resource: %s".formatted(name));
    }
}
