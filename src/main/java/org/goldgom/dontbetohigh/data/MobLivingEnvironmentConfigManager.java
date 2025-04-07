package org.goldgom.dontbetohigh.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.goldgom.dontbetohigh.Dontbetohigh;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MobLivingEnvironmentConfigManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("Dontbetohigh");

    private static final Map<EntityType<?>, MobEnvironmentConfig> mobConfigs = new HashMap<>();

    public static void addMobConfig(EntityType<?> entityType, MobEnvironmentConfig config) {
        mobConfigs.put(entityType, config);
    }

    public static MobEnvironmentConfig getMobConfig(EntityType<?> entityType) {
        // 如果找得到则返回
        if (mobConfigs.containsKey(entityType)) {
            return mobConfigs.get(entityType);
        } else {
            // 如果找不到则尝试读取对应的配置文件data\realenvironment\moblivingenvironment\player.json
            try (InputStreamReader reader = new InputStreamReader(
                Objects.requireNonNull(Dontbetohigh.class.getResourceAsStream("/data/dontbetohigh/moblivingenvironment/" + entityType.toString() + ".json")),
                StandardCharsets.UTF_8)){
                
                // 读取配置文件
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                LOGGER.info("Loaded mob config for {}: {}", entityType, json);
                // 解析配置文件
                double air = json.has("air") ? json.get("air").getAsDouble() : 0.8;
                double temperature = json.has("temperature") ? json.get("temperature").getAsDouble() : 20.0;
                // 创建配置对象
                MobLivingEnvironmentConfigManager.addMobConfig(entityType, new MobEnvironmentConfig(air, temperature));
                LOGGER.info("Loaded mob config for {}: {}", entityType, mobConfigs.get(entityType));
                return mobConfigs.get(entityType);
            }catch (Exception e)
            {
                LOGGER.warn("Failed to load mob config for {}: {}", entityType, e.getMessage());
                // 如果读取失败则使用默认值
                //怪物和生物的默认值分开
                // 默认值
                double air = 0.8;
                double temperature = 20.0;
                if(entityType.getCategory() == MobCategory.MONSTER) {
                    // 默认值
                    air = 0.1;
                    temperature = -40.0;
                }
                // 创建配置对象
                MobLivingEnvironmentConfigManager.addMobConfig(entityType, new MobEnvironmentConfig(air, temperature));
                LOGGER.info("Loaded mob config for {}: {}", entityType, mobConfigs.get(entityType));
                return mobConfigs.get(entityType);
                
            }

        }

    }

    public static class MobEnvironmentConfig {
        public final double air;
        public final double temperature;

        public MobEnvironmentConfig(double air, double temperature) {
            this.air = air;
            this.temperature = temperature;
        }
    }

}