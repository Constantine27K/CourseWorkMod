package me.constantine.courseworkmod.ai;

import me.constantine.courseworkmod.CourseWorkMod;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Objects;
import java.util.function.Predicate;

public class DefendGoal<T extends LivingEntity> extends TargetGoal {
    protected final Class<T> targetClass;
    protected final int targetChance;
    protected LivingEntity nearestTarget;
    private int targetUnseenTicks;

    protected EntityPredicate targetEntitySelector;

    public DefendGoal(MobEntity p_i50313_1_, Class<T> p_i50313_2_, boolean p_i50313_3_) {
        this(p_i50313_1_, p_i50313_2_, p_i50313_3_, false);
    }

    public DefendGoal(MobEntity p_i50314_1_, Class<T> p_i50314_2_, boolean p_i50314_3_, boolean p_i50314_4_) {
        this(p_i50314_1_, p_i50314_2_, 10, p_i50314_3_, p_i50314_4_, (Predicate<LivingEntity>) null);
    }

    public DefendGoal(MobEntity p_i50315_1_, Class<T> p_i50315_2_, int p_i50315_3_, boolean p_i50315_4_, boolean p_i50315_5_, @Nullable Predicate<LivingEntity> p_i50315_6_) {
        super(p_i50315_1_, p_i50315_4_, p_i50315_5_);
        this.targetClass = p_i50315_2_;
        this.targetChance = p_i50315_3_;
        this.setMutexFlags(EnumSet.of(Goal.Flag.TARGET));
        this.targetEntitySelector = (new EntityPredicate()).setDistance(this.getTargetDistance()).setCustomPredicate(p_i50315_6_);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (this.targetChance > 0 && this.goalOwner.getRNG().nextInt(this.targetChance) != 0) {
            return false;
        } else {
            this.findNearestTarget();
            return this.nearestTarget != null;
        }
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.goalOwner.getBoundingBox().grow(targetDistance, 4.0D, targetDistance);
    }

    protected void findNearestTarget() {
        if (CourseWorkMod.MOB != null) {
            if (!CourseWorkMod.MOB.goalsPresent) {
                this.nearestTarget = null;
                return;
            }
        }
        LivingEntity target = this.goalOwner.world.getClosestPlayer(this.targetEntitySelector, this.goalOwner,
                this.goalOwner.posX, this.goalOwner.posY + (double) this.goalOwner.getEyeHeight(),
                this.goalOwner.posZ);
        if (target != null && CourseWorkMod.PLAYER != null) {
            if (Objects.requireNonNull(CourseWorkMod.PLAYER.getLastDamageSource()).isCreativePlayer())
                this.nearestTarget = target;
            if (Objects.requireNonNull(CourseWorkMod.MOB.getLastDamageSource()).isCreativePlayer())
                this.nearestTarget = target;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.goalOwner.setAttackTarget(this.nearestTarget);
        super.startExecuting();
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (!CourseWorkMod.MOB.goalsPresent) return false;
        LivingEntity livingentity = this.goalOwner.getAttackTarget();
        if (livingentity == null) {
            livingentity = this.target;
        }

        if (livingentity == null) {
            return false;
        } else if (!livingentity.isAlive()) {
            return false;
        } else {
            Team team = this.goalOwner.getTeam();
            Team team1 = livingentity.getTeam();
            if (team != null && team1 == team) {
                return false;
            } else {
                double d0 = this.getTargetDistance();
                if (this.goalOwner.getDistanceSq(livingentity) > d0 * d0) {
                    return false;
                } else {
                    if (this.shouldCheckSight) {
                        if (this.goalOwner.getEntitySenses().canSee(livingentity)) {
                            this.targetUnseenTicks = 0;
                        } else if (++this.targetUnseenTicks > this.unseenMemoryTicks) {
                            return false;
                        }
                    }

                    if (livingentity instanceof PlayerEntity && ((PlayerEntity) livingentity).abilities.disableDamage) {
                        return false;
                    } else {
                        this.goalOwner.setAttackTarget(livingentity);
                        return true;
                    }
                }
            }
        }
    }
}