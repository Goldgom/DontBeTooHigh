package org.goldgom.dontbetohigh.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.goldgom.dontbetohigh.utils.IEntityWalkDetect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class) // Target the correct class
public abstract class EntityWalkDetect implements IEntityWalkDetect {

    @Shadow
    public abstract Vec3 getDeltaMovement(); // Shadow the method from the target class

    @Shadow
    public abstract LivingEntity getControllingPassenger(); // Shadow the method from the target class

    @Shadow
    public abstract boolean isAlive(); // Shadow the method from the target class

    public float lastwalkDist;
    @Shadow public float walkDist;
    @Unique
    private float lastDist = 0; // 是否正在走
    @Unique
    @Override
    public boolean isWalking()
    {
        boolean r = lastwalkDist - this.walkDist != 0;
        //this.setLastDist();
        return r;
    }

    @Unique
    @Override
    public void setLastDist(){
        this.lastwalkDist = this.walkDist;
    }
}