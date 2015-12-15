package com.ac1d.rsbot.util;

import com.ac1d.rsbot.util.gui.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;

public class AcidGUI {
    private AcidGUI() {}

    // Sizes
    private static final int MARGIN = 10;
    private static final int PADDING = 6;
    private static final int MIN_WIDTH = 10;
    private static final int MIN_HEIGHT = 10;

    // Fonts
    public static final Font TITLE = new Font("Verdana", Font.BOLD, 20);
    public static final Font SUBTITLE = new Font("Verdana", Font.BOLD, 10);
    public static final Font BOLD = new Font("Verdana", Font.BOLD, 14);
    public static final Font TEXT = new Font("Verdana", Font.PLAIN, 14);

    // Colors
    public static final Color ACID = new Color(.8f, 1f, .2f);
    public static final Color BACKGROUND = new Color(0f, 0f, 0f, 0.75f);

    // Data
    private static TitleView title;

    // Layout n Views
    public static int guiWidth;
    public static int guiHeight;
    private static ArrayList<View> views = new ArrayList<>();
    private static HashMap<String, StatusView> statusViews = new HashMap<>();
    private static HashMap<View, Rectangle> viewLocations = new HashMap<>();

    static {
        title = addView(new TitleView("AcidTitle", TITLE));
        addView(new TitleView("by Ac1d8lank", SUBTITLE));
    }

    public static void setTitle(String title) {
        AcidGUI.title.setTitle(title);
    }

    public static void draw(Graphics2D g) {
        layout(g);

        AffineTransform origin = g.getTransform();

        // Box
        g.setColor(BACKGROUND);
        g.fillRect(MARGIN, MARGIN, guiWidth + PADDING * 2, guiHeight + PADDING * 2);
        g.setColor(ACID);
        g.drawRect(MARGIN, MARGIN, guiWidth + PADDING * 2, guiHeight + PADDING * 2);

        // Views
        for(View v : viewLocations.keySet()) {
            if(!v.isVisible()) continue;
            g.setTransform(origin);

            Rectangle r = viewLocations.get(v);
//            g.draw(r);
            g.translate(r.x, r.y);

            v.draw(g);
        }

        g.setTransform(origin);
    }

    private static void layout(Graphics2D g) {
        int x = 0;
        int y = 0;
        guiWidth = MIN_WIDTH;
        guiHeight = MIN_HEIGHT;

        x += MARGIN + PADDING;
        y += MARGIN + PADDING;

        // Sizing pass
        for(View v : views) {
            if(!v.isVisible()) continue;
            final int w = v.getWidth(g);
            final int h = v.getHeight(g);

            guiWidth = Math.max(guiWidth, w);
            guiHeight += h;
        }

        // Bounds pass
        for(View v : views) {
            if(!v.isVisible()) continue;
            final int w = v.getWidth(g);
            final int h = v.getHeight(g);

            final Rectangle r;
            if(!viewLocations.containsKey(v)) {
                r = new Rectangle();
                viewLocations.put(v, r);
            } else {
                r = viewLocations.get(v);
            }
            r.setBounds(x, y, guiWidth, h);

            y += h;
        }
    }

    public static void invalidate() {

    }

    public static void onMouseEvent(MouseEvent e) {
        if(e.getID() == MouseEvent.MOUSE_RELEASED) {
            for(View v : viewLocations.keySet()) {
                if(viewLocations.get(v).contains(e.getPoint())) {
                    v.onClick();
                }
            }
        }
    }

    public static <V extends View> V addView(V view) {
        views.add(view);
        if(view instanceof StatusView) {
            final StatusView sv = (StatusView) view;
            statusViews.put(sv.getName(), sv);
        }
        return view;
    }

    public static void setStatus(String name, Object value) {
        if(!statusViews.containsKey(name)) {
            addView(new StatusView(name));
        }
        String str = value == null ? "" : value.toString();
        statusViews.get(name).setValue(str);
    }
}
