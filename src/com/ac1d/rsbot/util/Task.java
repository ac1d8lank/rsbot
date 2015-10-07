package com.ac1d.rsbot.util;

import org.powerbot.script.ClientContext;

public abstract class Task<C extends ClientContext> {

    private long mCooldownOverTime;
    private TickResult mResult = new TickResult();

    /**
     * Allow this Task to perform some non-blocking work.
     * @return the state of this task
     */
    public abstract TickResult tick(C ctx);

    /**
     * Sets a cooldown on this task, this instance will be skipped until the cooldown ends.
     * Override to set a cooldown after done.
     * @return a cooldown in milliseconds.
     */
    public long getCooldownMillis() {
        return 0;
    }
    /**
     * @return true if this task is on cooldown
     */
    public boolean onCooldown() {
        return System.currentTimeMillis() < mCooldownOverTime;
    }

    public enum ResultState {
        /** Run next task, next tick (also wait for cooldown if used) **/
        DONE,
        /** Run next task, this tick **/
        SKIP,
        /** Run this task, next tick **/
        RETRY,
    }

    protected final TickResult done(String description) {
        mCooldownOverTime = System.currentTimeMillis() + getCooldownMillis();
        return setResult(ResultState.DONE, description);
    }

    protected final TickResult skip(String description) {
        return setResult(ResultState.SKIP, description);
    }

    protected final TickResult retry(String description) {
        return setResult(ResultState.RETRY, description);
    }

    private TickResult setResult(ResultState status, String description) {
        mResult.state = status;
        mResult.description = description;
        return mResult;
    }

    public class TickResult {
        private TickResult() {}
        private ResultState state;
        private String description;

        public ResultState getState() {
            return state;
        }

        public String getDescription() {
            return description;
        }
    }
}