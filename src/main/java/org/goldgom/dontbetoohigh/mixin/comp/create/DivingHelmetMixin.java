package org.goldgom.dontbetoohigh.mixin.comp.create;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "com.simibubi.create.content.equipment.armor.DivingHelmetItem", remap = false)
public abstract class DivingHelmetMixin {
    @Redirect(
            method = "breatheUnderwater",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;setAirSupply(I)V"
            )
    )
    private static void redirectSetAirSupply(LivingEntity entity, int airSupply) {
        // Do nothing to prevent setting air supply
    }

    /**
     * Redirects the call to addEffect to prevent it from being executed.
     */
    @Redirect(
            method = "breatheUnderwater",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z"
            )
    )
    private static boolean redirectAddEffect(LivingEntity entity, MobEffectInstance effectInstance) {
        // Do nothing to prevent adding the water breathing effect
        return false;
    }


}