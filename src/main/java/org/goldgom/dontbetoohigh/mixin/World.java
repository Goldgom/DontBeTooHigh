package org.goldgom.dontbetoohigh.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.goldgom.dontbetoohigh.Dontbetoohigh;
import org.goldgom.dontbetoohigh.data.DimensionConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Mixin(Level.class)
public class World {
    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("Dontbetoohigh");
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {

        Level level = (Level) (Object) this;
        ResourceKey<Level> dimensionKey = level.dimension();
        String dimensionName = dimensionKey.location().getPath();
        LOGGER.info("Initializing dimension: {}", dimensionName);

        // 尝试从 /data/minecraft/dimension_type/<dimensionName>.json 加载配置
        try (InputStreamReader reader = new InputStreamReader(
                Objects.requireNonNull(Dontbetoohigh.class.getResourceAsStream("/data/dontbetoohigh/dimension_environment/" + dimensionName + ".json")),
                StandardCharsets.UTF_8)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            LOGGER.info("Loaded dimension config for {}: {}", dimensionName, json);
            float temperature = json.has("temperature") ? json.get("temperature").getAsFloat() : 20.0f;

            boolean hasTroposphere = json.has("has_troposphere") && json.get("has_troposphere").getAsBoolean();
            int[] troposphereHeight = json.has("troposphere_height")
                    ? parseHeight(json.getAsJsonArray("troposphere_height")) : new int[]{0, 300};
            float[] troposphereAirDensity = json.has("troposphere_air_density")
                    ? parseDensity(json.getAsJsonArray("troposphere_air_density")) : new float[]{1.0f, 0.6f};

            boolean hasStratosphere = json.has("has_stratosphere") && json.get("has_stratosphere").getAsBoolean();
            int[] stratosphereHeight = json.has("stratosphere_height")
                    ? parseHeight(json.getAsJsonArray("stratosphere_height")) : new int[]{300, 480};
            float[] stratosphereAirDensity = json.has("stratosphere_air_density")
                    ? parseDensity(json.getAsJsonArray("stratosphere_air_density")) : new float[]{0.6f, 0.2f};

            boolean hasMesosphere = json.has("has_mesosphere") && json.get("has_mesosphere").getAsBoolean();
            int[] mesosphereHeight = json.has("mesosphere_height")
                    ? parseHeight(json.getAsJsonArray("mesosphere_height")) : new int[]{480, 1000};
            float[] mesosphereAirDensity = json.has("mesosphere_air_density")
                    ? parseDensity(json.getAsJsonArray("mesosphere_air_density")) : new float[]{0.2f, 0.05f};

            boolean hasThermosphere = json.has("has_thermosphere") && json.get("has_thermosphere").getAsBoolean();
            int[] thermosphereHeight = json.has("thermosphere_height")
                    ? parseHeight(json.getAsJsonArray("thermosphere_height")) : new int[]{1000, 2000};
            float[] thermosphereAirDensity = json.has("thermosphere_air_density")
                    ? parseDensity(json.getAsJsonArray("thermosphere_air_density")) : new float[]{0.05f, 0.01f};

            boolean hasExosphere = json.has("has_exosphere") && json.get("has_exosphere").getAsBoolean();
            int[] exosphereHeight = json.has("exosphere_height")
                    ? parseHeight(json.getAsJsonArray("exosphere_height")) : new int[]{2000, 5000};
            float[] exosphereAirDensity = json.has("exosphere_air_density")
                    ? parseDensity(json.getAsJsonArray("exosphere_air_density")) : new float[]{0.01f, 0.001f};

            // 将当前维度的配置存储到 DimensionConfigManager 中
            DimensionConfigManager.addDimensionConfig(dimensionKey, new DimensionConfigManager.DimensionConfig(
                    temperature,
                    hasTroposphere, troposphereHeight, troposphereAirDensity,
                    hasStratosphere, stratosphereHeight, stratosphereAirDensity,
                    hasMesosphere, mesosphereHeight, mesosphereAirDensity,
                    hasThermosphere, thermosphereHeight, thermosphereAirDensity,
                    hasExosphere, exosphereHeight, exosphereAirDensity
            ));

        } catch (Exception e) {
            LOGGER.error("Failed to load dimension config for {}: {}", dimensionName, e.getMessage());
            // 如果没有找到配置文件，则使用默认值
            DimensionConfigManager.addDimensionConfig(dimensionKey, new DimensionConfigManager.DimensionConfig(
                    20.0f,
                    false, new int[]{0, 300}, new float[]{1.0f, 0.6f},
                    false, new int[]{300, 480}, new float[]{0.6f, 0.2f},
                    false, new int[]{480, 1000}, new float[]{0.2f, 0.05f},
                    false, new int[]{1000, 2000}, new float[]{0.05f, 0.01f},
                    false, new int[]{2000, 5000}, new float[]{0.01f, 0.001f}
            ));
        }


    }

    private int[] parseHeight(JsonArray jsonArray) {
        return new int[]{jsonArray.get(0).getAsInt(), jsonArray.get(1).getAsInt()};
    }

    private float[] parseDensity(JsonArray jsonArray) {
        return new float[]{jsonArray.get(0).getAsFloat(), jsonArray.get(1).getAsFloat()};
    }
}