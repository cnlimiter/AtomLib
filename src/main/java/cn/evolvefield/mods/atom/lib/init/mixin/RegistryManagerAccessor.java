package cn.evolvefield.mods.atom.lib.init.mixin;

import com.google.common.collect.BiMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Project: atomlib
 * Author: cnlimiter
 * Date: 2022/11/13 2:23
 * Description:
 */
@Mixin(RegistryManager.class)
public interface RegistryManagerAccessor {

    @Accessor
    BiMap<ResourceLocation, ForgeRegistry<?>> getRegistries();

}
