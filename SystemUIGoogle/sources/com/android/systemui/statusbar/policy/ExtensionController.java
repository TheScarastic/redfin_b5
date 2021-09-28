package com.android.systemui.statusbar.policy;

import android.content.Context;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public interface ExtensionController {

    /* loaded from: classes.dex */
    public interface Extension<T> {
        void addCallback(Consumer<T> consumer);

        void clearItem(boolean z);

        void destroy();

        T get();

        Context getContext();
    }

    /* loaded from: classes.dex */
    public interface ExtensionBuilder<T> {
        Extension<T> build();

        ExtensionBuilder<T> withCallback(Consumer<T> consumer);

        ExtensionBuilder<T> withDefault(Supplier<T> supplier);

        <P extends T> ExtensionBuilder<T> withPlugin(Class<P> cls);

        <P> ExtensionBuilder<T> withPlugin(Class<P> cls, String str, PluginConverter<T, P> pluginConverter);

        ExtensionBuilder<T> withTunerFactory(TunerFactory<T> tunerFactory);
    }

    /* loaded from: classes.dex */
    public interface PluginConverter<T, P> {
        T getInterfaceFromPlugin(P p);
    }

    /* loaded from: classes.dex */
    public interface TunerFactory<T> {
        T create(Map<String, String> map);

        String[] keys();
    }

    <T> ExtensionBuilder<T> newExtension(Class<T> cls);
}
