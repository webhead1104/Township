package me.webhead1104.towncraft.commands.arguments;

import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.TowncraftPlayer;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.command.CommandParameter;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ContextParameter;

public class TowncraftPlayerArgument<A extends CommandActor> implements ContextParameter<A, TowncraftPlayer> {

    @Override
    public TowncraftPlayer resolve(@NotNull CommandParameter parameter, @NotNull ExecutionContext<A> context) {
        return Towncraft.getPlayer(context.actor().uniqueId());
    }
}
