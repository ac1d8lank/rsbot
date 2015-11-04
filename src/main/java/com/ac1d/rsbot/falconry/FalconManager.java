package com.ac1d.rsbot.falconry;

import com.ac1d.rsbot.falconry.data.FalconItem;
import com.ac1d.rsbot.falconry.data.FalconNpc;
import com.ac1d.rsbot.util.Task;
import com.ac1d.rsbot.util.TaskManager;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Equipment;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

public class FalconManager extends TaskManager<ClientContext> {

    public static final DropTask DROP = new DropTask();
    public static final FalconNpc.Task PICKUP = new FalconNpc.Task(FalconNpc.GYR_FALCON);
    public static final FalconNpc.Task HUNT = new FalconNpc.Task(FalconNpc.SPOTTED_KEBBIT); // TODO: use user-chosen kebbit

    public FalconManager(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public List<Task<ClientContext>> getAvailableTasks() {
        FalconItem inHand = FalconItem.get(ctx.equipment.itemAt(Equipment.Slot.MAIN_HAND).id());
        if(inHand == null) {
            JOptionPane.showMessageDialog(null, "Hold a falcon before starting bot.", "Error", JOptionPane.WARNING_MESSAGE);
            ctx.controller.stop();
            return null;
        }
        ArrayList<Task<ClientContext>> tasks = new ArrayList<>();

        if(ctx.backpack.select().count() > 26) {
            tasks.add(DROP);
        } else {
            switch (inHand) {
                case GLOVE_EMPTY:
                    tasks.add(PICKUP);
                    break;
                case GLOVE_FALCON:
                    tasks.add(HUNT);
                    break;
            }
        }

        return tasks;
    }

}
