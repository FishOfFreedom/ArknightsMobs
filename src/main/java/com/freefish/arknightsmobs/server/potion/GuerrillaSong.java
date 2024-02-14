package com.freefish.arknightsmobs.server.potion;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class GuerrillaSong extends Effect {
    public GuerrillaSong() {
        super(EffectType.BENEFICIAL, 9643043);
        this.addAttributesModifier(Attributes.ARMOR, "46a095be-82dd-43fd-8b67-13f51591eb8e", (double)2F, AttributeModifier.Operation.ADDITION);
        this.addAttributesModifier(Attributes.ARMOR_TOUGHNESS, "46a095be-82dd-43fd-8b67-13f51591eb8e", (double)2F, AttributeModifier.Operation.ADDITION);
    }
}
