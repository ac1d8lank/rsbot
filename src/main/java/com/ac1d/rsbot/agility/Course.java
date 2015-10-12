package com.ac1d.rsbot.agility;

import com.ac1d.rsbot.util.*;
import com.ac1d.rsbot.util.rt6.MoveTask;
import com.ac1d.rsbot.util.rt6.ObjectInteractTask;
import org.powerbot.script.Area;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.MobileIdNameQuery;
import org.powerbot.script.rt6.Player;

public class Course {
    //TODO: simplify these constructors with a builder

    public static final Course GNOME_STRONGHOLD = new Course(
            "Gnome Stronghold", AreaUtils.rect(2469, 3440, 2490, 3414),
            new Obstacle("Walk-across", "Log balance", 69526,
                    AreaUtils.rect(0, 2468, 3440, 2488, 3436)),
            new Obstacle("Climb-over", "Obstacle net", 69383,
                    AreaUtils.rect(0, 2470, 3425, 2478, 3430)),
            new Obstacle("Climb", "Tree branch", 69508,
                    AreaUtils.rect(1, 2476, 3424, 2471, 3422)),
            new Obstacle("Walk-on", "Balancing rope", 2312,
                    AreaUtils.rect(2, 2472, 3421, 2477, 3418)),
            new Obstacle("Climb-down", "Tree branch", 69507,
                    AreaUtils.rect(2, 2483, 3421, 2487, 3418)),
            new Obstacle("Climb-over", "Obstacle net", 69384,
                    AreaUtils.rect(0, 2481, 3414, 2490, 3425)),
            new Obstacle("Squeeze-through", "Obstacle pipe", 69378,
                    AreaUtils.rect(0, 2490, 3426, 2481, 3432))
    );

    public static final Course BURTHORPE = new Course(
            "Burthorpe", AreaUtils.rect(2922, 3548, 2908, 3566),
            new Obstacle("Walk", "Log beam", 66895,
                    AreaUtils.rect(2913, 3553, 2922, 3548)),
            new Obstacle("Climb-up", "Wall", 66912,
                    AreaUtils.rect(2918, 3558, 2921, 3561)),
            new Obstacle("Walk-across", "Balancing ledge", 66909,
                    AreaUtils.rect(1, 2920, 3562, 2918, 3564), AreaUtils.rect(1, 2917, 3564, 2916, 3564)),
            new Obstacle("Climb-over", "Obstacle low wall", 66902,
                    AreaUtils.rect(1, 2912, 3564, 2910, 3564), AreaUtils.rect(1, 2910, 3563, 2910, 3561)),
            new Obstacle("Swing-on", "Rope swing", 66904,
                    AreaUtils.rect(1, 2912, 3563, 2912, 3561)),
            new Obstacle("Swing-across", "Monkey bars", 66897,
                    AreaUtils.rect(1, 2917, 3563, 2916, 3561)),
            new Obstacle("Jump-down", "Ledge", 66910,
                    AreaUtils.rect(1, 2915, 3354, 2917, 3553))
    );

    //TODO: Add zones for initial tube crawl-thru
    public static final Course BARBARIAN_OUTPOST = new Course(
            "Barbarian Outpost", new Area[] {AreaUtils.rect(2555, 3559, 2528, 3542), AreaUtils.rect(2546, 9955, 2555, 9948)},
            new Obstacle("Swing-on", "Rope swing", 43526,
                    AreaUtils.rect(2554, 3559, 2543, 3550)),
            new Obstacle("Climb-up", "Ladder", 32015,
                    AreaUtils.rect(2546, 9955, 2555, 9948)),
            new Obstacle("Walk-across", "Log balance", 43595,
                    AreaUtils.rect(2554, 3549, 2549, 3543)),
            new Obstacle("Climb over", "Obstacle net", 20211,
                    AreaUtils.rect(2545, 3549, 2539, 3542)),
            new Obstacle("Walk-across", "Balancing ledge", 2302,
                    AreaUtils.rect(1, 2538, 3547, 2536, 3545)),
            new MoveTask(AreaUtils.rect(2533, 3545, 2538, 3547), AreaUtils.rect(2539, 3544, 2541, 3548)),
            new Obstacle("Climb-down", "Ladder", 3205,
                    AreaUtils.rect(1, 2532, 3547, 2532, 3546)),
            new Obstacle("Climb-over", "Crumbling wall", 1948,
                    AreaUtils.rect(2532, 3546, 2532, 3547), AreaUtils.rect(2532, 3548, 2537, 3555)),
            new Obstacle("Climb-over", "Crumbling wall", 1948, false,
                    AreaUtils.rect(2538, 3552, 2542, 3554))
    );

    public static final Course[] ALL = {
            GNOME_STRONGHOLD,
            BURTHORPE,
            BARBARIAN_OUTPOST,
    };

    public final String name;
    public final Area[] courseAreas;
    public final Task<ClientContext>[] actions;

    @SafeVarargs
    protected Course(String name, Area courseArea, Task<ClientContext>... actions) {
        this(name, new Area[]{courseArea}, actions);
    }

    @SafeVarargs
    protected Course(String name, Area[] courseAreas, Task<ClientContext>... actions) {
        this.name = name;
        this.courseAreas = courseAreas;
        this.actions = actions;
    }

    public Task<ClientContext>[] getActions() {
        return actions;
    }

    public static class Obstacle extends ObjectInteractTask {
        public final Area[] areas;
        public boolean nearest;

        public Obstacle(String action, String name, int id, Area... areas) {
            this(action, name, id, true, areas);
        }

        public Obstacle(String action, String name, int id, boolean nearest, Area... areas) {
            super(id, action, name);
            this.areas = areas;
            this.nearest = nearest;
        }

        @Override
        public boolean isReady(ClientContext ctx) {
            final Player p = ctx.players.local();

            boolean inArea = false;
            for (Area a : areas) {
                if (a.contains(p)) {
                    inArea = true;
                    break;
                }
            }
            return inArea && super.isReady(ctx);
        }

        @Override
        protected GameObject getObject(ClientContext ctx, int id) {
            final MobileIdNameQuery<GameObject> q = ctx.objects.select().id(id).nearest();
            GameObject obj = q.poll();
            if(!nearest) {
                // Get furthest
                for(GameObject o : q) {
                    obj = o;
                }
            }
            return obj;
        }
    }
}
