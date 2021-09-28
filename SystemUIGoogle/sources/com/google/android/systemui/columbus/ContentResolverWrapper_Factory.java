package com.google.android.systemui.columbus;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ContentResolverWrapper_Factory implements Factory<ContentResolverWrapper> {
    private final Provider<Context> contextProvider;

    public ContentResolverWrapper_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public ContentResolverWrapper get() {
        return newInstance(this.contextProvider.get());
    }

    public static ContentResolverWrapper_Factory create(Provider<Context> provider) {
        return new ContentResolverWrapper_Factory(provider);
    }

    public static ContentResolverWrapper newInstance(Context context) {
        return new ContentResolverWrapper(context);
    }
}
