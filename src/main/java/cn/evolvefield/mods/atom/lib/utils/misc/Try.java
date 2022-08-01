package cn.evolvefield.mods.atom.lib.utils.misc;

import java.util.function.Supplier;

public class Try<T> {
    private final Supplier<T> sup;

    public Try(Supplier<T> sup) {
        this.sup = sup;
    }

    public T get() {
        return sup.get();
    }

    public T getOrElse(T defaultValue) {
        T value;
        try {
            value = get();
        } catch (Exception ignored) {
            value = defaultValue;
        }
        return value;
    }

}
