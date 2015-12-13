package com.ac1d.rsbot.winter15.skating;

import com.ac1d.rsbot.util.*;
import com.ac1d.rsbot.util.Random;
import com.ac1d.rsbot.util.rt6.ComponentInteractTask;
import com.ac1d.rsbot.util.rt6.NpcInteractTask;
import org.powerbot.script.*;
import org.powerbot.script.rt6.*;
import org.powerbot.script.rt6.ClientContext;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SkateManager extends TaskManager<ClientContext> {

    private final NpcInteractTask START_SKATING = new NpcInteractTask(22013, "Ice skate", "Jack Frost") {
        @Override
        protected long getIdleDelayMillis() {
            return 0; // We'll be non-idle while skating
        }

        @Override
        public boolean isDone(ClientContext ctx) {
            return isSkating(ctx) || super.isDone(ctx);
        }
    };

    private final ComponentInteractTask EXIT_RINK = new ComponentInteractTask("Select", 1697, 37) {
        @Override
        public void onFinish(boolean success) {
            super.onFinish(success);
            mHitPenguin = false;
        }
    };

    private static final Task<ClientContext> RANDOM_LOOK = new Task<ClientContext>() {
        public boolean mDone;

        @Override
        public boolean isReady(ClientContext ctx) {
            return com.ac1d.rsbot.util.Random.oneIn(100);
        }

        @Override
        public void onPoll(ClientContext ctx) throws FailureException {
            ctx.camera.angle(90 * Random.get(3) + Random.get(-10, 10));
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

    private final Pattern PENGUIN = Pattern.compile("You bump into a penguin\\. Your XP is now reduced for the next [0-9]+ laps?\\.");
    private final Pattern ICE = Pattern.compile("You slip on the ice\\.");
    private final Pattern SIR_AMIK = Pattern.compile("You bump into Sir Amik Varze who spills his drink and promptly escorts you off the course\\.");

    public static final Area RINK = AreaUtils.inclusive(new Tile(3662, 3734, 1), new Tile(3683, 3744, 1));
    public static final Area AREA_INNER = AreaUtils.inclusive(new Tile(3664, 3736, 1), new Tile(3681, 3742, 1));

    public boolean mHitPenguin = false;

    public SkateManager(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public List<Task<ClientContext>> getAvailableTasks() {
        ArrayList<Task<ClientContext>> tasks = new ArrayList<>();

        if(mHitPenguin || isWalkGlitch()) {
            tasks.add(EXIT_RINK);
        }

        if(!isSkating(ctx)) {
            tasks.add(START_SKATING);
        }

        if(new Path(ctx).isBlocked(ctx)) {
            tasks.add(Lane.INNER.task);
            tasks.add(Lane.MIDDLE.task);
            tasks.add(Lane.OUTSIDE.task);
        }
        // Always try to get back to the inner lane if possible
        if(!AREA_INNER.contains(ctx.players.local())) {
            tasks.add(Lane.INNER.task);
        }

        tasks.add(RANDOM_LOOK);

        return tasks;
    }

    @Override
    public void onMessage(MessageEvent messageEvent) {
        super.onMessage(messageEvent);

        if(messageEvent.source() == null || messageEvent.source().isEmpty()) {
            if(PENGUIN.matcher(messageEvent.text()).matches()) {
                mHitPenguin = true;
            }
        }
    }

    private boolean isWalkGlitch() {
        final Player p = ctx.players.local();
        return p.stance() == 18040 && RINK.contains(p);
    }

    private static boolean isSkating(ClientContext ctx) {
        switch(ctx.players.local().stance()) {
            //18040?
            case 27845:
                return true;
            default:
                return false;
        }
    }

}
