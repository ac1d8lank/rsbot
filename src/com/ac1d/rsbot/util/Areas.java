package com.ac1d.rsbot.util;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;

public class Areas {
    public static Area rect(int x1, int y1, int x2, int y2) {
        return rect(0, x1, y1, x2, y2);
    }

    public static Area rect(int z, int x1, int y1, int x2, int y2) {
        final int w = Math.min(x1, x2);
        final int e = Math.max(x1, x2)+1; // E is exclusive
        final int n = Math.max(y1, y2)+1; // N is exclusive
        final int s = Math.min(y1, y2);
        return new Area(new Tile(w, n, z), new Tile(e, s, z));
    }
}
