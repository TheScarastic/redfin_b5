package com.android.systemui.dagger;

import android.app.AlarmManager;
import android.content.Context;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideAlarmManagerFactory implements Factory<AlarmManager> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideAlarmManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public AlarmManager get() {
        return provideAlarmManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideAlarmManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideAlarmManagerFactory(provider);
    }

    public static AlarmManager provideAlarmManager(Context context) {
        return (AlarmManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideAlarmManager(context));
    }
}
