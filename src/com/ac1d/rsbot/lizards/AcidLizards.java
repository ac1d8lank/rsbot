package com.ac1d.rsbot.lizards;

import com.ac1d.rsbot.util.AcidScript;
import com.ac1d.rsbot.util.CycleTaskManager;
import com.ac1d.rsbot.util.TaskManager;
import com.ac1d.rsbot.util.rt6.InteractTask;
import com.ac1d.rsbot.util.rt6.PickupTask;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;

@Script.Manifest(name = "AcidLizard", description = "Sets lizard traps")
public class AcidLizards extends AcidScript<ClientContext>{
    private CycleTaskManager<ClientContext> mManager;

    public AcidLizards() {
        mManager = new CycleTaskManager<>(ctx);

        // Set traps
        mManager.addTask(new InteractTask(19679, "Set-trap", "Young tree") {
            @Override
            public String tick(ClientContext ctx) {
                if(trapsInInventory() == 0) {
                    done();
                    return "No Traps";
                }
                return super.tick(ctx);
            }
        });

        // Pick up lizard
        mManager.addTask(new InteractTask(19675, "Check", "Net trap"));

        // Pick up failed trap
        mManager.addTask(new PickupTask(954, "Rope"));
        mManager.addTask(new PickupTask(303, "Small fishing net"));
    }

    private int trapsInInventory() {
        final int ropeCount = ctx.backpack.select().id(954).count();
        final int netCount = ctx.backpack.select().id(303).count();
        return Math.min(ropeCount, netCount);
    }

    @Override
    public TaskManager<ClientContext> getTaskManager() {
        return mManager;
    }
}