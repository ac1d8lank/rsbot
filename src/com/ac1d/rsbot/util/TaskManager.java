package com.ac1d.rsbot.util;

import org.powerbot.script.ClientAccessor;
import org.powerbot.script.ClientContext;

public abstract class TaskManager<C extends ClientContext> extends ClientAccessor<C> {
    public TaskManager(C ctx) {
        super(ctx);
    }

    public abstract Task<C> nextTask();
}
