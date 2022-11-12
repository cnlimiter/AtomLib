package cn.evolvefield.mods.atom.lib.utils.java;

import java.util.function.Supplier;

/**
 * @param <T> The type of value that should be returned
 * @author Tapio
 */
public class Try<T> {
    private final Supplier<T> sup;

    public Try(Supplier<T> sup) {
        this.sup = sup;
    }

    /**
     * @return Directly returns the value provided by the supplier, throwables may appear.
     */
    public T get() {
        return sup.get();
    }

    /**
     * @param defaultValue The default value to return if exception appeared while getting the exact value.
     * @return The exact value if no exceptions happen, or else the default one.
     */
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
