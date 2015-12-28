package com.ac1d.rsbot.artisan.cannon;

import com.ac1d.rsbot.util.Task;
import com.ac1d.rsbot.util.TaskManager;
import org.powerbot.script.rt6.ClientContext;

import java.util.ArrayList;
import java.util.List;

public class CannonManager extends TaskManager<ClientContext> {
    public CannonManager(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public List<Task<ClientContext>> getAvailableTasks() {
        ArrayList<Task<ClientContext>> tasks = new ArrayList<>();



        return tasks;
    }
}
