package org.goldgom.dontbetohigh.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Mixin(LivingEntity.class)
public abstract class AirSupply {
    @Unique

    private static final Logger LOGGER = LoggerFactory.getLogger("Dontbetohigh");

    /**
     * @author 
     * @reason 完全替换原版空气补充逻辑为自定义逻辑
     */
    @Overwrite
    protected int increaseAirSupply(int currentAir) {
        return currentAir;
    }
}