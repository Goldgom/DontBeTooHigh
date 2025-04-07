package org.goldgom.dontbetohigh;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.living.LivingBreatheEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.goldgom.dontbetohigh.data.MobLivingEnvironmentConfigManager;
import org.goldgom.dontbetohigh.mixin.EntityWalkDetect;
import org.goldgom.dontbetohigh.utils.IEntityWalkDetect;
import org.goldgom.dontbetohigh.world.Air;
import org.slf4j.Logger;
import org.goldgom.dontbetohigh.utils.IRealEnvironmentPlayer;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Dontbetohigh.MODID)
public class Dontbetohigh {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "dontbetohigh";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public Dontbetohigh() {
       // IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
       // modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
       // BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
       // ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        //CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        //modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        if (Config.logDirtBlock) LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
      //  if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) event.accept(EXAMPLE_BLOCK_ITEM);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }


    @SubscribeEvent
    public void onLivingTick(LivingBreatheEvent event) {
        var entity = event.getEntity();
        if (true ) {
            if(entity.isUnderWater())
            {
                return;
            }

            // 每 10 tick 执行一次逻辑
            if (entity.tickCount % 20 == 0) {
                // 获取生物的配置
                MobLivingEnvironmentConfigManager.MobEnvironmentConfig living_config = MobLivingEnvironmentConfigManager.getMobConfig(entity.getType());


                // 计算空气浓度
                float airConcentration = Air.calculateAirConcentration(entity);
                double d_airConcentration = (living_config.air - airConcentration);

                // 如果玩家在奔跑，加快氧气消耗
                if (entity.isSprinting()) {
                    d_airConcentration += 0.05;
                } else if ((entity instanceof IEntityWalkDetect new_entity&& new_entity.isWalking()) ) {
                    d_airConcentration += 0.025;
                }
                if(d_airConcentration <= 0){
                    // LOGGER.info("entity instanceof IRealEnvironmentPlayer player&& player.isMiningBlock())：{}",entity instanceof IRealEnvironmentPlayer player&& player.isMiningBlock());
                    if(entity.isSprinting() ||(entity instanceof IEntityWalkDetect new_entity&& new_entity.isWalking()) || (entity instanceof IRealEnvironmentPlayer player&& player.isMiningBlock()))
                    {
                        d_airConcentration *= 80;

                    }else{
                        d_airConcentration *= 450;
                    }
//                    if(entity.getAirSupply() >= 300) {
//                        d_airConcentration = 0;
//                    }

                } else {
                    d_airConcentration *= 40;


                }
                if((int)(entity.getAirSupply() - d_airConcentration) <= 300)
                    entity.setAirSupply((int)(entity.getAirSupply() - d_airConcentration));

                // 如果空气浓度小于 0，并且玩家没有这些效果给玩家虚弱和挖掘疲劳效果
                if (entity.getAirSupply() < 0 && !entity.hasEffect(MobEffects.WEAKNESS) && !entity.hasEffect(MobEffects.DIG_SLOWDOWN)) {
                    // 每次有20% 概率添加虚弱和挖掘疲劳效果
                    if (entity.getRandom().nextInt(100) < 20) {
                        entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 0)); // 10 seconds of Weakness
                        entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 200, 0)); // 10 seconds of Mining Fatigue
                    }

                }
                if(entity.getAirSupply() < 0 && entity.getHealth() < entity.getMaxHealth() / 3){
                    entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION,200,0));
                }
                if(entity instanceof IEntityWalkDetect new_entity)
                {
                  //  LOGGER.info("new_entity.isWalking():{}",new_entity.isWalking());
                    new_entity.setLastDist();
                }

//                // 日志输出
//                if (entity instanceof ServerPlayer player) {
//                    LOGGER.info("Player {} is suffocating! Air concentration: {}, Air supply: {} d_airConcentration:{}  entity.walkDist:{}  entity.walkDistO:{} entity.isUsingItem() :{}",
//                            player.getName().getString(),
//                            airConcentration,
//                            entity.getAirSupply(),
//                            d_airConcentration,
//                            entity.walkDist ,
//                            entity.walkDistO ,
//                            entity.isUsingItem());
//                }

            }

        }
    }


    @SubscribeEvent
    public void onPlayerLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        // 获取玩家和方块信息
        var player = event.getEntity();
        player.setAirSupply(player.getAirSupply() - 2);
        // 检查玩家是否实现了 IRealEnvironmentPlayer 接口
        if (player instanceof IRealEnvironmentPlayer realEnvironmentPlayer) {
            // 设置正在挖掘方块状态
            realEnvironmentPlayer.setMiningBlock(true);
        }


    }
    @SubscribeEvent
    public void onPlayerLeftRightBlock(PlayerInteractEvent.RightClickBlock event) {
        var player = event.getEntity();
        player.setAirSupply(player.getAirSupply() - 1);
        // 检查玩家是否实现了 IRealEnvironmentPlayer 接口
        if (player instanceof IRealEnvironmentPlayer realEnvironmentPlayer) {
            // 设置正在挖掘方块状态
            realEnvironmentPlayer.setMiningBlock(true);
        }

    }
}
