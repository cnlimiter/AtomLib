package cn.evolvefield.mods.atom.lib.init.handler;

import cn.evolvefield.mods.atom.lib.init.config.AbstractModConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Set;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/6/25 14:08
 * Version: 1.0
 */
public class ConfigBaseHandler {

    public static final ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
    private static ForgeConfigSpec SERVER_CONFIG;

    private static Logger LOGGER;
    private static Set<AbstractModConfig> CONFIGS;

    public ConfigBaseHandler(Logger logger) {
        LOGGER = logger;
        SERVER_CONFIG = SERVER_BUILDER.build();
        init(CONFIGS);
        get(CONFIGS);
    }

    public static void init(Set<? extends AbstractModConfig> configs) {
        configs.forEach(AbstractModConfig::init);
        LOGGER.info("AL Config init!");
    }

    public static void get(Set<? extends AbstractModConfig> configs) {
        configs.forEach(AbstractModConfig::get);
    }

    public static boolean isResourceLocationList(Object o) {
        if (!(o instanceof List<?> list)) {
            return false;
        }
        for (Object s : list) {
            if (!s.toString().contains(":")) {
                return false;
            }
        }
        return true;
    }

    public <T extends AbstractModConfig> void register(T clazz) {
        CONFIGS.add(clazz);
    }

    public void eventParse() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.register(this);
    }

    @SubscribeEvent
    public void onLoading(ModConfigEvent.Loading event) {
        get(CONFIGS);
        LOGGER.info("AL Config loaded!");
    }

    @SubscribeEvent
    public void onReloading(ModConfigEvent.Reloading event) {
        get(CONFIGS);
        LOGGER.info("AL Config Reloaded!");
    }
}
