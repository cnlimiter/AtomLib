package cn.evolvefield.mods.atom.lib.utils.reflect;

import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/7/26 13:38
 * Version: 1.0
 */
public class ProtectedMethodAccess<C, T> {
    private LazyOptional<Method> method;

    /**
     * 一种访问受保护字段的方法
     *
     * @param classContainingField 这是该字段所属的类.
     * @param fieldName            这是字段的 SRG 名称（未映射的名称）.
     **/
    public ProtectedMethodAccess(Class<C> classContainingField, String fieldName, Class<?>... args) {
        this.method = LazyOptional.of(() ->
        {
            Method innerField = ObfuscationReflectionHelper.findMethod(classContainingField, fieldName, args);
            innerField.setAccessible(true);
            return innerField;
        });
    }

    /**
     * 返回字段的值，如果有错误则返回 null.
     **/
    @SuppressWarnings("unchecked")
    public T getValue(C instance, Object... args) {
        try {
            if (method.resolve().isPresent())
                return (T) method.resolve().get().invoke(instance, args);
            else
                return (T) method.resolve().orElseThrow();
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
