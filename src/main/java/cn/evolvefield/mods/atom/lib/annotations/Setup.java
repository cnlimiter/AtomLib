package cn.evolvefield.mods.atom.lib.annotations;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Project: atomlib
 * Author: cnlimiter
 * Date: 2022/11/13 2:39
 * Description:链接到{@link FMLCommonSetupEvent}
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Setup {
    Dist[] side() default {
            Dist.CLIENT,
            Dist.DEDICATED_SERVER
    };
}
