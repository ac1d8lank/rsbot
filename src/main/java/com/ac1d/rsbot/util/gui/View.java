package com.ac1d.rsbot.util.gui;

import java.awt.Font;
import java.awt.Graphics2D;

public abstract class View {
    private boolean mVisible = true;

    public abstract void draw(Graphics2D g);
    public abstract int getWidth(Graphics2D g);
    public abstract int getHeight(Graphics2D g);

    public void onClick() {
    }

    protected static int getStringHeight(Graphics2D g, String value, Font f) {
        return g.getFontMetrics(f).getHeight();
    }

    protected static int getStringWidth(Graphics2D g, String value, Font f) {
        return g.getFontMetrics(f).stringWidth(value);
    }

    public void setVisible(boolean visible) {
        mVisible = visible;
    }

    public boolean isVisible() {
        return mVisible;
    }
}
