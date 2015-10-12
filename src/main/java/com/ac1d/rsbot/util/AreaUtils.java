package com.ac1d.rsbot.util;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;

import java.awt.*;

public class AreaUtils {
    /**
     * Creates an {@link Area} bounded by (x1, y1) and (x2, y2), inclusive
     */
    public static Area rect(int x1, int y1, int x2, int y2) {
        return rect(0, x1, y1, x2, y2);
    }


    /**
     * Creates an {@link Area} bounded by (x1, y1) and (x2, y2) at z height, inclusive
     */
    public static Area rect(int z, int x1, int y1, int x2, int y2) {
        return inclusive(new Tile(x1, y1, z), new Tile(x2, y2, z));
    }

    public static Area poly(int z, int... coords) {
        Tile[] tiles = new Tile[coords.length/2];
        for(int i = 0; i < tiles.length; i++) {
            tiles[i] = new Tile(coords[i*2], coords[i*2+1], z);
        }
        return inclusive(tiles);
    }

    public static Area inclusive(Tile t1, Tile t2) {
        final int w = Math.min(t1.x(), t2.x());
        final int e = Math.max(t1.x(), t2.x())+1; // E is exclusive
        final int n = Math.max(t1.y(), t2.y())+1; // N is exclusive
        final int s = Math.min(t1.y(), t2.y());
        return new Area(new Tile(w, n, t1.floor()), new Tile(e, s, t2.floor()));
    }

    public static Area inclusive(Tile... tiles) {
        // TODO: make this one work in testing project first
        return new Area(tiles);
    }
}
