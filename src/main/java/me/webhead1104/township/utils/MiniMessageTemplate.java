package me.webhead1104.township.utils;

import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;

@NoArgsConstructor
public final class MiniMessageTemplate {
    public static final StringTemplate.Processor<Component, RuntimeException> MM = stringTemplate -> {
        String interpolated = STR.process(stringTemplate);
        return MiniMessage.miniMessage().deserialize(interpolated)
                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    };
    public static final StringTemplate.Processor<String, RuntimeException> MM_TO_JSON = stringTemplate -> {
        String interpolated = STR.process(stringTemplate);
        return JSONComponentSerializer.json().serialize(MiniMessage.miniMessage().deserialize(interpolated)
                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
    };
}