package com.ac1d.rsbot.winter15.skating;

import org.powerbot.script.Filter;
import org.powerbot.script.Locatable;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Npc;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Path {
    private ArrayList<Tile> tiles = new ArrayList<>();
    private static final Stroke sDebugStroke = new BasicStroke(2);
    private static final HashMap<Tile, Point> sDebugPoints = new HashMap<>();

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

    public static void onDraw() {
        sDebugPoints.clear();
    }

    public void debugDraw(ClientContext ctx, Graphics g) {
        g.setColor(isBlocked(ctx) ? Color.RED : Color.BLUE);
        final Stroke oldStroke = ((Graphics2D)g).getStroke();
        ((Graphics2D)g).setStroke(sDebugStroke);

        for(int i = 0; i < tiles.size(); i++) {
            final Tile t = tiles.get(i);

            Point point = getPoint(ctx, t);
            if(point.x > 0 && point.y > 0) {
                g.fillOval(point.x-3, point.y-3, 6, 6);

                if(i > 0) {
                    Point prev = getPoint(ctx, tiles.get(i - 1));
                    if(prev.x > 0 && prev.y > 0) {
                        g.drawLine(point.x, point.y, prev.x, prev.y);
                    }
                }
            }
        }
        ((Graphics2D)g).setStroke(oldStroke);
    }

    private Point getPoint(ClientContext ctx, Tile t) {
        if(!sDebugPoints.containsKey(t)) {
            sDebugPoints.put(t, t.matrix(ctx).centerPoint());
        }
        return sDebugPoints.get(t);
    }
}
