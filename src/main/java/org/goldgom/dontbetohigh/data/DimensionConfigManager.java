package org.goldgom.dontbetohigh.data;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class DimensionConfigManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("Dontbetohigh");

    private static final Map<ResourceKey<Level>, DimensionConfig> dimensionConfigs = new HashMap<>();

    public static void addDimensionConfig(ResourceKey<Level> dimensionKey, DimensionConfig config) {
        dimensionConfigs.put(dimensionKey, config);
    }

    public static DimensionConfig getDimensionConfig(ResourceKey<Level> dimensionKey) {
        return dimensionConfigs.get(dimensionKey);
    }

    public static class DimensionConfig {
        public final float temperature;
        public final boolean hasTroposphere;
        public final int[] troposphereHeight;
        public final float[] troposphereAirDensity;
        public final boolean hasStratosphere;
        public final int[] stratosphereHeight;
        public final float[] stratosphereAirDensity;
        public final boolean hasMesosphere;
        public final int[] mesosphereHeight;
        public final float[] mesosphereAirDensity;
        public final boolean hasThermosphere;
        public final int[] thermosphereHeight;
        public final float[] thermosphereAirDensity;
        public final boolean hasExosphere;
        public final int[] exosphereHeight;
        public final float[] exosphereAirDensity;

        public DimensionConfig(float temperature,
                               boolean hasTroposphere, int[] troposphereHeight, float[] troposphereAirDensity,
                               boolean hasStratosphere, int[] stratosphereHeight, float[] stratosphereAirDensity,
                               boolean hasMesosphere, int[] mesosphereHeight, float[] mesosphereAirDensity,
                               boolean hasThermosphere, int[] thermosphereHeight, float[] thermosphereAirDensity,
                               boolean hasExosphere, int[] exosphereHeight, float[] exosphereAirDensity) {
            this.temperature = temperature;
            this.hasTroposphere = hasTroposphere;
            this.troposphereHeight = troposphereHeight;
            this.troposphereAirDensity = troposphereAirDensity;
            this.hasStratosphere = hasStratosphere;
            this.stratosphereHeight = stratosphereHeight;
            this.stratosphereAirDensity = stratosphereAirDensity;
            this.hasMesosphere = hasMesosphere;
            this.mesosphereHeight = mesosphereHeight;
            this.mesosphereAirDensity = mesosphereAirDensity;
            this.hasThermosphere = hasThermosphere;
            this.thermosphereHeight = thermosphereHeight;
            this.thermosphereAirDensity = thermosphereAirDensity;
            this.hasExosphere = hasExosphere;
            this.exosphereHeight = exosphereHeight;
            this.exosphereAirDensity = exosphereAirDensity;
        }
    }
}