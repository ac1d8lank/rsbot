package com.ac1d.rsbot.agility;

import com.ac1d.rsbot.util.PlayerUtils;
import com.ac1d.rsbot.util.Task;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Player;

public class RestHealTask extends Task<ClientContext> {
    public static int lastRestingAnimation;

    @Override
    public String tick(ClientContext ctx) {
        final Player p = ctx.players.local();
        if(PlayerUtils.getHealthPercent(ctx) == 100) {
            done();
            return "Healed";
        }
        if(p.animation() == -1) {
            ctx.widgets.component(1465, 47).interact("Rest");
            return "Clicking rest";
        }
        lastRestingAnimation = p.animation();
        return "Resting";
    }
}
