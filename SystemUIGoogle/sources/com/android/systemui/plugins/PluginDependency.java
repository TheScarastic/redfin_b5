package com.android.systemui.plugins;

import com.android.systemui.plugins.annotations.ProvidesInterface;
@ProvidesInterface(version = 1)
/* loaded from: classes.dex */
public class PluginDependency {
    public static final int VERSION = 1;
    static DependencyProvider sProvider;

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static abstract class DependencyProvider {
        abstract <T> T get(Plugin plugin, Class<T> cls);
    }

    public static <T> T get(Plugin plugin, Class<T> cls) {
        return (T) sProvider.get(plugin, cls);
    }
}
