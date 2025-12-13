package me.webhead1104.tools.wikiScraper.parser;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;

import java.util.List;

@Slf4j
@Getter
public class Row {
    private final String text;
    private final List<Value> values;

    public Row(Element element) {
        text = element.text();
        this.values = element.select("td").stream().map(Element::text).map(Value::new).toList();
    }

    public Value getValue(int col) {
        if (col == -1) {
            return new Value("0");
        }
        return values.get(col);
    }
}
