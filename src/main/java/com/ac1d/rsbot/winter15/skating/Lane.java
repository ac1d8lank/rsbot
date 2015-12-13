package com.ac1d.rsbot.winter15.skating;

import com.ac1d.rsbot.util.rt6.ComponentInteractTask;
import org.powerbot.script.Locatable;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;

public enum Lane {
    OUTSIDE (1697, 13),
    MIDDLE  (1697, 21),
    INNER   (1697, 29),
    ;

    public final ComponentInteractTask task;

    Lane(int widget, int component) {
        task = new ComponentInteractTask("Select", widget, component) {
            @Override
            public boolean isReady(ClientContext ctx) {
                return !(new Path(ctx, Lane.this).isBlocked(ctx)) && super.isReady(ctx);
            }

            @Override
            protected boolean interact(Component obj, String action, String option) {
                return obj.click();
            }

            @Override
            public boolean isDone(ClientContext ctx) {
                return ofPlayer(ctx) == Lane.this;
            }
        };
    }

    public static Lane ofPlayer(ClientContext ctx) {
        return get(ctx.players.local());
    }

    public static Lane get(Locatable loc) {
        final Segment s  = Segment.get(loc);
        if(s == null) return null;

        final int relX = loc.tile().x() - s.left;
        final int relY = loc.tile().y() - s.bottom;

        final int ordinal;

        switch(s) {
            case NORTH:
                ordinal = 2 - relY;
                break;
            case EAST:
                ordinal = 2 - relX;
                break;
            case SOUTH:
                ordinal = relY;
                break;
            case WEST:
                ordinal = relX;
                break;
            default:
                return null;
        }

        return Lane.values()[ordinal];
    }
}
