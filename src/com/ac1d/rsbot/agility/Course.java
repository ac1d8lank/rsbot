package com.ac1d.rsbot.agility;

import com.ac1d.rsbot.util.*;
import org.powerbot.script.Area;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.MobileIdNameQuery;
import org.powerbot.script.rt6.Player;

public class Course {
    public static final Course GNOME_STRONGHOLD = new Course(
            "Gnome Stronghold", Areas.rect(2469, 3440, 2490, 3414),
            new Obstacle("Walk-across", "Log balance", 69526,
                    Areas.rect(0, 2468, 3440, 2488, 3436)),
            new Obstacle("Climb-over", "Obstacle net", 69383,
                    Areas.rect(0, 2470, 3425, 2478, 3430)),
            new Obstacle("Climb", "Tree branch", 69508,
                    Areas.rect(1, 2476, 3424, 2471, 3422)),
            new Obstacle("Walk-on", "Balancing rope", 2312,
                    Areas.rect(2, 2472, 3421, 2477, 3418)),
            new Obstacle("Climb-down", "Tree branch", 69507,
                    Areas.rect(2, 2483, 3421, 2487, 3418)),
            new Obstacle("Climb-over", "Obstacle net", 69384,
                    Areas.rect(0, 2481, 3414, 2490, 3425)),
            new Obstacle("Squeeze-through", "Obstacle pipe", 69378,
                    Areas.rect(0, 2490, 3426, 2481, 3432))
    );

    public static final Course BURTHORPE = new Course(
            "Burthorpe", Areas.rect(2922, 3548, 2908, 3566),
            new Obstacle("Walk", "Log beam", 66895,
                    Areas.rect(2913, 3553, 2922, 3548)),
            new Obstacle("Climb-up", "Wall", 66912,
                    Areas.rect(2918, 3558, 2921, 3561)),
            new Obstacle("Walk-across", "Balancing ledge", 66909,
                    Areas.rect(1, 2920, 3562, 2918, 3564), Areas.rect(1, 2917, 3564, 2916, 3564)),
            new Obstacle("Climb-over", "Obstacle low wall", 66902,
                    Areas.rect(1, 2912, 3564, 2910, 3564), Areas.rect(1, 2910, 3563, 2910, 3561)),
            new Obstacle("Swing-on", "Rope swing", 66904,
                    Areas.rect(1, 2912, 3563, 2912, 3561)),
            new Obstacle("Swing-across", "Monkey bars", 66897,
                    Areas.rect(1, 2917, 3563, 2916, 3561)),
            new Obstacle("Jump-down", "Ledge", 66910,
                    Areas.rect(1, 2915, 3354, 2917, 3553))
    );

    public static final Course BARBARIAN_OUTPOST = new Course(
            "Barbarian Outpost", Areas.rect(2555, 3559, 2528, 3542),
            new Obstacle("Swing-on", "Rope swing", 43526,
                    Areas.rect(2554, 3559, 2543, 3550)), //TODO: Add obstacle for fail ladder
            new Obstacle("Walk-across", "Log balance", 43595,
                    Areas.rect(2554, 3549, 2549, 3543)), //TODO: Find/Add failure area
            new Obstacle("Climb over", "Obstacle net", 20211,
                    Areas.rect(2541, 3549, 2539, 3543)), // TODO: Find/Add failure area for balancing ledge
            new Obstacle("Walk-across", "Balancing ledge", 2302,
                    Areas.rect(1, 2538, 3547, 2536, 3545)),
            new Obstacle("Climb-down", "Ladder", 3205,
                    Areas.rect(1, 2532, 3547, 2532, 3546)),
            new Obstacle("Climb-over", "Crumbling wall", 1948,
                    Areas.rect(2532, 3546, 2537, 3555)),
            new Obstacle("Climb-over", "Crumbling wall", 1948, false,
                    Areas.rect(2538, 3552, 2542, 3554))
    );

    public static final Course[] ALL = {
            GNOME_STRONGHOLD,
            BURTHORPE,
            BARBARIAN_OUTPOST,
    };

    public final String name;
    public final Area courseArea;
    public final Obstacle[] obstacles;

    protected Course(String name, Area courseArea, Obstacle... obstacles) {
        this.name = name;
        this.courseArea = courseArea;
        this.obstacles = obstacles;
    }

    public Obstacle[] getObstacles() {
        return obstacles;
    }

    public static class Obstacle {
        public final String name;
        public final String action;
        public final int id;
        public final Area[] areas;
        public boolean nearest;

        public Obstacle(String action, String name, int id, Area... areas) {
            this(action, name, id, true, areas);
        }

        public Obstacle(String action, String name, int id, boolean nearest, Area... areas) {
            this.name = name;
            this.action = action;
            this.id = id;
            this.areas = areas;
            this.nearest = nearest;
        }
    }

    public static class ObstacleTask extends Task<ClientContext> {
        private final Obstacle o;

        public ObstacleTask(ClientContext ctx, Obstacle obstacle) {
            super(ctx);
            this.o = obstacle;
        }

        @Override
        public String tick() {
            final Player p = ctx.players.local();
            if(p.inMotion() || p.animation() != -1) {
                // We're running or animating, wait.
                return "Player active";
            }

            boolean inArea = false;
            for(Area a : o.areas) {
                if(a.contains(p)) {
                    inArea = true;
                    break;
                }
            }
            if(!inArea) {
                done();
                return "Finding obstacle";
            }

            final MobileIdNameQuery<GameObject> q = ctx.objects.select().id(o.id).nearest();
            GameObject obj = q.poll();
            if(!o.nearest) {
                // Get furthest
                for(GameObject o : q) {
                    obj = o;
                }
            }

            if(obj == null) {
                done();
                return "Finding obstacle";
            }

            if(!obj.inViewport() || Random.oneIn(3)) {
                ctx.camera.turnTo(obj);
                return "Moving camera";
            }


            if(obj.interact(o.action, o.name)) {
                done();
                return "Interacting";
            } else {
                return "Waiting to interact";
            }
        }
    }
}
