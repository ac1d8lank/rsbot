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

        final boolean hasIngots = hasIngots(ctx);
        final boolean hasArmour = hasArmour(ctx);

        if(hasIngots) {
            final BurialArmour.Piece instruction = BurialArmour.getInstruction(ctx);
            final BurialArmour.Piece selected = BurialArmour.getSelected(ctx);
            if(instruction != selected) {
                // Change Selection
                tasks.add(mButtonTasks.get(instruction));
            }
            if(!isInteracting(ctx)) {
                // Interact with anvil
                tasks.add(ArmourObject.ANVIL.task);
            }
        } else if(hasArmour) {
            // Deposit armor
            tasks.add(ArmourObject.CHUTE.task);
        } else if(!isSmelterOpen(ctx)) {
            // Open Smelter
            tasks.add(ArmourObject.SMELTER.task);
        } else if(canSmelt(ctx)) {
            // Click Smelt
            tasks.add(mSmeltTask);
        } else {
            // Stop bot.
            ctx.controller.stop();
        }

        return tasks;
    }

    // States

    public static boolean canSmelt(ClientContext ctx) {
        Component smeltDisabledBg = ctx.widgets.component(1370, 39);
        return smeltDisabledBg.valid() && !smeltDisabledBg.visible();
    }

    public static boolean isSmelterOpen(ClientContext ctx) {
        Component smeltButton = ctx.widgets.component(1370, 38);
        return smeltButton.valid() && smeltButton.visible();
    }

    public static boolean isInteracting(ClientContext ctx) {
        Component cancelButton = ctx.widgets.component(1251, 49);
        return cancelButton.valid() && cancelButton.visible();
    }

    public static boolean hasIngots(ClientContext ctx) {
        return ctx.backpack.select().id(BurialArmour.ALL_INGOTS).count() > 0;
    }

    public static boolean hasArmour(ClientContext ctx) {
        return ctx.backpack.select().id(BurialArmour.ALL_ITEMS).count() > 0;
    }

    // Tasks

    private final Task<ClientContext> mSmeltTask = new Task<ClientContext>() {
        @Override
        public boolean isReady(ClientContext ctx) {
            return canSmelt(ctx);
        }

        @Override
        public void onPoll(ClientContext ctx) throws FailureException {
            ctx.widgets.component(1370, 38).click();
        }

        @Override
        public boolean isDone(ClientContext ctx) {
            return !isSmelterOpen(ctx) || hasIngots(ctx) || !canSmelt(ctx);
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
            final BurialArmour.Piece instruction = BurialArmour.getInstruction(ctx);
            final BurialArmour.Piece selected = BurialArmour.getSelected(ctx);
            if(instruction == selected) {
                return true;
            }
            return super.isDone(ctx);
        }
    }
}
