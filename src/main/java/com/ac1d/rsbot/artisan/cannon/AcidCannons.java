package com.ac1d.rsbot.artisan.cannon;

import com.ac1d.rsbot.util.AcidScript;
import com.ac1d.rsbot.util.TaskManager;
import org.powerbot.script.rt6.ClientContext;

public class AcidCannons extends AcidScript<ClientContext> {

    private CannonManager mManager;

    @Override
    public void start() {
        super.start();

        mManager = new CannonManager(ctx);
    }

    @Override
    public TaskManager<ClientContext> getTaskManager() {
        return mManager;
    }
}
