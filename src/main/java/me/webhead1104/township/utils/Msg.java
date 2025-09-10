package me.webhead1104.township.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class Msg {

    public static Component format(String miniMessageString, Object... args) {
        return MiniMessage.miniMessage().deserialize(String.format(miniMessageString, args)).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }
}
