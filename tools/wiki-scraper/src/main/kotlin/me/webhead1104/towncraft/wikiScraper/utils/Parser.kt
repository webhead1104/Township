/*
 * MIT License
 *
 * Copyright (c) 2026 Webhead1104
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
package me.webhead1104.towncraft.wikiScraper.utils

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

data class Page(val document: Document, val cssSelector: String) {
    val tables: MutableList<Table> =
        document.select(cssSelector).stream().map<Table> { table: Element -> Table(table) }.toList()
}

data class Table(val table: Element) {
    val rows: MutableList<Row>

    init {
        val rows = table.select("tr")
        this.rows = rows.stream().map { Row(it) }.toList()
    }

    val firstRow: Row
        get() = rows.first()

    fun getRow(row: Int): Row {
        return rows[row]
    }

    fun getRowsBetween(start: Int, end: Int): MutableList<Row> {
        return rows.subList(start, end)
    }
}


data class Row(val element: Element) {
    val text: String = element.text()
    val values: MutableList<Value> =
        ArrayList(element.select("th, td").stream().map<String> { obj: Element -> obj.text() }
            .map<Value> { text: String -> Value(text) }.toList())

    fun getValue(col: Int): Value {
        if (col == -1) {
            return Value("0")
        }
        return values[col]
    }
}

data class Value(val text: String) {
    val key: String
        get() = Utils.normalizeForKey(text)

    val coins: Int
        get() {
            if (text == "Free") {
                return 0
            }
            if (text == "-") return 0
            return text.replace("N/A".toRegex(), "0").replace("\\D+".toRegex(), "").toInt()
        }

    val level: Int
        get() = text.replace("\\D+".toRegex(), "").toInt()

    val xp: Int
        get() {
            if (text.isEmpty()) return 0
            if (text == "-") return 0
            return text.replace("\\D+".toRegex(), "").toInt()
        }

    val population: Int
        get() {
            if (text.isEmpty()) return 0
            return text.replace("[Nn]/[Aa]".toRegex(), "0").replace("\\D+".toRegex(), "").toInt()
        }
}

