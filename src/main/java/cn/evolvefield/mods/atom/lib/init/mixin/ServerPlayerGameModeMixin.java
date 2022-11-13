package cn.evolvefield.mods.atom.lib.init.mixin;

import cn.evolvefield.mods.atom.lib.api.events.HarvestDropsEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.Containers;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Project: atomlib
 * Author: cnlimiter
 * Date: 2022/11/13 12:49
 * Description:
 */
@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {
    @Shadow
    protected ServerLevel level;

    @Shadow
    @Final
    protected ServerPlayer player;

    @Inject(
            method = "removeBlock",
            at = @At("HEAD"),
            remap = false
    )
    public void removeBlock_AL(BlockPos p_180235_1_, boolean canHarvest, CallbackInfoReturnable<Boolean> cir) {
        var drops = new HarvestDropsEvent(level, p_180235_1_, level.getBlockState(p_180235_1_), player);
        MinecraftForge.EVENT_BUS.post(drops);
        Containers.dropContents(level, p_180235_1_, drops.getDrops());
    }
}
