package me.webhead1104.tools.wikiScraper.parser;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

@Slf4j
@Getter
public class Page {
    private final List<Table> tables;

    public Page(Document document, String cssSelector) {
        Elements elements = document.select(cssSelector);
        tables = elements.stream().map(Table::new).toList();
    }
}
