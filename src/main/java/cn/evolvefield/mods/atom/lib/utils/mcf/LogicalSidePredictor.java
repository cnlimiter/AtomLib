package cn.evolvefield.mods.atom.lib.utils.mcf;

import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;

/**
 * Project: atomlib
 * Author: cnlimiter
 * Date: 2022/11/13 2:53
 * Description:检测目前逻辑端
 */
public class LogicalSidePredictor {

    public static LogicalSide getCurrentLogicalSide() {
        return Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER ? LogicalSide.SERVER : LogicalSide.CLIENT;
    }
}
