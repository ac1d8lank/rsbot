package com.ac1d.rsbot.temp.portables;

import org.powerbot.script.Script;
import org.powerbot.script.rt6.Constants;

@Script.Manifest(name = "AAHerb", description = "Temp herb")
public class AcidHerb extends AcidPortables {
    @Override
    protected PortableConfig getPortableConfig() {
        return new PortableConfig() {{
            itemIds = new int[] {3018, 157};
            portId = 89770;
            portAction = "Mix Potions";
            portOption = "Portable well";
            skill = Constants.SKILLS_HERBLORE;
        }}  ;
    }
}
