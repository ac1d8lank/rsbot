package com.ac1d.rsbot.util;

import org.powerbot.script.ClientContext;

public abstract class Task<C extends ClientContext> {
    /**
     * @return true if the Task is ready to run
     */
    public abstract boolean isReady(C ctx);

    /**
     * Runs before the first poll of this Task's lifecycle
     */
    public void onStart(C ctx) {}

    /**
     * Runs each poll while this Task is active
     */
    public abstract void onPoll(C ctx) throws FailureException;

    /**
     * @return true if the Task has completed
     */
    public abstract boolean isDone(C ctx);

    /**
     * Runs once at the end of the Task's lifecycle
     */
    public void onFinish() {}

    public static class FailureException extends Exception {}
}