package org.goldgom.dontbetohigh.world;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.goldgom.dontbetohigh.data.DimensionConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Air {

    private static final Logger LOGGER = LoggerFactory.getLogger("Dontbetohigh");

  /**
     * 根据生物高度计算空气浓度
     * @param entity 生物实例
     * @return 空气浓度（0.0 - 1.0）
     */
    public static float calculateAirConcentration(LivingEntity entity) {

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

    /**
     * 判断玩家是否窒息
     * @param airConcentration 当前空气浓度
     * @return 是否窒息
     */
    public static boolean isSuffocating(float airConcentration) {
        return airConcentration < 0.8f; // 空气浓度低于0.3时，玩家窒息
    }
}