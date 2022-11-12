package cn.evolvefield.mods.atom.lib.common.net;

import cn.evolvefield.mods.atom.lib.ALConstants;
import cn.evolvefield.mods.atom.lib.annotations.Setup;
import cn.evolvefield.mods.atom.lib.utils.java.Cast;
import cn.evolvefield.mods.atom.lib.utils.math.TargetPoint;
import cn.evolvefield.mods.atom.lib.utils.mcf.LogicalSidePredictor;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.IndexedMessageCodec;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Project: atomlib
 * Author: cnlimiter
 * Date: 2022/11/13 2:51
 * Description:
 */
public class Network {
    public static final ResourceLocation MAIN_CHANNEL = new ResourceLocation(ALConstants.MOD_ID, "main");
    private static SimpleChannel channel;

    @Setup
    private static void initialize() {
        ALConstants.LOG.info("Setup AtomLib networking!");
        channel = NetworkRegistry.newSimpleChannel(MAIN_CHANNEL, () -> "1", s -> true, s -> true);
        channel.registerMessage(1, PlainMessage.class, PlainMessage::write, PlainMessage::new, PlainMessage::handle);
    }


    ///

    public static void sendTo(Player player, IPacket packet) {
        sendTo(packet, player);
    }

    public static void sendTo(ServerPlayer player, IPacket packet) {
        sendTo(packet, player);
    }

    public static void sendTo(IPacket packet, Player player) {
        if (packet != null && player instanceof ServerPlayer sp)
            channel.send(PacketDistributor.PLAYER.with(() -> sp), toPlain(packet));
    }

    public static void sendTo(IPacket packet, ServerPlayer player) {
        if (player != null && packet != null)
            channel.send(PacketDistributor.PLAYER.with(() -> player), toPlain(packet));
    }

    public static void sendToTracking(LevelChunk chunk, IPacket packet) {
        sendToTracking(packet, chunk);
    }

    public static void sendToTracking(IPacket packet, LevelChunk chunk) {
        if (packet != null && chunk != null)
            channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), toPlain(packet));
    }

    public static void sendToTracking(BlockEntity tile, IPacket packet) {
        sendToTracking(packet, tile);
    }

    public static void sendToTracking(IPacket packet, BlockEntity tile) {
        if (packet != null && tile != null && tile.hasLevel() && !tile.getLevel().isClientSide)
            sendToTracking(packet, tile.getLevel().getChunkAt(tile.getBlockPos()));
    }

    public static void sendToTracking(Entity entity, IPacket packet) {
        sendToTracking(packet, entity);
    }

    public static void sendToTracking(IPacket packet, Entity entity) {
        if (packet != null && entity != null)
            channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), toPlain(packet));
    }

    public static void sendToTrackingAndSelf(Entity entity, IPacket packet) {
        sendToTrackingAndSelf(packet, entity);
    }

    public static void sendToTrackingAndSelf(IPacket packet, Entity entity) {
        if (packet != null && entity != null)
            channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), toPlain(packet));
    }

    public static void sendToDimension(Level dim, IPacket packet) {
        sendToDimension(packet, dim.dimension());
    }

    public static void sendToDimension(ResourceKey<Level> dim, IPacket packet) {
        sendToDimension(packet, dim);
    }

    public static void sendToDimension(IPacket packet, ResourceKey<Level> dim) {
        if (dim == null || packet == null)
            return;
        if (LogicalSidePredictor.getCurrentLogicalSide() == LogicalSide.SERVER)
            channel.send(PacketDistributor.DIMENSION.with(Cast.staticValue(dim)), toPlain(packet));
    }

    public static void sendToAll(IPacket packet) {
        if (packet == null)
            return;
        if (LogicalSidePredictor.getCurrentLogicalSide() == LogicalSide.SERVER)
            channel.send(PacketDistributor.ALL.noArg(), toPlain(packet));
    }

    public static void sendToArea(TargetPoint point, IPacket packet) {
        sendToArea(point.toForge().get(), packet);
    }

    public static void sendToArea(PacketDistributor.TargetPoint point, IPacket packet) {
        if (point == null || packet == null) return;
        if (LogicalSidePredictor.getCurrentLogicalSide() == LogicalSide.SERVER)
            channel.send(PacketDistributor.NEAR.with(Cast.staticValue(point)), toPlain(packet));
    }

    public static void sendToServer(IPacket packet) {
        if (packet == null)
            return;
        if (LogicalSidePredictor.getCurrentLogicalSide() == LogicalSide.CLIENT)
            channel.sendToServer(toPlain(packet));
    }

    public static void send(PacketDistributor.PacketTarget target, IPacket packet) {
        if (target == null || packet == null) return;
        if (LogicalSidePredictor.getCurrentLogicalSide() == LogicalSide.SERVER)
            channel.send(target, toPlain(packet));
    }

    ///

    public static PlainMessage toPlain(IPacket packet) {
        return new PlainMessage(packet);
    }

    public static FriendlyByteBuf toBuffer(PlainMessage msg) {
        final FriendlyByteBuf bufIn = new FriendlyByteBuf(Unpooled.buffer());
        channel.encodeMessage(msg, bufIn);
        return bufIn;
    }

    ///

    public static void swingHand(Player player, InteractionHand hand) {
        ServerPlayer spe = Cast.cast(player, ServerPlayer.class);
        if (spe != null)
            spe.getLevel().getChunkSource().broadcastAndSend(spe, new ClientboundAnimatePacket(player, hand == InteractionHand.MAIN_HAND ? 0 : 3));
    }

}
