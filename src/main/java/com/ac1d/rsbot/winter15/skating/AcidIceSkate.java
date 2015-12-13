package com.ac1d.rsbot.winter15.skating;

import com.ac1d.rsbot.util.AcidGUI;
import com.ac1d.rsbot.util.AcidScript;
import com.ac1d.rsbot.util.TaskManager;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Constants;
import org.powerbot.script.rt6.Skills;

import java.awt.*;

@Script.Manifest(name = "Acid Ice Skate", description = "Ice Skates at the 2015 Christmas Event (Live and Let Slide)", properties = "client=6;hidden=false;vip=false;")
public class AcidIceSkate extends AcidScript<ClientContext> {
    private SkateManager mManager;
    private int mStartXp;

    @Override
    public void start() {
        super.start();

        mManager = new SkateManager(ctx);
        mStartXp = ctx.skills.experience(Constants.SKILLS_AGILITY);
    }

    @Override
    public TaskManager<ClientContext> getTaskManager() {
        return mManager;
    }

    @Override
    public void onGUI() {
        if(mManager != null) {
            final long runtime = ctx.controller.script().getRuntime();
            final int runHours = (int) (runtime / (60 * 60 * 1000));
            final int runMins = (int) runtime / (60 * 1000);
            final int runSecs = (int) runtime / 1000;

            AcidGUI.setData("Uptime", String.format("%02d:%02d:%02d", runHours, runMins, runSecs));

            final long xp = ctx.skills.experience(Constants.SKILLS_AGILITY) - mStartXp;

            AcidGUI.setData("XP Gained", xp);
            AcidGUI.setData("XP/hr", (xp * 60 * 60 * 1000) / runtime);

            AcidGUI.setData("Task", mManager.currentTask());
            AcidGUI.setData("Segment", Segment.ofPlayer(ctx));
            AcidGUI.setData("Lane", Lane.ofPlayer(ctx));
        }
    }

    @Override
    public void onDraw(Graphics2D g2d) {
        super.onDraw(g2d);

        for(Lane l : Lane.values()) {
            new Path(ctx, l).debugDraw(ctx, g2d);
        }
    }
}
