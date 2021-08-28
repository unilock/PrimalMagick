package com.verdantartifice.primalmagic.common.entities.misc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.util.EntityUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

/**
 * Definition of a sin crystal entity.  Created by an inner demon to heal it, similar to an ender crystal.
 * 
 * @author Daedalus4096
 */
public class SinCrystalEntity extends Entity {
    private static final EntityDataAccessor<Optional<BlockPos>> BEAM_TARGET = SynchedEntityData.defineId(SinCrystalEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    private static final EntityDataAccessor<Optional<UUID>> DAMAGE_CLOUD = SynchedEntityData.defineId(SinCrystalEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    
    public int innerRotation;

    public SinCrystalEntity(EntityType<? extends SinCrystalEntity> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.blocksBuilding = true;
        this.innerRotation = this.random.nextInt(100000);
    }
    
    public SinCrystalEntity(Level worldIn, double x, double y, double z) {
        this(EntityTypesPM.SIN_CRYSTAL.get(), worldIn);
        this.setPos(x, y, z);
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(BEAM_TARGET, Optional.empty());
        this.getEntityData().define(DAMAGE_CLOUD, Optional.empty());
    }

    @Override
    public void tick() {
        super.tick();
        this.innerRotation++;
        
        // Create or extend damage cloud
        if (!this.level.isClientSide && this.level instanceof ServerLevel) {
            UUID cloudId = this.getDamageCloud();
            if (cloudId == null) {
                AreaEffectCloud cloud = new AreaEffectCloud(this.level, this.getX(), this.getY(), this.getZ());
                cloud.setParticle(ParticleTypes.DRAGON_BREATH);
                cloud.setRadius(3.0F);
                cloud.setDuration(5);
                cloud.setWaitTime(0);
                cloud.addEffect(new MobEffectInstance(MobEffects.HARM, 1, 1));
                this.level.addFreshEntity(cloud);
                this.setDamageCloud(cloud.getUUID());
            } else {
                ServerLevel serverWorld = (ServerLevel)this.level;
                Entity entity = serverWorld.getEntity(cloudId);
                if (entity instanceof AreaEffectCloud) {
                    AreaEffectCloud cloud = (AreaEffectCloud)entity;
                    cloud.setDuration(1 + cloud.getDuration());     // Extend the cloud's duration by a tick so that it lives as long as the crystal
                } else {
                    this.setDamageCloud(null);
                }
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("BeamTarget", Constants.NBT.TAG_COMPOUND)) {
            this.setBeamTarget(NbtUtils.readBlockPos(compound.getCompound("BeamTarget")));
        }
        if (compound.contains("DamageCloudUUID", Constants.NBT.TAG_COMPOUND)) {
            this.setDamageCloud(compound.getUUID("DamageCloudUUID"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        if (this.getBeamTarget() != null) {
            compound.put("BeamTarget", NbtUtils.writeBlockPos(this.getBeamTarget()));
        }
        if (this.getDamageCloud() != null) {
            compound.putUUID("DamageCloudUUID", this.getDamageCloud());
        }
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (source.getEntity() instanceof InnerDemonEntity) {
            return false;
        } else {
            if (this.isAlive() && !this.level.isClientSide) {
                // Cause backlash to any inner demons being healed by this crystal
                List<InnerDemonEntity> demonsInRange = EntityUtils.getEntitiesInRange(this.level, this.position(), null, InnerDemonEntity.class, InnerDemonEntity.HEAL_RANGE);
                if (!demonsInRange.isEmpty()) {
                    LivingEntity trueSource = source.getEntity() instanceof LivingEntity ? (LivingEntity)source.getEntity() : null;
                    for (InnerDemonEntity demon : demonsInRange) {
                        demon.hurt(DamageSource.explosion(trueSource), 10.0F);
                    }
                }
                
                // Detonate when attacked
                this.discard();
                if (!source.isExplosion()) {
                    this.level.explode(null, this.getX(), this.getY(), this.getZ(), 4.0F, Explosion.BlockInteraction.DESTROY);
                }
            }
            return true;
        }
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void setBeamTarget(@Nullable BlockPos beamTarget) {
        this.getEntityData().set(BEAM_TARGET, Optional.ofNullable(beamTarget));
    }

    @Nullable
    public BlockPos getBeamTarget() {
        return this.getEntityData().get(BEAM_TARGET).orElse((BlockPos)null);
    }
    
    public void setDamageCloud(@Nullable UUID cloudId) {
        this.getEntityData().set(DAMAGE_CLOUD, Optional.ofNullable(cloudId));
    }
    
    @Nullable
    public UUID getDamageCloud() {
        return this.getEntityData().get(DAMAGE_CLOUD).orElse(null);
    }
    
    /**
     * Checks if the entity is in range to render.
     */
    public boolean shouldRenderAtSqrDistance(double distance) {
        return super.shouldRenderAtSqrDistance(distance) || this.getBeamTarget() != null;
    }
}
