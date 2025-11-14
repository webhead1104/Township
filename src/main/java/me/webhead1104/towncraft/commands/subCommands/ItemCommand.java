package me.webhead1104.towncraft.commands.subCommands;

import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.commands.CommandUtils;
import me.webhead1104.towncraft.commands.arguments.ItemTypeArgument;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.dataLoaders.ItemType;
import me.webhead1104.towncraft.utils.Msg;
import net.strokkur.commands.annotations.Executes;
import net.strokkur.commands.annotations.Executor;
import net.strokkur.commands.annotations.arguments.CustomArg;
import net.strokkur.commands.annotations.arguments.IntArg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class ItemCommand {

    @Executes("get")
    public void get(CommandSender $, @Executor Player player, @CustomArg(ItemTypeArgument.class) ItemType.Item item) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        player.sendMessage(Msg.format("<green>You have %d of %s!", user.getBarn().getItem(item.key()), item.key().value()));
    }

    @Executes("set")
    public void set(CommandSender $, @Executor Player player, @CustomArg(ItemTypeArgument.class) ItemType.Item item, @IntArg(min = 0) int amount) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        if (CommandUtils.validate(user.getBarn().getItem(item.key()) + amount, player)) {
            return;
        }
        user.getBarn().setItem(item.key(), amount);
        player.sendMessage(Msg.format("<green>Set %s to %d!", item.key().value(), amount));
    }

    @Executes("add")
    public void add(CommandSender $, @Executor Player player, @CustomArg(ItemTypeArgument.class) ItemType.Item item, @IntArg(min = 0) int amount) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        if (CommandUtils.validate(user.getBarn().getItem(item.key()) + amount, player)) {
            return;
        }
        user.getBarn().addAmountToItem(item.key(), amount);
        player.sendMessage(Msg.format("<green>Added %d to %s!", amount, item.key().value()));
    }

    @Executes("remove")
    public void remove(CommandSender $, @Executor Player player, @CustomArg(ItemTypeArgument.class) ItemType.Item item, @IntArg(min = 0) int amount) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        if (CommandUtils.validate(user.getBarn().getItem(item.key()) - amount, player)) {
            return;
        }
        user.getBarn().removeAmountFromItem(item.key(), amount);
        player.sendMessage(Msg.format("<green>Removed %s amount!", amount));
    }
}
