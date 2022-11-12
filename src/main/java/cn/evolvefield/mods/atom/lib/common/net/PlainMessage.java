package cn.evolvefield.mods.atom.lib.common.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * Project: atomlib
 * Author: cnlimiter
 * Date: 2022/11/13 3:06
 * Description:
 */
public class PlainMessage {
    IPacket packet;

    public PlainMessage() {
    }


    public PlainMessage(IPacket packet) {
        this.packet = packet;
    }

    public PlainMessage(FriendlyByteBuf buf) {
        packet = PacketFactory.createEmpty(buf.readUtf(256));
        if (packet != null)
            packet.read(buf);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(PacketFactory.getPacketId(packet));
        if (packet != null)
            packet.write(buf);
    }

    public boolean isValid() {
        return packet != null;
    }

    public IPacket unwrap() {
        return packet;
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        PacketContext pctx = new PacketContext(ctx);
        if (packet != null) {
            CompletableFuture<Void> exec;
            if (packet.executeOnMainThread()) {
                exec = ctx.enqueueWork(() -> packet.execute(pctx));
            } else {
                packet.execute(pctx);
                exec = CompletableFuture.completedFuture(null);
            }

            exec.thenRun(() ->
            {
                IPacket reply = pctx.getReply();
                if (reply != null)
                    ctx.getPacketDispatcher().sendPacket(Network.MAIN_CHANNEL, Network.toBuffer(new PlainMessage(reply)));
            });

            ctx.setPacketHandled(true);
        }
    }
}
