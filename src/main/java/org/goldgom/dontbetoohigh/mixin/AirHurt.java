package org.goldgom.dontbetoohigh.mixin;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ForgeHooks.class)
public abstract class AirHurt {

    @Redirect(
        method = "onLivingBreathe",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"
        )
    )
    private static void redirectAddParticle(Level level, ParticleOptions particle, double x, double y, double z, double dx, double dy, double dz, LivingEntity entity) {
        // 自定义粒子效果
        if(entity.isUnderWater())
        {
            Vec3 vec3 = entity.getDeltaMovement(); // 获取实体的移动向量
            for (int i = 0; i < 10; ++i) { // 添加多个粒子
                double offsetX = entity.getRandom().nextDouble() * 0.5 - 0.25;
                double offsetY = entity.getRandom().nextDouble() * 0.5 - 0.25;
                double offsetZ = entity.getRandom().nextDouble() * 0.5 - 0.25;
                level.addParticle(ParticleTypes.BUBBLE, x + offsetX, y + offsetY, z + offsetZ, vec3.x * 0.1, vec3.y * 0.1, vec3.z * 0.1);
            }
        }

    }
}