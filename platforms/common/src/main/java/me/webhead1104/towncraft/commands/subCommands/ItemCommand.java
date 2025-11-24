package me.webhead1104.towncraft.commands.subCommands;

import studio.mevera.imperat.annotations.SubCommand;

@SubCommand("item")
public final class ItemCommand {
//todo
//    @SubCommand("get")
//    public void get(TowncraftPlayer player, @CustomArg(ItemTypeArgument.class) ItemType.Item item) {
//        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
//        player.sendMessage(Msg.format("<green>You have %d of %s!", user.getBarn().getItem(item.key()), item.key().value()));
//    }
//
//    @SubCommand("set")
//    public void set(TowncraftPlayer player, @CustomArg(ItemTypeArgument.class) ItemType.Item item, @IntArg(min = 0) int amount) {
//        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
//        if (CommandUtils.validate(user.getBarn().getItem(item.key()) + amount, player)) {
//            return;
//        }
//        user.getBarn().setItem(item.key(), amount);
//        player.sendMessage(Msg.format("<green>Set %s to %d!", item.key().value(), amount));
//    }
//
//    @SubCommand("add")
//    public void add(TowncraftPlayer player, @CustomArg(ItemTypeArgument.class) ItemType.Item item, @IntArg(min = 0) int amount) {
//        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
//        if (CommandUtils.validate(user.getBarn().getItem(item.key()) + amount, player)) {
//            return;
//        }
//        user.getBarn().addAmountToItem(item.key(), amount);
//        player.sendMessage(Msg.format("<green>Added %d to %s!", amount, item.key().value()));
//    }
//
//    @SubCommand("remove")
//    public void remove(TowncraftPlayer player, @CustomArg(ItemTypeArgument.class) ItemType.Item item, @IntArg(min = 0) int amount) {
//        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
//        if (CommandUtils.validate(user.getBarn().getItem(item.key()) - amount, player)) {
//            return;
//        }
//        user.getBarn().removeAmountFromItem(item.key(), amount);
//        player.sendMessage(Msg.format("<green>Removed %s amount!", amount));
//    }
}
