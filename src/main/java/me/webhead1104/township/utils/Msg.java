package me.webhead1104.township.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;

public class Msg {

    public static Component format(String miniMessageString) {
        return MiniMessage.miniMessage().deserialize(miniMessageString);
    }

    public static String formatToJson(String miniMessageString) {
        return JSONComponentSerializer.json().serialize(MiniMessage.miniMessage().deserialize(miniMessageString));
    }
}
