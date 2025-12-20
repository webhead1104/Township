package me.webhead1104.tools.wikiScraper.scrapers;

import com.google.errorprone.annotations.Keep;
import lombok.extern.slf4j.Slf4j;
import me.webhead1104.tools.wikiScraper.annotations.DependsOn;
import me.webhead1104.tools.wikiScraper.core.Scraper;
import me.webhead1104.tools.wikiScraper.core.Utils;
import me.webhead1104.tools.wikiScraper.model.Factory;
import me.webhead1104.tools.wikiScraper.model.Item;

import java.util.*;

@Keep
@Slf4j
@DependsOn(ItemScraper.class)
public class FactoryScraper implements Scraper<Factory> {

    @Override
    public String id() {
        return "factories";
    }

    @Override
    public List<Factory> scrape() {
        List<Factory> factories = new ArrayList<>();
        Map<String, List<Item>> factoryItems = new HashMap<>();
        getFrom(ItemScraper.class, Item.class).stream()
                .filter(item -> item.getFactoryData() != null)
                .forEach(item -> {
                    if (!factoryItems.containsKey(item.getFactoryData().factoryName())) {
                        factoryItems.put(item.getFactoryData().factoryName(), new ArrayList<>());
                    }
                    factoryItems.get(item.getFactoryData().factoryName()).add(item);
                });

        factoryItems.forEach((factoryName, items) -> {
            log.debug("going for factoryname {}", factoryName);
            List<Factory.Recipe> recipes = new ArrayList<>();
            for (Item item : items) {
                Item.FactoryData factoryData = item.getFactoryData();
                recipes.add(new Factory.Recipe(
                        item.getKey(),
                        factoryData.itemName().contains("x3") ? 3 : 1,
                        factoryData.ingredients(),
                        factoryData.time(),
                        item.getLevelNeeded(),
                        factoryData.xpGiven()
                ));
            }
            String factoryKey = Utils.normalizeForKey(factoryName);

            factories.add(new Factory(
                    factoryKey,
                    factoryKey.equals("feed_mill") ? 3 : -1,
                    factoryName,
                    factoryKey,
                    recipes
            ));
        });
        factories.sort(Comparator.comparingInt(factory ->
                factory.getRecipes().stream()
                        .mapToInt(Factory.Recipe::getLevelNeeded)
                        .min()
                        .orElse(Integer.MAX_VALUE)
        ));

        return factories;
    }

    @Override
    public Class<Factory> resultType() {
        return Factory.class;
    }
}
