package me.webhead1104.menuDesigner;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public final class MiniMessageFx {
    private static final MiniMessage MM = MiniMessage.miniMessage();
    private static final double BASE_FONT_SIZE_PX = 18.0;
    private static final double MC_SHADOW_ALPHA = 0.8;
    private static final double MC_SHADOW_OFFSET_X = 1.0;
    private static final double MC_SHADOW_OFFSET_Y = 1.0;

    public static TextFlow render(String text) {
        Component component = MM.deserialize(text);
        List<Node> nodes = new ArrayList<>();
        visit(component, nodes);
        TextFlow flow = new TextFlow();
        flow.getChildren().setAll(nodes);
        flow.setTextAlignment(TextAlignment.CENTER);
        flow.setMaxWidth(Double.MAX_VALUE);
        flow.setStyle("-fx-font-size: " + BASE_FONT_SIZE_PX + "px;");
        return flow;
    }

    private static void visit(Component component, List<Node> out) {
        if (component instanceof TextComponent textComponent) {
            String content = textComponent.content();
            if (!content.isEmpty()) {
                Node layered = layeredText(content, new StyleState(component));
                out.add(layered);
            }
        }
        for (Component child : component.children()) {
            visit(child, out);
        }
    }

    private static Node layeredText(String content, StyleState styleState) {
        Text shadow = getText(content, styleState);

        Text main = new Text(content);
        if (styleState.getColor() != null) {
            main.setFill(styleState.getColor());
        }
        main.setUnderline(styleState.isUnderlined());
        main.setStrikethrough(styleState.isStrikethrough());
        main.setStyle("-fx-font-size: " + BASE_FONT_SIZE_PX + "px;" +
                "; -fx-font-weight: " + (styleState.isBold() ? "700" : "400") +
                "; -fx-font-style: " + (styleState.isItalic() ? "italic" : "normal") +
                "; -fx-strikethrough: " + (styleState.isStrikethrough() ? "true" : "false") +
                "; -fx-underline: " + (styleState.isUnderlined() ? "true" : "false") +
                ";");

        return new Group(shadow, main);
    }

    private static @NotNull Text getText(String content, StyleState styleState) {
        Text shadow = new Text(content);
        shadow.setFill(new Color(0, 0, 0, MC_SHADOW_ALPHA));
        shadow.setTranslateX(MC_SHADOW_OFFSET_X);
        shadow.setTranslateY(MC_SHADOW_OFFSET_Y);
        shadow.setUnderline(false);
        shadow.setStrikethrough(false);
        shadow.setStyle("-fx-font-size: " + BASE_FONT_SIZE_PX + "px;" +
                "; -fx-font-weight: " + (styleState.isBold() ? "700" : "400") +
                "; -fx-font-style: " + (styleState.isItalic() ? "italic" : "normal") +
                ";");
        return shadow;
    }
}
