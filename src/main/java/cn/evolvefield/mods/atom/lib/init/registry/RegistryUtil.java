package cn.evolvefield.mods.atom.lib.init.registry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.network.IContainerFactory;

public class RegistryUtil {

    @SuppressWarnings("unchecked")
    public static <T extends BlockEntity> BlockEntityType<T> build(BlockEntityType.BlockEntitySupplier<T> factory, Block... block) {
        //noinspection ConstantConditions
        return BlockEntityType.Builder.of(factory, block).build(null);
    }


    public static Item blockItem(Block block, Rarity rarity, CreativeModeTab tab) {
        return blockItem(block, new Item.Properties().rarity(rarity).tab(tab));
    }

    public static Item blockItem(Block block, CreativeModeTab tab) {
        return blockItem(block, new Item.Properties().tab(tab));
    }

    private static Item blockItem(Block block, Item.Properties properties) {
        return new BlockItem(block, properties);
    }


    @SuppressWarnings("unchecked")
    public static <T extends AbstractContainerMenu> MenuType<T> registerContainer(IContainerFactory<T> containerFactory) {
        return (MenuType<T>) new MenuType<>(containerFactory);
    }

    public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S registerRecipe(String p_44099_, S p_44100_) {
        return Registry.register(Registry.RECIPE_SERIALIZER, p_44099_, p_44100_);
    }


    public static <T extends ParticleOptions> void registerParticleFactory(ParticleType<T> particleTypeRO, ParticleEngine.SpriteParticleRegistration<T> factory) {
        Minecraft.getInstance().particleEngine.register(particleTypeRO, factory);
    }

}
