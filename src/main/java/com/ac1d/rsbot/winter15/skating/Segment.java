package com.ac1d.rsbot.winter15.skating;

import com.ac1d.rsbot.util.AreaUtils;
import org.powerbot.script.Area;
import org.powerbot.script.Locatable;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

public enum Segment {
    NORTH(3665, 3742, 3680, 3744),
    EAST(3681, 3737, 3683, 3741),
    SOUTH(3665, 3734, 3680, 3736),
    WEST(3662, 3737, 3664, 3741),;

    public final Area area;
    public final int width;
    public final int height;
    public final int left;
    public final int bottom;

    Segment(int x1, int y1, int x2, int y2) {
        area = AreaUtils.inclusive(new Tile(x1, y1, 1), new Tile(x2, y2, 1));
        width = Math.abs(x2 - x1);
        height = Math.abs(y2 - y1);
        left = Math.min(x1, x2);
        bottom = Math.min(y1, y2);
    }

    public boolean containsPlayer(ClientContext ctx) {
        return area.contains(ctx.players.local());
    }

    public static Segment ofPlayer(ClientContext ctx) {
        return get(ctx.players.local());
    }

    public static Segment get(Locatable loc) {
        for(Segment s : values()) {
            if(s.area.contains(loc)) {
                return s;
            }
        }
        return null;
    }

    public Segment next() {
        return values()[(ordinal() + 1) % values().length];
    }

    public Tile getTile(Lane l, int index) {
        if(l == null) return null;

        int x = left;
        int y = bottom;

        // Lane offset
        switch(this) {
            case NORTH:
                y += 2 - l.ordinal();
                break;
            case EAST:
                x += 2 - l.ordinal();
                y += height;
                break;
            case SOUTH:
                y += l.ordinal();
                x += width;
                break;
            case WEST:
                x += l.ordinal();
                break;
        }

        // Maximum amount we can go straight in this lane
        int laneLength = getLength();

        // Outside adjustment part 1. We go one additionally straight
        if(l == Lane.OUTSIDE) {
            laneLength++;
        }

        // Amount to go straight
        final int straightAmt = Math.min(index, laneLength);
        switch(this) {
            case NORTH:
                x += straightAmt;
                break;
            case EAST:
                y -= straightAmt;
                break;
            case SOUTH:
                x -= straightAmt;
                break;
            case WEST:
                y += straightAmt;
                break;
        }

        // Amount to go after straight
        final int diagonalRemainder = index - straightAmt;
        final int diagonalMax = 2 - l.ordinal();
        final int diagonalAmt = Math.min(diagonalMax, diagonalRemainder);

        switch(this) {
            case NORTH:
                x += diagonalAmt;
                y -= diagonalAmt;
                break;
            case EAST:
                y -= diagonalAmt;
                x -= diagonalAmt;
                break;
            case SOUTH:
                x -= diagonalAmt;
                y += diagonalAmt;
                break;
            case WEST:
                y += diagonalAmt;
                x += diagonalAmt;
                break;
        }

        int finalRemainder = diagonalRemainder - diagonalAmt;

        // Outside adjustment part 2. We go one additionally perpendicular
        if(l == Lane.OUTSIDE && finalRemainder > 0) {
            finalRemainder--;
            switch(this) {
                case NORTH:
                    y--;
                    break;
                case EAST:
                    x--;
                    break;
                case SOUTH:
                    y++;
                    break;
                case WEST:
                    x++;
                    break;
            }
        }

        if(finalRemainder == 0) {
            return new Tile(x, y, 1);
        } else {
            return next().getTile(l, finalRemainder - 1);
        }
    }

    private int getLength() {
        if(this == NORTH || this == SOUTH) {
            return width;
        } else {
            return height;
        }
    }

    public int getIndex(Tile position) {
        final Lane l = Lane.get(position);
        for(int i = 0; i < getLength(); i++) {
            if(position.equals(getTile(l, i))) {
                return i;
            }
        }
        return getLength();
    }
}
