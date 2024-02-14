package com.freefish.arknightsmobs.server.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class AMEffect extends Effect {
    private boolean instant;
    private boolean isRegistered = false;

    public AMEffect(EffectType type, int color, boolean isInstant) {
        super(type, color);
        this.instant = isInstant;
    }

    public boolean isInstantenous() {
        return false;
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        if (isInstantenous()) {
            return true;
        }
        return canApplyEffect(duration, amplifier);
    }

    protected boolean canApplyEffect(int remainingTicks, int level) {
        if (!isInstantenous()) {
            //Log.w("Non instant effects should override canApplyEffect!");
            Thread.dumpStack();
        }
        return false;
    }

    @Override
    public void performEffect(LivingEntity entity, int amplifier) {
        if (isInstantenous()) {
            //applyInstantenousEffect(null, null, entity, amplifier, 1.0d);
            affectEntity(null,null ,entity , amplifier, 1.0d);
        }
    }

    public AMEffect onRegister() {
        isRegistered = true;
        return this;
    }

    public boolean isRegistered() {
        return isRegistered;
    }
}
