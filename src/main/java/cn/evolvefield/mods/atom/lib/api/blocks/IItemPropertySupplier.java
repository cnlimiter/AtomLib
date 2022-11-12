package cn.evolvefield.mods.atom.lib.api.blocks;

import net.minecraft.world.item.Item;

/**
 * Project: atomlib
 * Author: cnlimiter
 * Date: 2022/11/13 2:21
 * Description:方块物品状态的属性
 */
public interface IItemPropertySupplier {
    Item.Properties createItemProperties(Item.Properties props);
}
