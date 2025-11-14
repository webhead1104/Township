package me.webhead1104.towncraft.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.dataLoaders.DataLoader;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unchecked")
public abstract class TowncraftDataLoaderArgument<T> implements CustomArgumentType<@NotNull T, @NotNull Key> {
    private static final SimpleCommandExceptionType INVALID_KEY =
            new SimpleCommandExceptionType(() -> "Invalid key format");

    public abstract @NotNull Class<? extends DataLoader.KeyBasedDataLoader<?>> getDataLoaderClass();

    @Override
    public T parse(@NotNull StringReader reader) throws CommandSyntaxException {
        int start = reader.getCursor();

        while (reader.canRead() && reader.peek() != ' ') {
            reader.skip();
        }

        try {
            String result = reader.getString().substring(start, reader.getCursor());
            Key key;
            if (result.startsWith("towncraft:")) {
                key = Key.key(result);
            } else {
                key = Towncraft.key(result);
            }
            return (T) getDataLoader().get(key);
        } catch (Exception e) {
            throw INVALID_KEY.createWithContext(reader);
        }
    }

    @Override
    public @NotNull ArgumentType<Key> getNativeType() {
        return ArgumentTypes.key();
    }

    public DataLoader.KeyBasedDataLoader<?> getDataLoader() {
        return Towncraft.getDataLoader(getDataLoaderClass());
    }

    @Override
    public <S> @NotNull CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, SuggestionsBuilder builder) {
        getDataLoader().keys().stream().map(Key::asString).forEach(key -> {
            if (builder.getRemainingLowerCase().contains(":")) {
                if (key.startsWith(builder.getRemainingLowerCase())) {
                    builder.suggest(key);
                }
            } else {
                if (key.contains(builder.getRemainingLowerCase())) {
                    builder.suggest(key);
                }
            }
        });
        return builder.buildFuture();
    }
}
