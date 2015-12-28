package com.ac1d.rsbot.xmas15.crafting;

import com.ac1d.rsbot.util.AcidGUI;
import com.ac1d.rsbot.util.AcidScript;
import com.ac1d.rsbot.util.TaskManager;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Constants;

@Script.Manifest(name = "AcidIceCrafting", description = "Crafts ice sculptures at 2015 Christmas event.", properties = "client=6;hidden=false;vip=false")
public class AcidIceCrafting extends AcidScript<ClientContext> {
    private TaskManager<ClientContext> mManager;
    private int mStartXp;
    private int mLastXp;

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

            AcidGUI.setStatus("Uptime", formatMillis(runtime));

            final int xp = ctx.skills.experience(Constants.SKILLS_CRAFTING);
            if(xp != mLastXp) {
                mLastXp = xp;
                final long xpSoFar = xp - mStartXp;

                AcidGUI.setStatus("XP Gained", xpSoFar);
                AcidGUI.setStatus("XP/hr", (xpSoFar * 60 * 60 * 1000) / runtime);

                int nextLevel = ctx.skills.level(Constants.SKILLS_CRAFTING) + 1;
                final long xpToNext = ctx.skills.experienceAt(nextLevel) - xp;
                final long timeToNext = (xpToNext * runtime) / xpSoFar;
                AcidGUI.setStatus("Next Level", nextLevel + " in " + formatMillis(timeToNext));
            }

            AcidGUI.setStatus("Task", mManager.currentTask());
        }
    }

    private static String formatMillis(long millis) {
        final int hours = (int) (millis / (60 * 60 * 1000));
        final int mins = (int) (millis / (60 * 1000)) % 60;
        final int secs = (int) (millis / 1000) % 60;
        return String.format("%02d:%02d:%02d", hours, mins, secs);
    }
}
