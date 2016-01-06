package com.ac1d.rsbot.artisan.armour;

import com.ac1d.rsbot.util.AcidGUI;
import com.ac1d.rsbot.util.AcidScript;
import com.ac1d.rsbot.util.TaskManager;
import com.ac1d.rsbot.util.stats.SkillTrak;
import com.ac1d.rsbot.util.stats.StatTrak;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Constants;

@Script.Manifest(name = "AcidArmour", description = "Makes Burial Armour in the Artisan Workshop!", properties = "client=6;hidden=false;vip=false;")
public class AcidArmour extends AcidScript<ClientContext> {

    private ArmourManager mManager;
    private int mLastXp;
    private int mStartXp;
    private int mLastBarCount;

    @Override
    public void start() {
        super.start();
        mManager = new ArmourManager(ctx);
        mStartXp = ctx.skills.experience(Constants.SKILLS_SMITHING);
    }

    @Override
    public TaskManager<ClientContext> getTaskManager() {
        return mManager;
    }

    @Override
    public void onGUI() {
        super.onGUI();

        if (mManager != null) {
            SkillTrak.drawBasicUI(ctx, Constants.SKILLS_SMITHING);

            // Track # of pieces made
            int barCount = ctx.backpack.select().id(BurialArmour.ALL_INGOTS).count();
            if(barCount != mLastBarCount) {
                if(barCount < mLastBarCount) {
                    StatTrak.addEvent("piece", mLastBarCount-barCount);
                    AcidGUI.setStatus("Pieces Made", StatTrak.TOTAL.getTotal("piece"));
                    AcidGUI.setStatus("Pieces/hr", StatTrak.HOURLY.getAverage("piece"));
                }
                mLastBarCount = barCount;
            }
        }
    }
}
