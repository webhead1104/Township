package me.webhead1104.towncraft.commands;

import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.factories.TowncraftPlayerFactory;
import org.jetbrains.annotations.NotNull;
import studio.mevera.imperat.context.Context;
import studio.mevera.imperat.context.Source;
import studio.mevera.imperat.exception.ImperatException;
import studio.mevera.imperat.exception.SourceException;
import studio.mevera.imperat.resolvers.SourceResolver;

public class TowncraftPlayerSourceResolver<T extends Source> implements SourceResolver<T, TowncraftPlayer> {
    @Override
    public @NotNull TowncraftPlayer resolve(T s, Context<T> ctx) throws ImperatException {
        System.out.println("resolving " + s);
        if (s.isConsole()) {
            throw new SourceException(ctx, "Only players are allowed to execute this command");
        }
        return TowncraftPlayerFactory.INSTANCE.of(s.uuid());
    }
}
