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
import me.webhead1104.township.data.enums.ItemType;
import me.webhead1104.township.utils.Msg;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ItemTypeArgument implements CustomArgumentType.Converted<ItemType, String> {

    private static final List<ItemType> TYPES = List.of(ItemType.values());

    private static final DynamicCommandExceptionType NOT_TYPE = new DynamicCommandExceptionType(
            obj -> MessageComponentSerializer.message().serialize(Msg.format("<red>No animal type found with name <white>%s", obj))
    );

    @Override
    public @NotNull ItemType convert(String nativeType) throws CommandSyntaxException {
        try {
            return ItemType.valueOf(nativeType.toUpperCase());
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
        TYPES.stream()
                .map(Object::toString)
                .filter(name -> name.toLowerCase().startsWith(builder.getRemainingLowerCase()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}
