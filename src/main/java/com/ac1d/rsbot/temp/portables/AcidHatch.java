package com.ac1d.rsbot.temp.portables;

import com.ac1d.rsbot.util.AcidGUI;
import com.ac1d.rsbot.util.stats.StatTrak;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.Constants;

@Script.Manifest(name = "AAHatch", description = "Makes hatchies.")
public class AcidHatch extends AcidPortables {
    @Override
    protected PortableConfig getPortableConfig() {
        return new PortableConfig() {{
            itemIds = new int[] {2361};
            portId = 97270;
            portAction = "Smith";
            portOption = "Portable forge";
            skill = Constants.SKILLS_SMITHING;
        }};
    }

    @Override
    public void onGUI() {
        super.onGUI();

        AcidGUI.setStatus("Hatchets Made", StatTrak.TOTAL.getTotal("hatch"));
        AcidGUI.setStatus("Bonus Hatchets", StatTrak.TOTAL.getTotal("bonus"));
        AcidGUI.setStatus("Hatchets/hr", StatTrak.HOURLY.getAverage("hatch"));
    }

    @Override
    public void messaged(MessageEvent messageEvent) {
        super.messaged(messageEvent);

        if(messageEvent.source() == null || messageEvent.source().isEmpty()) {
            if(messageEvent.text().contains("You make a hat")) {
                StatTrak.addEvent("hatch");
            }

            if(messageEvent.text().contains("saved one of your")) {
                StatTrak.addEvent("hatch");
                StatTrak.addEvent("bonus");
            }
        }
    }
}
