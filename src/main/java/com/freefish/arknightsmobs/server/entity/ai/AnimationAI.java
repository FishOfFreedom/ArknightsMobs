package com.freefish.arknightsmobs.server.entity.ai;

import com.freefish.arknightsmobs.server.entity.FreeFishEntity;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.ai.goal.Goal;
import software.bernie.geckolib3.core.IAnimatable;

public abstract class AnimationAI<T extends FreeFishEntity & IAnimatedEntity & IAnimatable> extends Goal {
    protected final T entity;

    protected AnimationAI(T entity) {
        this.entity = entity;
    }

    @Override
    public boolean shouldExecute() {
        return this.test(this.entity.getAnimation());
    }

    @Override
    public void startExecuting() {
        //this.entity.hurtInterruptsAnimation = this.hurtInterruptsAnimation;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.test(this.entity.getAnimation()) && this.entity.getAnimationTick() < this.entity.getAnimation().getDuration();
    }

    @Override
    public void resetTask() {
        if (this.test(this.entity.getAnimation())) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this.entity, IAnimatedEntity.NO_ANIMATION);
        }
    }

    protected abstract boolean test(Animation animation);
}
