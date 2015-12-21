package com.ac1d.rsbot.xmas15.crafting;

import com.ac1d.rsbot.util.AcidGUI;
import com.ac1d.rsbot.util.AcidScript;
import com.ac1d.rsbot.util.TaskManager;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Constants;

@Script.Manifest(name = "AcidIceCrafting", description = "Crafts ice scultures at 2015 Christmas event.")
public class AcidIceCrafting extends AcidScript<ClientContext> {
    private TaskManager<ClientContext> mManager;
    private int mStartXp;

    @Override
    public void start() {
        super.start();

        mStartXp = ctx.skills.experience(Constants.SKILLS_CRAFTING);
        mManager = new IceCraftingManager(ctx);
    }

    @Override
    public TaskManager<ClientContext> getTaskManager() {
        return mManager;
    }

    @Override
    public void onGUI() {
        super.onGUI();

        if (mManager != null) {
            final long runtime = Math.max(0, ctx.controller.script().getRuntime());
            final int runHours = (int) (runtime / (60 * 60 * 1000));
            final int runMins = (int) (runtime / (60 * 1000)) % 60;
            final int runSecs = (int) (runtime / 1000) % 60;

            AcidGUI.setStatus("Uptime", String.format("%02d:%02d:%02d", runHours, runMins, runSecs));

            final long xp = ctx.skills.experience(Constants.SKILLS_CRAFTING) - mStartXp;

            AcidGUI.setStatus("XP Gained", xp);
            AcidGUI.setStatus("XP/hr", (xp * 60 * 60 * 1000) / runtime);
            AcidGUI.setStatus("Task", mManager.currentTask());
        }
    }
}
