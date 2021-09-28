package com.android.wallpaper.module;
/* loaded from: classes.dex */
public class InjectorProvider {
    public static Injector sInjector;

    public static synchronized Injector getInjector() {
        Injector injector;
        synchronized (InjectorProvider.class) {
            injector = sInjector;
        }
        return injector;
    }
}
