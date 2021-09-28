package com.android.systemui.statusbar.notification.icon;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class IconBuilder_Factory implements Factory<IconBuilder> {
    private final Provider<Context> contextProvider;

    public IconBuilder_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public IconBuilder get() {
        return newInstance(this.contextProvider.get());
    }

    public static IconBuilder_Factory create(Provider<Context> provider) {
        return new IconBuilder_Factory(provider);
    }

    public static IconBuilder newInstance(Context context) {
        return new IconBuilder(context);
    }
}
