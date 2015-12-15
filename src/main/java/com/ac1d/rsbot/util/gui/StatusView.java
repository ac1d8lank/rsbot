package com.ac1d.rsbot.util.gui;

import com.ac1d.rsbot.util.AcidGUI;

import java.awt.Graphics2D;

public class StatusView extends View {

    private String name;
    private String label;
    private String value;

    public StatusView(String name) {
        this.name = name;
        this.label = name+": ";
    }

    @Override
    public void draw(Graphics2D g) {
        g.setFont(AcidGUI.BOLD);
        g.drawString(label, 0, getHeight(g));

        g.setFont(AcidGUI.TEXT);
        g.drawString(value, getStringWidth(g, label, AcidGUI.BOLD), getHeight(g));
    }

    @Override
    public int getWidth(Graphics2D g) {
        return getStringWidth(g, label, AcidGUI.BOLD) + getStringWidth(g, value, AcidGUI.TEXT);
    }

    @Override
    public int getHeight(Graphics2D g) {
        return Math.max(getStringHeight(g, label, AcidGUI.BOLD), getStringHeight(g, value, AcidGUI.TEXT));
    }

    public void setValue(String value) {
        this.value = value;
        AcidGUI.invalidate();
    }

    public String getName() {
        return name;
    }
}
