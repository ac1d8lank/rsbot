package com.ac1d.rsbot.util.rt6;

import com.ac1d.rsbot.util.Task;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

public class InteractTask extends Task<ClientContext> {

    private int mId;
    private String mAction;
    private String mOption;

    public InteractTask(int id, String action, String option) {
        mId = id;
        mAction = action;
        mOption = option;
    }

    @Override
    public TickResult tick(ClientContext ctx) {
        if(!ctx.players.local().idle()) {
            return retry("Player busy");
        }

        GameObject obj = getGameObject(ctx, mId);
        if(obj == null || !obj.valid()) {
            return skip("Can't find "+mAction+" "+mOption);
        }

        if(!obj.inViewport()) {
            ctx.camera.turnTo(obj);
            return retry("Looking towards "+mAction+" "+mOption);
        }

        if(obj.interact(mAction, mOption)) {
            return done("Interacting: "+mAction+" "+mOption);
        }

        return done("Unable to interact: "+mAction+" "+mOption);
    }

    protected GameObject getGameObject(ClientContext ctx, int id) {
        return ctx.objects.select().id(id).nearest().poll();
    }
}
