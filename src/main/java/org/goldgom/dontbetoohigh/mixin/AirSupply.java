package org.goldgom.dontbetoohigh.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
@Mixin(LivingEntity.class)
public abstract class AirSupply {
    @Unique

    private static final Logger LOGGER = LoggerFactory.getLogger("Dontbetoohigh");

    /**
     * @author 
     * @reason 完全替换原版空气补充逻辑为自定义逻辑
     */
    @Overwrite
    protected int increaseAirSupply(int currentAir) {
        return currentAir;
    }
}