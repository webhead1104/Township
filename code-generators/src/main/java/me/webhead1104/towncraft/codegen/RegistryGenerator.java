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
package me.webhead1104.towncraft.codegen;

import com.google.gson.JsonObject;
import com.palantir.javapoet.*;
import net.kyori.adventure.key.Key;

import javax.lang.model.element.Modifier;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Objects;

public record RegistryGenerator(Path outputFolder) implements TowncraftCodeGenerator {
    public RegistryGenerator {
        Objects.requireNonNull(outputFolder, "Output folder cannot be null");
    }

    public void generate(InputStream resourceFile, String packageName, String typeName, String loaderName, String generatedName) {
        ensureDirectory(outputFolder);

        ClassName typeClass = ClassName.get(packageName, typeName);
        ClassName loaderClass = ClassName.get("me.webhead1104.towncraft.factories", loaderName);
        JsonObject json = GSON.fromJson(new InputStreamReader(resourceFile), JsonObject.class);
        ClassName generatedCN = ClassName.get(packageName, generatedName);
        // BlockConstants class
        TypeSpec.Builder blockConstantsClass = TypeSpec.interfaceBuilder(generatedCN)
                // Add @SuppressWarnings("unused")
                .addModifiers(Modifier.SEALED)
                .addPermittedSubclass(typeClass)
                .addAnnotation(AnnotationSpec.builder(SuppressWarnings.class).addMember("value", "{$S, $S}", "unused", "SpellCheckingInspection").build())
                .addJavadoc(generateJavadoc(typeClass));

        // Use data
        json.keySet().forEach(namespace -> {
            final String constantName = toConstant(namespace);
            final String namespaceString = namespaceShort(namespace);
            blockConstantsClass.addField(
                    FieldSpec.builder(typeClass, constantName)
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .initializer(
                                    // TypeClass.CONSTANT_NAME = LoaderClass.get(namespaceString)
                                    "$T.get(key($S))",
                                    loaderClass,
                                    namespaceString
                            )
                            .build()
            );
        });
        writeFiles(JavaFile.builder(packageName, blockConstantsClass.build())
                .indent("    ")
                .skipJavaLangImports(true)
                .addStaticImport(Key.class, "key")
                .build()
        );
    }

    @Override
    public void generate() {
        throw new UnsupportedOperationException("Use generate(InputStream, String, String, String, String) instead");
    }
}
