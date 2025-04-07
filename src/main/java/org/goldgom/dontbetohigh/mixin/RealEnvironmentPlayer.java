package org.goldgom.dontbetohigh.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.goldgom.dontbetohigh.utils.IRealEnvironmentPlayer;

@Mixin(Player.class) // 将此 Mixin 注入到原版 Player 类
public class RealEnvironmentPlayer implements IRealEnvironmentPlayer {

    @Unique
    private boolean isMiningBlock = false; // 是否正在挖掘方块的标志

    @Override
    public void setMiningBlock(boolean isMining) {
        this.isMiningBlock = isMining;
    }

    @Override
    public boolean isMiningBlock() {
        boolean currentState = this.isMiningBlock;
        this.isMiningBlock = false; // 重置状态
        return currentState;    }
}