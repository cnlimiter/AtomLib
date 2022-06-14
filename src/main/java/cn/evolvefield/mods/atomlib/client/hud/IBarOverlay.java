package cn.evolvefield.mods.atomlib.client.hud;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Player;

public interface IBarOverlay {
    void renderBar(Player player, PoseStack poseStack);

    void renderText(Player player, PoseStack poseStack);

    void renderIcon(Player player, PoseStack poseStack);
}
