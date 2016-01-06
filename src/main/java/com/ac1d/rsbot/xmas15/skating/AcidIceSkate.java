package com.ac1d.rsbot.xmas15.skating;

import com.ac1d.rsbot.util.AcidGUI;
import com.ac1d.rsbot.util.AcidScript;
import com.ac1d.rsbot.util.TaskManager;
import com.ac1d.rsbot.util.stats.SkillTrak;
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
            SkillTrak.drawBasicUI(ctx, Constants.SKILLS_AGILITY);

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
