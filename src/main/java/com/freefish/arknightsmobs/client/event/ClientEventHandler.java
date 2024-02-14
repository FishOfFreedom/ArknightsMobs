package com.freefish.arknightsmobs.client.event;

import com.freefish.arknightsmobs.server.entity.special.EntityCameraShake;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public enum ClientEventHandler {
    INSTANCE;
    @SubscribeEvent
    public void onSetupCamera(EntityViewRenderEvent.CameraSetup event) {
        PlayerEntity player = Minecraft.getInstance().player;
        float delta = Minecraft.getInstance().getRenderPartialTicks();
        float ticksExistedDelta = player.ticksExisted + delta;
        if (player != null){

            float shakeAmplitude = 0;
            for (EntityCameraShake cameraShake : player.world.getEntitiesWithinAABB(EntityCameraShake.class, player.getBoundingBox().grow(20, 20, 20))) {
                if (cameraShake.getDistance(player) < cameraShake.getRadius()) {
                    shakeAmplitude += cameraShake.getShakeAmount(player, delta);
                }
            }
            if (shakeAmplitude > 1.0f) shakeAmplitude = 1.0f;
            event.setPitch((float) (event.getPitch() + shakeAmplitude * Math.cos(ticksExistedDelta * 3 + 2) * 25));
            event.setYaw((float) (event.getYaw() + shakeAmplitude * Math.cos(ticksExistedDelta * 5 + 1) * 25));
            event.setRoll((float) (event.getRoll() + shakeAmplitude * Math.cos(ticksExistedDelta * 4) * 25));
        }
    }
}
