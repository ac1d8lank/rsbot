package com.ac1d.rsbot.util;

import org.powerbot.script.ClientContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple TaskManager that loops through a list of tasks
 */
public class CycleTaskManager<C extends ClientContext> extends TaskManager<C> {

    private List<Task<C>> mList = new ArrayList<>();
    private int mPosition = 0;

    public CycleTaskManager(C ctx) {
        super(ctx);
    }

    public void addTask(Task<C> task) {
        mList.add(task);
    }

    public void clearTasks() {
        mList.clear();
        mPosition = 0;
    }

    @Override
    public Task<C> nextTask() {
        if(mPosition >= mList.size()) {
            return null;
        }

        final Task<C> next =  mList.get(mPosition);
        mPosition = (mPosition + 1) % mList.size();
        return next;
    }
}
