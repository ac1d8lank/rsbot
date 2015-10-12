package com.ac1d.rsbot.util.rt6;

import com.ac1d.rsbot.util.Task;
import org.powerbot.script.Interactive;
import org.powerbot.script.Locatable;
import org.powerbot.script.rt6.ClientContext;

public abstract class InteractTask<O extends Interactive> extends Task<ClientContext> {

    private String mAction;
    private String mOption;

    private boolean mDone;
    private long mInteractTime;

    public InteractTask(String action, String option) {
        mAction = action;
        mOption = option;
    }

    @Override
    public boolean isReady(ClientContext ctx) {
        return getEntity(ctx).valid();
    }

    @Override
    public void onStart(ClientContext ctx) {
        super.onStart(ctx);
        mDone = false;
    }

    @Override
    public void onPoll(ClientContext ctx) throws FailureException {
        O obj = getEntity(ctx);
        if(!obj.valid()) {
            throw new Task.FailureException();
        }
        if(obj instanceof Locatable && !obj.inViewport()) {
            ctx.camera.turnTo((Locatable)obj);
            return;
        }

        if(ctx.players.local().idle() && !onCooldown()) {
            mDone = obj.interact(mAction, mOption);
            if(mDone) {
                mInteractTime = System.currentTimeMillis();
            }
        }
    }

    private boolean onCooldown() {
        final long now = System.currentTimeMillis();
        return now - mInteractTime < getCooldownMillis();
    }

    protected long getCooldownMillis() {
        return 500;
    }

    @Override
    public boolean isDone(ClientContext ctx) {
        return !onCooldown() && ctx.players.local().idle() && mDone;
    }

    protected abstract O getEntity(ClientContext ctx);
}
