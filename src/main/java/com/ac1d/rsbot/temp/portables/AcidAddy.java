package com.ac1d.rsbot.temp.portables;

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
}
