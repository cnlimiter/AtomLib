package cn.evolvefield.mods.atom.lib.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Project: atomlib
 * Author: cnlimiter
 * Date: 2022/11/13 2:28
 * Description:一个筛选注解，用于满足条件的启用
 */
@Retention(RUNTIME)
@Target({
        FIELD,
        METHOD
})
public @interface OnlyIf {

    Class<?> owner();

    String member() default "";

    boolean invert() default false;
}
