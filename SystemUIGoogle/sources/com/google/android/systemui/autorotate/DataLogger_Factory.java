package com.google.android.systemui.autorotate;

import android.app.StatsManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class DataLogger_Factory implements Factory<DataLogger> {
    private final Provider<StatsManager> statsManagerProvider;

    public DataLogger_Factory(Provider<StatsManager> provider) {
        this.statsManagerProvider = provider;
    }

    @Override // javax.inject.Provider
    public DataLogger get() {
        return newInstance(this.statsManagerProvider.get());
    }

    public static DataLogger_Factory create(Provider<StatsManager> provider) {
        return new DataLogger_Factory(provider);
    }

    public static DataLogger newInstance(StatsManager statsManager) {
        return new DataLogger(statsManager);
    }
}
