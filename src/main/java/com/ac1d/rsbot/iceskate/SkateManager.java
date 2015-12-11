package com.ac1d.rsbot.iceskate;

import com.ac1d.rsbot.util.AreaUtils;
import com.ac1d.rsbot.util.Task;
import com.ac1d.rsbot.util.TaskManager;
import com.ac1d.rsbot.util.rt6.ComponentInteractTask;
import com.ac1d.rsbot.util.rt6.NpcInteractTask;
import org.powerbot.script.Area;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SkateManager extends TaskManager<ClientContext> {

    /*
     * Objects:
     * 100381 - Ice Patch
     */

    /*
     * NPCs:
     * 22021 - Penguin
     * 22022 - Sir Amik
     * 22013 - Jack Frost
     */

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

    private final ComponentInteractTask OUTSIDE_LANE = new ComponentInteractTask("Select", 1697, 13);
    private final ComponentInteractTask MIDDLE_LANE = new ComponentInteractTask("Select", 1697, 21);
    private final ComponentInteractTask INSIDE_LANE = new ComponentInteractTask("Select", 1697, 29) {
        @Override
        protected long getIdleDelayMillis() {
            return 0; // We'll be non-idle while skating
        }

        @Override
        public boolean isDone(ClientContext ctx) {
            return INNER.contains(ctx.players.local()) || super.isDone(ctx);
        }
    };

    private final ComponentInteractTask EXIT_RINK = new ComponentInteractTask("Select", 1697, 37) {
        @Override
        protected long getIdleDelayMillis() {
            return 0; // We'll be non-idle while skating
        }

        @Override
        public void onFinish(boolean success) {
            super.onFinish(success);
            if(success) {
                mHitPenguin = false;
            }
        }
    };

    private final Pattern PENGUIN = Pattern.compile("You bump into a penguin\\. Your XP is now reduced for the next [0-9]+ laps?\\.");
    private final Pattern ICE = Pattern.compile("You slip on the ice\\.");
    private final Pattern SIR_AMIK = Pattern.compile("You bump into Sir Amik Varze who spills his drink and promptly escorts you off the course\\.");

    public static final Area RINK = AreaUtils.inclusive(new Tile(3662, 3734, 1), new Tile(3683, 3744, 1));
    public static final Area INNER = AreaUtils.inclusive(new Tile(3664, 3736, 1), new Tile(3681, 3742, 1));

    public boolean mHitPenguin = false;

    public SkateManager(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public List<Task<ClientContext>> getAvailableTasks() {
        ArrayList<Task<ClientContext>> tasks = new ArrayList<>();

        if(mHitPenguin) {
            tasks.add(EXIT_RINK);
        }

        if(!isSkating(ctx)) {
            tasks.add(START_SKATING);
        }

        if(!INNER.contains(ctx.players.local())) {
            tasks.add(INSIDE_LANE);
        }

        return tasks;
    }

    @Override
    public void onMessage(MessageEvent messageEvent) {
        super.onMessage(messageEvent);

        if(messageEvent.source() == null) {
            if(PENGUIN.matcher(messageEvent.text()).matches()) {
                mHitPenguin = true;
            }
        }
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
