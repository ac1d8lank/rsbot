package com.ac1d.rsbot.hallo15.data;

import com.ac1d.rsbot.util.rt6.NpcInteractTask;
import org.powerbot.script.Filter;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Npc;

public enum HalloNpc {
    WAYWARD("Rescue", "Wayward Soul", new IntRange(21945, 21976)),
    XP("Rescue", null, 21932), // Same for river and bridge souls
    RESCUED(null, "Rescued souls", 21934),
    ATTACK_DEVOURER("Attack", "Soul Devourer", 21937), // they stay within 4 tiles
    SIPHON_DEVOURER("Siphon", "Soul Devourer", 21938), // they stay within 4 tiles
    AVATAR("Attack", "Avatar of Amascut" ,21939)
    ;

    /*  XP SOULS:
     *  Soul of a strongman - #21932
     *  Soul of a ranger - #21932
     *  Soul of a dungeoneer - #21932
     *  Soul of a warrior - #21932
     */

    /** State when the soul is visible **/
    private static int STANCE_VISIBLE = 27644;

    private final String action;
    private final String name;
    private final int[] ids;

    HalloNpc(String action, String name, IntRange range) {
        this(action, name, range.toArray());
    }

    HalloNpc(String action, String name, int... ids) {
        this.action = action;
        this.name = name;
        this.ids = ids;
    }

    public Npc find(ClientContext ctx) {
        return ctx.npcs.select().within(16).select(new Filter<Npc>() {
            @Override
            public boolean accept(Npc npc) {
                switch(HalloNpc.this) {
                    case WAYWARD:
                    case XP:
                        return npc.stance() == STANCE_VISIBLE;
                }
                return true;
            }
        }).id(ids).nearest().poll();
    }

    private static class IntRange {
        private final int first;
        private final int last;

        public IntRange(int first, int last) {
            this.first = first;
            this.last = last;
        }

        public int[] toArray() {
            int[] values = new int[last - first + 1];
            for(int i = 0; i < values.length; i++) {
                values[i] = first + i;
            }
            return values;
        }
    }

    public static class InteractTask extends NpcInteractTask {
        private final HalloNpc halloNpc;

        public InteractTask(HalloNpc npc) {
            super(npc.ids, npc.action, npc.name);
            halloNpc = npc;
        }

        @Override
        protected Npc getNpc(final ClientContext ctx, int[] ids) {
            return halloNpc.find(ctx);
        }
    }
}
