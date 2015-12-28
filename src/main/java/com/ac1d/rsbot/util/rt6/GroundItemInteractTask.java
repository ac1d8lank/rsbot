package com.ac1d.rsbot.util.rt6;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GroundItem;

public class GroundItemInteractTask extends InteractTask<GroundItem>{

    private int mId;

    public GroundItemInteractTask(int id, String action, String option) {
        super(action, option);
        mId = id;
    }

    @Override
    protected final GroundItem findEntity(ClientContext ctx) {
        return getGroundItem(ctx, mId);
    }

    /** Override this for other selection methods */
    protected GroundItem getGroundItem(ClientContext ctx, int id) {
        return ctx.groundItems.select().id(id).nearest().poll();
    }
}
