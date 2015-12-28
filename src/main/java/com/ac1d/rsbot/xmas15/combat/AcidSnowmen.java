package com.ac1d.rsbot.xmas15.combat;

import com.ac1d.rsbot.util.AcidGUI;
import com.ac1d.rsbot.util.AcidScript;
import com.ac1d.rsbot.util.TaskManager;
import com.ac1d.rsbot.util.gui.Checkbox;
import com.ac1d.rsbot.util.gui.StatusView;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Constants;

import java.util.HashMap;

@Script.Manifest(name = "AcidSnowmen", properties = "Build snowmen at 2015 Christmas Event", description = "client=6;hidden=true;vip=false")
public class AcidSnowmen extends AcidScript<ClientContext> {
    private static final boolean DEBUG = false;
    private SnowmenManager mManager;

    private HashMap<Integer, Integer> mStartXps = new HashMap<>();
    private HashMap<Integer, StatusView> mXpViews = new HashMap<>();
    private HashMap<SnowmanType, Checkbox> checkboxes = new HashMap<>();

    @Override
    public void start() {
        super.start();
        mManager = new SnowmenManager(ctx);

        buildUI();
    }

    private void buildUI() {
        AcidGUI.setStatus("Uptime", null);

        mStartXps.put(Constants.SKILLS_CONSTRUCTION, ctx.skills.experience(Constants.SKILLS_CONSTRUCTION));
        mXpViews.put(Constants.SKILLS_CONSTRUCTION, AcidGUI.addView(new StatusView("Cstr. XP")));

        for(SnowmanType type : SnowmanType.values()) {
            mStartXps.put(type.skill, ctx.skills.experience(type.skill));
            final Checkbox c = AcidGUI.addView(new Checkbox(type.name(), true));
            mXpViews.put(type.skill, AcidGUI.addView(new StatusView("  XP"))); // cheap indent

            checkboxes.put(type, c);
        }
        mManager.setCheckboxes(checkboxes);
    }

    @Override
    public void onGUI() {
        super.onGUI();

        final long runtime = ctx.controller.script().getRuntime();
        final int runHours = (int) (runtime / (60 * 60 * 1000));
        final int runMins = (int) (runtime / (60 * 1000)) % 60;
        final int runSecs = (int) (runtime / 1000) % 60;

        AcidGUI.setStatus("Uptime", String.format("%02d:%02d:%02d", runHours, runMins, runSecs));
        
        if(mManager != null) {
            for(int skill : mXpViews.keySet()) {
                mXpViews.get(skill).setValue(getXPStatus(skill));
            }

            for(SnowmanType t : SnowmanType.values()) {
                mXpViews.get(t.skill).setVisible(checkboxes.get(t).isChecked());
            }

            if(DEBUG) {
                AcidGUI.setStatus("Task", mManager.currentTask());
            }
        }
    }

    @Override
    public TaskManager<ClientContext> getTaskManager() {
        return mManager;
    }

    private String getXPStatus(int skill) {
        final long xp = ctx.skills.experience(skill) - mStartXps.get(skill);
        final long hourly =  (xp * 60 * 60 * 1000) / ctx.controller.script().getRuntime();

        return xp+" ("+hourly+"/hr)";
    }
}
