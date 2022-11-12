package cn.evolvefield.mods.atom.lib.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Project: atomlib
 * Author: cnlimiter
 * Date: 2022/11/13 2:17
 * Description:
 */

@Retention(RUNTIME)
@Target({
        TYPE,
        METHOD
})
public @interface SimplyRegister {
    String prefix() default "";

}
