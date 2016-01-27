package com.ac1d.rsbot.temp.portables;

import com.ac1d.rsbot.util.AcidGUI;
import com.ac1d.rsbot.util.stats.StatTrak;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.Constants;

@Script.Manifest(name = "AAddy", description = "Addy yo.")
public class AcidAddy extends AcidPortables {
    @Override
    protected PortableConfig getPortableConfig() {
        return new PortableConfig() {{
            itemIds = new int[] {449};
            portId = 97271;
            portAction = "Smelt";
            portOption = "Portable forge";
            skill = Constants.SKILLS_SMITHING;
        }};
    }

    @Override
    public void onGUI() {
        super.onGUI();

        AcidGUI.setStatus("Bars Smelted", StatTrak.TOTAL.getTotal("addy"));
        AcidGUI.setStatus("Bonus Bars", StatTrak.TOTAL.getTotal("bonus"));
        AcidGUI.setStatus("Bars/hr", StatTrak.HOURLY.getAverage("addy"));
    }

    @Override
    public void messaged(MessageEvent messageEvent) {
        super.messaged(messageEvent);

        if(messageEvent.source() == null || messageEvent.source().isEmpty()) {
            if(messageEvent.text().contains("You retrieve a bar")) {
                StatTrak.addEvent("addy");
            }

            if(messageEvent.text().contains("You manage to make an extra bar")) {
                StatTrak.addEvent("addy");
                StatTrak.addEvent("bonus");
            }
        }
    }
}