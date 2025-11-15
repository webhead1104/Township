package me.webhead1104.menuDesigner;

import javafx.scene.paint.Color;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

@Getter
public class StyleState {
    private final boolean bold;
    private final boolean italic;
    private final boolean underlined;
    private final boolean strikethrough;
    private Color color;

    public StyleState(Component component) {
        TextColor textColor = component.color();
        if (textColor != null) {
            this.color = Color.rgb(textColor.red(), textColor.green(), textColor.blue());
        }
        Style style = component.style();
        this.bold = style.hasDecoration(TextDecoration.BOLD) && style.decoration(TextDecoration.BOLD) == TextDecoration.State.TRUE;
        this.italic = style.hasDecoration(TextDecoration.ITALIC) && style.decoration(TextDecoration.ITALIC) == TextDecoration.State.TRUE;
        this.underlined = style.hasDecoration(TextDecoration.UNDERLINED) && style.decoration(TextDecoration.UNDERLINED) == TextDecoration.State.TRUE;
        this.strikethrough = style.hasDecoration(TextDecoration.STRIKETHROUGH) && style.decoration(TextDecoration.STRIKETHROUGH) == TextDecoration.State.TRUE;
    }
}