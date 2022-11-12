package cn.evolvefield.mods.atom.lib.annotations.client;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Project: atomlib
 * Author: cnlimiter
 * Date: 2022/11/13 2:41
 * Description:链接到{@link FMLClientSetupEvent}
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface ClientSetup {
}
