package me.webhead1104.township;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class TownshipLoader implements PluginLoader {

    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        System.setProperty("bstats.relocatecheck", "false");
        MavenLibraryResolver resolver = new MavenLibraryResolver();

        resolver.addRepository(new RemoteRepository.Builder("central", "default", "https://repo1.maven.org/maven2/").build());
        resolver.addRepository(new RemoteRepository.Builder("xenondevs", "default", "https://repo.xenondevs.xyz/releases/").build());

        resolver.addDependency(new Dependency(new DefaultArtifact("dev.velix:imperat-core:1.9.0"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("dev.velix:imperat-bukkit:1.9.0"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("org.spongepowered:configurate-gson:4.2.0"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("xyz.xenondevs.invui:invui-core:1.45"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("xyz.xenondevs.invui:invui-core:1.45"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("xyz.xenondevs.invui:inventory-access-r22:1.45"), null));

        classpathBuilder.addLibrary(resolver);
    }
}
