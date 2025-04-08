package org.goldgom.dontbetoohigh.world;

import com.simibubi.create.content.equipment.armor.BacktankUtil;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.fml.ModList;
import org.goldgom.dontbetoohigh.data.DimensionConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.simibubi.create.content.equipment.armor.DivingHelmetItem.getWornItem;

public class Air {

    private static final Logger LOGGER = LoggerFactory.getLogger("dontbetoohigh");

  /**
     * 根据生物高度计算空气浓度
     * @param entity 生物实例
     * @return 空气浓度（0.0 - 1.0）
     */
    public static float calculateAirConcentration(LivingEntity entity) {

        
        if(Air.isOxygenHelmetActive(entity))
            return 1.0f;

        if(entity.isUnderWater()){
            int i = EnchantmentHelper.getRespiration(entity);
            return (float)(Math.pow(2,-1.0/i));

        }
        // 获取生物当前高度
        float height = (float) entity.getY();

        // 获取当前生物所处维度的配置
        DimensionConfigManager.DimensionConfig config = DimensionConfigManager.getDimensionConfig(entity.level().dimension());
        if (config == null) {
            return 1.0f; // 默认空气浓度
        }

        // 根据高度计算空气浓度
        Float airConcentration = 1.0f;
        if (config.hasTroposphere && height < config.troposphereHeight[1] && height >= config.troposphereHeight[0]) {
            // 对流层
            airConcentration = interpolateDensity(height, config.troposphereHeight, config.troposphereAirDensity);
        } else if (config.hasStratosphere && height < config.stratosphereHeight[1] && height >= config.stratosphereHeight[0]) {
            // 平流层
            airConcentration = interpolateDensity(height, config.stratosphereHeight, config.stratosphereAirDensity);
        } else if (config.hasMesosphere && height < config.mesosphereHeight[1] && height >= config.mesosphereHeight[0]) {
            // 中间层
            airConcentration = interpolateDensity(height, config.mesosphereHeight, config.mesosphereAirDensity);
        } else if (config.hasThermosphere && height < config.thermosphereHeight[1] && height >= config.thermosphereHeight[0]) {
            // 热层
            airConcentration = interpolateDensity(height, config.thermosphereHeight, config.thermosphereAirDensity);
        } else if (config.hasExosphere && height < config.exosphereHeight[1] && height >= config.exosphereHeight[0]) {
            // 外层空间
            airConcentration = interpolateDensity(height, config.exosphereHeight, config.exosphereAirDensity);
        }else if (config.hasExosphere && height >= config.exosphereHeight[1]) {
            // 外层空间
            airConcentration = 0.0F; // 外层空间空气浓度为0
        }
        if(entity.level().isRaining())
        {
            airConcentration *= 0.98f;
        }
      //  LOGGER.warn("Unknown height: {} in dimension: {}", height, entity.level().dimension().location());
        return airConcentration; // 默认空气浓度
    }
    /**
     * 根据高度插值计算空气浓度
     * @param height 当前高度
     * @param layerHeight 大气层高度范围
     * @param airDensity 大气层空气密度范围
     * @return 插值后的空气浓度
     */
    private static float interpolateDensity(float height, int[] layerHeight, float[] airDensity) {
        float ratio = (height - layerHeight[0]) / (layerHeight[1] - layerHeight[0]);
        return airDensity[0] + ratio * (airDensity[1] - airDensity[0]);
    }

    public static boolean isOxygenHelmetActive(LivingEntity entity) {
        if(ModList.get().isLoaded("create"))
        {
            ItemStack helmet = getWornItem(entity);
            if (helmet.isEmpty())
                return false;
            List<ItemStack> backtanks = BacktankUtil.getAllWithAir(entity);
            if (backtanks.isEmpty())
                return false;
            if (entity.isInLava()) {
                if (entity instanceof ServerPlayer sp)
                    AllAdvancements.DIVING_SUIT_LAVA.awardTo(sp);
                if (backtanks.stream()
                        .noneMatch(backtank -> backtank.getItem()
                                .isFireResistant()))
                    return false;
            }
        }

            return true;

    }
}