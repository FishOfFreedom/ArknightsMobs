package com.freefish.arknightsmobs.server.item.shield;

import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class SOTIRenderer extends GeoItemRenderer<ShieldOfTheInfected> {
    public SOTIRenderer() {
        super(new SOTIModel());
    }
}
