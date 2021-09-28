package com.google.android.systemui.elmyra.gates;

import android.content.Context;
import com.android.systemui.telephony.TelephonyListenerManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class TelephonyActivity_Factory implements Factory<TelephonyActivity> {
    private final Provider<Context> contextProvider;
    private final Provider<TelephonyListenerManager> telephonyListenerManagerProvider;

    public TelephonyActivity_Factory(Provider<Context> provider, Provider<TelephonyListenerManager> provider2) {
        this.contextProvider = provider;
        this.telephonyListenerManagerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public TelephonyActivity get() {
        return newInstance(this.contextProvider.get(), this.telephonyListenerManagerProvider.get());
    }

    public static TelephonyActivity_Factory create(Provider<Context> provider, Provider<TelephonyListenerManager> provider2) {
        return new TelephonyActivity_Factory(provider, provider2);
    }

    public static TelephonyActivity newInstance(Context context, TelephonyListenerManager telephonyListenerManager) {
        return new TelephonyActivity(context, telephonyListenerManager);
    }
}
