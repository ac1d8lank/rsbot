package com.ac1d.rsbot.ecto;

import com.ac1d.rsbot.util.AreaUtils;
import com.ac1d.rsbot.util.rt6.MoveTask;
import org.powerbot.script.Area;
import org.powerbot.script.rt6.ClientContext;

public enum EctoArea {
    BANK        (0, 3686, 3471, 3691, 3466),
    PORT_CENTER (0, 3658, 3491, 3669, 3485),
    PORT_EXIT   (0, 3657, 3507, 3662, 3504),
    PORT_ENTER  (0, 3662, 3508, 3657, 3510),
    ECTO        (0, 3656, 3527+1,
                    3663, 3527+1,
                    3667+1, 3523,
                    3667+1, 3516,
                    3663+1, 3512,
                    3656, 3512,
                    3652, 3516,
                    3652, 3523+1),
    GRINDER     (1, 3666, 3522, 3654, 3527),
    // Pool Tiers:
    T1          (3, 3668, 9903, 3692, 9873),
    T1_UP       (3, 3668, 9883, 3670, 9893),
    T1_DOWN     (3, 3692, 9893, 3690, 9883),
    T2          (2, 3689, 9876, 3671, 9900),
    T2_UP       (2, 3689, 9892, 3688, 9884),
    T2_DOWN     (2, 3671, 9889, 3672, 9884),
    T3          (1, 3673, 9878, 3687, 9900),
    T3_UP       (1, 3673, 9885, 3675, 9891),
    T3_DOWN     (1, 3686, 9891, 3687, 9885),
    POOL        (0, 3683, 9889, 3683, 9887),
    ;

    public final Area area;

    EctoArea(int floor, int x1, int y1, int x2, int y2) {
        area = AreaUtils.rect(floor, x1, y1, x2, y2);
    }

    EctoArea(int floor, int... coords) {
        area = AreaUtils.poly(floor, coords);
    }

    public boolean playerInside(ClientContext ctx) {
        return area.contains(ctx.players.local());
    }

    public static class AreaMoveTask extends MoveTask {
        public AreaMoveTask(EctoArea from, EctoArea to) {
            super(from.area, to.area);
        }
    }
}
