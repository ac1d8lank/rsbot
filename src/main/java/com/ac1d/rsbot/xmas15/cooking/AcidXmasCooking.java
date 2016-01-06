package com.ac1d.rsbot.xmas15.cooking;

import com.ac1d.rsbot.util.AcidGUI;
import com.ac1d.rsbot.util.AcidScript;
import com.ac1d.rsbot.util.TaskManager;
import com.ac1d.rsbot.util.stats.SkillTrak;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Constants;

@Script.Manifest(name = "AcidXmasCooking", description = "Cooks food at 2015 Christmas Event (For Your Ice Only)", properties = "client=6;hidden=true;vip=false;")
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
        SkillTrak.drawBasicUI(ctx, Constants.SKILLS_COOKING);
    }
}
