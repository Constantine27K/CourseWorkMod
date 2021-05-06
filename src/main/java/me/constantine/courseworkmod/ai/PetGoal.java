package me.constantine.courseworkmod.ai;

import me.constantine.courseworkmod.CourseWorkMod;
import me.constantine.courseworkmod.entity.Mob;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.EnumSet;

public class PetGoal extends Goal {
    private Mob pet;
    private PlayerEntity owner;
    private PlayerEntity entity;
    private World world;

    private float minDistance;
    private float maxDistance;
    private double speed;

    private PathNavigator navigator;
    private int timeToRecalcPath;
    private float oldWaterCost;

    public PetGoal(Mob pet, double speed, float minDistance, float maxDistance) {
        this.pet = pet;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.speed = speed;
        this.navigator = pet.getNavigator();
        this.world = pet.world;
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (!CourseWorkMod.MOB.standBy)
            return false;
        entity = this.pet.getOwner();
        if (entity == null) return false;
        if (entity.isSpectator()) return false;
        if (pet.getDistanceSq(entity) < (double) (minDistance * minDistance)) return false;
        else {
            owner = entity;
            return true;
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (!CourseWorkMod.MOB.standBy)
            return false;
        return !this.navigator.noPath() && this.pet.getDistanceSq(owner) >
                (double) (maxDistance * maxDistance);
    }

    @Override
    public void startExecuting() {
        if (!CourseWorkMod.MOB.standBy)
            return;
        this.timeToRecalcPath = 0;
        this.oldWaterCost = pet.getPathPriority(PathNodeType.WATER);
        this.pet.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    @Override
    public void resetTask() {
        this.owner = null;
        this.navigator.clearPath();
        this.pet.setPathPriority(PathNodeType.WATER, oldWaterCost);
    }

    @Override
    public void tick() {
        this.pet.getLookController().setLookPositionWithEntity(this.owner, 10.0F,
                (float) this.pet.getVerticalFaceSpeed());
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            if (!this.navigator.tryMoveToEntityLiving(this.owner, this.speed)) {
                if (!this.pet.getLeashed() && !this.pet.isPassenger()) {
                    if (!(this.pet.getDistanceSq(this.owner) < 144.0D)) {
                        int i = MathHelper.floor(this.owner.posX) - 2;
                        int j = MathHelper.floor(this.owner.posZ) - 2;
                        int k = MathHelper.floor(this.owner.getBoundingBox().minY);

                        for (int l = 0; l <= 4; ++l) {
                            for (int i1 = 0; i1 <= 4; ++i1) {
                                if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) &&
                                        this.canTeleportToBlock(new BlockPos(i + l, k - 1, j + i1))) {
                                    this.pet.setLocationAndAngles((double) ((float) (i + l) + 0.5F), (double) k,
                                            (double) ((float) (j + i1) + 0.5F), this.pet.rotationYaw,
                                            this.pet.rotationPitch);
                                    this.navigator.clearPath();
                                    return;
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    private boolean canTeleportToBlock(BlockPos pos) {
        BlockState blockstate = this.world.getBlockState(pos);
        return blockstate.canEntitySpawn(this.world, pos, this.pet.getType()) && this.world.isAirBlock(pos.up()) && this.world.isAirBlock(pos.up(2));
    }
}

