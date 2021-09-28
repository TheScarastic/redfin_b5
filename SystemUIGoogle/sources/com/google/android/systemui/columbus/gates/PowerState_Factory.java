package com.google.android.systemui.columbus.gates;

import android.content.Context;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class PowerState_Factory implements Factory<PowerState> {
    private final Provider<Context> contextProvider;
    private final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public PowerState_Factory(Provider<Context> provider, Provider<WakefulnessLifecycle> provider2) {
        this.contextProvider = provider;
        this.wakefulnessLifecycleProvider = provider2;
    }

    @Override // javax.inject.Provider
    public PowerState get() {
        return newInstance(this.contextProvider.get(), DoubleCheck.lazy(this.wakefulnessLifecycleProvider));
    }

    public static PowerState_Factory create(Provider<Context> provider, Provider<WakefulnessLifecycle> provider2) {
        return new PowerState_Factory(provider, provider2);
    }

    public static PowerState newInstance(Context context, Lazy<WakefulnessLifecycle> lazy) {
        return new PowerState(context, lazy);
    }
}
