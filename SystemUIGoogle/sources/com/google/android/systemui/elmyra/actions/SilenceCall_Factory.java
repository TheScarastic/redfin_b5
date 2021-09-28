package com.google.android.systemui.elmyra.actions;

import android.content.Context;
import com.android.systemui.telephony.TelephonyListenerManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SilenceCall_Factory implements Factory<SilenceCall> {
    private final Provider<Context> contextProvider;
    private final Provider<TelephonyListenerManager> telephonyListenerManagerProvider;

    public SilenceCall_Factory(Provider<Context> provider, Provider<TelephonyListenerManager> provider2) {
        this.contextProvider = provider;
        this.telephonyListenerManagerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public SilenceCall get() {
        return newInstance(this.contextProvider.get(), this.telephonyListenerManagerProvider.get());
    }

    public static SilenceCall_Factory create(Provider<Context> provider, Provider<TelephonyListenerManager> provider2) {
        return new SilenceCall_Factory(provider, provider2);
    }

    public static SilenceCall newInstance(Context context, TelephonyListenerManager telephonyListenerManager) {
        return new SilenceCall(context, telephonyListenerManager);
    }
}
