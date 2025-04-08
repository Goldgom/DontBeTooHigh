package org.goldgom.dontbetoohigh.comp.create.utils;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;

public class HelmetUtil {

    public static boolean isOxygenHelmetActive(LivingEntity entity) {
        if (!ModList.get().isLoaded("create")) {
            return false; // 如果没有加载 create 模组，直接返回 false
        }

        ItemStack helmet = com.simibubi.create.content.equipment.armor.DivingHelmetItem.getWornItem(entity);
        return !helmet.isEmpty() && helmet.getItem() instanceof com.simibubi.create.content.equipment.armor.DivingHelmetItem;
    }
}