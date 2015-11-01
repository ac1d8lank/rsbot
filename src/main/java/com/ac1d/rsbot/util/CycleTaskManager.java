package com.ac1d.rsbot.util;

import org.powerbot.script.ClientContext;

import java.util.ArrayList;
import java.util.Collections;
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
    public List<Task<C>> getAvailableTasks() {
        Collections.rotate(mList, -1);
        return mList;
    }
}
