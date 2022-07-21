package cn.evolvefield.mods.atom.lib.common.item;

import cn.evolvefield.mods.atom.lib.utils.NBTUtil;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/6/2 20:44
 * Version: 1.0
 */
public class StackUtil {
    public static ItemStack withSize(ItemStack stack, int size, boolean container) {
        if (size <= 0) {
            if (container && stack.hasContainerItem()) {
                return stack.getContainerItem();
            } else {
                return ItemStack.EMPTY;
            }
        }

        stack = stack.copy();
        stack.setCount(size);

        return stack;
    }

    public static ItemStack grow(ItemStack stack, int amount) {
        return withSize(stack, stack.getCount() + amount, false);
    }

    public static ItemStack shrink(ItemStack stack, int amount, boolean container) {
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        return withSize(stack, stack.getCount() - amount, container);
    }

    public static boolean areItemsEqual(ItemStack stack1, ItemStack stack2) {
        if (stack1.isEmpty() && stack2.isEmpty())
            return true;

        return !stack1.isEmpty() && stack1.sameItem(stack2);
    }

    public static boolean areStacksEqual(ItemStack stack1, ItemStack stack2) {
        return areItemsEqual(stack1, stack2) && ItemStack.tagMatches(stack1, stack2);
    }

    /**
     * 检查是否可以将 stack2 合并到 stack1
     *
     * @param stack1 the current stack
     * @param stack2 the additional stack
     * @return can these stacks be combined
     */
    public static boolean canCombineStacks(ItemStack stack1, ItemStack stack2) {
        if (!stack1.isEmpty() && stack2.isEmpty())
            return true;

        return areStacksEqual(stack1, stack2) && (stack1.getCount() + stack2.getCount()) <= stack1.getMaxStackSize();
    }

    /**
     * 将 stack2 合并到 stack1
     *
     * @param stack1 the current stack
     * @param stack2 the additional stack
     * @return the new combined stack
     */
    public static ItemStack combineStacks(ItemStack stack1, ItemStack stack2) {
        if (stack1.isEmpty())
            return stack2.copy();

        return grow(stack1, stack2.getCount());
    }

    /**
     * 将 stack1 中的标签与 stack2 中的相应标签进行比较
     *
     * @param stack1 the reference stack
     * @param stack2 the actual stack
     * @return 所有对应的标签都是一样的
     */
    public static boolean compareTags(ItemStack stack1, ItemStack stack2) {
        if (!stack1.hasTag())
            return true;

        if (stack1.hasTag() && !stack2.hasTag())
            return false;

        var stack1Keys = NBTUtil.getTagCompound(stack1).getAllKeys();
        var stack2Keys = NBTUtil.getTagCompound(stack2).getAllKeys();

        for (var key : stack1Keys) {
            if (stack2Keys.contains(key)) {
                if (!NbtUtils.compareNbt(NBTUtil.getTag(stack1, key), NBTUtil.getTag(stack2, key), true)) {
                    return false;
                }
            } else {
                return false;
            }
        }

        return true;
    }
}
