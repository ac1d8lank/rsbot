package com.ac1d.rsbot.util.rt6;

import com.ac1d.rsbot.util.RandomUtils;
import com.ac1d.rsbot.util.Task;
import org.powerbot.script.Interactive;
import org.powerbot.script.Locatable;
import org.powerbot.script.rt6.ClientContext;

public abstract class InteractTask<O extends Interactive> extends Task<ClientContext> {

    private String mAction;
    private String mOption;

    private O mObj;
    private boolean mInteracted;
    private long mInteractTime;
    private long mIdleStartTime;

    public InteractTask(String action, String option) {
        mAction = action;
        mOption = option;
    }

    @Override
    public boolean isReady(ClientContext ctx) {
        if(!ctx.players.local().idle()) {
            return false;
        }
        mObj = findEntity(ctx);
        return mObj.valid();
    }

    @Override
    public void onStart(ClientContext ctx) {
        super.onStart(ctx);
        mInteracted = false;
        mIdleStartTime = System.currentTimeMillis();
    }

    @Override
    public void onPoll(ClientContext ctx) throws FailureException {
        if(!mObj.valid()) {
            throw new Task.FailureException();
        }

        if(!ctx.players.local().idle()) {
            mIdleStartTime = System.currentTimeMillis();
        }

        if(mObj instanceof Locatable && (RandomUtils.percent(15) || !mObj.inViewport())) {
            ctx.camera.turnTo((Locatable)mObj);
            return;
        }

        if(!onInteractCooldown() && !onIdleDelay()) {
            if(interact(mObj, mAction, mOption)) {
                mInteractTime = System.currentTimeMillis();
                afterInteract(mObj);
            }
            mInteracted = true;
        }
    }

    protected boolean interact(O obj, String action, String option) {
        return option != null ? obj.interact(action, option) : obj.interact(action);
    }

    /** Called after each successful interaction */
    protected void afterInteract(O obj) {}

    private boolean onInteractCooldown() {
        final long now = System.currentTimeMillis();
        return now - mInteractTime < getInteractDelayMillis();
    }

    private long getIdle() {
        final long now = System.currentTimeMillis();
        return now - mIdleStartTime;
    }

    private boolean onIdleDelay() {
        return getIdle() < getIdleDelayMillis();
    }

    /**
     * Wait this many milliseconds before interacting or checking completeness
     */
    protected long getInteractDelayMillis() {
        return 3000;
    }

    /**
     * Player must be idle for this many milliseconds before interacting or completing
     */
    protected long getIdleDelayMillis() {
        return 1000;
    }

    @Override
    public boolean isDone(ClientContext ctx) {
        boolean done = !onInteractCooldown() && !onIdleDelay() && ctx.players.local().idle() && mInteracted;
        return done;
    }

    protected final O getEntity() {
        return mObj;
    }

    @Override
    public String toString() {
        return "InteractTask["+mAction+" "+mOption+", onIdleDelay="+onIdleDelay()+"("+getIdle()+"), onInteractCd="+onInteractCooldown()+"]";
    }

    protected abstract O findEntity(ClientContext ctx);
}
