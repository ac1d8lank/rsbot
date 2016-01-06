package com.ac1d.rsbot.temp.portables;

import com.ac1d.rsbot.util.AcidGUI;
import com.ac1d.rsbot.util.AcidScript;
import com.ac1d.rsbot.util.TaskManager;
import com.ac1d.rsbot.util.stats.SkillTrak;
import org.powerbot.script.rt6.ClientContext;

public abstract class AcidPortables extends AcidScript<ClientContext> {

    private PortableManager mManager;
    private int mLastXp;
    private int mStartXp;
    private PortableConfig config;

    @Override
    public void start() {
        super.start();

        config = getPortableConfig();
        mManager = new PortableManager(ctx, config);
        mStartXp = ctx.skills.experience(config.skill);
    }

    protected abstract PortableConfig getPortableConfig();

    @Override
    public TaskManager<ClientContext> getTaskManager() {
        return mManager;
    }

    @Override
    public void onGUI() {
        super.onGUI();

        if (mManager != null) {
            SkillTrak.drawBasicUI(ctx, config.skill);
            AcidGUI.setStatus("Task", mManager.currentTask());
        }
    }

    public static class PortableConfig {
        public int[]  itemIds;
        public int    skill;
        public int    portId;
        public String portAction;
        public String portOption;
    }
}
