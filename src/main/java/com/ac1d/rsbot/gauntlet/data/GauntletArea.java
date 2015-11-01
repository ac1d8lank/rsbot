package com.ac1d.rsbot.gauntlet.data;

import com.ac1d.rsbot.util.AreaUtils;
import com.ac1d.rsbot.util.Task;
import org.powerbot.script.Area;
import org.powerbot.script.Locatable;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.LocalPath;
import org.powerbot.script.rt6.Player;

import java.util.HashMap;

public enum GauntletArea {
    BURTH_LODE      (0, 2880, 3547, 2904, 3554, false),
    MAIN            (1, 10509, 4868, 10545, 4986),
    RIVER           (0, 10498, 4877, 10558, 4986),

    JETTY1_UP       (0, 10513, 4906, 10519, 4912),
    JETTY2_UP       (0, 10543, 4906, 10536, 4912),
    JETTY1_DOWN     (1, 10515, 4901, 10517, 4906),
    JETTY2_DOWN     (1, 10537, 4901, 10539, 4906),

    BRIDGE          (1, 10524, 4898, 10530, 4976),
    MEETING_POINT   (1, 10524, 4903, 10530, 4916), // AKA BRIDGE 0
    BRIDGE1         (1, 10524, 4917, 10530, 4934),
    BRIDGE2         (1, 10524, 4935, 10530, 4950),
    BRIDGE3         (1, 10524, 4951, 10530, 4970),
    ;

    // They seem to either have different instance locations, or moved the event after I measured everything


    public static GauntletArea[] COMBAT_AREAS = { BRIDGE1, BRIDGE2, BRIDGE3 };

    /**
     * Map of the possible areas for this instance, null key is non-offset
     */
    private final HashMap<Offset, Area> areas = new HashMap<>();
    private final boolean useOffsets;
    private Offset lastMatchedOffset;

    GauntletArea(int floor, int x1, int y1, int x2, int y2) {
        this(floor,  x1,  y1,  x2,  y2, true);
    }

    GauntletArea(int floor, int x1, int y1, int x2, int y2, boolean useOffsets) {
        // Can't define this as a constant due to illegal forward referencing :(
        final Offset[] OFFSETS = {
                new Offset(960, 0),
        };

        areas.put(null, AreaUtils.rect(floor, x1, y1, x2, y2));
        if(useOffsets) {
            for (Offset o : OFFSETS) {
                areas.put(o, AreaUtils.rect(floor, x1 + o.x, y1 + o.y, x2 + o.x, y2 + o.y));
            }
        }

        this.useOffsets = useOffsets;
    }

    public Area getArea() {
        return areas.get(lastMatchedOffset);
    }

    public boolean containsPlayer(ClientContext ctx) {
        return contains(ctx.players.local());
    }

    public boolean contains(Locatable l) {
        final Area defaultArea = areas.get(null);
        if(defaultArea.contains(l)) {
            return true;
        }
        if(!useOffsets) {
            return false;
        }
        for(Offset o : areas.keySet()) {
            if(areas.get(o).contains(l)) {
                lastMatchedOffset = o;
                return true;
            }
        }
        return false;
    }

    // Copied MoveTask, allows for switching to other areas by the offset
    public static class MoveTask extends Task<ClientContext> {
        private GauntletArea mFrom;
        private GauntletArea mTo;
        private boolean mMoved;

        public MoveTask(GauntletArea from, GauntletArea to) {
            mFrom = from;
            mTo = to;
        }

        @Override
        public boolean isReady(ClientContext ctx) {
            mTo.containsPlayer(ctx); // Re-check offset
            final LocalPath path = ctx.movement.findPath(mTo.getArea().getRandomTile());
            System.out.println(toString()+" valid?="+path.valid());
            return (mFrom == null || mFrom.containsPlayer(ctx)) && path.valid();
        }

        @Override
        public void onPoll(ClientContext ctx) throws FailureException {
            if(ctx.players.local().idle()) {
                ctx.movement.findPath(mTo.getArea().getRandomTile()).traverse();
                mMoved = true;
            }
        }

        @Override
        public boolean isDone(ClientContext ctx) {
            final Player p = ctx.players.local();
            System.out.println(toString()+" Moved="+mMoved+", idle="+p.idle()+", toContains="+mTo.contains(p));
            return mMoved && p.idle() && mTo.contains(p);
        }

        @Override
        public String toString() {
            return String.format("MoveTask[from=%1$s, to=%2$s]", mFrom, mTo);
        }
    }

    private static class Offset {
        public int x;
        public int y;

        public Offset(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
