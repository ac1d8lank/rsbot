package com.ac1d.rsbot.util;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class AcidGUI {
    private AcidGUI() {}

    // Sizes
    private static final int MARGIN = 10;
    private static final int PADDING = 4;
    private static final int MIN_WIDTH = 10;
    private static final int MIN_HEIGHT = 10;

    // Fonts
    private static final Font TITLE = new Font("Verdana", Font.BOLD, 20);
    private static final Font SUBTITLE = new Font("Verdana", Font.BOLD, 10);
    private static final Font BOLD = new Font("Verdana", Font.BOLD, 14);
    private static final Font TEXT = new Font("Verdana", Font.PLAIN, 14);

    // Colors
    private static final Color YELLOW = new Color(.8f, 1f, .2f);
    private static final Color BACKGROUND = new Color(0f, 0f, 0f, 0.75f);

    // Data
    private static String title = "AcidTest";
    private static String subtitle = "v1.01";
    private static LinkedHashMap<String, String> data = new LinkedHashMap<>();

    public static void setData(String key, Object value) {
        data.put(key, value != null ? value.toString() : "");
    }

    public static void draw(Graphics2D g) {
        AffineTransform origin = g.getTransform();
        g.translate(MARGIN, MARGIN);

        final int w = getWidth(g);
        final int h = getHeight(g);

        // Box
        g.setColor(BACKGROUND);
        g.fillRect(0, 0, w, h);
        g.setColor(YELLOW);
        g.drawRect(0, 0, w, h);

        // Text
        g.translate(PADDING, PADDING);

        // Titles
        g.setFont(TITLE);
        g.translate(0, getStringHeight(g, title));
        g.drawString(title, (w - getStringWidth(g, title)) / 2, 0);

        g.setFont(SUBTITLE);
        g.translate(0, getStringHeight(g, subtitle));
        g.drawString(subtitle, (w - getStringWidth(g, subtitle)) / 2, 0);

        // Data
        for(String key : data.keySet()) {
            final String left = key+": ";
            g.setFont(BOLD);
            g.translate(0, getStringHeight(g, left));
            g.drawString(left, 0, 0);

            final int leftWidth = getStringWidth(g, left);
            g.setFont(TEXT);
            g.drawString(data.get(key), leftWidth, 0);
        }

        g.setTransform(origin);
    }

    private static int getWidth(Graphics2D g) {
        int contentWidth = 0;

        g.setFont(TITLE);
        contentWidth = Math.max(contentWidth, getStringWidth(g, title));
        g.setFont(SUBTITLE);
        contentWidth = Math.max(contentWidth, getStringWidth(g, subtitle));

        for(String key : data.keySet()) {
            int rowWidth = 0;
            g.setFont(BOLD);
            rowWidth += getStringWidth(g, key+": ");
            g.setFont(TEXT);
            rowWidth += getStringWidth(g, data.get(key));

            contentWidth = Math.max(contentWidth, rowWidth);
        }

        contentWidth += PADDING * 2;
        return Math.max(MIN_WIDTH, contentWidth);
    }

    private static int getHeight(Graphics2D g) {
        int contentHeight = 0;

        contentHeight += 2 * PADDING;

        g.setFont(TITLE);
        contentHeight += getStringHeight(g, title);
        g.setFont(SUBTITLE);
        contentHeight += getStringHeight(g, subtitle);

        g.setFont(BOLD);
        for(String key : data.keySet()) {
            contentHeight += getStringHeight(g, key);
        }

        return Math.max(MIN_HEIGHT, contentHeight);
    }

    private static int getStringHeight(Graphics2D g, String value) {
        return g.getFontMetrics().getHeight();
    }

    private static int getStringWidth(Graphics2D g, String value) {
        return g.getFontMetrics().stringWidth(value);
    }
}
