package com.ac1d.rsbot.util;

import org.powerbot.script.ClientAccessor;
import org.powerbot.script.ClientContext;

public abstract class Task<C extends ClientContext> {

    private boolean mDone;
    private long mCooldownOverTime;
    private boolean mSkipped;


    /**
     * Allow this Task to perform some non-blocking work.
     * @return the state of this task
     */
    public abstract String tick(C ctx);

    /**
     * Sets a cooldown on this task, this instance will be skipped until the cooldown ends.
     * @return a cooldown in milliseconds.
     */
    public long getCooldownMillis() {
        return 0;
    }

    /**
     * Reset this task to be ready to run again
     */
    public void reset() {
        mDone = false;
        mSkipped = false;
    }

    /**
     * @return whether this Task has completed
     */
    public final boolean isDone() {
        return mDone;
    }

    /**
     * @return whether skip was called to finish this task.
     */
    public boolean wasSkipped() {
        return mSkipped;
    }

    /**
     * @return true if this task is on cooldown
     */
    public boolean onCooldown() {
        return System.currentTimeMillis() < mCooldownOverTime;
    }

    /**
     * Mark this Task as done
     */
    public void done() {
        mDone = true;
        mSkipped = false;
        mCooldownOverTime = System.currentTimeMillis() + getCooldownMillis();
    }

    /**
     * Mark this Task as done, and go to next task in same poll
     */
    public void skip() {
        mDone = true;
        mSkipped = true;
        mCooldownOverTime = System.currentTimeMillis() + getCooldownMillis();
    }
}
