package com.ac1d.rsbot.winter15.cooking;

import com.ac1d.rsbot.util.Random;
import com.ac1d.rsbot.util.Task;
import com.ac1d.rsbot.util.TaskManager;
import com.ac1d.rsbot.util.rt6.ComponentInteractTask;
import org.powerbot.script.rt6.ClientContext;

import java.util.ArrayList;
import java.util.List;

public class CookingManager extends TaskManager<ClientContext> {


    public enum Food {
        PUDDING ("Christmas pudding", 1692, 11),
        POTATOES("Roast potatoes", 1692, 21),
        TURKEY  ("Roast turkey", 1692, 31),
        YULE_LOG("Yule log", 1692, 41),
        ;

        private final String instruction;
        private final int widget;
        private final int component;
        public final ComponentInteractTask task;

        Food(String instruction, int widget, int component) {
            this.instruction = instruction;
            this.widget = widget;
            this.component = component;
            task = new ComponentInteractTask("Select", widget, component) {
                @Override
                public boolean isReady(ClientContext ctx) {
                    return !isEnabled(ctx) && isCurrent(ctx) && super.isReady(ctx);
                }

                @Override
                public boolean isDone(ClientContext ctx) {
                    return !isCurrent(ctx) || isEnabled(ctx) || super.isDone(ctx);
                }
            };
        }

        public static Food getInstruction(ClientContext ctx) {
            final String instruction = ctx.widgets.component(1694, 6).text();
            for(Food f : values()) {
                if(f.instruction.equals(instruction)) {
                    return f;
                }
            }
            return null;
        }

        public boolean isCurrent(ClientContext ctx) {
            return getInstruction(ctx) == this;
        }

        public boolean isEnabled(ClientContext ctx) {
            return ctx.widgets.component(widget, component).visible();
        }
    }

    private static final Task<ClientContext> RANDOM_LOOK = new Task<ClientContext>() {
        public boolean mDone;

        @Override
        public boolean isReady(ClientContext ctx) {
            return Random.oneIn(100);
        }

        @Override
        public void onPoll(ClientContext ctx) throws FailureException {
            ctx.camera.angle(Random.get(359));
            mDone = true;
        }

        @Override
        public boolean isDone(ClientContext ctx) {
            if(mDone) {
                mDone = false;
                return true;
            }
            return false;
        }
    };

    public CookingManager(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public List<Task<ClientContext>> getAvailableTasks() {
        ArrayList<Task<ClientContext>> tasks = new ArrayList<>();

        for(Food f : Food.values()) {
            tasks.add(f.task);
        }

        tasks.add(RANDOM_LOOK);

        return tasks;
    }
}
