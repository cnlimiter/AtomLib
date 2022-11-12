package cn.evolvefield.mods.atom.lib;

import cn.evolvefield.mods.atom.lib.annotations.Setup;
import cn.evolvefield.mods.atom.lib.annotations.SimplyRegister;
import cn.evolvefield.mods.atom.lib.annotations.client.ClientSetup;
import cn.evolvefield.mods.atom.lib.api.registry.RegistryMapping;
import cn.evolvefield.mods.atom.lib.init.adapter.RegistryAdapter;
import cn.evolvefield.mods.atom.lib.init.mixin.RegistryManagerAccessor;
import cn.evolvefield.mods.atom.lib.utils.mcf.ScanDataHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.moddiscovery.ModAnnotation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.ElementType;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mod(ALConstants.MOD_ID)
public class AtomLib {

    public static final Logger LOG = LogManager.getLogger("Atom Lib");

    public AtomLib() {
        this.addAutoRegistry();
        this.addAutoCommonSetup();
        this.addAutoClientSetup();

    }


    public void addAutoRegistry() {
        // 注册所有有关注册的提供者
        if (RegistryManager.ACTIVE instanceof RegistryManagerAccessor activeRegistries) {
            for (var registry : activeRegistries.getRegistries().values()) {
                var superType = RegistryMapping.getSuperType(registry);
                if (superType == null)
                    LOG.error("Found registry without defined super type: " + registry.getRegistryKey());
            }

            ScanDataHelper.lookupAnnotatedObjects(SimplyRegister.class).forEach(data ->
            {
                if (data.getTargetType() == ElementType.TYPE)
                    data.getOwnerMod()
                            .ifPresent(mc ->
                            {
                                LOG.info("Hooked " + data.clazz() + " from " + mc.getModId() + " to register it's stuff.");
                                mc.getEventBus()
                                        .addListener((Consumer<RegisterEvent>) event ->
                                        {
                                            IForgeRegistry<?> reg = event.getForgeRegistry();
                                            if (reg == null)
                                                reg = RegistryMapping.getRegistryByType(RegistryMapping.getSuperType(event.getRegistryKey()));
                                            RegistryAdapter.register(reg, data.getOwnerClass(), mc.getModId(), data.getProperty("prefix").map(Objects::toString).orElse(""));
                                        });
                            });
            });
        } else
            throw new RuntimeException("Unable to cast RegistryManager to RegistryManagerAccessor. Mixin apply failed?");
    }

    public void addAutoCommonSetup() {
        List<ModAnnotation.EnumHolder> bothSides = Stream.of(Dist.values())
                .map(dst -> new ModAnnotation.EnumHolder("Lnet/minecraftforge/itf/distmarker/Dist;", dst.name()))
                .collect(Collectors.toList());


        // Register all setups
        ScanDataHelper.lookupAnnotatedObjects(Setup.class).forEach(data ->
        {
            Object side = data.getProperty("side")
                    .orElse(bothSides);

            if (side instanceof List<?> lst && !lst.isEmpty()) {
                for (Object o : lst) {
                    if (o instanceof ModAnnotation.EnumHolder h && FMLEnvironment.dist.name().equals(h.getValue())) {
                        if (data.getTargetType() == ElementType.METHOD) {
                            LOG.info("Injecting setup into " + data.clazz().getClassName());
                            data.getOwnerMod()
                                    .map(FMLModContainer::getEventBus)
                                    .ifPresent(b -> b.addListener((Consumer<FMLCommonSetupEvent>) event -> RegistryAdapter.setup(event, data.getOwnerClass(), data.getMemberName())));
                        }

                        break;
                    }
                }
            } else
                LOG.warn("What the hell is this? " + data.parent.clazz() + "->" + data.getMemberName());
        });
    }

    public void addAutoClientSetup() {
        ScanDataHelper.lookupAnnotatedObjects(ClientSetup.class).forEach(data ->
        {
            if (data.getTargetType() == ElementType.METHOD) {
                LOG.info("Injecting client-setup into " + data.clazz().getClassName());
                data.getOwnerMod()
                        .map(FMLModContainer::getEventBus)
                        .ifPresent(b -> b.addListener((Consumer<FMLClientSetupEvent>) event -> RegistryAdapter.clientSetup(event, data.getOwnerClass(), data.getMemberName())));
            }
        });
    }

}
