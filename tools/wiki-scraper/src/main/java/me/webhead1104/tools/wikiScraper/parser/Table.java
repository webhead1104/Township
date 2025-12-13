package me.webhead1104.tools.wikiScraper.parser;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

@Slf4j
@Getter
public class Table {
    private final List<Row> rows;
    private final Element table;

    public Table(Element table) {
        this.table = table;
        Elements rows = table.select("tr");
        log.debug("Found {} rows", rows.size());
        this.rows = rows.stream().map(Row::new).toList();
    }

    public Row getFirstRow() {
        return rows.getFirst();
    }

    public Row getRow(int row) {
        return rows.get(row);
    }

    public List<Row> getRowsBetween(int start, int end) {
        List<Row> rows = this.rows.subList(start, end);
        log.debug("Found {} rows between {} and {}", rows.size(), start, end);
        return rows;
    }
}
