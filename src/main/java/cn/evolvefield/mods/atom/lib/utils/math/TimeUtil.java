package cn.evolvefield.mods.atom.lib.utils.math;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/7/25 15:30
 * Version: 1.0
 */
public class TimeUtil {

    public static int getUniversalTickTime() {
        if (ServerLifecycleHooks.getCurrentServer() == null) {
            AtomicLong atomicLong = new AtomicLong(-1);

            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
            {
                if (Minecraft.getInstance().level != null)
                    atomicLong.set(Minecraft.getInstance().level.getGameTime());
            });

            return (int) atomicLong.get();
        } else {
            return ServerLifecycleHooks.getCurrentServer().getTickCount();
        }
    }

    public static int convertToRelativeTime(int timestampUntilFinished) {
        return timestampUntilFinished - TimeUtil.getUniversalTickTime();
    }

    public static int convertToAbsoluteTime(int relativeTime) {
        return relativeTime + TimeUtil.getUniversalTickTime();
    }
}
