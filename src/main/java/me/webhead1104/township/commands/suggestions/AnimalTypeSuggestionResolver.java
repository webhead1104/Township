package me.webhead1104.township.commands.suggestions;

import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.command.parameters.CommandParameter;
import dev.velix.imperat.context.SuggestionContext;
import dev.velix.imperat.resolvers.SuggestionResolver;
import me.webhead1104.township.data.enums.AnimalType;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public final class AnimalTypeSuggestionResolver implements SuggestionResolver<BukkitSource> {

    private static final List<String> SUGGESTIONS = Arrays.stream(AnimalType.values()).map(Enum::name).toList();

    @Override
    public Collection<String> autoComplete(SuggestionContext<BukkitSource> context, CommandParameter<BukkitSource> parameter) {
        return SUGGESTIONS;
    }
}
