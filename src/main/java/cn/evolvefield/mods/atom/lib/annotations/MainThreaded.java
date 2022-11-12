package cn.evolvefield.mods.atom.lib.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Project: atomlib
 * Author: cnlimiter
 * Date: 2022/11/13 2:47
 * Description:用于网络的线程选择
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MainThreaded {
    boolean value() default true;
}
