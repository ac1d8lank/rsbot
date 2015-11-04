package com.ac1d.rsbot.falconry.data;

import com.ac1d.rsbot.util.rt6.NpcInteractTask;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Interactive;
import org.powerbot.script.rt6.Npc;

public enum FalconNpc {
    SPOTTED_KEBBIT  (5098, "Catch", "Spotted kebbit"),
    DARK_KEBBIT     (5099, "Catch", "Dark kebbit"),
    DASHING_KEBBIT  (5100, "Catch", "Dashing kebbit"),
    GYR_FALCON      (5094, "Retrieve", "Gyr Falcon"),
    ;


    public final int id;
    private final String name;
    private final String option;

    FalconNpc(int id, String name, String option) {
        this.id = id;
        this.name = name;
        this.option = option;
    }

    public static class Task extends NpcInteractTask {
        private static final int[] SMALL_BOUNDS = new int[] {-100, 100, -200, 0, -100, 100};
        private FalconNpc npc;

        public Task(FalconNpc npc) {
            super(npc.id, npc.name, npc.option);
            this.npc = npc;
        }

        @Override
        protected Npc getNpc(ClientContext ctx, int[] ids) {
            return ctx.npcs.select().id(ids).each(Interactive.doSetBounds(SMALL_BOUNDS)).nearest().poll();
        }

        @Override
        protected long getInteractDelayMillis() {
            return 1000;
        }
    }
}
