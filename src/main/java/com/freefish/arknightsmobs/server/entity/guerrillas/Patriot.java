package com.freefish.arknightsmobs.server.entity.guerrillas;

import com.freefish.arknightsmobs.client.particle.*;
import com.freefish.arknightsmobs.client.render.model.tools.Parabola;
import com.freefish.arknightsmobs.client.particle.util.AdvancedParticleBase;
import com.freefish.arknightsmobs.client.particle.util.ParticleComponent;
import com.freefish.arknightsmobs.client.particle.util.RibbonComponent;
import com.freefish.arknightsmobs.server.attribute.AttributeRegistry;
import com.freefish.arknightsmobs.server.entity.SmartBodyHelper;
import com.freefish.arknightsmobs.server.entity.ai.MMPathNavigateGround;
import com.freefish.arknightsmobs.server.entity.ai.guerrillasAi.patriot.*;
import com.freefish.arknightsmobs.server.entity.ganranzhezhiji.GanRanZheZhiJi;
import com.freefish.arknightsmobs.server.entity.ai.guerrillasAi.ProtectionVillager;
import com.freefish.arknightsmobs.server.item.ItemRegistry;
import com.freefish.arknightsmobs.server.sound.MMSounds;
import com.freefish.arknightsmobs.server.world.savedate.GuerrillasWorldData;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.ai.controller.BodyController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.world.NoteBlockEvent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Patriot extends GuerrillasEntity {
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private final List<Messenger> messengers = new ArrayList<>();
    public static final Animation RIGHT_SWEPT = Animation.create(40);
    public static final Animation LEFT_SWEPT = Animation.create(60);
    public static final Animation LIGHT_SWEPT = Animation.create(20);
    public static final Animation SWEPT_2 = Animation.create(30);
    public static final Animation SHIELD_ATTACK = Animation.create(40);
    public static final Animation LIFT = Animation.create(40);
    public static final Animation RAISE_ATTACK = Animation.create(85);
    public static final Animation RAISE_SWEPT = Animation.create(20);
    public static final Animation STOMP = Animation.create(40);
    public static final Animation STRENGTHEN = Animation.create(60);
    public static final Animation THROW = Animation.create(66);
    public static final Animation PIERCE = Animation.create(25);
    public static final Animation ENHANCED_1 = Animation.create(21);
    public static final Animation ENHANCED_2 = Animation.create(60);
    public static final Animation CHEER = Animation.create(10);
    public static final Animation RAISE_CHANGE = Animation.create(40);
    public static final Animation RUIN_CHANGE = Animation.create(40);
    public static final Animation RUIN_STOMP = Animation.create(50);
    public static final Animation RUSH_ATTACK = Animation.create(30);
    public static final Animation RUIN_HEAVY_STOMP = Animation.create(200);
    public static final Animation JUMP_1 = Animation.create(30);
    public static final Animation JUMP_2 = Animation.create(30);
    public static final Animation PRIZE = Animation.create(160);

    private static final Animation[] ANIMATIONS = {
            NO_ANIMATION,RIGHT_SWEPT,LEFT_SWEPT,THROW,PIERCE,ENHANCED_1,ENHANCED_2,CHEER,LIGHT_SWEPT,SWEPT_2,LIFT
            ,RAISE_SWEPT,RAISE_ATTACK,STRENGTHEN,STOMP,SHIELD_ATTACK,RAISE_CHANGE,RUIN_CHANGE,
            RUIN_STOMP,RUSH_ATTACK,RUIN_HEAVY_STOMP,JUMP_1,JUMP_2,PRIZE
    };

    public AttackMode attackMode;
    public Parabola parabola = new Parabola();
    public boolean isCanBeAttacking = false;
    public List<ArmorCounter> armorCounters = new ArrayList();

    @OnlyIn(Dist.CLIENT)
    public Vector3d[] clientVectors;

    private Vector3d prevRightHandPos;
    private int timeSinceEnhanced = -1;
    private int maxEnhancedTime;
    private int enhancedTime;
    public Vector3d[] trailPositions = new Vector3d[64];
    public int trailPointer = -1;
    public int time=0;
    public int pointer;
    public double rate;
    public int timeSincePierce;
    public int timeSinceThrow;

    private static final float[][] ATTACK_BLOCK_OFFSETS = {
            {-0.1F, -0.1F},
            {-0.1F, 0.1F},
            {0.1F, 0.1F},
            {0.1F, -0.1F}
    };

    private static final DataParameter<Float> TARGET_POSX = EntityDataManager.createKey(Patriot.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> TARGET_POSY = EntityDataManager.createKey(Patriot.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> TARGET_POSZ = EntityDataManager.createKey(Patriot.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> IS_RUNNING = EntityDataManager.createKey(Patriot.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_HAND_SHIELD = EntityDataManager.createKey(Patriot.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_ENHANCED = EntityDataManager.createKey(Patriot.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> PREDICATE = EntityDataManager.createKey(Patriot.class, DataSerializers.VARINT);




    public Patriot(EntityType<? extends CreatureEntity> p_i48575_1_, World p_i48575_2_) {
        super(p_i48575_1_, p_i48575_2_);
        attackMode = AttackMode.NONE;
        if (world.isRemote)
            clientVectors = new Vector3d[] {new Vector3d(0, 0, 0), new Vector3d(0, 0, 0)};
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new PatriotAttackAI(this));
        //this.goalSelector.addGoal(2,new MoveTowardsRestrictionGoal(this,1.0d));

        this.goalSelector.addGoal(1, new AttackAnimationAI(this, RIGHT_SWEPT));
        this.goalSelector.addGoal(1,new ThrowAnimationAI(this, THROW));
        this.goalSelector.addGoal(1,new PierceAnimationAI(this, PIERCE));
        this.goalSelector.addGoal(1,new EnhanceAnimationAI(this, ENHANCED_2));
        this.goalSelector.addGoal(1,new StompAnimationAI(this, STOMP));
        this.goalSelector.addGoal(1,new LightSweptAnimationAI(this, LIGHT_SWEPT));
        this.goalSelector.addGoal(1,new ShieldAttackAnimationAI(this, SHIELD_ATTACK));
        this.goalSelector.addGoal(1,new RaiseChangeAnimationAI(this, RAISE_CHANGE));
        this.goalSelector.addGoal(1,new StrengthenAnimationAI(this, STRENGTHEN));
        this.goalSelector.addGoal(1,new RaiseAttackAnimationAI(this, RAISE_ATTACK));

        this.goalSelector.addGoal(1,new RuinChangeAnimationAI(this, RAISE_ATTACK));
        this.goalSelector.addGoal(1,new RuinStompAnimationAI(this, RUIN_STOMP));
        this.goalSelector.addGoal(1,new RushAttackAnimationAI(this, RUSH_ATTACK));
        this.goalSelector.addGoal(1,new RuinHeavyStompAnimationAI(this, RUIN_HEAVY_STOMP));


        this.goalSelector.addGoal(1,new PrizeAnimationAI(this, PRIZE));

        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this , 1F));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<ZombieEntity>(this, ZombieEntity.class, true,true){
            @Override
            public void startExecuting() {
                attackMode = AttackMode.ENTITY;
                super.startExecuting();
            }
            @Override
            public boolean shouldExecute() {
                return super.shouldExecute()&&getAttackTarget()==null;
            }
        });
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<AbstractRaiderEntity>(this, AbstractRaiderEntity.class, true,true){
            @Override
            public void startExecuting() {
                attackMode = AttackMode.ENTITY;
                super.startExecuting();
            }
            @Override
            public boolean shouldExecute() {
                return super.shouldExecute()&&getAttackTarget()==null;
            }
        });
        this.targetSelector.addGoal(1,new ProtectionVillager(this));
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.getAttackTarget();
        if((target == null || !target.isAlive()) && attackMode != AttackMode.NONE)
            attackMode = AttackMode.NONE;
        if(target != null && target.isAlive())
            setTargetPos(new Vector3f(target.getPositionVec()));
        else {
            setAttackTarget(null);
            if(getArmorDurability()<getMaxArmorDurability()-5&&ticksExisted%30==0)
                setArmorDurability(getArmorDurability()+5);
        }

        if(timeSinceEnhanced >= 0) {
            timeSinceEnhanced--;
            if(timeSinceEnhanced == 0){
                AnimationHandler.INSTANCE.sendAnimationMessage(this, ENHANCED_2);
            }
        }

        if (getAnimation() != NO_ANIMATION) {
            rotationYawHead = renderYawOffset = rotationYaw;
        }

        repelEntities(1.7F, 4.5f, 1.7F, 1.7F);

        if(!world.isRemote && getPredicate()==2){
            double maxHealth = getMaxHealth();
            double health = getHealth();
            if(health <= maxHealth*(maxEnhancedTime-enhancedTime)/maxEnhancedTime){
                enhancedTime++;
                startEnhanced(200);
            }
        }

        if(this.getAnimation() == THROW){
            int tick = getAnimationTick();
            Vector3f targetPos = getTargetPos();
            if (tick == 30 || tick ==45) {
                parabola.mathParabola(this, targetPos);
            }
            if (this.world.isRemote) {
                if (tick >= 30 && tick < 66) {
                    double i = ((double) (tick - 31)) / 34;
                    double i1 = ((double) (tick - 30)) / 34;
                    double posX = getPosX() + parabola.getX() * i;
                    double posZ = getPosZ() + parabola.getZ() * i;
                    double targetPosX = getPosX() + parabola.getX() * i1;
                    double targetPosZ = getPosZ() + parabola.getZ() * i1;
                    double x3 = parabola.getX2() * i;
                    double targetX3 = parabola.getX2() * i1;
                    double posY = getPosY() + parabola.getY(x3);
                    double targetPosY = getPosY() + parabola.getY(targetX3);
                    Vector3d particleMotion = new Vector3d(posX-targetPosX, posY-targetPosY,posZ-targetPosZ);
                    int smokeNumber = 4 + rand.nextInt(5);
                    for(int j =1;j<=smokeNumber;j++){
                        double speed = 0.5 + rand.nextDouble();
                        Vector3d newParticleMotion = particleMotion.rotateYaw((float)(Math.PI*Math.cos(2*i/smokeNumber*Math.PI)/6)).rotatePitch((float)(Math.PI*Math.sin(2*i/smokeNumber*Math.PI)/6)).mul(speed,speed,speed);
                        world.addParticle(ParticleTypes.SMOKE, posX,posY,posZ,newParticleMotion.x,newParticleMotion.y,newParticleMotion.z);
                    }
                    AdvancedParticleBase.spawnParticle(world, ParticleHandler.ARROW_HEAD.get(), posX, posY, posZ, 0, 0, 0, false, 0, 0, 0, 0, 40f, 1, 1, 1, 0.75, 1, 2, true, false, new ParticleComponent[]{
                            new ParticleComponent.Attractor(new Vector3d[]{new Vector3d(targetPosX,targetPosY,targetPosZ)}, 0.5f, 0.2f, ParticleComponent.Attractor.EnumAttractorBehavior.LINEAR),
                            new RibbonComponent(ParticleHandler.RIBBON_FLAT.get(), 10, 0, 0, 0, 0.8F, 1, 1, 1, 0.75, true, true, new ParticleComponent[]{
                                    new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(1, 0))
                            }),
                            new ParticleComponent.FaceMotion(),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new ParticleComponent.KeyTrack(new float[]{0, 0, 1}, new float[]{0, 0.05f, 0.06f}), false),
                    });
                }
            }
            if (tick == 56) {
                double i = ((double) (tick - 30)) / 34;
                double x3 = parabola.getX2() * i;
                double posX = getPosX() + parabola.getX() * i;
                double posZ = getPosZ() + parabola.getZ() * i;
                double posY = getPosY() + parabola.getY(x3);
                Vector3d vector3d = new Vector3d(targetPos.getX() - posX,targetPos.getY() -posY,targetPos.getZ() -posZ);
                float f = MathHelper.sqrt(horizontalMag(vector3d));
                float yaw = (float)(MathHelper.atan2(vector3d.x, vector3d.z) * (double)(180F / (float)Math.PI));
                float pitch = (float)(MathHelper.atan2(vector3d.y, (double)f) * (double)(180F / (float)Math.PI));
                GanRanZheZhiJi ganRanZheZhiJi = new GanRanZheZhiJi(world, this, new ItemStack(ItemRegistry.GAN_RAN_ZHE_ZHI_JI_ITEM.get()),true);
                ganRanZheZhiJi.setPositionAndRotation(getPosX() + parabola.getX()*i,getPosY() + parabola.getY(x3),getPosZ() + parabola.getZ()*i, -pitch, yaw);
                ganRanZheZhiJi.setDirectionAndMovement(this, -pitch, -yaw, 0.0F, 6F, 0f);
                ganRanZheZhiJi.setNoGravity(true);
                if(!world.isRemote)world.addEntity(ganRanZheZhiJi);
            }
        }

        if(getIsEnhanced()){
            setMotion(0, getMotion().y, 0);
            rotationYaw = prevRotationYaw;
        }
        if(!world.isRemote&&getIsRunning()){
            double yaw = renderYawOffset/180*Math.PI;
            Vector3d vector3d =new Vector3d(getPosX()-Math.sin(yaw),getPosY(),getPosZ()+Math.cos(yaw));
            BlockPos blockPos = new BlockPos(vector3d);
            BlockPos blockPos1 = new BlockPos(vector3d.add(0,1,0));
            BlockState blockState = world.getBlockState(blockPos);
            BlockState blockState1 = world.getBlockState(blockPos1);
            if(blockState.isSolid()&&blockState.isNormalCube(world,blockPos)&&(!blockState1.isNormalCube(world,blockPos1))){
                setPosition(getPosX()-Math.sin(yaw)/4,getPosY()+1,getPosZ()+Math.cos(yaw)/4);
            }
        }
        //addSweptParticle();
        addShieldParticle();
        addRuinHeavyStompFX();
        addPredicateTwoFX();
        //addPierceParticle();
        if(world.isRemote){
            int tick = getAnimationTick();
            if(getAnimation() == RAISE_ATTACK && tick == 46)
                doWeaponOnGround();
            else if(getAnimation()==LIFT&&tick<=24)
                doWeaponOnGround();
            else if(getAnimation()==LIGHT_SWEPT&&tick>=5&&tick<=10)
                doWeaponOnGround();

            rate+=1.05-(getArmorDurability()/getMaxArmorDurability());
            if(rate>=1) {
                pointer++;
                rate=0;
            }
            if(pointer>=3)pointer=0;
        }

        float moveX = (float) (getPosX() - prevPosX);
        float moveZ = (float) (getPosZ() - prevPosZ);
        float speed = MathHelper.sqrt(moveX * moveX + moveZ * moveZ);
        if(!getIsRunning()){
            if (this.world.isRemote && frame % 20 == 1 && speed > 0.03 && getAnimation() == NO_ANIMATION)
                this.world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), MMSounds.ENTITY_PATRIOT_STEP.get(), this.getSoundCategory(), 20F, 1F, false);
        }else {
            if(this.world.isRemote && frame % 10 == 1 && speed > 0.03 && getAnimation() == NO_ANIMATION)
                this.world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), MMSounds.ENTITY_PATRIOT_STEP.get(), this.getSoundCategory(), 20F, 1F, false);
        }

        double armor = getArmorDurability();
        if(armor <= 0 && getPredicate()==1) {
            GuerrillasWorldData guerrillasWorldData = GuerrillasWorldData.get(world);
            boolean isWinOnce = guerrillasWorldData.getIsWinOnce();
            if(!world.isRemote){
                if (!isWinOnce&&attackMode==AttackMode.PLAYER){
                    setArmorDurability(getMaxArmorDurability());
                    AnimationHandler.INSTANCE.sendAnimationMessage(this,PRIZE);
                    guerrillasWorldData.setIsWinOnce(true);
                }
                else
                    setPredicateEffect(2);
            }

        }
        if(!world.isRemote) {
            setIsHandShield(target != null && target.isAlive()&&getPredicate()==1);
            if(getArmorDurability()>40&&getPredicate()==2)
                setPredicateEffect(1);
        }
        prevRotationYaw = rotationYaw;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        Entity entitySource = source.getTrueSource();
        if (entitySource != null){
            if (getIsEnhanced()) return false;
            if (attackMode == AttackMode.NONE){
                if(entitySource instanceof PlayerEntity){
                    PlayerEntity playerEntitySource = (PlayerEntity) entitySource;
                    if(amount <= 5) {
                        if(!world.isRemote) {
                            faceEntity(playerEntitySource, 30F, 30F);
                            TranslationTextComponent translationTextComponent = new TranslationTextComponent("message.hello_block.hello");
                            playerEntitySource.sendStatusMessage(translationTextComponent, false);
                        }
                        return false;
                    }
                    if(!playerEntitySource.isCreative()) {
                        attackMode = AttackMode.PLAYER;
                        setAttackTarget(playerEntitySource);
                    }
                    return attackWithShield(source,amount);
                }
                attackMode = AttackMode.ENTITY;
                if(entitySource instanceof LivingEntity)
                    setAttackTarget((LivingEntity) entitySource);
            }
            if(entitySource instanceof LivingEntity&&getDistanceSq(entitySource)>25) {
                setAttackTarget((LivingEntity) entitySource);
            }
            return attackWithShield(source, amount);
        }
        else if (source.canHarmInCreative()) {
            return super.attackEntityFrom(source, amount);
        }
        return false;
    }

    @Override
    public boolean canCollide(Entity entity) {
        return false;
    }

    public boolean attackWithShield(DamageSource source, float amount){
        Entity entitySource = source.getTrueSource();
        if (entitySource != null) {
            if(attackMode==AttackMode.PLAYER&&entitySource instanceof PlayerEntity) amount*=2.5;
            if (getIsHandShield() && !isCanBeAttacking) {
                int arc = 80;
                float entityHitAngle = (float) ((Math.atan2(entitySource.getPosZ() - getPosZ(), entitySource.getPosX() - getPosX()) * (180 / Math.PI) - 90) % 360);
                float entityAttackingAngle = renderYawOffset % 360;
                if (entityHitAngle < 0) {
                    entityHitAngle += 360;
                }
                if (entityAttackingAngle < 0) {
                    entityAttackingAngle += 360;
                }
                float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
                if ((entityRelativeAngle <= arc / 2f && entityRelativeAngle >= -arc / 2f) || (entityRelativeAngle >= 360 - arc / 2f || entityRelativeAngle <= -arc + 90f / 2f)) {
                    playSound(SoundEvents.ITEM_SHIELD_BLOCK,0.4f,2); //playSound(MMSounds.ENTITY_WROUGHT_UNDAMAGED.get(), 0.4F, 2);
                    addShieldArmorParticle();
                    return false;
                }
                else
                    return super.attackEntityFrom(source, amount);
            } else {
                playSound(SoundEvents.ITEM_SHIELD_BLOCK,0.4f,2); //playSound(MMSounds.ENTITY_WROUGHT_UNDAMAGED.get(), 0.4F, 2);
                return super.attackEntityFrom(source, amount);
            }
        }
        return false;
    }

    @Override
    public Animation getDeathAnimation() {
        return null;
    }

    @Override
    public Animation getHurtAnimation() {
        return null;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "Controller", 3, this::predicate));
        data.addAnimationController(new AnimationController<>(this,"shieldController",10,this::shieldPredicate));
    }

    //TODO
    @Override
    protected PathNavigator createNavigator(World world) {
        return new MMPathNavigateGround(this, world);
    }

    @Override
    protected BodyController createBodyController() {
        return new SmartBodyHelper(this);
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return GuerrillasEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 200.0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 30.0f)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2f)
                .createMutableAttribute(Attributes.ARMOR, 20.0D)
                .createMutableAttribute(Attributes.ARMOR_TOUGHNESS, 10.0D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 48)
                .createMutableAttribute(AttributeRegistry.ARMOR_DURABILITY.get(),250f)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE,1f);
    }

    @Override
    public Animation[] getAnimations() {
        return ANIMATIONS;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event){
        if(getAnimation() == IAnimatedEntity.NO_ANIMATION) {
            if(getIsEnhanced()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("revive_2", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
            }
            else {
                if (event.isMoving()) {
                    if (getIsRunning())
                        event.getController().setAnimation(new AnimationBuilder().addAnimation("run", ILoopType.EDefaultLoopTypes.LOOP));
                    else
                        event.getController().setAnimation(new AnimationBuilder().addAnimation("walk_2", ILoopType.EDefaultLoopTypes.LOOP));
                }
                else
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_1", ILoopType.EDefaultLoopTypes.LOOP));
            }
        }
        else {
            if(getAnimation() == RIGHT_SWEPT)
                event.getController().setAnimation(new AnimationBuilder().addAnimation("right_heavy_swept", ILoopType.EDefaultLoopTypes.LOOP));
            else if(getAnimation() == LEFT_SWEPT)
                event.getController().setAnimation(new AnimationBuilder().addAnimation("left_heavy_swept", ILoopType.EDefaultLoopTypes.LOOP));
            else if(getAnimation() == PIERCE)
                event.getController().setAnimation(new AnimationBuilder().addAnimation("change", ILoopType.EDefaultLoopTypes.LOOP));
            else if(getAnimation() == THROW)
                event.getController().setAnimation(new AnimationBuilder().addAnimation("throw", ILoopType.EDefaultLoopTypes.LOOP));
            else if(getAnimation() == ENHANCED_2)
                event.getController().setAnimation(new AnimationBuilder().addAnimation("revive_3", ILoopType.EDefaultLoopTypes.LOOP));
            else if(getAnimation() == ENHANCED_1)
                event.getController().setAnimation(new AnimationBuilder().addAnimation("revive_1", ILoopType.EDefaultLoopTypes.LOOP));
            else if(getAnimation() == CHEER)
                event.getController().setAnimation(new AnimationBuilder().addAnimation("cheer", ILoopType.EDefaultLoopTypes.LOOP));
            else if(getAnimation() == STRENGTHEN)
                event.getController().setAnimation(new AnimationBuilder().addAnimation("strengthen", ILoopType.EDefaultLoopTypes.LOOP));
            else if(getAnimation() == STOMP)
                event.getController().setAnimation(new AnimationBuilder().addAnimation("stomp", ILoopType.EDefaultLoopTypes.LOOP));
            else if(getAnimation() == LIGHT_SWEPT)
                event.getController().setAnimation(new AnimationBuilder().addAnimation("light_swept", ILoopType.EDefaultLoopTypes.LOOP));
            else if(getAnimation() == LIFT)
                event.getController().setAnimation(new AnimationBuilder().addAnimation("lift", ILoopType.EDefaultLoopTypes.LOOP));
            else if(getAnimation() == RAISE_SWEPT)
                event.getController().setAnimation(new AnimationBuilder().addAnimation("raise_change_swept", ILoopType.EDefaultLoopTypes.LOOP));
            else if(getAnimation() == RAISE_ATTACK)
                event.getController().setAnimation(new AnimationBuilder().addAnimation("raise_attack", ILoopType.EDefaultLoopTypes.LOOP));
            else if(getAnimation() == SHIELD_ATTACK)
                event.getController().setAnimation(new AnimationBuilder().addAnimation("shield_attack", ILoopType.EDefaultLoopTypes.LOOP));
            else if(getAnimation() == SWEPT_2)
                event.getController().setAnimation(new AnimationBuilder().addAnimation("heavy_swept_2", ILoopType.EDefaultLoopTypes.LOOP));
            else if(getAnimation() == RAISE_CHANGE)
                event.getController().setAnimation(new AnimationBuilder().addAnimation("raise_change", ILoopType.EDefaultLoopTypes.LOOP));
            else if(getAnimation() == RUIN_CHANGE)
                event.getController().setAnimation(new AnimationBuilder().addAnimation("ruin_change", ILoopType.EDefaultLoopTypes.LOOP));
            else if(getAnimation() == RUIN_STOMP)
                event.getController().setAnimation(new AnimationBuilder().addAnimation("ruin_stomp", ILoopType.EDefaultLoopTypes.LOOP));
            else if(getAnimation() == RUSH_ATTACK)
                event.getController().setAnimation(new AnimationBuilder().addAnimation("rush_attack", ILoopType.EDefaultLoopTypes.LOOP));
            else if(getAnimation() == RUIN_HEAVY_STOMP)
                event.getController().setAnimation(new AnimationBuilder().addAnimation("ruin_heavy_stomp", ILoopType.EDefaultLoopTypes.LOOP));
            else if(getAnimation() == JUMP_1)
                event.getController().setAnimation(new AnimationBuilder().addAnimation("jump_1", ILoopType.EDefaultLoopTypes.LOOP));
            else if(getAnimation() == JUMP_2)
                event.getController().setAnimation(new AnimationBuilder().addAnimation("jump_2", ILoopType.EDefaultLoopTypes.LOOP));
            else if(getAnimation() == PRIZE)
                event.getController().setAnimation(new AnimationBuilder().addAnimation("prize_1", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }

    private <T extends IAnimatable> PlayState shieldPredicate(AnimationEvent<T> event) {
        if(getIsHandShield()){
            if(getAnimation() == IAnimatedEntity.NO_ANIMATION){
                if(event.isMoving()) {
                    if(getIsRunning())
                       event.getController().setAnimation(new AnimationBuilder().addAnimation("run_shield", ILoopType.EDefaultLoopTypes.LOOP));
                    else {
                        if(getIsHandShield())
                            event.getController().setAnimation(new AnimationBuilder().addAnimation("walk_shield", ILoopType.EDefaultLoopTypes.LOOP));
                        else
                            event.getController().setAnimation(new AnimationBuilder().addAnimation("pose_3", ILoopType.EDefaultLoopTypes.LOOP));
                    }
                } else {
                    if(getIsHandShield())
                        event.getController().setAnimation(new AnimationBuilder().addAnimation("pose_2", ILoopType.EDefaultLoopTypes.LOOP));
                    else
                        event.getController().setAnimation(new AnimationBuilder().addAnimation("pose_3", ILoopType.EDefaultLoopTypes.LOOP));
                }
            }
            else {
                if(getAnimation() == RIGHT_SWEPT || getAnimation() == LEFT_SWEPT)
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("swept_shield", ILoopType.EDefaultLoopTypes.LOOP));
                else if(getAnimation() == LIGHT_SWEPT || getAnimation() == SWEPT_2 || getAnimation() == LIFT)
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("light_swept_shield", ILoopType.EDefaultLoopTypes.LOOP));
                else if(getAnimation() == PIERCE)
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("change_shield", ILoopType.EDefaultLoopTypes.LOOP));
                else if(getAnimation() == THROW)
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("throw_shield", ILoopType.EDefaultLoopTypes.LOOP));
                else if(getAnimation() == RAISE_ATTACK)
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("raise_attack_shield", ILoopType.EDefaultLoopTypes.LOOP));
                else if(getAnimation() == RAISE_CHANGE)
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("raise_change_shield", ILoopType.EDefaultLoopTypes.LOOP));
                else if(getAnimation() == STOMP || getAnimation() == STRENGTHEN)
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("pose_2", ILoopType.EDefaultLoopTypes.LOOP));
                else if(getAnimation() == RUIN_CHANGE || getAnimation() == RUSH_ATTACK|| getAnimation() == RUIN_STOMP|| getAnimation() == RUIN_HEAVY_STOMP|| getAnimation() == JUMP_1|| getAnimation() == JUMP_2||getAnimation()==PRIZE)
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("pose_3", ILoopType.EDefaultLoopTypes.LOOP));
            }
        }
        else
            event.getController().setAnimation(new AnimationBuilder().addAnimation("pose_3", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        maxEnhancedTime = 2;
        enhancedTime = 0;
        if(!world.isRemote) {
            GuerrillasWorldData guerrillasWorldData = GuerrillasWorldData.get(world);
            guerrillasWorldData.setExistedNumber(guerrillasWorldData.getExistedNumber() + 1);
        }
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn) {
        GuerrillasWorldData guerrillasWorldData = GuerrillasWorldData.get(world);
        int number = guerrillasWorldData.getExistedNumber();
        if (number>0&&spawnReasonIn == SpawnReason.NATURAL)
            return false;
        return super.canSpawn(worldIn, spawnReasonIn);
    }

    @Override
    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        if(!world.isRemote) {
            GuerrillasWorldData guerrillasWorldData = GuerrillasWorldData.get(world);
            guerrillasWorldData.setExistedNumber(guerrillasWorldData.getExistedNumber() - 1);
        }
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(TARGET_POSX, 0f);
        this.dataManager.register(TARGET_POSY, 0f);
        this.dataManager.register(TARGET_POSZ, 0f);
        this.dataManager.register(IS_RUNNING, false);
        this.dataManager.register(IS_HAND_SHIELD, true);
        this.dataManager.register(IS_ENHANCED,false);
        this.dataManager.register(PREDICATE,1);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);

        Vector3f vector3f = getTargetPos();
        compound.putFloat("targetPosX",vector3f.getX());
        compound.putFloat("targetPosY",vector3f.getY());
        compound.putFloat("targetPosZ",vector3f.getZ());
        compound.putBoolean("isRunning",getIsRunning());
        compound.putBoolean("isHandShield", getIsHandShield());
        compound.putInt("predicate", getPredicate());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        float x = compound.getFloat("targetPosX");
        float y = compound.getFloat("targetPosY");
        float z = compound.getFloat("targetPosZ");
        setTargetPos(new Vector3f(x,y,z));
        setIsRunning(compound.getBoolean("isRunning"));
        setIsHandShield(compound.getBoolean("isHandShield"));
        setPredicate(compound.getInt("predicate"));
    }

    @Override
    public SoundEvent getBossMusic() {
        return MMSounds.PATRIOT_THEME.get();
    }

    @Override
    protected boolean canPlayMusic() {
        return super.canPlayMusic() && getPredicate()==1;
    }

    public void doRangeAttack(double range, double arc,float damage,boolean isBreakingShield){
        List<LivingEntity> entitiesHit = getEntityLivingBaseNearby(range, 3, range, range);
        boolean hit = false;
        for (LivingEntity entityHit : entitiesHit) {
            float entityHitAngle = (float) ((Math.atan2(entityHit.getPosZ() - getPosZ(), entityHit.getPosX() - getPosX()) * (180 / Math.PI) - 90) % 360);
            float entityAttackingAngle = renderYawOffset % 360;
            if (entityHitAngle < 0) {
                entityHitAngle += 360;
            }
            if (entityAttackingAngle < 0) {
                entityAttackingAngle += 360;
            }
            float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
            float entityHitDistance = (float) Math.sqrt((entityHit.getPosZ() - getPosZ()) * (entityHit.getPosZ() - getPosZ()) + (entityHit.getPosX() - getPosX()) * (entityHit.getPosX() - getPosX())) - entityHit.getWidth() / 2f;
            if (entityHitDistance <= range && (entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2) || (entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -360 + arc / 2)) {
                attackEntityByPatriot(entityHit, damage,isBreakingShield);
                entityHit.setMotion(entityHit.getMotion().x * 1, entityHit.getMotion().y, entityHit.getMotion().z * 1);
                hit = true;
            }
        }
        if (hit) {
            playSound(MMSounds.ENTITY_WROUGHT_AXE_HIT.get(), 1, 0.5F);
        }
    }

    public void attackEntityByPatriot(LivingEntity target,float amount,boolean isBreakingShield){
        if(target instanceof PlayerEntity&&attackMode == AttackMode.PLAYER){
            PlayerEntity player = (PlayerEntity) target;
            target.attackEntityFrom(DamageSource.causeMobDamage(this), amount / 3);
            if(isBreakingShield){
                ItemStack itemStackFromSlot = player.getItemStackFromSlot(EquipmentSlotType.OFFHAND);
                if(player.isActiveItemStackBlocking()&&itemStackFromSlot.getItem() instanceof ShieldItem){
                    player.getCooldownTracker().setCooldown(Items.SHIELD, 100);
                    player.resetActiveHand();
                }
            }
            return;
        }
        target.attackEntityFrom(DamageSource.causeMobDamage(this), amount);
    }

    public void addShieldParticle(){
        if(world.isRemote&&getIsHandShield()&&getAnimation() == NO_ANIMATION){
            Vector3d motion = getMotion().scale(2);
            Vector3d shieldVector = clientVectors[1];
            double yaw = -this.renderYawOffset/180*Math.PI;
            if (ticksExisted % 5 == 0) AdvancedParticleBase.spawnParticle(world, ParticleHandler.SIGNAL_LOOP.get(), shieldVector.getX(), shieldVector.getY(), shieldVector.getZ(), motion.x, 0, motion.z, false, yaw, 0, 0, 0, 5F, 204/255f, 0f, 0f, 1, 1, 10, true, false, new ParticleComponent[]{
                    //new ParticleComponent.Attractor(new Vector3d[]{new Vector3d(shieldVector.x+motionX,shieldVector.y+motionY,shieldVector.z+motionZ)}, 1f, 1f, ParticleComponent.Attractor.EnumAttractorBehavior.LINEAR),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(20f, 1f), false)
            });
        }
    }

    private void addShieldArmorParticle(){
        if(world.isRemote&&getIsHandShield()&&getAnimation() == NO_ANIMATION) {
            Vector3d motion = getMotion().scale(2);
            Vector3d shieldVector = clientVectors[1];
            double yaw = -this.renderYawOffset / 180 * Math.PI;
            AdvancedParticleBase.spawnParticle(world, ParticleHandler.HEXAGON.get(), shieldVector.getX(), shieldVector.getY() - 1, shieldVector.getZ(), motion.x, 0, motion.z, false, yaw, 0, 0, 0, 1.5F, 204 / 255f, 0f, 0f, 1, 1, 20, true, false, new ParticleComponent[]{
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false)
            });
        }
    }

    private void addPredicateTwoFX(){
        if(world.isRemote&&getPredicate()==2){
            Vector3d motion = getMotion().scale(2);
            Vector3d vector3d = getPositionVec().add(0,0.1,0);
            double yaw = renderYawOffset/180*Math.PI;
            if(ticksExisted%100==0){
                AdvancedParticleBase.spawnParticle(world, ParticleHandler.RING2.get(), vector3d.getX(), vector3d.getY() , vector3d.getZ(), motion.x, 0, motion.z, false, 0, Math.PI/2, 0, 0, 7F, 204 / 255f, 0f, 0f, 1, 1, 100, true, false, new ParticleComponent[]{
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false)
                });
            }

            if(ticksExisted%10==0){
                double yaw1 = yaw + Math.PI*(rand.nextDouble()-0.5)/2;
                Vector3d vector3d1 = vector3d.add(Math.sin(yaw1)*2,0,-Math.cos(yaw1)*2);
                AdvancedParticleBase.spawnStretchParticle(world, ParticleHandler.CRACK_3.get(), vector3d1.getX(), vector3d1.getY() , vector3d1.getZ(), motion.x, 0, motion.z, false, -yaw1 - Math.PI/2, 0, -Math.PI/4, 0, 25F, 204 / 255f, 0f, 0f, 1, 20);
            }
            if(ticksExisted%5==0){
                Vector3d vector3d1 = vector3d.add(Math.sin(yaw)*2,2,-Math.cos(yaw)*2);
                world.addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD.get(),1f,0f,0f,10f,10, ParticleCloud.EnumCloudBehavior.CONSTANT,1f),vector3d1.getX(), vector3d1.getY() , vector3d1.getZ(), motion.x, 0, motion.z);
            }
        }
    }

    public void addSweptParticle(){
        if (world.isRemote) {
            double motionX = getMotion().getX();
            double motionY = getMotion().getY();
            double motionZ = getMotion().getZ();

            int snowflakeDensity = 4;
            float snowflakeRandomness = 0.5f;
            int cloudDensity = 2;
            float cloudRandomness = 0.5f;
            if(true){
                Vector3d rightHandPos = clientVectors[0];
                if (getAnimationTick() > 1 && getAnimationTick() < 23) {
                    double length = prevRightHandPos.subtract(rightHandPos).length();
                    int numClouds = (int) Math.floor(2 * length);
                    for (int i = 0; i < numClouds; i++) {
                        double x = prevRightHandPos.x + i * (rightHandPos.x - prevRightHandPos.x) / numClouds;
                        double y = prevRightHandPos.y + i * (rightHandPos.y - prevRightHandPos.y) / numClouds;
                        double z = prevRightHandPos.z + i * (rightHandPos.z - prevRightHandPos.z) / numClouds;
                        for (int j = 0; j < snowflakeDensity; j++) {
                            float xOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
                            float yOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
                            float zOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
                            world.addParticle(new ParticleSnowFlake.SnowflakeData(40, false), x + xOffset, y + yOffset, z + zOffset, motionX, motionY - 0.01f, motionZ);
                        }
                        for (int j = 0; j < cloudDensity; j++) {
                            float xOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
                            float yOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
                            float zOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
                            float value = rand.nextFloat() * 0.1f;
                            world.addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD.get(), 0.8f + value, 0.8f + value, 1f, (float) (10d + rand.nextDouble() * 10d), 40, ParticleCloud.EnumCloudBehavior.SHRINK, 1f), x + xOffset, y + yOffset, z + zOffset, motionX, motionY, motionZ);
                        }
                    }
                    AdvancedParticleBase.spawnParticle(world, ParticleHandler.ARROW_HEAD.get(), prevRightHandPos.x, prevRightHandPos.y, prevRightHandPos.z, 0, 0, 0, false, 0, 0, 0, 0, 5f, 0, 0, 0, 0.75, 1, 4, true, false, new ParticleComponent[]{
                            new ParticleComponent.Attractor(new Vector3d[]{new Vector3d(rightHandPos.x,rightHandPos.y,rightHandPos.z)}, 0.5f, 0.2f, ParticleComponent.Attractor.EnumAttractorBehavior.LINEAR),
                            new RibbonComponent(ParticleHandler.RIBBON_STREAKS.get(), 10, 0, 0, 0, 0.8F, 1, 1, 1, 0.75, true, true, new ParticleComponent[]{
                                    new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(1, 0))
                            }),
                            new ParticleComponent.FaceMotion(),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new ParticleComponent.KeyTrack(new float[]{0, 0, 1}, new float[]{0, 0.05f, 0.06f}), false),
                    });
                }
                prevRightHandPos = rightHandPos;
            }
        }
    }

    public void addPierceParticle(){
        if (world.isRemote && getAnimation() == PIERCE) {
            Vector3d rightHandPos = clientVectors[0];
            if (getAnimationTick() > 1 && getAnimationTick() < 23){
                AdvancedParticleBase.spawnParticle(world, ParticleHandler.ARROW_HEAD.get(), prevRightHandPos.x, prevRightHandPos.y, prevRightHandPos.z, 0, 0, 0, false, 0, 0, 0, 0, 5f, 0, 0, 0, 0.75, 1, 4, true, false, new ParticleComponent[]{
                        new ParticleComponent.Attractor(new Vector3d[]{new Vector3d(rightHandPos.x,rightHandPos.y,rightHandPos.z)}, 0.5f, 0.2f, ParticleComponent.Attractor.EnumAttractorBehavior.LINEAR),
                        new RibbonComponent(ParticleHandler.RIBBON_STREAKS.get(), 10, 0, 0, 0, 0.8F, 1, 1, 1, 0.75, true, true, new ParticleComponent[]{
                                new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(1, 0))
                        }),
                        new ParticleComponent.FaceMotion(),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new ParticleComponent.KeyTrack(new float[]{0, 0, 1}, new float[]{0, 0.05f, 0.06f}), false),
                });
            }
            prevRightHandPos = rightHandPos;
        }
    }

    private void addRuinHeavyStompFX(){
        if(world.isRemote&&getAnimation()==RUIN_HEAVY_STOMP) {
            int tick = getAnimationTick();
            if (tick == 30||tick==55||tick==100||tick==140){
                for(int i=0;i<10;i++) {
                    double vx = Math.sin(Math.PI*i/5);
                    double vz = Math.cos(Math.PI*i/5);
                    Vector3d vector3Rand = new Vector3d(vx, 0, vz).scale(rand.nextDouble()*10).add(0,1,0);
                    world.addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD.get(), 1, 0.2f, 0.2f, (float) (30d + rand.nextDouble() * 30d), 200, ParticleCloud.EnumCloudBehavior.CONSTANT, 1f), getPosX() + vector3Rand.getX(), getPosY() + vector3Rand.getY(), getPosZ() + vector3Rand.getZ(), 0, rand.nextDouble(), 0);
                }
            }
            if(tick==100){
                Vector3d[] vector3ds = new Vector3d[]{new Vector3d(10,0,0),new Vector3d(10,5,15),new Vector3d(0,8,0)};
                for(int i = 0;i<4;i++){
                    float yaw = (float) Math.PI*i/2;
                    world.addParticle(new WindParticle.WindParticleData(vector3ds[0].rotateYaw(yaw).add(getPositionVec()),vector3ds[1].rotateYaw(yaw).add(getPositionVec()),vector3ds[2].add(getPositionVec()))
                            ,getPosX(),getPosY(),getPosZ(),0,0,0);
                }
                world.addParticle(ParticleHandler.VOID_BEING_CLOUD.get(), getPosX(),getPosY()+8,getPosZ(),0,0,0);
            }
        }
    }

    public void doWeaponOnGround(){
        double x = clientVectors[0].x;
        double z = clientVectors[0].z;
        double y = getBoundingBox().minY;
        double hitY = y-0.2;
        for (float[] attackBlockOffset : ATTACK_BLOCK_OFFSETS) {
            float ox = attackBlockOffset[0], oy = attackBlockOffset[1];
            int hitX = MathHelper.floor(x + ox);
            int hitZ = MathHelper.floor(z + oy);
            BlockPos hit = new BlockPos(hitX, hitY, hitZ);
            BlockState block = world.getBlockState(hit);
            for (int n = 0; n < 6; n++) {
                double pa = rand.nextDouble() * 2 * Math.PI;
                double pd = rand.nextDouble() * 0.6 + 0.1;
                double px = x + Math.cos(pa) * pd;
                double pz = z + Math.sin(pa) * pd;
                double velY = rand.nextDouble() * 3 + 12;
                world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, block), px, y, pz, 0, velY, 0);
            }
            if(rand.nextBoolean()){
                world.playSound(null, hit, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
        }

    }

    public void addMessengerMember(Messenger messenger) {
        messengers.add(messenger);
    }

    public void removeMessengerMember(Messenger messenger) {}

    public List<Messenger> getMessengerMember() {
        return messengers;
    }

    public void messengerAttackModel() {
        if(!messengers.isEmpty()) {
            int n = messengers.size();
            double r = this.rotationYaw<0?(this.rotationYaw + 360)+90+180.0f/(n+1):this.rotationYaw+90+180f/(n+1);
            for(int n1 = 0; n1 < n; n1++) {
                Messenger member = messengers.get(n1);
                if(member.getAttackTarget() == null) {
                    double r1 = (r / 180) * Math.PI;
                    member.getNavigator().tryMoveToXYZ(getPosX() - 6 * Math.sin(r1), getPosY(), getPosZ() + 6 * Math.cos(r1), 1.5);
                    r += 180.0f / n;
                }
            }
        }
    }

    public void startEnhanced(int time){
        if(time <= 30) return;
        this.timeSinceEnhanced = time;
        setIsEnhanced(true);
        AnimationHandler.INSTANCE.sendAnimationMessage(this, ENHANCED_1);
    }

    @OnlyIn(Dist.CLIENT)
    public void setClientVectors(int index, Vector3d pos) {
        if (clientVectors != null && clientVectors.length > index) {
            clientVectors[index] = pos;
        }
    }

    public Vector3f getTargetPos() {
        float x = getDataManager().get(TARGET_POSX);
        float y = getDataManager().get(TARGET_POSY);
        float z = getDataManager().get(TARGET_POSZ);
        return new Vector3f(x,y,z);
    }

    public void setTargetPos(Vector3f vector3f) {
        getDataManager().set(TARGET_POSX, vector3f.getX());
        getDataManager().set(TARGET_POSY, vector3f.getY());
        getDataManager().set(TARGET_POSZ, vector3f.getZ());
    }

    public Boolean getIsRunning() {
        return this.dataManager.get(IS_RUNNING);
    }

    public void setIsRunning(boolean isRunning) {
        if(getIsRunning() == isRunning) return;
        if(!world.isRemote) {
            ModifiableAttributeInstance speedAttribute = getAttribute(Attributes.MOVEMENT_SPEED);
            if (speedAttribute != null) {
                if (isRunning) {
                    speedAttribute.applyNonPersistentModifier(new AttributeModifier("speed", 0.2F, AttributeModifier.Operation.ADDITION));
                } else {
                    speedAttribute.applyNonPersistentModifier(new AttributeModifier("speed", -0.2F, AttributeModifier.Operation.ADDITION));
                }
            }
        }
        this.dataManager.set(IS_RUNNING, isRunning);
    }
    public Boolean getIsHandShield() {
        return this.dataManager.get(IS_HAND_SHIELD);
    }

    public void setIsHandShield(boolean isHandShield) {
        this.dataManager.set(IS_HAND_SHIELD, isHandShield);
    }
    public Boolean getIsEnhanced() {
        return this.dataManager.get(IS_ENHANCED);
    }
    public void setIsEnhanced(boolean isEnhanced) {
        this.dataManager.set(IS_ENHANCED, isEnhanced);
    }
    public int getPredicate() {
        return this.dataManager.get(PREDICATE);
    }
    public void setPredicateEffect(int predicate){
        if(predicate == getPredicate()) return;
        ModifiableAttributeInstance attack = getAttribute(Attributes.ATTACK_DAMAGE);
        if (attack != null) {
            if (predicate == 1) {
                enhancedTime = 0;
                attack.applyNonPersistentModifier(new AttributeModifier("attack_damage", -2.0F, AttributeModifier.Operation.MULTIPLY_BASE));
            } else {
                attack.applyNonPersistentModifier(new AttributeModifier("attack_damage", 2.0F, AttributeModifier.Operation.MULTIPLY_BASE));
            }
        }
        setPredicate(predicate);
    }
    public void setPredicate(int predicate) {
        this.dataManager.set(PREDICATE, predicate);
    }

    public enum AttackMode{
        PLAYER,ENTITY,NONE;
    }

    class ArmorCounter{
        private double x;
        private double y;
        private final int maxTime;
        private int time;
        
        public ArmorCounter(double x,double y,int maxTime,int time){
            this.x = x;
            this.y = y;
            this.time = time;
            this.maxTime = maxTime;
        }

        public void update(){
            time++;
            if(time >= maxTime)
                armorCounters.remove(this);
        }

        public double x(){
            return x;
        }

        public double y(){
            return y;
        }
    }

}
