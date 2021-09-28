package com.android.systemui.doze;

import android.app.AlarmManager;
import android.os.Handler;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozePauser_Factory implements Factory<DozePauser> {
    private final Provider<AlarmManager> alarmManagerProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<AlwaysOnDisplayPolicy> policyProvider;

    public DozePauser_Factory(Provider<Handler> provider, Provider<AlarmManager> provider2, Provider<AlwaysOnDisplayPolicy> provider3) {
        this.handlerProvider = provider;
        this.alarmManagerProvider = provider2;
        this.policyProvider = provider3;
    }

    @Override // javax.inject.Provider
    public DozePauser get() {
        return newInstance(this.handlerProvider.get(), this.alarmManagerProvider.get(), this.policyProvider.get());
    }

    public static DozePauser_Factory create(Provider<Handler> provider, Provider<AlarmManager> provider2, Provider<AlwaysOnDisplayPolicy> provider3) {
        return new DozePauser_Factory(provider, provider2, provider3);
    }

    public static DozePauser newInstance(Handler handler, AlarmManager alarmManager, AlwaysOnDisplayPolicy alwaysOnDisplayPolicy) {
        return new DozePauser(handler, alarmManager, alwaysOnDisplayPolicy);
    }
}
