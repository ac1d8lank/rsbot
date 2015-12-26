package com.ac1d.rsbot.artisan.armour;

import com.ac1d.rsbot.util.Task;
import com.ac1d.rsbot.util.TaskManager;
import com.ac1d.rsbot.util.rt6.ComponentInteractTask;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArmourManager extends TaskManager<ClientContext> {
    public ArmourManager(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public List<Task<ClientContext>> getAvailableTasks() {
        ArrayList<Task<ClientContext>> tasks = new ArrayList<>();

        final boolean hasIngots = hasIngots();
        final boolean hasArmour = hasArmour();

        if(hasIngots) {
            final BurialArmour.Piece instruction = BurialArmour.getInstruction(ctx);
            final BurialArmour.Piece selected = BurialArmour.getSelected(ctx);
            if(instruction != selected) {
                // Change Selection
                tasks.add(mButtonTasks.get(instruction));
            }
            if(!isInteracting()) {
                // Interact with anvil
                tasks.add(ArmourObject.ANVIL.task);
            }
        } else if(hasArmour) {
            // Deposit armor
            tasks.add(ArmourObject.CHUTE.task);
        } else if(!isSmelterOpen()) {
            // Open Smelter
            tasks.add(ArmourObject.SMELTER.task);
        } else if(canSmelt()) {
            // Click Smelt
            tasks.add(mSmeltTask);
        } else {
            // Stop bot.
            ctx.controller.stop();
        }

        return tasks;
    }

    // States

    private boolean canSmelt() {
        Component smeltDisabledBg = ctx.widgets.component(1370, 39);
        return smeltDisabledBg.valid() && !smeltDisabledBg.visible();
    }

    private boolean isSmelterOpen() {
        Component smeltButton = ctx.widgets.component(1370, 38);
        return smeltButton.valid() && smeltButton.visible();
    }

    private boolean isInteracting() {
        Component cancelButton = ctx.widgets.component(1251, 49);
        return cancelButton.valid() && cancelButton.visible();
    }

    private boolean hasIngots() {
        return ctx.backpack.select().id(BurialArmour.ALL_INGOTS).count() > 0;
    }

    private boolean hasArmour() {
        return ctx.backpack.select().id(BurialArmour.ALL_ITEMS).count() > 0;
    }

    // Tasks

    private final Task<ClientContext> mSmeltTask = new Task<ClientContext>() {
        @Override
        public boolean isReady(ClientContext ctx) {
            return canSmelt();
        }

        @Override
        public void onPoll(ClientContext ctx) throws FailureException {
            ctx.widgets.component(1370, 38).click();
        }

        @Override
        public boolean isDone(ClientContext ctx) {
            return !isSmelterOpen() || hasIngots() || !canSmelt();
        }
    };

    private final HashMap<BurialArmour.Piece, ButtonTask> mButtonTasks = new HashMap<BurialArmour.Piece, ButtonTask>() {{
        for(BurialArmour.Piece p : BurialArmour.Piece.values()) {
            put(p, new ButtonTask(p));
        }
    }};

    private class ButtonTask extends ComponentInteractTask {
        public ButtonTask(BurialArmour.Piece p) {
            super("Select", p.widget, p.component);
        }

        @Override
        public boolean isDone(ClientContext ctx) {
            return super.isDone(ctx);
        }
    }
}
