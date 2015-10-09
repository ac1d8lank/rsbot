package com.ac1d.rsbot.util.rt6;

import com.ac1d.rsbot.util.Task;
import org.powerbot.script.Interactive;
import org.powerbot.script.Locatable;
import org.powerbot.script.rt6.ClientContext;

public abstract class InteractTask<O extends Interactive> extends Task<ClientContext> {

    private String mAction;
    private String mOption;

    private boolean mDone;

    public InteractTask(String action, String option) {
        mAction = action;
        mOption = option;
    }

    @Override
    public boolean isReady(ClientContext ctx) {
        if(!ctx.players.local().idle()) {
            return false;
        }

        return getEntity(ctx).valid();
    }

    @Override
    public void onStart(ClientContext ctx) {
        super.onStart(ctx);
        mDone = false;
    }

    @Override
    public void onPoll(ClientContext ctx) {
        O obj = getEntity(ctx);
        if(obj instanceof Locatable && !obj.inViewport()) {
            ctx.camera.turnTo((Locatable)obj);
            return;
        }

        mDone = obj.interact(mAction, mOption);
    }

    @Override
    public boolean isDone(ClientContext ctx) {
        return mDone;
    }

    protected abstract O getEntity(ClientContext ctx);
}
