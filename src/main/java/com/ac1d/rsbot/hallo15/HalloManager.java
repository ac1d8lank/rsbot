package com.ac1d.rsbot.hallo15;

import com.ac1d.rsbot.hallo15.data.HalloArea;
import com.ac1d.rsbot.hallo15.data.HalloMessage;
import com.ac1d.rsbot.hallo15.data.HalloNpc;
import com.ac1d.rsbot.hallo15.data.HalloTasks;
import com.ac1d.rsbot.util.Task;
import com.ac1d.rsbot.util.TaskManager;
import com.ac1d.rsbot.util.stats.StatTrak;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.Npc;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class HalloManager extends TaskManager<ClientContext>{

    private boolean openedChest = false;
    private boolean alreadyEscorting = false;

    public HalloManager(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public void onMessage(MessageEvent messageEvent) {
        super.onMessage(messageEvent);
        // FIXME: DEBUG
        System.out.println(String.format("[%1$s]: %2$s", messageEvent.source(), messageEvent.text()));
        
        final HalloMessage hm = HalloMessage.get(messageEvent.text());
        if(hm == null) {
            return;
        }
        System.out.println("HalloMessage: "+hm);
               
        switch(hm) {
            // TODO: handle AVATAR spawn
            case CHEST_SPAWN:
                // Chest has spawned
                openedChest = false;
                break;
            case CHEST_LOOT:
                // Chest has been successfully looted
                openedChest = true;
                break;
            case ALL_RESCUED:
                alreadyEscorting = false;
                break;
            case ALREADY_ESCORTING:
                alreadyEscorting = true;
                break;
            // TODO: Stat track XP_RESCUE, XP_DEVOUR, RESCUED, COMPLETE, FAVOUR, etc
            case FAVOUR:
                StatTrak.HOURLY.addEvent("favour", HalloMessage.FAVOUR.getData(messageEvent.text(), 1, 0));
                break;
        }
    }

    @Override
    public List<Task<ClientContext>> getAvailableTasks() {
        List<Task<ClientContext>> tasks = new ArrayList<>();

        // Always collect an xp soul if present.
        tasks.add(HalloTasks.COLLECT_XP);

        switch(State.get(ctx)) {
            case COLLECT:
                tasks.add(HalloTasks.TO_JETTY_DOWN);
                tasks.add(HalloTasks.EMBARK);
                // if we haven't already opened this chest
                if(!openedChest) {
                    tasks.add(HalloTasks.GET_CHEST);
                }
                if(HalloArea.RIVER.containsPlayer(ctx)) {
                    tasks.add(HalloTasks.COLLECT);
                    tasks.add(HalloTasks.SEARCH);
                }

                // Fallback to the meeting point if we can't get to the jettys
                tasks.add(HalloTasks.TO_MEETING_POINT);
                break;
            case GUIDE:
                tasks.add(HalloTasks.TO_JETTY_UP);
                tasks.add(HalloTasks.DISEMBARK);

                final Npc souls = HalloNpc.RESCUED.find(ctx);
                final boolean soulsNpcVisible = souls != null;

                if(!soulsNpcVisible || !alreadyEscorting) {
                    tasks.add(HalloTasks.TO_MEETING_POINT);
                    tasks.add(HalloTasks.GUIDE_SOULS);
                }

                boolean inSoulsArea = false;
                if(soulsNpcVisible) {
                    for (HalloArea combatArea : HalloArea.COMBAT_AREAS) {
                        if(combatArea.containsPlayer(ctx) && combatArea.contains(souls)) {
                            inSoulsArea = true;
                            tasks.add(HalloTasks.SIPHON);
                        }
                    }
                }
                // Follow or search for souls NPC
                if(!soulsNpcVisible || !inSoulsArea) {
                    tasks.add(HalloTasks.TO_BRIDGE1);
                    tasks.add(HalloTasks.TO_BRIDGE2);
                    tasks.add(HalloTasks.TO_BRIDGE3);
                }
                break;
        }

        // When in doubt, lode to burth or enter the portal
        if(!HalloArea.BURTH_LODE.containsPlayer(ctx)) {
            tasks.add(HalloTasks.BURTHORPE_LODE);
        } else {
            tasks.add(HalloTasks.ENTER_RIFT);
        }
        return tasks;
    }


    public static enum State {
        COLLECT("<col=ffffff>Souls Remaining: ([0-9]+)"),
        GUIDE("<col=00ff00>Escort souls up the bridge\\."),
        ;

        private final Pattern pattern;

        State(String regex) {
            pattern = Pattern.compile(regex);
        }

        public static State get(ClientContext ctx) {
            final Component component = ctx.widgets.component(1686, 3);
            if(component.valid() && component.visible()) {
                final String text = component.text();
                for(State state : State.values()) {
                    if(state.pattern.matcher(text).matches()) {
                        return state;
                    }
                }
            }
            return COLLECT; // COLLECT is the first state
        }
    }
}
