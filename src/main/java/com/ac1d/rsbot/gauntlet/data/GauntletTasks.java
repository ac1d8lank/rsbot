package com.ac1d.rsbot.gauntlet.data;

import com.ac1d.rsbot.util.LodestoneTask;
import com.ac1d.rsbot.util.Task;
import com.ac1d.rsbot.util.imported.Lodestone;
import org.powerbot.script.rt6.ClientContext;

public class GauntletTasks {
    // Burthorpe Lode
    public static final Task<ClientContext> BURTHORPE_LODE = new LodestoneTask<>(Lodestone.BURTHORPE);

    // Enter rift
    public static final Task<ClientContext> ENTER_RIFT = new GauntletObject.ObjectTask(GauntletObject.SOUL_RIFT);

    // Walk to Jetty area
    public static final Task<ClientContext> TO_JETTY_DOWN = new GauntletArea.MoveTask(GauntletArea.MAIN, GauntletArea.JETTY1_DOWN);

    // Embark on Jetty
    public static final Task<ClientContext> EMBARK = new GauntletObject.ObjectTask(GauntletObject.JETTY_DOWN);

    // Collect souls
    public static final Task<ClientContext> COLLECT = new GauntletNpc.InteractTask(GauntletNpc.WAYWARD);

    // Collect XP souls
    public static final Task<ClientContext> COLLECT_XP = new GauntletNpc.InteractTask(GauntletNpc.XP);

    // Get chests
    public static final Task<ClientContext> GET_CHEST = new GauntletObject.ObjectTask(GauntletObject.BUBBLING_WATER);

    // Move to new area in river
    public static final Task<ClientContext> SEARCH = new GauntletArea.MoveTask(GauntletArea.RIVER, GauntletArea.RIVER);

    // Move to closest Disembarkment
    public static final Task<ClientContext> TO_JETTY_UP = new GauntletArea.MoveTask(GauntletArea.RIVER, GauntletArea.JETTY1_UP);

    // Disembark Jetty
    public static final Task<ClientContext> DISEMBARK = new GauntletObject.ObjectTask(GauntletObject.JETTY_UP);

    // Walk to meeting point
    public static final Task<ClientContext> TO_MEETING_POINT = new GauntletArea.MoveTask(GauntletArea.MAIN, GauntletArea.MEETING_POINT);

    // Start bridge
    public static final Task<ClientContext> GUIDE_SOULS = new GauntletObject.ObjectTask(GauntletObject.MEETING_POINT);

    // Guide souls
    public static final Task<ClientContext> TO_BRIDGE1 = new GauntletArea.MoveTask(GauntletArea.MEETING_POINT, GauntletArea.BRIDGE1);
    public static final Task<ClientContext> TO_BRIDGE2 = new GauntletArea.MoveTask(GauntletArea.BRIDGE1, GauntletArea.BRIDGE2);
    public static final Task<ClientContext> TO_BRIDGE3 = new GauntletArea.MoveTask(GauntletArea.BRIDGE2, GauntletArea.BRIDGE3);

    // Attack / Siphon enemies within 4 tiles of souls
    public static final Task<ClientContext> SIPHON = new GauntletNpc.InteractTask(GauntletNpc.SIPHON_DEVOURER);
}
