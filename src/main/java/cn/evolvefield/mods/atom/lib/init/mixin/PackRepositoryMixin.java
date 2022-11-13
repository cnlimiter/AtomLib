package cn.evolvefield.mods.atom.lib.init.mixin;

import cn.evolvefield.mods.atom.lib.api.fml.IRegisterListener;
import cn.evolvefield.mods.atom.lib.init.adapter.ResourcePackAdapter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.PackSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project: atomlib
 * Author: cnlimiter
 * Date: 2022/11/13 2:15
 * Description:
 */
@Mixin(PackRepository.class)
public class PackRepositoryMixin {
    @Shadow
    private Map<String, Pack> available;
    @Shadow
    private List<Pack> selected;

    @Inject(
            method = "reload",
            at = @At("TAIL")
    )
    public void reloadHook_AtomLib(CallbackInfo ci) {
        if (selected instanceof ImmutableList)
            selected = new ArrayList<>(selected);
        if (available instanceof ImmutableMap)
            available = new HashMap<>(available);

        for (PackResources pack : ResourcePackAdapter.BUILTIN_PACKS) {
            if (pack instanceof IRegisterListener listener)
                listener.onPreRegistered();
            Pack rpi = new Pack(pack.getName(), true, () -> pack, Component.literal(pack.getName()), Component.literal("Builtin."), PackCompatibility.COMPATIBLE, Pack.Position.TOP, true, PackSource.DEFAULT, true);
            available.put(pack.getName(), rpi);
            selected.add(rpi);
            if (pack instanceof IRegisterListener listener)
                listener.onPostRegistered();
        }
    }
}
