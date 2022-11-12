package cn.evolvefield.mods.atom.lib.common.net;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

/**
 * Project: atomlib
 * Author: cnlimiter
 * Date: 2022/11/13 2:46
 * Description:
 */
public class PacketContext {
    private final ServerPlayer sender;
    private final LogicalSide side;
    private IPacket reply;

    public PacketContext(NetworkEvent.Context ctx) {
        this.side = ctx.getDirection().getReceptionSide();
        this.sender = ctx.getSender();
    }

    public boolean hasSender() {
        return sender != null;
    }

    public ServerPlayer getSender() {
        return sender;
    }

    public LogicalSide getSide() {
        return side;
    }

    public void withReply(IPacket reply) {
        this.reply = reply;
    }

    public IPacket getReply() {
        return reply;
    }
}
