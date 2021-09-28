package com.android.systemui.util.settings;

import android.content.ContentResolver;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SystemSettingsImpl_Factory implements Factory<SystemSettingsImpl> {
    private final Provider<ContentResolver> contentResolverProvider;

    public SystemSettingsImpl_Factory(Provider<ContentResolver> provider) {
        this.contentResolverProvider = provider;
    }

    @Override // javax.inject.Provider
    public SystemSettingsImpl get() {
        return newInstance(this.contentResolverProvider.get());
    }

    public static SystemSettingsImpl_Factory create(Provider<ContentResolver> provider) {
        return new SystemSettingsImpl_Factory(provider);
    }

    public static SystemSettingsImpl newInstance(ContentResolver contentResolver) {
        return new SystemSettingsImpl(contentResolver);
    }
}
