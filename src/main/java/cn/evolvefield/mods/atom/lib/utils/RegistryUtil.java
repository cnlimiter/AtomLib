package cn.evolvefield.mods.atom.lib.utils;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;

import java.util.Objects;

/**
 * Project: atomlib
 * Author: cnlimiter
 * Date: 2022/11/13 3:55
 * Description:
 */
public class RegistryUtil {

    public static <T extends BlockEntity> BlockEntityType<T> build(BlockEntityType.BlockEntitySupplier<T> factory, String registryName, Block... block) {
        //noinspection ConstantConditions
        return (BlockEntityType<T>) BlockEntityType.Builder.of(factory, block).build(null);
    }

    public static <T extends BlockEntity> BlockEntityType<T> build(BlockEntityType.BlockEntitySupplier<T> factory, ResourceLocation registryName, Block... block) {
        //noinspection ConstantConditions
        return (BlockEntityType<T>) BlockEntityType.Builder.of(factory, block).build(null);
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

    public static <T extends AbstractContainerMenu> MenuType<T> registerContainer(IContainerFactory<T> containerFactory) {
        return IForgeMenuType.create(containerFactory);
    }

    public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S registerRecipe(String name, S serializer) {
        return Registry.register(Registry.RECIPE_SERIALIZER, name, serializer);
    }

    public static <T extends Recipe<?>> RecipeType<T> registerRecipeType(String modId, String name) {
        return RecipeType.simple(new ResourceLocation(modId, name));
    }
}
