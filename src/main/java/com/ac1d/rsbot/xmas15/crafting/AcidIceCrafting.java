package com.ac1d.rsbot.xmas15.crafting;

import com.ac1d.rsbot.util.AcidGUI;
import com.ac1d.rsbot.util.AcidScript;
import com.ac1d.rsbot.util.TaskManager;
import com.ac1d.rsbot.util.stats.SkillTrak;
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
            SkillTrak.drawBasicUI(ctx, Constants.SKILLS_CRAFTING);
            AcidGUI.setStatus("Task", mManager.currentTask());
        }
    }
}
