/*
 * MIT License
 *
 * Copyright (c) 2025 Webhead1104
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
