package cn.evolvefield.mods.atom.lib.utils.reflect;

import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/7/26 15:14
 * Version: 1.0
 */
public class ProtectedFieldAccess<T, C> {
    private LazyOptional<Field> field;

    /**
     * 一种访问受保护字段的方法。
     *
     * @param classContainingField 这是该字段所属的类.
     * @param fieldName            这是字段的 SRG 名称（未映射的名称）.
     **/
    public ProtectedFieldAccess(Class<C> classContainingField, String fieldName) {
        this.field = LazyOptional.of(() ->
        {
            Field innerField = ObfuscationReflectionHelper.findField(classContainingField, fieldName);
            innerField.setAccessible(true);
            return innerField;
        });
    }

    /**
     * 返回字段的值，如果有错误则返回 null.
     **/
    @SuppressWarnings("unchecked")
    public T getValue(C instance) {
        try {
            return (T) field.resolve().get().get(instance);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
