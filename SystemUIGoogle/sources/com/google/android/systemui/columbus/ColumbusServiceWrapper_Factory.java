package com.google.android.systemui.columbus;

import com.google.android.systemui.columbus.actions.SettingsAction;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ColumbusServiceWrapper_Factory implements Factory<ColumbusServiceWrapper> {
    private final Provider<ColumbusService> columbusServiceProvider;
    private final Provider<ColumbusSettings> columbusSettingsProvider;
    private final Provider<SettingsAction> settingsActionProvider;

    public ColumbusServiceWrapper_Factory(Provider<ColumbusSettings> provider, Provider<ColumbusService> provider2, Provider<SettingsAction> provider3) {
        this.columbusSettingsProvider = provider;
        this.columbusServiceProvider = provider2;
        this.settingsActionProvider = provider3;
    }

    @Override // javax.inject.Provider
    public ColumbusServiceWrapper get() {
        return newInstance(this.columbusSettingsProvider.get(), DoubleCheck.lazy(this.columbusServiceProvider), DoubleCheck.lazy(this.settingsActionProvider));
    }

    public static ColumbusServiceWrapper_Factory create(Provider<ColumbusSettings> provider, Provider<ColumbusService> provider2, Provider<SettingsAction> provider3) {
        return new ColumbusServiceWrapper_Factory(provider, provider2, provider3);
    }

    public static ColumbusServiceWrapper newInstance(ColumbusSettings columbusSettings, Lazy<ColumbusService> lazy, Lazy<SettingsAction> lazy2) {
        return new ColumbusServiceWrapper(columbusSettings, lazy, lazy2);
    }
}
