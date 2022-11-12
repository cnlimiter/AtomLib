package cn.evolvefield.mods.atom.lib.api.fml;

/**
 * Project: atomlib
 * Author: cnlimiter
 * Date: 2022/11/13 2:15
 * Description:
 */
public interface IRegisterListener {
    default void onPostRegistered() {
    }

    default void onPreRegistered() {
    }
}
