package com.ac1d.rsbot.ecto;

import com.ac1d.rsbot.util.rt6.ObjectInteractTask;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

public enum EctoObject {
    ENERGY_BARRIER      (52761, "Pass", "Energy Barrier"),
    ECTOFUNTUS          (5282, "Worship", "Ectofuntus"),
    GRINDER_UP          (37454, "Climb-up", "Staircase"),
    HOPPER              (11162, "Fill", "Hopper"),
    GRINDER_DOWN        (5281, "Climb-down", "Staircase"),
    TRAPDOOR_CLOSED     (5267, "Open", "Trapdoor"),
    TRAPDOOR_OPEN       (5268, "Climb-down", "Trapdoor"),
    LADDER              (5264, "Climb-up", "Ladder"),
    WEATHERED_WALL_DOWN (9308, "Jump-down", "Weathered wall"),
    WEATHERED_WALL_UP   (9307, "Jump-up", "Weathered wall"),
    T1_DOWN             (5263, "Climb-down", "Stairs"),
    T2_UP               (5262, "Climb-up", "Stairs"),
    T2_DOWN             (5263, "Climb-down", "Stairs"),
    T3_UP               (5262, "Climb-up", "Stairs"),
    T3_DOWN             (5263, "Climb-down", "Stairs"),
    POOL_UP             (5262, "Climb-up", "Stairs"),
    SLIME_POOL          (17119, "Use slime", "Pool of Slime"),
    ;

    public final int id;
    public final String action;
    public final String name;

    EctoObject(int id, String action, String name) {
        this.id = id;
        this.action = action;
        this.name = name;
    }

    public static class ObjectTask extends ObjectInteractTask {
        private EctoObject mObject;
        public ObjectTask(EctoObject o) {
            super(o.id, o.action, o.name);
            mObject = o;
        }

        @Override
        protected GameObject getObject(ClientContext ctx, int id) {
            switch(mObject) {
                case ENERGY_BARRIER:
                    return ctx.objects.select().id(id).nearest(EctoArea.PORT_EXIT.area.getCentralTile()).poll();
                default:
                    return super.getObject(ctx, id);
            }
        }
    }
}
