package com.ac1d.rsbot.temp.portables;

import com.ac1d.rsbot.util.AcidScript;
import com.ac1d.rsbot.util.TaskManager;
import org.powerbot.script.rt6.ClientContext;

public abstract class AcidPortables extends AcidScript<ClientContext> {

    private PortableManager mManager;

    @Override
    public void start() {
        super.start();

        mManager = new PortableManager(ctx, getPortableConfig());
    }

    protected abstract PortableConfig getPortableConfig();

    @Override
    public TaskManager<ClientContext> getTaskManager() {
        return mManager;
    }

    public static class PortableConfig {
        public int[]  itemIds;
        public int    portId;
        public String portAction;
        public String portOption;
    }
}
