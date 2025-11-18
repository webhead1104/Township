package me.webhead1104.menuDesigner.ui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class MinecraftTooltipSkin extends StackPane {

    private static final double PADDING_TOP = 4;
    private static final double PADDING_RIGHT = 6;
    private static final double PADDING_BOTTOM = 4;
    private static final double PADDING_LEFT = 6;
    private static final int BG_ARGB = 0xF0100010;
    private static final int BORDER_LT_ARGB = 0x505000FF;
    private static final int BORDER_RB_ARGB = 0x5028007F;
    private final Canvas bg = new Canvas();
    private final Node content;

    public MinecraftTooltipSkin(Node content) {
        setPickOnBounds(false);
        setStyle("-fx-background-color: transparent; -fx-padding: 0;");

        this.content = content;
        getChildren().setAll(bg, wrap(content));
    }

    private static Color argb(int argb) {
        double a = ((argb >> 24) & 0xFF) / 255.0;
        double r = ((argb >> 16) & 0xFF) / 255.0;
        double g = ((argb >> 8) & 0xFF) / 255.0;
        double b = (argb & 0xFF) / 255.0;
        return new Color(r, g, b, a);
    }

    private static double prefW(Node n, double h) {
        if (n instanceof Region r) return r.prefWidth(h);
        double pw = n.prefWidth(h);
        return Double.isNaN(pw) ? n.maxWidth(h) : pw;
    }

    private static double prefH(Node n, double w) {
        if (n instanceof Region r) return r.prefHeight(w);
        double ph = n.prefHeight(w);
        return Double.isNaN(ph) ? n.maxHeight(w) : ph;
    }

    private StackPane wrap(Node node) {
        StackPane wrapper = new StackPane(node);
        wrapper.setPadding(new Insets(PADDING_TOP, PADDING_RIGHT, PADDING_BOTTOM, PADDING_LEFT));
        wrapper.setStyle("-fx-background-color: transparent;");
        return wrapper;
    }

    @Override
    protected double computePrefWidth(double height) {
        double cw = content == null ? 0 : prefW(content, height);
        return cw + PADDING_LEFT + PADDING_RIGHT;
    }

    @Override
    protected double computePrefHeight(double width) {
        double ch = content == null ? 0 : prefH(content, width);
        return ch + PADDING_TOP + PADDING_BOTTOM;
    }

    @Override
    protected void layoutChildren() {
        double w = Math.max(1, getWidth());
        double h = Math.max(1, getHeight());

        bg.setWidth(w);
        bg.setHeight(h);
        paintBackground(w, h);

        super.layoutChildren();
    }

    private void paintBackground(double w, double h) {
        GraphicsContext g = bg.getGraphicsContext2D();
        g.clearRect(0, 0, w, h);

        g.setFill(argb(BG_ARGB));
        g.fillRect(1, 1, Math.max(0, w - 2), Math.max(0, h - 2));

        g.setFill(argb(BORDER_LT_ARGB));
        g.fillRect(0, 0, w, 1);
        g.fillRect(0, 0, 1, h);

        g.setFill(argb(BORDER_RB_ARGB));
        g.fillRect(0, h - 1, w, 1);
        g.fillRect(w - 1, 0, 1, h);
    }
}
