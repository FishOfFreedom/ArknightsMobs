package com.freefish.arknightsmobs.client.render.entity;

import com.freefish.arknightsmobs.client.render.model.GanRanZheZhiJiModel;
import com.freefish.arknightsmobs.server.entity.ganranzhezhiji.GanRanZheZhiJi;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class GanRanZheZhiJiRenderer extends GeoProjectilesRenderer<GanRanZheZhiJi> {

    public GanRanZheZhiJiRenderer(EntityRendererManager renderManager) {
        super(renderManager, new GanRanZheZhiJiModel());
    }
}
