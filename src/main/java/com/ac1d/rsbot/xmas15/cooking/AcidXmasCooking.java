package com.ac1d.rsbot.xmas15.cooking;

import com.ac1d.rsbot.util.AcidGUI;
import com.ac1d.rsbot.util.AcidScript;
import com.ac1d.rsbot.util.TaskManager;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Constants;

import java.awt.*;

@Script.Manifest(name = "AcidXmasCooking", description = "Cooks food at 2015 Christmas Event (For Your Ice Only)", properties = "client=6;hidden=false;vip=false;")
public class AcidXmasCooking extends AcidScript<ClientContext> {
    private CookingManager mManager;
    private int mStartXp;

    @Override
    public void start() {
        super.start();
        mManager = new CookingManager(ctx);
        mStartXp = ctx.skills.experience(Constants.SKILLS_COOKING);
    }

    @Override
    public TaskManager<ClientContext> getTaskManager() {
        return mManager;
    }

    @Override
    public void onGUI() {

        if (mManager != null) {
            final long runtime = Math.max(0, ctx.controller.script().getRuntime());
            final int runHours = (int) (runtime / (60 * 60 * 1000));
            final int runMins = (int) (runtime / (60 * 1000)) % 60;
            final int runSecs = (int) (runtime / 1000) % 60;

            AcidGUI.setStatus("Uptime", String.format("%02d:%02d:%02d", runHours, runMins, runSecs));

            final long xp = ctx.skills.experience(Constants.SKILLS_COOKING) - mStartXp;

            AcidGUI.setStatus("XP Gained", xp);
            AcidGUI.setStatus("XP/hr", (xp * 60 * 60 * 1000) / runtime);

        }
    }
}
