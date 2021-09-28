package com.android.systemui.qs.external;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class CustomTileStatePersister_Factory implements Factory<CustomTileStatePersister> {
    private final Provider<Context> contextProvider;

    public CustomTileStatePersister_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public CustomTileStatePersister get() {
        return newInstance(this.contextProvider.get());
    }

    public static CustomTileStatePersister_Factory create(Provider<Context> provider) {
        return new CustomTileStatePersister_Factory(provider);
    }

    public static CustomTileStatePersister newInstance(Context context) {
        return new CustomTileStatePersister(context);
    }
}
