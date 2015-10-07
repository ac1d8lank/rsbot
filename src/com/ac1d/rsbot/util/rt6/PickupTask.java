package com.ac1d.rsbot.util.rt6;

import com.ac1d.rsbot.util.Task;
import org.powerbot.script.Nillable;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.GroundItem;

public class PickupTask extends Task<ClientContext> {

    private int mId;
    private String mName;

    public PickupTask(int id, String name) {
        mId = id;
        mName = name;
    }

    @Override
    public String tick(ClientContext ctx) {
        if(!ctx.players.local().idle()) {
            skip();
            return "Player busy";
        }

        GroundItem item = findGroundItem(ctx, mId);
        if(item == null || !item.valid()) {
            skip();
            return "Can't find " + mName;
        }

        if(!item.inViewport()) {
            ctx.camera.turnTo(item);
            return "Looking towards " + mName;
        }

        if(item.interact("Take", mName)) {
            done();
            return "Picking up " + mName;
        }

        return "Unable to pick up " + mName;
    }

    protected GroundItem findGroundItem(ClientContext ctx, int id) {
        return ctx.groundItems.select().id(id).nearest().poll();
    }
}
