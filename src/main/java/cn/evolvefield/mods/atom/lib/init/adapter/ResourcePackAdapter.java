package cn.evolvefield.mods.atom.lib.init.adapter;

import net.minecraft.server.packs.PackResources;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: atomlib
 * Author: cnlimiter
 * Date: 2022/11/13 2:16
 * Description:
 */
public class ResourcePackAdapter {
    public static final List<PackResources> BUILTIN_PACKS = new ArrayList<>();

    public static void registerResourcePack(PackResources pack) {
        if (!BUILTIN_PACKS.contains(pack))
            BUILTIN_PACKS.add(pack);
    }
}
