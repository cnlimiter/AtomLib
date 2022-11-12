package cn.evolvefield.mods.atom.lib.utils.java;

import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

/**
 * Project: atomlib
 * Author: cnlimiter
 * Date: 2022/11/13 2:25
 * Description:
 */
public class RateTicker
        implements BooleanSupplier {
    private final long timerMS;

    private boolean called;
    private long lastCall;

    public RateTicker(long timer, TimeUnit unit) {
        this.timerMS = TimeUnit.MILLISECONDS.convert(timer, unit);
    }

    @Override
    public boolean getAsBoolean() {
        long now = System.currentTimeMillis();

        if (now - lastCall > timerMS || !called) {
            called = true;
            lastCall = System.currentTimeMillis();
            return true;
        }

        return false;
    }
}
