package me.webhead1104.towncraft.commands.arguments;

import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.dataLoaders.DataLoader;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.exception.CommandErrorException;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ParameterType;
import revxrsal.commands.stream.MutableStringStream;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class TowncraftDataLoaderArgument<A extends CommandActor, T> implements ParameterType<A, T> {

    @Override
    public T parse(@NotNull MutableStringStream input, @NotNull ExecutionContext<@NotNull A> context) {
        while (input.canRead(input.position()) && input.peek() != ' ') {
            input.read();
        }
        String string = input.readString();
        try {
            Key key = Towncraft.key(string);
            return (T) getDataLoader().get(key);
        } catch (Exception e) {
            throw new CommandErrorException("Unknown %s: %s".formatted(resultClass().getSimpleName().toLowerCase(), string));
        }
    }

    public abstract @NotNull Class<? extends DataLoader.KeyBasedDataLoader<?>> getDataLoaderClass();

    public abstract @NotNull Class<T> resultClass();

    public DataLoader.KeyBasedDataLoader<?> getDataLoader() {
        return Towncraft.getDataLoader(getDataLoaderClass());
    }

    @Override
    public @NotNull SuggestionProvider<A> defaultSuggestions() {
        return context -> {
            List<String> suggestions = new ArrayList<>();
            String remaining = context.input().peekString().toLowerCase();
            getDataLoader().keys().stream().map(Key::value).forEach(key -> {
                if (key.contains(remaining)) {
                    suggestions.add(key);
                }
            });
            return suggestions;
        };
    }
}