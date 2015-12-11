package com.ac1d.rsbot.iceskate;

import com.ac1d.rsbot.util.*;
import com.ac1d.rsbot.util.Random;
import com.ac1d.rsbot.util.rt6.ComponentInteractTask;
import com.ac1d.rsbot.util.rt6.NpcInteractTask;
import org.powerbot.script.*;
import org.powerbot.script.rt6.*;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SkateManager extends TaskManager<ClientContext> {

    /*
     * Objects:
     * 100381 - Ice Patch
     */

    /*
     * NPCs:
     * 22021 - Penguin
     * 22022 - Sir Amik
     * 22013 - Jack Frost
     */

    private final NpcInteractTask START_SKATING = new NpcInteractTask(22013, "Ice skate", "Jack Frost") {
        @Override
        protected long getIdleDelayMillis() {
            return 0; // We'll be non-idle while skating
        }

        @Override
        public boolean isDone(ClientContext ctx) {
            return isSkating(ctx) || super.isDone(ctx);
        }
    };

    private final ComponentInteractTask EXIT_RINK = new ComponentInteractTask("Select", 1697, 37) {
        @Override
        public void onFinish(boolean success) {
            super.onFinish(success);
            mHitPenguin = false;
        }
    };

    private final Pattern PENGUIN = Pattern.compile("You bump into a penguin\\. Your XP is now reduced for the next [0-9]+ laps?\\.");
    private final Pattern ICE = Pattern.compile("You slip on the ice\\.");
    private final Pattern SIR_AMIK = Pattern.compile("You bump into Sir Amik Varze who spills his drink and promptly escorts you off the course\\.");

    public static final Area RINK = AreaUtils.inclusive(new Tile(3662, 3734, 1), new Tile(3683, 3744, 1));
    public static final Area AREA_INNER = AreaUtils.inclusive(new Tile(3664, 3736, 1), new Tile(3681, 3742, 1));

    public boolean mHitPenguin = false;

    public SkateManager(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public List<Task<ClientContext>> getAvailableTasks() {
        ArrayList<Task<ClientContext>> tasks = new ArrayList<>();

        if(mHitPenguin || isWalkGlitch()) {
            tasks.add(EXIT_RINK);
        }

        if(!isSkating(ctx)) {
            tasks.add(START_SKATING);
        }

        if(new Path(ctx).isBlocked(ctx)) {
            tasks.add(Lane.INNER.task);
            tasks.add(Lane.MIDDLE.task);
            tasks.add(Lane.OUTSIDE.task);
        }
        // Always try to get back to the inner lane if possible
        if(!AREA_INNER.contains(ctx.players.local())) {
            tasks.add(Lane.INNER.task);
        }

        tasks.add(RANDOM_LOOK);

        return tasks;
    }

    @Override
    public void onMessage(MessageEvent messageEvent) {
        super.onMessage(messageEvent);

        if(messageEvent.source() == null || messageEvent.source().isEmpty()) {
            if(PENGUIN.matcher(messageEvent.text()).matches()) {
                mHitPenguin = true;
            }
        }
    }

    private boolean isWalkGlitch() {
        final Player p = ctx.players.local();
        return p.stance() == 18040 && RINK.contains(p);
    }

    private static boolean isSkating(ClientContext ctx) {
        switch(ctx.players.local().stance()) {
            //18040?
            case 27845:
                return true;
            default:
                return false;
        }
    }

    private static final Task<ClientContext> RANDOM_LOOK = new Task<ClientContext>() {
        public boolean mDone;

        @Override
        public boolean isReady(ClientContext ctx) {
            return com.ac1d.rsbot.util.Random.oneIn(100);
        }

        @Override
        public void onPoll(ClientContext ctx) throws FailureException {
            ctx.camera.angle(90 * Random.get(3) + Random.get(-10, 10));
            mDone = true;
        }

        @Override
        public boolean isDone(ClientContext ctx) {
            if(mDone) {
                mDone = false;
                return true;
            }
            return false;
        }
    };

    public enum Segment {
        NORTH(3665, 3742, 3680, 3744),
        EAST(3681, 3737, 3683, 3741),
        SOUTH(3665, 3734, 3680, 3736),
        WEST(3662, 3737, 3664, 3741),;

        public final Area area;
        private final int width;
        private final int height;
        private final int left;
        private final int bottom;

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

    // Pathing goes current, same, same, diagonal, [diagonal]
    public static class Path {
        private ArrayList<Tile> tiles = new ArrayList<>();
        private static final Stroke sDebugStroke = new BasicStroke(2);

        public Path(ClientContext ctx) {
            this(ctx, Lane.get(ctx.players.local()));
        }

        public Path(ClientContext ctx, Lane to) {
            this(ctx.players.local().tile(), to);
        }

        public Path(Tile position, Lane to) {
            final Segment s = Segment.get(position);
            if(s == null) return;

            final Lane from = Lane.get(position);
            if(from == null) return;

            final int direction = to.ordinal() > from.ordinal() ? 1 : -1;

            Lane l = from;

            int idx = s.getIndex(position);
            tiles.add(s.getTile(l, ++idx));
            tiles.add(s.getTile(l, ++idx));
            while(l != to) {
                l = Lane.values()[l.ordinal() + direction];
                if(l != to) {
                    tiles.add(s.getTile(l, ++idx));
                }
            }
            tiles.add(s.getTile(l, ++idx));
            tiles.add(s.getTile(l, ++idx));
            tiles.add(s.getTile(l, ++idx));
            tiles.add(s.getTile(l, ++idx));
            tiles.add(s.getTile(l, ++idx));
        }

        public boolean isBlocked(ClientContext ctx) {
            return ctx.npcs.select().select(npcFilter).size() > 0 || ctx.objects.select().select(objectFilter).size() > 0;
        }

        private boolean contains(Locatable loc) {
            final Tile locTile = loc.tile();
            for (Tile t : tiles) {
                if (t.x() == locTile.x() && t.y() == locTile.y()) {
                    return true;
                }
            }
            return false;
        }

        private final Filter<Npc> npcFilter = new Filter<Npc>() {
            @Override
            public boolean accept(Npc npc) {
                return (npc.id() == 22021 || npc.id() == 22022) && contains(npc);
            }
        };

        private final Filter<GameObject> objectFilter = new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject obj) {
                return (obj.id() == 100381) && contains(obj);
            }
        };

        public void debugDraw(ClientContext ctx, Graphics g) {
            g.setColor(isBlocked(ctx) ? Color.RED : Color.BLUE);
            final Stroke oldStroke = ((Graphics2D)g).getStroke();
            ((Graphics2D)g).setStroke(sDebugStroke);

            for(int i = 0; i < tiles.size(); i++) {
                final Tile t = tiles.get(i);

                Point point = t.matrix(ctx).centerPoint();
                if(point.x > 0 && point.y > 0) {
                    g.fillOval(point.x-3, point.y-3, 6, 6);

                    if(i > 0) {
                        Point prev = tiles.get(i - 1).matrix(ctx).centerPoint();
                        if(prev.x > 0 && prev.y > 0) {
                            g.drawLine(point.x, point.y, prev.x, prev.y);
                        }
                    }
                }
            }
            ((Graphics2D)g).setStroke(oldStroke);
        }
    }
}
