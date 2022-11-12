package cn.evolvefield.mods.atom.lib.utils.math;

import com.mojang.math.Vector3d;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

/**
 * Project: atomlib
 * Author: cnlimiter
 * Date: 2022/11/13 2:54
 * Description:
 */
public class TargetPoint extends Vec3 {
    public final ServerPlayer excluded;
    public final double range;
    public final ResourceKey<Level> dim;

    /**
     * A target point with excluded entity
     *
     * @param excluded Entity to exclude
     * @param x        X
     * @param y        Y
     * @param z        Z
     * @param range    Radius
     * @param dim      DimensionType
     */
    public TargetPoint(final ServerPlayer excluded, final double x, final double y, final double z, final double range, final ResourceKey<Level> dim) {
        super(x, y, z);
        this.excluded = excluded;
        this.range = range;
        this.dim = dim;
    }

    /**
     * A target point without excluded entity
     *
     * @param x     X
     * @param y     Y
     * @param z     Z
     * @param range Radius
     * @param dim   DimensionType
     */
    public TargetPoint(final double x, final double y, final double z, final double range, final ResourceKey<Level> dim) {
        super(x, y, z);
        this.excluded = null;
        this.range = range;
        this.dim = dim;
    }

    public TargetPoint(final Vec3i pos, final double range, final ResourceKey<Level> dim) {
        this(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, range, dim);
    }

    public TargetPoint(final Vector3d pos, final double range, final ResourceKey<Level> dim) {
        this(pos.x, pos.y, pos.z, range, dim);
    }

    // WORLD-based versions

    /**
     * A target point with excluded entity
     *
     * @param excluded Entity to exclude
     * @param x        X
     * @param y        Y
     * @param z        Z
     * @param range    Radius
     * @param world    World
     */
    public TargetPoint(final ServerPlayer excluded, final double x, final double y, final double z, final double range, final Level world) {
        super(x, y, z);
        this.excluded = excluded;
        this.range = range;
        this.dim = world.dimension();
    }

    /**
     * A target point without excluded entity
     *
     * @param x     X
     * @param y     Y
     * @param z     Z
     * @param range Radius
     * @param world World
     */
    public TargetPoint(final double x, final double y, final double z, final double range, final Level world) {
        super(x, y, z);
        this.excluded = null;
        this.range = range;
        this.dim = world.dimension();
    }

    public TargetPoint(final Vec3i pos, final double range, final Level world) {
        this(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, range, world.dimension());
    }

    public TargetPoint(final Vector3d pos, final double range, final Level world) {
        this(pos.x, pos.y, pos.z, range, world.dimension());
    }

    public Supplier<PacketDistributor.TargetPoint> toForge() {
        PacketDistributor.TargetPoint tp = new PacketDistributor.TargetPoint(excluded, x, y, z, range, dim);
        return () -> tp;
    }
}
