package com.ac1d.rsbot.util.gui;

import com.ac1d.rsbot.util.AcidGUI;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Checkbox extends View {
    private static final int BOX_PADDING = 8;

    private final String text;
    private boolean checked;

    public Checkbox(String text, boolean defaultValue) {
        this.text = text;
        this.checked = defaultValue;
    }

    public boolean isChecked() {
        return checked;
    }

    @Override
    public void onClick() {
        checked = !checked;
        AcidGUI.invalidate();
    }

    public void draw(Graphics2D g) {
        final AffineTransform t = g.getTransform();

        g.translate(0, BOX_PADDING);

        g.setColor(AcidGUI.ACID);
        final int size = getCheckboxSize(g);
        final int height = getHeight(g);
        g.drawRect(0, 0, size, size);
        if(checked) {
            g.fillRect( 3, 3, size - 5, size - 5);
        }

        g.setFont(AcidGUI.BOLD);
        g.drawString(text, size + BOX_PADDING, height - BOX_PADDING);
        g.setTransform(t);
    }

    private int getCheckboxSize(Graphics2D g) {
        return g.getFontMetrics(AcidGUI.BOLD).getHeight();
    }

    public int getHeight(Graphics2D g) {
        return getCheckboxSize(g) + BOX_PADDING;
    }

    public int getWidth(Graphics2D g) {
        return getHeight(g) + BOX_PADDING + g.getFontMetrics(AcidGUI.BOLD).stringWidth(text);
    }
}
