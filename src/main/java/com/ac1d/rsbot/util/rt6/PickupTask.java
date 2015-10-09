package com.ac1d.rsbot.util.rt6;

import com.ac1d.rsbot.util.Task;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GroundItem;

public class PickupTask extends GroundItemInteractTask {
    public PickupTask(int id, String name) {
        super(id, "Take", name);
    }
}
