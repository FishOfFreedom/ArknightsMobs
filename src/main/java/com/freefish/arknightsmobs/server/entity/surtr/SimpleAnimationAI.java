package com.freefish.arknightsmobs.server.entity.surtr;


import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;
import software.bernie.geckolib3.core.IAnimatable;

public class SimpleAnimationAI<T extends Entity & IAnimatedEntity & IAnimatable> extends AnimationAI<T> {
    protected final Animation animation;

    public SimpleAnimationAI(T entity, Animation animation) {
        super(entity);
        this.animation = animation;
    }

    @Override
    protected boolean test(Animation animation) {
        return animation == this.animation;
    }
}
