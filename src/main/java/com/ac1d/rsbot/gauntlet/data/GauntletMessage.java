package com.ac1d.rsbot.gauntlet.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GauntletMessage {
    AVATAR("<col=ce5200>The gates to the Underworld have been opened, Amascut's Avatar must be defeated!"),
    FAVOUR("You receive ([0-9]+) Noumenon Favour."),
    CHEST_SPAWN("The belongings of long passed adventurers have been spotted beneath the surface of the river."),
    CHEST_LOOT("You haul a chest out of the murky depths."),
    XP_RESCUE("<col=00ffff>As you rescue the soul you notice another soul that stands out from the rest."),
    XP_DEVOUR("<col=00ffff>As the devourer expires an undigested soul escapes from its remains."),
    XP_AWARD("<col=00ffff>You have been awarded ([0-9]+) XP in (.*)!</col>"),

    RESCUED("You have rescued ([0-9]+)/20 souls."),
    ALL_RESCUED("You have rescued 20 souls, you should go to the bridge and escort them to Icthlarin."),
    ALREADY_ESCORTING("You are already escorting your souls."),
    DEVOUR("Amascut's devourers are preventing your souls from progressing. Dispatch devourers to weaken Amascut's power."),
    SOULS_MOVING("Your souls have been released and are moving up the bridge."),
    COMPLETE("Your souls have successfully completed their journey, Icthlarin can now escort them to the afterlife."),
    ;


    private final Pattern pattern;

    GauntletMessage(String regex) {
        pattern = Pattern.compile(regex);
    }

    public static GauntletMessage get(String msg) {
        for(GauntletMessage sysMsg : values()) {
            if(sysMsg.pattern.matcher(msg).matches()) {
                return sysMsg;
            }
        }
        return null;
    }

    public int getData(String msg, int position, int defValue) {
        final String match = getData(msg, position, null);
        return match == null ? defValue : Integer.parseInt(match);
    }

    public String getData(String msg, int position, String defValue) {
        final Matcher matcher = pattern.matcher(msg);
        if(!matcher.find()) {
            return defValue;
        }
        final String match = matcher.group(position);
        return match == null || match.isEmpty() ? defValue : match;
    }
}
