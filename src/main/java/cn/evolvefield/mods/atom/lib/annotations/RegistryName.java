package cn.evolvefield.mods.atom.lib.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Project: atomlib
 * Author: cnlimiter
 * Date: 2022/11/13 2:10
 * Description:注册名注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RegistryName {
    String value();
}
