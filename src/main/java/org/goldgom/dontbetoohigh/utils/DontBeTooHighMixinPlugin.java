package org.goldgom.dontbetoohigh.utils;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class DontBeTooHighMixinPlugin implements IMixinConfigPlugin {

    Logger LOGGER = Logger.getLogger("DontBeTooHigh");
    @Override
    public void onLoad(String mixinPackage) {
        // No initialization needed
    }

    @Override
    public String getRefMapperConfig() {
        return null; // No refmapper configuration
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        // Only apply DivingHelmetMixin if the "create" mod is loaded
        return true; // Apply all other mixins
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
        // No target manipulation needed
    }

    @Override
    public List<String> getMixins() {
        return null; // Use the default mixin list from the configuration file
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        // No pre-apply logic needed
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        // No post-apply logic needed
    }
}