package me.webhead1104.township.commands.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import me.webhead1104.township.Township;
import me.webhead1104.township.dataLoaders.ItemType;
import me.webhead1104.township.utils.Msg;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ItemTypeArgument implements CustomArgumentType.Converted<@NotNull Key, @NotNull String> {

    private static final DynamicCommandExceptionType NOT_TYPE = new DynamicCommandExceptionType(
            obj -> MessageComponentSerializer.message().serialize(Msg.format("<red>No item type found with name <white>%s", obj))
    );

    @Override
    public @NotNull Key convert(String nativeType) throws CommandSyntaxException {
        try {
            return Township.key(nativeType.toLowerCase());
        } catch (IllegalArgumentException e) {
            throw NOT_TYPE.create(nativeType);
        }
    }

    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> @NotNull CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, SuggestionsBuilder builder) {
        ItemType.values().forEach(item -> {
            String value = item.key().value();
            if (value.startsWith(builder.getRemainingLowerCase())) {
                builder.suggest(value);
            }
        });
        return builder.buildFuture();
    }
}
