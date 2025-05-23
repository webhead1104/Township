package me.webhead1104.township.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;

public class Msg {

    public static Component format(String miniMessageString, Object... args) {
        return MiniMessage.miniMessage().deserialize(String.format(miniMessageString, args)).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    public static String formatToJson(String miniMessageString) {
        return JSONComponentSerializer.json().serialize(format(miniMessageString));
    }
}
