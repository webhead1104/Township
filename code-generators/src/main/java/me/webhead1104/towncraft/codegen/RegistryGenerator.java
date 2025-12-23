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
