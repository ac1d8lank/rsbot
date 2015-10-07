package com.ac1d.rsbot.agility;

import com.ac1d.rsbot.util.PlayerUtils;
import com.ac1d.rsbot.util.Task;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Player;

public class RestHealTask extends Task<ClientContext> {

    @Override
    public TickResult tick(ClientContext ctx) {
        final Player p = ctx.players.local();
        if(PlayerUtils.getHealthPercent(ctx) == 100) {
            return done("Healed");
        }
        if(p.animation() == -1) {
            if(ctx.widgets.component(1465, 47).interact("Rest")) {
                return retry("Clicking rest");
            } else {
                return done("Failed to click rest");
            }
        }
        return retry("Resting");
    }
}
