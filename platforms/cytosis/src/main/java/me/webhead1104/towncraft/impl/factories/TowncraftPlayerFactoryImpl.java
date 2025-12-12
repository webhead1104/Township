package me.webhead1104.towncraft.impl.factories;

import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.factories.TowncraftPlayerFactory;
import me.webhead1104.towncraft.impl.TowncraftPlayerImpl;
import net.cytonic.cytosis.Cytosis;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TowncraftPlayerFactoryImpl implements TowncraftPlayerFactory {

    @Override
    public @NotNull TowncraftPlayer of(UUID uuid) {
        return new TowncraftPlayerImpl(Cytosis.getPlayer(uuid).orElseThrow());
    }
}
