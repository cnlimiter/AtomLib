package cn.evolvefield.mods.atom.lib.utils.java;

import cn.evolvefield.mods.atom.lib.annotations.MainThreaded;

/**
 * Project: atomlib
 * Author: cnlimiter
 * Date: 2022/11/13 2:46
 * Description:
 */
public class Threading {
    public static boolean isMainThreaded(Class<?> type) {
        MainThreaded mt = type.getAnnotation(MainThreaded.class);
        if (mt == null)
            mt = type.getDeclaredAnnotation(MainThreaded.class);
        return mt != null && mt.value();
    }

    public static boolean isRunning(Thread thr) {
        return thr != null && thr.isAlive();
    }

    public static Thread createAndStart(Runnable run) {
        Thread thr = new Thread(run);
        thr.start();
        return thr;
    }

    public static Thread createAndStart(String name, Runnable run) {
        Thread thr = new Thread(run);
        thr.setName(name);
        thr.start();
        return thr;
    }

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
