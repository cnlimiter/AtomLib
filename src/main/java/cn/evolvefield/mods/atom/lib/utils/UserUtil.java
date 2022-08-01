package cn.evolvefield.mods.atom.lib.utils;

import cn.evolvefield.mods.atom.lib.AtomLib;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class UserUtil {
    public static final String DEFAULT_NAME = "";
    public static final UUID DEFAULT_UUID = new UUID(0, 0);

    public static boolean isDefaultUser(UUID uuid) {
        return DEFAULT_UUID.equals(uuid);
    }

    public static final GameProfile gameProfile = new GameProfile(UUID.nameUUIDFromBytes("atomlib.common".getBytes(StandardCharsets.UTF_8)), "[AtomFake]");
    private static final List<UUID> warnedFails = new ArrayList<>();

    public static @Nullable
    ServerPlayer getPlayer(UUID uuid) {
        return ServerLifecycleHooks.getCurrentServer() == null ? null : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid);
    }

    public static @Nullable
    ServerPlayer getPlayer(@Nullable Level level, UUID uuid) {
        return level == null || level.getServer() == null ? null : level.getServer().getPlayerList().getPlayer(uuid);
    }

    public static String getNameByUUID(UUID uuid) {
        if (uuid == null) return DEFAULT_NAME;

        if (FMLEnvironment.dist.isClient() && Minecraft.getInstance().level != null) {
            Player player = Minecraft.getInstance().level.getPlayerByUUID(uuid);
            if (player != null)
                return player.getName().getString();
        }
        String lastKnownUsername = UsernameCache.getLastKnownUsername(uuid);
        return lastKnownUsername == null ? DEFAULT_NAME : lastKnownUsername;
    }

    public static UUID getUUIDByName(String name) {
        if (FMLEnvironment.dist == Dist.CLIENT && Minecraft.getInstance().level != null) {
            List<AbstractClientPlayer> players = Minecraft.getInstance().level.players();
            Optional<AbstractClientPlayer> first = players.stream().filter(t -> t.getName().getString().equals(name)).findFirst();
            if (first.isPresent())
                return first.get().getUUID();
        }
        if (name.equals(DEFAULT_NAME))
            return DEFAULT_UUID;
        Optional<Map.Entry<UUID, String>> first = UsernameCache.getMap().entrySet().stream().filter(entry -> name.equals(entry.getValue())).findFirst();
        if (first.isPresent())
            return first.get().getKey();
        else
            return DEFAULT_UUID;
    }

    public static boolean hasPlayer(UUID uuid) {
        if (UsernameCache.containsUUID(uuid))
            return true;

        if (FMLEnvironment.dist.isClient() && Minecraft.getInstance().level != null) {
            Player player = Minecraft.getInstance().level.getPlayerByUUID(uuid);
            return player != null;
        }
        return false;
    }

    public static boolean hasPlayer(String name) {
        if (UsernameCache.getMap().values().stream().anyMatch(name::equals))
            return true;
        if (FMLEnvironment.dist.isClient() && Minecraft.getInstance().level != null) {
            return Minecraft.getInstance().level.players().stream().anyMatch(t -> t.getName().toString().equals(name));
        }

        return false;
    }

    public static List<String> getAllPlayerName() {
        List<String> result = new ArrayList<>(UsernameCache.getMap().values());
        if (FMLEnvironment.dist.isClient() && Minecraft.getInstance().level != null) {
            for (AbstractClientPlayer player : Minecraft.getInstance().level.players()) {
                result.add(player.getName().getString());
            }
        }
        return result;
    }

    @Nonnull
    public static String getLastKnownUsername(@Nullable UUID uuid) {
        if (uuid == null) {
            return "<???>";
        }
        String ret = UsernameCache.getLastKnownUsername(uuid);
        if (ret == null && !warnedFails.contains(uuid) && EffectiveSide.get().isServer()) { // see if MC/Yggdrasil knows about it?!
            Optional<GameProfile> gp = ServerLifecycleHooks.getCurrentServer().getProfileCache().get(uuid);
            if (gp.isPresent()) {
                ret = gp.get().getName();
            }
        }
        if (ret == null && !warnedFails.contains(uuid)) {
            AtomLib.LOGGER.warn("Failed to retrieve username for UUID {}, you might want to add it to the JSON cache", uuid);
            warnedFails.add(uuid);
        }
        return ret == null ? "<" + uuid + ">" : ret;
    }
}
