package com.ac1d.rsbot.xmas15.crafting;

import com.ac1d.rsbot.util.Task;
import com.ac1d.rsbot.util.TaskManager;
import com.ac1d.rsbot.util.rt6.InteractTask;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class IceCraftingManager extends TaskManager<ClientContext> {

    private static final Pattern TIRED = Pattern.compile("Your arms grow tired from sculpting the ice so you stop to catch your breath\\.");
    private static final int[] BLOCK_IDS = {
            100423, 100424, 100425, 100426, // TODO: who dis
            100427, 100428, 100429, 100430, // Ice Queen
            100431, 100432, 100433, 100434, // Santa

    };

    private long mLastTiredMessage;
    private Task<ClientContext> mCraftTask = new InteractTask<GameObject>("Craft", "Ice block") {
        public long mmStartTime;

        @Override
        public void onStart(ClientContext ctx) {
            super.onStart(ctx);
            mmStartTime = System.currentTimeMillis();
        }

        @Override
        public boolean isDone(ClientContext ctx) {
            return mLastTiredMessage > mmStartTime || super.isDone(ctx);
        }

        @Override
        protected GameObject findEntity(ClientContext ctx) {
            return ctx.objects.select().id(BLOCK_IDS).nearest().poll();
        }
    };

    public IceCraftingManager(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public List<Task<ClientContext>> getAvailableTasks() {
        ArrayList<Task<ClientContext>> tasks = new ArrayList<>();

        tasks.add(mCraftTask);

        return tasks;
    }

    @Override
    public void onMessage(MessageEvent messageEvent) {
        super.onMessage(messageEvent);

        final String src = messageEvent.source();
        if(src != null && !src.isEmpty()) {
            return;
        }

        if(TIRED.matcher(messageEvent.text()).matches()) {
            mLastTiredMessage = System.currentTimeMillis();
        }
    }
}
