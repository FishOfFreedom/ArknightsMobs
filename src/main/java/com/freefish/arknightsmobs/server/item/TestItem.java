package com.freefish.arknightsmobs.server.item;

import com.freefish.arknightsmobs.client.particle.*;
import com.freefish.arknightsmobs.client.particle.util.AdvancedParticleBase;
import com.freefish.arknightsmobs.client.particle.util.ParticleComponent;
import com.freefish.arknightsmobs.server.entity.EntityRegistry;
import com.freefish.arknightsmobs.server.entity.FreeFishEntity;
import com.freefish.arknightsmobs.server.entity.special.FallingStone;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.commons.lang3.ArrayUtils;

public class TestItem extends Item {
    public TestItem(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }
    public LivingEntity target;
    public int index = -1;
    public Modle modle = Modle.SET_MOVE;
    public Vector3d[] barakoPos = new Vector3d[]{new Vector3d(0,0,0)};
    public Vector3d[] staffPos = new Vector3d[]{new Vector3d(0,0,0)};

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity user) {
        MobEntity mobEntity = (MobEntity) target;
        this.target = target;
        mobEntity.getNavigator().tryMoveToXYZ(user.getPosX(), user.getPosY(), user.getPosZ(), 1.0);
        return super.hitEntity(stack, target, user);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World p_77659_1_, PlayerEntity player, Hand p_77659_3_) {
        World world = player.world;
        if(!p_77659_1_.isRemote) {
            if (player.isCrouching()) {
                modle = Modle.getProvince(modle);
                if(modle == Modle.SET_PARTICLE){
                    barakoPos[0] = new Vector3d(player.getPosX(),player.getPosY() + player.getBoundingBox().getYSize()/2,player.getPosZ());
                }
                TranslationTextComponent translationTextComponent = new TranslationTextComponent("message.test_item.hello" + modle);
                player.sendStatusMessage(translationTextComponent, false);
                return super.onItemRightClick(p_77659_1_, player, p_77659_3_);
            }
            if (modle == Modle.SET_ANIMATION && target != null) {
                if (target instanceof FreeFishEntity) {
                    FreeFishEntity freeFishEntity = (FreeFishEntity) target;
                    Animation[] animations = freeFishEntity.getAnimations();
                    Animation animation = freeFishEntity.getAnimation();
                    index = index ==-1? ArrayUtils.indexOf(animations, animation) + 1:index+1;
                    if (index > animations.length - 1) index = 0;
                    AnimationHandler.INSTANCE.sendAnimationMessage(freeFishEntity, animations[index]);
                }
            }

        }
        if(modle == Modle.SET_PARTICLE && world.isRemote){
            Vector3d vector3d = player.getPositionVec();
            AdvancedParticleBase.spawnStretchParticle(world, ParticleHandler.CRACK_1.get(), vector3d.getX(), vector3d.getY(), vector3d.getZ(), 0, 0, 0, false, 0, 0, 0, 0, 15F, 1, 223 / 255f, 66 / 255f, 1, 20);
            AdvancedParticleBase.spawnStretchParticle(world, ParticleHandler.CRACK_3.get(), vector3d.getX(), vector3d.getY()+3, vector3d.getZ(), 0, 0, 0, false, Math.PI*-0.5, 0, Math.PI*0.25, 0, 15F, 1, 223 / 255f, 66 / 255f, 1, 20);
            //world.addParticle(new WindParticle.WindParticleData(new Vector3f(vector3d),new Vector3f(vector3d.add(-10,-5,-10 )),new Vector3f(vector3d.add(-10,10,10 ))),vector3d.x,vector3d.y, vector3d.z, 0, 0, 0);
            //world.addParticle(new WindParticle.WindParticleData(new Vector3f(vector3d),new Vector3f(vector3d.add(10,-5,-10)),new Vector3f(vector3d.add(-10,10,-10))),vector3d.x,vector3d.y, vector3d.z, 0, 0, 0);
            //world.addParticle(new WindParticle.WindParticleData(new Vector3f(vector3d),new Vector3f(vector3d.add(10,-5,10 )),new Vector3f(vector3d.add(10,10,-10 ))),vector3d.x,vector3d.y, vector3d.z, 0, 0, 0);
            //world.addParticle(new WindParticle.WindParticleData(new Vector3f(vector3d),new Vector3f(vector3d.add(-10,-5,10  )),new Vector3f(vector3d.add(10,10,10  ))),vector3d.x,vector3d.y, vector3d.z, 0, 0, 0);
        }
        if(modle == Modle.BLINDING){
            FallingStone fallingStone = new FallingStone(EntityRegistry.FALLING_STONE.get(),world,player);
            fallingStone.setPosition(player.getPosX(),player.getPosY()+80,player.getPosZ());
            fallingStone.setMotion(0,1,2);
            if(!world.isRemote){
                world.addEntity(fallingStone);
            }
        }

        return super.onItemRightClick(p_77659_1_, player, p_77659_3_);
    }

    public void setParticle(PlayerEntity player) {
        World world = player.world;
        int ticksExisted = player.ticksExisted;
        if (barakoPos != null) {

            if (ticksExisted % 5 == 0) AdvancedParticleBase.spawnParticle(world, ParticleHandler.SIGNAL_LOOP.get(), barakoPos[0].getX(), barakoPos[0].getY(), barakoPos[0].getZ(), 0, 0, 0, false, 0, 0, 0, 0, 1.5F, 1, 223 / 255f, 66 / 255f, 1, 1, 15, true, false, new ParticleComponent[]{
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(1f, 10f), false)
            });

             /*
            staffPos[0] = new Vector3d(player.getPosX(),player.getPosY() + player.getBoundingBox().getYSize()/2,player.getPosZ());
            if (staffPos != null && staffPos[0] != null) {
                double dist = Math.max(barakoPos[0].distanceTo(staffPos[0]), 0.01);
                double radius = 0.5f;
                Random rand = new Random();
                double yaw = rand.nextFloat() * 2 * Math.PI;
                double pitch = rand.nextFloat() * 2 * Math.PI;
                double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                double oy = radius * Math.cos(pitch);
                double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                int spawnFreq = 5;
                if (ticksExisted % spawnFreq == 0) ParticleRibbon.spawnRibbon(world, ParticleHandler.RIBBON_STREAKS.get(), (int)(0.5 * dist), staffPos[0].getX(), staffPos[0].getY(), staffPos[0].getZ(), 0, 0, 0, true, 0, 0, 0, 0.5F, 0.95, 0.9, 0.35, 0.75, 1, spawnFreq, true, new ParticleComponent[]{
                        new RibbonComponent.BeamPinning(staffPos, barakoPos),
                        new RibbonComponent.PanTexture(0, 1)
                });
            }*/

        }
    }


    public enum Modle{
        SET_MOVE(1),SET_ANIMATION(2),SET_PARTICLE(3),GET_CAPABILITY(4),BLINDING(5);

        final int index;
        Modle(int index) {
            this.index = index;
        }

        public static Modle getProvince(Modle code){
            switch(code.index){
                case 1:
                    return Modle.SET_ANIMATION;
                case 2:
                    return Modle.SET_PARTICLE;
                case 3:
                    return Modle.GET_CAPABILITY;
                case 4:
                    return Modle.BLINDING;
                default:
                    return Modle.SET_MOVE;
            }
        }
    }
}
