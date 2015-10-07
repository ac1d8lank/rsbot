package com.ac1d.rsbot.util;

import org.powerbot.script.rt6.ClientContext;

public class PlayerUtils {
    public static <C extends ClientContext> int getHealthPercent(C ctx) {
        final String[] health = ctx.widgets.component(1430, 4).component(7).text().split("/");
        try {
            return (100 * Integer.parseInt(health[0])) / Integer.parseInt(health[1]);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
