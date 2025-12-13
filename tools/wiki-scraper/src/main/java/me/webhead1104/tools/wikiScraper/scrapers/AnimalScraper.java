package me.webhead1104.tools.wikiScraper.scrapers;

import com.google.errorprone.annotations.Keep;
import io.leangen.geantyref.TypeToken;
import lombok.extern.slf4j.Slf4j;
import me.webhead1104.tools.wikiScraper.cli.Main;
import me.webhead1104.tools.wikiScraper.core.Scraper;
import me.webhead1104.tools.wikiScraper.core.Utils;
import me.webhead1104.tools.wikiScraper.model.Animal;
import me.webhead1104.tools.wikiScraper.parser.Page;
import me.webhead1104.tools.wikiScraper.parser.Table;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Keep
@Slf4j
public class AnimalScraper implements Scraper<Animal> {
    public static final Map<String, String> ANIMAL_MATERIALS = new HashMap<>();
    private static final String BASE_URL = "https://township.fandom.com/wiki/Goods";
    private static final TypeToken<Map<String, String>> TOKEN = new TypeToken<>() {
    };

    static {
        try {
            ANIMAL_MATERIALS.putAll(Utils.readJson("/data/animal_data.json", TOKEN));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String id() {
        return "animals";
    }

    @Override
    public List<Animal> scrape() throws IOException {
        List<Animal> animals = new ArrayList<>();

        Table goodsTable = new Page(fetchPage(BASE_URL), "div.mw-collapsible").getTables().get(1);
        List<String> urls = goodsTable.getRows().get(2).getElement().child(0).child(0).children()
                .stream().map(element -> element.absUrl("href")).toList();
        int i = 0;
        for (String url : urls) {
            if (url.contains("Duck_Feeder") || url.contains("Otter_Pond") || url.contains("Mushroom_Farm")) {
                //todo add support for duck feeder, otter pond, and mushroom farm
                continue;
            }
            log.debug(url);
            Document document = fetchPage(url);
            Page page = new Page(document, "table.wikitable:nth-child(8)");
            Table table = page.getTables().getFirst();
            Table buildingTable = new Table(page.getDocument().expectFirst("table.wikitable:nth-child(4) > tbody:nth-child(1)"));

            String name = document.select(".mw-page-title-main").text();
            String key = url.split("https://township.fandom.com/wiki/")[1].toLowerCase();
            int adjustment = i > 0 ? 1 : 0;
            Animal animal = new Animal(
                    key,
                    buildingTable.getRows().stream().filter(row -> row.getText().startsWith(name)).toList().size(),
                    name,
                    table.getRow(1).getValues().getFirst().text().split(" Feed x3")[0],
                    ANIMAL_MATERIALS.get(key.replaceAll("_\\d+$", "")),
                    table.getRow(1).getValues().getFirst().getAsKey(),
                    table.getRow(2).getValues().getFirst().getAsKey(),
                    table.getRow(2).getValues().get(4 + adjustment).getAsXp(),
                    table.getRow(2).getValues().get(3 + adjustment).getAsKey(),
                    key.replaceAll("_\\d+$", ""),
                    table.getRow(2).getValue(2).getAsLevel()
            );
            animals.add(animal);
            i++;
        }
        animals.removeIf(animal -> animal.getLevelNeeded() > Main.MAX_LEVEL);
        return animals;
    }
}
