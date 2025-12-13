package me.webhead1104.tools.wikiScraper.parser;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
public class Row {
    private final String text;
    private final List<Value> values;
    private final Element element;

    public Row(Element element) {
        this.text = element.text();
        this.values = new ArrayList<>(element.select("th").stream().map(Element::text).map(Value::new).toList());
        this.values.addAll(element.select("td").stream().map(Element::text).map(Value::new).toList());
        this.element = element;
    }

    public Value getValue(int col) {
        if (col == -1) {
            return new Value("0");
        }
        return values.get(col);
    }

    @Override
    public String toString() {
        return text;
    }
}
