package com.ac1d.rsbot.xmas15.skating;

import com.ac1d.rsbot.util.AcidGUI;
import com.ac1d.rsbot.util.AcidScript;
import com.ac1d.rsbot.util.TaskManager;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Constants;

import java.awt.*;

@Script.Manifest(name = "AcidIceSkate", description = "Ice Skates at the 2015 Christmas Event for Agility (Live and Let Slide)", properties = "client=6;hidden=false;vip=false;")
public class AcidIceSkate extends AcidScript<ClientContext> {
    private static final boolean DEBUG = false;

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
            final int runMins = (int) (runtime / (60 * 1000)) % 60;
            final int runSecs = (int) (runtime / 1000) % 60;

            AcidGUI.setStatus("Uptime", String.format("%02d:%02d:%02d", runHours, runMins, runSecs));

            final long xp = ctx.skills.experience(Constants.SKILLS_AGILITY) - mStartXp;

            AcidGUI.setStatus("XP Gained", xp);
            AcidGUI.setStatus("XP/hr", (xp * 60 * 60 * 1000) / runtime);

            if(DEBUG) {
                AcidGUI.setStatus("Task", mManager.currentTask());
            }
            AcidGUI.setStatus("Segment", Segment.ofPlayer(ctx));
            AcidGUI.setStatus("Lane", Lane.ofPlayer(ctx));
        }
    }

    @Override
    public void onDraw(Graphics2D g2d) {
        super.onDraw(g2d);

        if(DEBUG) {
            Path.onDraw();
            for (Lane l : Lane.values()) {
                new Path(ctx, l).debugDraw(ctx, g2d);
            }
        }
    }
}
