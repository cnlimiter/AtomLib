package cn.evolvefield.mods.atom.lib.utils.java;

import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * Project: atomlib
 * Author: cnlimiter
 * Date: 2022/11/13 2:24
 * Description:
 */
public class Fetcher<T>
        implements Supplier<T> {
    private static final BooleanSupplier ALWAYS_FALSE = () -> false;
    private final Supplier<T> fetch;
    private final BooleanSupplier shouldRefetch;
    private boolean cached = false;
    private T cache;

    public Fetcher(Supplier<T> fetch, BooleanSupplier shouldRefetch) {
        this.fetch = fetch;
        this.shouldRefetch = shouldRefetch;
    }

    public static <T> Fetcher<T> fetchOnce(Supplier<T> origin) {
        return new Fetcher<>(origin, ALWAYS_FALSE);
    }

    public static <T> Fetcher<T> refetchAtRate(Supplier<T> origin, long refetchTime, TimeUnit refetchUnit) {
        return new Fetcher<>(origin, new RateTicker(refetchTime, refetchUnit));
    }

    @Override
    public T get() {
        if (!cached || shouldRefetch.getAsBoolean()) {
            cache = fetch.get();
            cached = true;
        }
        return cache;
    }
}
