package com.ac1d.rsbot.xmas15.combat;

import com.ac1d.rsbot.util.Task;
import com.ac1d.rsbot.util.TaskManager;
import com.ac1d.rsbot.util.gui.Checkbox;
import com.ac1d.rsbot.util.rt6.InteractTask;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SnowmenManager extends TaskManager<ClientContext> implements SnowmanTask.TypeChecker {

    private HashMap<SnowmanType, Checkbox> mCheckboxes;

    private InteractTask<GameObject> mBuildSnowmanTask = new SnowmanTask(this, "Snowman", SnowmanTask.STAGE_IDS);
    private HashMap<SnowmanType, SnowmanTask> mSnowmanTasks = new HashMap<SnowmanType, SnowmanTask>() {{
        for(SnowmanType t : SnowmanType.values()) {
            put(t, new SnowmanTask(SnowmenManager.this, t.meltedName, t.meltedId));
        }
    }};

    public SnowmenManager(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public List<Task<ClientContext>> getAvailableTasks() {
        ArrayList<Task<ClientContext>> tasks = new ArrayList<>();

        for(SnowmanType t : SnowmanType.values()) {
            if(mCheckboxes.get(t).isChecked()) {
                tasks.add(mSnowmanTasks.get(t));
            }
        }
        tasks.add(mBuildSnowmanTask);

        return tasks;
    }

    public void setCheckboxes(HashMap<SnowmanType, Checkbox> checkboxes) {
        mCheckboxes = checkboxes;
    }

    @Override
    public boolean typeEnabled(SnowmanType type) {
        return mCheckboxes != null && mCheckboxes.get(type).isChecked();
    }
}
