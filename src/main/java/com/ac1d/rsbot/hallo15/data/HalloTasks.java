package com.ac1d.rsbot.hallo15.data;

import com.ac1d.rsbot.util.LodestoneTask;
import com.ac1d.rsbot.util.Task;
import com.ac1d.rsbot.util.imported.Lodestone;
import org.powerbot.script.rt6.ClientContext;

public class HalloTasks {
    // Burthorpe Lode
    public static final Task<ClientContext> BURTHORPE_LODE = new LodestoneTask<>(Lodestone.BURTHORPE);

    // Enter rift
    public static final Task<ClientContext> ENTER_RIFT = new HalloObject.ObjectTask(HalloObject.SOUL_RIFT);

    // Walk to Jetty area
    public static final Task<ClientContext> TO_JETTY_DOWN = new HalloArea.MoveTask(HalloArea.MAIN, HalloArea.JETTY1_DOWN);

    // Embark on Jetty
    public static final Task<ClientContext> EMBARK = new HalloObject.ObjectTask(HalloObject.JETTY_DOWN);

    // Collect souls
    public static final Task<ClientContext> COLLECT = new HalloNpc.InteractTask(HalloNpc.WAYWARD);

    // Collect XP souls
    public static final Task<ClientContext> COLLECT_XP = new HalloNpc.InteractTask(HalloNpc.XP);

    // Get chests
    public static final Task<ClientContext> GET_CHEST = new HalloObject.ObjectTask(HalloObject.BUBBLING_WATER);

    // Move to new area in river
    public static final Task<ClientContext> SEARCH = new HalloArea.MoveTask(HalloArea.RIVER, HalloArea.RIVER);

    // Move to closest Disembarkment
    public static final Task<ClientContext> TO_JETTY_UP = new HalloArea.MoveTask(HalloArea.RIVER, HalloArea.JETTY1_UP);

    // Disembark Jetty
    public static final Task<ClientContext> DISEMBARK = new HalloObject.ObjectTask(HalloObject.JETTY_UP);

    // Walk to meeting point
    public static final Task<ClientContext> TO_MEETING_POINT = new HalloArea.MoveTask(HalloArea.MAIN, HalloArea.MEETING_POINT);

    // Start bridge
    public static final Task<ClientContext> GUIDE_SOULS = new HalloObject.ObjectTask(HalloObject.MEETING_POINT);

    // Guide souls
    public static final Task<ClientContext> TO_BRIDGE1 = new HalloArea.MoveTask(HalloArea.MEETING_POINT, HalloArea.BRIDGE1);
    public static final Task<ClientContext> TO_BRIDGE2 = new HalloArea.MoveTask(HalloArea.BRIDGE1, HalloArea.BRIDGE2);
    public static final Task<ClientContext> TO_BRIDGE3 = new HalloArea.MoveTask(HalloArea.BRIDGE2, HalloArea.BRIDGE3);

    // Attack / Siphon enemies within 4 tiles of souls
    public static final Task<ClientContext> SIPHON = new HalloNpc.InteractTask(HalloNpc.SIPHON_DEVOURER);
}
