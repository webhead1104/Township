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
