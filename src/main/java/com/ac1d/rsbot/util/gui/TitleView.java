package com.ac1d.rsbot.util.gui;

import com.ac1d.rsbot.util.AcidGUI;

import java.awt.Font;
import java.awt.Graphics2D;

public class TitleView extends View {
    private String title;
    private Font font;

    public TitleView(String title, Font font) {
        this.title = title;
        this.font = font;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setFont(font);
        g.drawString(title, (AcidGUI.guiWidth - getWidth(g)) / 2, getHeight(g));
    }

    @Override
    public int getWidth(Graphics2D g) {
        return getStringWidth(g, title, font);
    }

    @Override
    public int getHeight(Graphics2D g) {
        return getStringHeight(g, title, font);
    }

    public void setTitle(String title) {
        this.title = title;
        AcidGUI.invalidate();
    }
}
