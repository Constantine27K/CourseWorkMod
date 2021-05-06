package me.constantine.courseworkmod.entity;

import me.constantine.courseworkmod.CourseWorkMod;
import me.constantine.courseworkmod.ai.AttackGoal;
import me.constantine.courseworkmod.ai.DefendGoal;
import me.constantine.courseworkmod.ai.PetGoal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.NPCMerchant;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.merchant.IMerchant;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.MerchantInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffer;
import net.minecraft.item.MerchantOffers;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class Mob extends ZombieEntity implements IMerchant {
    protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID =
            EntityDataManager.createKey(ZombieEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private MerchantInventory merchantInventory;
    private PlayerEntity customer;
    private MerchantOffers offers = new MerchantOffers();
    private int field_213710_d;
    public boolean goalsPresent;
    public boolean standBy;
    private int xp;

    @SuppressWarnings("unchecked")
    public Mob(EntityType<? extends ZombieEntity> type, World worldIn) {
        super((EntityType<? extends ZombieEntity>) Entities.CUSTOM_ENTITY, worldIn);
        this.goalsPresent = true;
        this.standBy = true;
        this.setSilent(true);
        this.setCustomName(new StringTextComponent("Mob"));
        this.setChild(false);
        this.customer = CourseWorkMod.PLAYER;
        this.merchantInventory = new MerchantInventory(this);
        if (CourseWorkMod.PLAYER != null) this.setOwnerId(CourseWorkMod.PLAYER.getUniqueID());
        CourseWorkMod.MOB = this;
    }

    private Mob(World world) {
        super(EntityType.HUSK, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new PetGoal(this, 0.5d, 5.0f, 20.0f));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 0.5d, false));
        this.goalSelector.addGoal(2, new AttackGoal<>(this, PigEntity.class, false));
        this.goalSelector.addGoal(2, new DefendGoal<>(this, PlayerEntity.class, false));
        this.goalSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, MonsterEntity.class, false));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, MonsterEntity.class, false));
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        boolean canAttackFrom = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this),
                ((int) this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue()));
        if (canAttackFrom) {
            this.applyEnchantments(this, entityIn);
        }
        return canAttackFrom;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0d);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.2d);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(OWNER_UNIQUE_ID, Optional.empty());
    }

    @Nullable
    public UUID getOwnerId() {
        return this.dataManager.get(OWNER_UNIQUE_ID).orElse((UUID) null);
    }

    public void setOwnerId(@Nullable UUID uuid) {
        this.dataManager.set(OWNER_UNIQUE_ID, Optional.ofNullable(uuid));
    }

    @Nullable
    public ServerPlayerEntity getOwner() {
        try {
            UUID uuid = this.getOwnerId();
            return uuid == null ? null : (ServerPlayerEntity) this.world.getPlayerByUuid(uuid);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    public double getDistanceSq(Entity entityIn) {
        return this.getDistanceSq(entityIn.getPositionVec());
    }

    public double getDistanceSq(Vec3d vec) {
        double d0 = this.posX - vec.x;
        double d1 = this.posY - vec.y;
        double d2 = this.posZ - vec.z;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    @Override
    public void setFire(int seconds) {

    }

    @Override
    public boolean canRenderOnFire() {
        return false;
    }

    @Nullable
    public PlayerEntity getCustomer() {
        return this.customer;
    }

    public void setCustomer(@Nullable PlayerEntity player) {
    }

    public MerchantOffers getOffers() {
        return this.offers;
    }

    @OnlyIn(Dist.CLIENT)
    public void setClientSideOffers(@Nullable MerchantOffers offers) {
        this.offers = offers;
    }

    public void onTrade(MerchantOffer offer) {
        offer.increaseUses();
    }


    public void verifySellingItem(ItemStack stack) {
    }

    public World getWorld() {
        return this.customer.world;
    }

    public int getXp() {
        return this.xp;
    }

    public void setXP(int xpIn) {
        this.xp = xpIn;
    }

    public boolean func_213705_dZ() {
        return true;
    }

    public SoundEvent getYesSound() {
        return SoundEvents.ENTITY_VILLAGER_YES;
    }
}
