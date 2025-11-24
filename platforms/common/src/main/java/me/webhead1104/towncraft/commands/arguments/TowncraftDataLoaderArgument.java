package me.webhead1104.towncraft.commands.arguments;

import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.dataLoaders.DataLoader;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.mevera.imperat.command.parameters.CommandParameter;
import studio.mevera.imperat.command.parameters.type.BaseParameterType;
import studio.mevera.imperat.context.ExecutionContext;
import studio.mevera.imperat.context.Source;
import studio.mevera.imperat.context.SuggestionContext;
import studio.mevera.imperat.context.internal.CommandInputStream;
import studio.mevera.imperat.exception.ImperatException;
import studio.mevera.imperat.exception.SourceException;
import studio.mevera.imperat.resolvers.SuggestionResolver;

import java.util.List;

@SuppressWarnings("unchecked")
public abstract class TowncraftDataLoaderArgument<T> extends BaseParameterType<Source, T> {
    @Override
    public @Nullable T resolve(@NotNull ExecutionContext<Source> context, @NotNull CommandInputStream<Source> inputStream, @NotNull String input) throws ImperatException {
        try {
            Key key;
            if (input.startsWith("towncraft:")) {
                key = Key.key(input);
            } else {
                key = Towncraft.key(input);
            }
            return (T) getDataLoader().get(key);
        } catch (Exception e) {
            throw new SourceException(context, "Invalid key format", e);
        }
    }

    public abstract @NotNull Class<? extends DataLoader.KeyBasedDataLoader<?>> getDataLoaderClass();

    public DataLoader.KeyBasedDataLoader<?> getDataLoader() {
        return Towncraft.getDataLoader(getDataLoaderClass());
    }


    @Override
    public SuggestionResolver<Source> getSuggestionResolver() {
        return new SuggestionResolver<Source>() {
            @Override
            public List<String> autoComplete(SuggestionContext<Source> context, CommandParameter<Source> parameter) {
                return List.of();
            }
        };
    }

//    @Override
//    public <S> @NotNull CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, SuggestionsBuilder builder) {
//        getDataLoader().keys().stream().map(Key::asString).forEach(key -> {
//            if (builder.getRemainingLowerCase().contains(":")) {
//                if (key.startsWith(builder.getRemainingLowerCase())) {
//                    builder.suggest(key);
//                }
//            } else {
//                if (key.contains(builder.getRemainingLowerCase())) {
//                    builder.suggest(key);
//                }
//            }
//        });
//        return builder.buildFuture();
//    }
}
