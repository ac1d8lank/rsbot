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
    public String tick(ClientContext ctx) {
        if(!ctx.players.local().idle()) {
            skip();
            return "Player busy";
        }

        GameObject obj = getGameObject(ctx, mId);
        if(obj == null || !obj.valid()) {
            skip();
            return "Can't find " + mOption;
        }

        if(!obj.inViewport()) {
            ctx.camera.turnTo(obj);
            return "Looking towards " + mOption;
        }

        if(obj.interact(mAction, mOption)) {
            done();
            return "Interacting";
        }

        return "Unable to interact";
    }

    protected GameObject getGameObject(ClientContext ctx, int id) {
        return ctx.objects.select().id(id).nearest().poll();
    }
}
