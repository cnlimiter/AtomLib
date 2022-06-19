package cn.evolvefield.mods.atom.lib.init.registry;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/3/31 10:36
 * Version: 1.0
 */
public class ModTab extends CreativeModeTab {

    private final Item iconStack;

    public ModTab(String labelName, Item icon) {
        super(labelName);
        this.iconStack = icon;
    }

    @Override
    public @NotNull ItemStack makeIcon() {
        return new ItemStack(iconStack);
    }
}
