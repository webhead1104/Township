package me.webhead1104.tools.wikiScraper.parser;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;

import java.util.List;

@Slf4j
@Getter
public class Page {
    private final List<Table> tables;
    private final Document document;

    public Page(Document document, String cssSelector) {
        this.document = document;
        tables = document.select(cssSelector).stream().map(Table::new).toList();
    }
}
