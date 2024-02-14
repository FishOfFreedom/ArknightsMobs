package com.freefish.arknightsmobs.client.render.model;

import com.freefish.arknightsmobs.ArknightsMobs;
import com.freefish.arknightsmobs.server.entity.surtr.FlameEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FlameEntityModel extends AnimatedGeoModel<FlameEntity> {

	@Override
	public ResourceLocation getAnimationFileLocation(FlameEntity entity) {
			return new ResourceLocation(ArknightsMobs.MOD_ID, "animations/flame.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(FlameEntity entity) {
			return new ResourceLocation(ArknightsMobs.MOD_ID, "geo/flame.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(FlameEntity entity) {
			return new ResourceLocation(ArknightsMobs.MOD_ID, "textures/entity/flame.png");
	}
}