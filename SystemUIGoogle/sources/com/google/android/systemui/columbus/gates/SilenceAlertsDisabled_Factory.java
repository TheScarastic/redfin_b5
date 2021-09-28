package com.google.android.systemui.columbus.gates;

import android.content.Context;
import com.google.android.systemui.columbus.ColumbusSettings;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SilenceAlertsDisabled_Factory implements Factory<SilenceAlertsDisabled> {
    private final Provider<ColumbusSettings> columbusSettingsProvider;
    private final Provider<Context> contextProvider;

    public SilenceAlertsDisabled_Factory(Provider<Context> provider, Provider<ColumbusSettings> provider2) {
        this.contextProvider = provider;
        this.columbusSettingsProvider = provider2;
    }

    @Override // javax.inject.Provider
    public SilenceAlertsDisabled get() {
        return newInstance(this.contextProvider.get(), this.columbusSettingsProvider.get());
    }

    public static SilenceAlertsDisabled_Factory create(Provider<Context> provider, Provider<ColumbusSettings> provider2) {
        return new SilenceAlertsDisabled_Factory(provider, provider2);
    }

    public static SilenceAlertsDisabled newInstance(Context context, ColumbusSettings columbusSettings) {
        return new SilenceAlertsDisabled(context, columbusSettings);
    }
}
