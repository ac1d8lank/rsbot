package com.ac1d.rsbot.util.rt6;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Npc;

import java.awt.*;

public class NpcInteractTask extends InteractTask<Npc> {
    private int[] mIds;

    public NpcInteractTask(int id, String name, String option) {
        this(new int[] {id}, name, option);
    }

    public NpcInteractTask(int[] ids, String name, String option) {
        super(name, option);
        mIds = ids;
    }

    @Override
    protected Npc findEntity(ClientContext ctx) {
        return getNpc(ctx, mIds);
    }

    @Override
    protected boolean interact(Npc obj, String name, String option) {
        return super.interact(obj, name, option);
    }

    protected Npc getNpc(ClientContext ctx, int ids[]) {
        return ctx.npcs.select().id(ids).nearest().poll();
    }



    @Override
    public void onDraw(ClientContext ctx, Graphics2D g) {
        g.setColor(Color.yellow);
        getNpc(ctx, mIds).draw(g);
    }
}
