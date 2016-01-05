package com.ac1d.rsbot.temp.portables;

import org.powerbot.script.Script;
import org.powerbot.script.rt6.Constants;

@Script.Manifest(name = "AAFletch", description = "Fletches.")
public class AcidFletch extends AcidPortables {
    @Override
    protected PortableConfig getPortableConfig() {
        return new PortableConfig() {{
            itemIds = new int[] {1519};
            portId = 98285;
            portAction = "Fletch";
            portOption = "Portable fletcher";
            skill = Constants.SKILLS_FLETCHING;
        }};
    }
}
