package com.ac1d.rsbot.artisan.armour;

import com.ac1d.rsbot.util.AcidGUI;
import com.ac1d.rsbot.util.AcidScript;
import com.ac1d.rsbot.util.TaskManager;
import com.ac1d.rsbot.util.stats.StatTrak;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Constants;

@Script.Manifest(name = "AcidArmour", description = "Automate Burial Armour in the Artisan Workshop!")
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
            final long runtime = Math.max(0, ctx.controller.script().getRuntime());

            AcidGUI.setStatus("Uptime", formatMillis(runtime));

            AcidGUI.setStatus("Instruction", BurialArmour.getInstruction(ctx));
            AcidGUI.setStatus("Working On", BurialArmour.getSelected(ctx));

            // XP Calcs
            final int xp = ctx.skills.experience(Constants.SKILLS_SMITHING);
            if(xp != mLastXp) {
                mLastXp = xp;
                final long xpSoFar = xp - mStartXp;

                AcidGUI.setStatus("XP Gained", xpSoFar);
                AcidGUI.setStatus("XP/hr", (xpSoFar * 60 * 60 * 1000) / runtime);

                int nextLevel = ctx.skills.level(Constants.SKILLS_SMITHING) + 1;
                final long xpToNext = ctx.skills.experienceAt(nextLevel) - xp;
                final long timeToNext = (xpToNext * runtime) / xpSoFar;
                AcidGUI.setStatus("Next Level", nextLevel + " in " + formatMillis(timeToNext));
            }

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

    private static String formatMillis(long millis) {
        final int hours = (int) (millis / (60 * 60 * 1000));
        final int mins = (int) (millis / (60 * 1000)) % 60;
        final int secs = (int) (millis / 1000) % 60;
        return String.format("%02d:%02d:%02d", hours, mins, secs);
    }
}
