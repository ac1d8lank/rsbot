package com.ac1d.rsbot.util;

import org.powerbot.script.ClientAccessor;
import org.powerbot.script.ClientContext;
import org.powerbot.script.MessageEvent;

import java.util.List;

public abstract class TaskManager<C extends ClientContext> extends ClientAccessor<C> {
    public TaskManager(C ctx) {
        super(ctx);
    }

    private Task<C> currentTask;

    public final Task<C> nextTask() {
        final List<Task<C>> tasks = getAvailableTasks();
        for(Task<C> t : tasks) {
            if(t.isReady(ctx)) {
                currentTask = t;
                return t;
            }
        }
        currentTask = null;
        return null;
    }

    public final Task<C> currentTask() {
        return currentTask;
    }

    /**
     * @return a List of tasks that can run for the current state, in priority order
     */
    public abstract List<Task<C>> getAvailableTasks();

    public void onTaskSuccess(Task<C> task) {}

    public void onTaskFail(Task<C> task) {}

    public void onMessage(MessageEvent messageEvent) {
        if(currentTask != null) {
            currentTask.onMessage(messageEvent);
        }
    }
}
