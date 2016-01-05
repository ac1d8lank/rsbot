package com.ac1d.rsbot.temp.portables;

import com.ac1d.rsbot.util.Task;
import com.ac1d.rsbot.util.TaskManager;
import com.ac1d.rsbot.util.rt6.ComponentInteractTask;
import com.ac1d.rsbot.util.rt6.ObjectInteractTask;
import org.powerbot.script.rt6.ClientContext;

import java.util.ArrayList;
import java.util.List;

public class PortableManager extends TaskManager<ClientContext>{
    private final AcidPortables.PortableConfig config;

    public PortableManager(ClientContext ctx, AcidPortables.PortableConfig config) {
        super(ctx);
        this.config = config;

        CLICK_PORT = new ObjectInteractTask(config.portId, config.portAction, config.portOption);
    }

    @Override
    public List<Task<ClientContext>> getAvailableTasks() {
        ArrayList<Task<ClientContext>> tasks = new ArrayList<>();

        boolean hasItems = hasItemsToPort();
        boolean craftingOpen = ctx.widgets.component(1370, 38).visible();
        boolean progressOpen = ctx.widgets.component(1251, 8).visible();

        if(hasItems) {
            if(progressOpen) {
                // Do Nothing, it's working.
            } else if(craftingOpen) {
                // Click Craft
                tasks.add(CLICK_CRAFT);
            } else {
                // Interact w/ portable
                tasks.add(CLICK_PORT);
            }
        } else {
            // TODO: banking logic
            tasks.add(BANKING);
        }


        return tasks;
    }

    private static final ComponentInteractTask CLICK_CRAFT = new ComponentInteractTask(null, 1370, 38);
    private final ObjectInteractTask CLICK_PORT;
    private final Task<ClientContext> BANKING = new Task<ClientContext>() {

        @Override
        public boolean isReady(ClientContext ctx) {
            return ctx.bank.close();
        }

        @Override
        public void onStart(ClientContext ctx) {
            super.onStart(ctx);
        }

        @Override
        public void onPoll(ClientContext ctx) throws FailureException {
            boolean hasItems = hasItemsToPort();
            boolean bankOpen = ctx.bank.open();

            if(!hasItems) {
                if(!bankOpen) {
                    ctx.bank.open();
                } else {
                    // 762 . 43
                    ctx.widgets.component(762, 43).click();
                }
            }
        }

        @Override
        public boolean isDone(ClientContext ctx) {
            return hasItemsToPort();
        }
    };

    private boolean hasItemsToPort() {
        return ctx.backpack.select().id(config.itemIds).count() > 0;
    }
}
