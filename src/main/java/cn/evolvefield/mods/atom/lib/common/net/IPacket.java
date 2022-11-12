package cn.evolvefield.mods.atom.lib.common.net;

import cn.evolvefield.mods.atom.lib.utils.java.Threading;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Description:网络包基础类
 * Author: cnlimiter
 * Date: 2022/4/2 12:57
 * Version: 1.0
 */
public interface IPacket {
    default void write(FriendlyByteBuf buf) {
    }

    default void read(FriendlyByteBuf buf) {
    }

    default void execute(PacketContext ctx) {
        if (ctx.getSide() == LogicalSide.CLIENT) {
            clientExecute(ctx);
        } else {
            serverExecute(ctx);
        }
    }

    /**
     * 客户端执行
     *
     * @param ctx 执行参数
     */
    @OnlyIn(Dist.CLIENT)
    default void clientExecute(PacketContext ctx) {
    }

    /**
     * 服务端执行
     *
     * @param ctx 执行参数
     */
    default void serverExecute(PacketContext ctx) {
    }

    default boolean executeOnMainThread() {
        return Threading.isMainThreaded(getClass());
    }
}
