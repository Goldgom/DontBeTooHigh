package org.goldgom.dontbetoohigh.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
public abstract class AirSupplyUnderWater {


    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("Dontbetoohigh");

    /**
     * @author 
     * @reason 完全替换原版水下呼吸逻辑
     */
    @Overwrite
    protected int decreaseAirSupply(int currentAir) {

//        LivingEntity entity = (LivingEntity) (Object) this;
//
//        if(entity.isUnderWater()){
//            int i = EnchantmentHelper.getRespiration(entity);
//            return i > 0 && this.random.nextInt(i + 1) > 0 ? currentAir : currentAir - 1;
//        }
        return currentAir;
    }
}