package com.ac1d.rsbot.util.rt6;

import com.ac1d.rsbot.util.Task;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GroundItem;

public class PickupTask extends Task<ClientContext> {

    private int mId;
    private String mName;

    public PickupTask(int id, String name) {
        mId = id;
        mName = name;
    }

    @Override
    public TickResult tick(ClientContext ctx) {
        if(!ctx.players.local().idle()) {
            return skip("Player busy");
        }

        GroundItem item = findGroundItem(ctx, mId);
        if(item == null || !item.valid()) {
            return skip("Can't find " + mName);
        }

        if(!item.inViewport()) {
            ctx.camera.turnTo(item);
            return retry("Looking towards " + mName);
        }

        if(item.interact("Take", mName)) {
            return done("Picking up " + mName);
        }

        // TODO: this should be a retry with N attempts
        return done("Unable to pick up " + mName);
    }

    protected GroundItem findGroundItem(ClientContext ctx, int id) {
        return ctx.groundItems.select().id(id).nearest().poll();
    }
}
