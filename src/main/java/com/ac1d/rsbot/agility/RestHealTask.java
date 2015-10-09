package com.ac1d.rsbot.agility;

import com.ac1d.rsbot.util.PlayerUtils;
import com.ac1d.rsbot.util.Task;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Player;

public class RestHealTask extends Task<ClientContext> {
    @Override
    public boolean isReady(ClientContext ctx) {
        return true;
    }

    @Override
    public void onPoll(ClientContext ctx) {
        if(ctx.players.local().idle()) {
            ctx.widgets.component(1465, 47).interact("Rest");
        }
    }

    @Override
    public boolean isDone(ClientContext ctx) {
        return PlayerUtils.getHealthPercent(ctx) == 100;
    }
}
