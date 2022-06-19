package cn.evolvefield.mods.atom.lib.client.hud;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Player;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/3/30 12:07
 * Version: 1.0
 */
public abstract class AbstractBarOverlay implements IBarOverlay {

    public void renderAll(Player player, PoseStack poseStack) {
        this.renderBar(player, poseStack);
        this.renderText(player, poseStack);
        this.renderIcon(player, poseStack);
    }
}
