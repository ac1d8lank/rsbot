package com.ac1d.rsbot.util;

import org.powerbot.script.ClientAccessor;
import org.powerbot.script.ClientContext;

public abstract class Task<C extends ClientContext> extends ClientAccessor<C> {

    private boolean mDone;

    public Task(C ctx) {
        super(ctx);
    }

    public abstract String tick();

    /**
     * Reset this task to be ready to run again
     */
    public void reset() {
        mDone = false;
    }

    /**
     * @return whether this Task has completed
     */
    public final boolean isDone() {
        return mDone;
    }

    /**
     * Mark this Task as done
     */
    protected void done() {
        mDone = true;
    }
}
