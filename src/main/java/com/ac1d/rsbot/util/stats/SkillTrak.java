package com.ac1d.rsbot.util.stats;

import com.ac1d.rsbot.util.AcidGUI;
import org.powerbot.script.rt6.ClientContext;

import java.util.HashMap;

public abstract class SkillTrak {
    private static HashMap<Integer, Integer> sLastSeen = new HashMap<>();
    private static HashMap<Integer, Integer> sLastDrawn = new HashMap<>();

    public static void onPoll(ClientContext ctx) {
        final int[] experiences = ctx.skills.experiences();

        for(int skill = 0; skill < experiences.length; skill++) {
            final int xp = experiences[skill];

            // Returns -1 if not ready or invalid idx
            if(xp < 0) {
                continue;
            }

            if(sLastSeen.containsKey(skill)) {
                final int lastXp = sLastSeen.get(skill);
                if(lastXp < xp) {
                    StatTrak.addEvent(getName(skill), xp - lastXp);
                }
            }

            if(!sLastSeen.containsKey(skill) || sLastSeen.get(skill) < xp) {
                sLastSeen.put(skill, xp);
            }
        }
    }

    public static int getHourly(int skill) {
        return StatTrak.HOURLY.getAverage(getName(skill));
    }

    public static int getTotal(int skill) {
        return StatTrak.TOTAL.getTotal(getName(skill));
    }

    public static String timeUntil(ClientContext ctx, int skill, int level) {
        if(getTotal(skill) <= 0) {
            return null;
        }

        final long runtime = Math.max(0, ctx.controller.script().getRuntime());
        final int xp = ctx.skills.experience(skill);

        final long xpToNext = ctx.skills.experienceAt(level) - xp;
        final long timeToNext = (xpToNext * runtime) / getTotal(skill);
        return formatMillis(timeToNext);
    }

    public static String timeUntilNext(ClientContext ctx, int skill) {
        return timeUntil(ctx, skill, ctx.skills.realLevel(skill)+1);
    }

    public static void drawBasicUI(ClientContext ctx, int skill) {
        AcidGUI.setStatus("Uptime", formatMillis(Math.max(0, ctx.controller.script().getRuntime())));

        final int total = getTotal(skill);
        if(!sLastDrawn.containsKey(skill) || sLastDrawn.get(skill) != total) {
            AcidGUI.setStatus("XP Gained", total);
            AcidGUI.setStatus("XP/hr", getHourly(skill));
            AcidGUI.setStatus("Next Level", timeUntilNext(ctx, skill));
            sLastDrawn.put(skill, total);
        }
    }

    private static String formatMillis(long millis) {
        final int hours = (int) (millis / (60 * 60 * 1000));
        final int mins = (int) (millis / (60 * 1000)) % 60;
        final int secs = (int) (millis / 1000) % 60;
        return String.format("%02d:%02d:%02d", hours, mins, secs);
    }

    private static String getName(int skill) {
        return "skilltrak"+skill;
    }
}
