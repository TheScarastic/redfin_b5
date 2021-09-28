package com.google.android.systemui.columbus.gates;

import android.content.Context;
import android.telephony.TelephonyManager;
import com.android.systemui.telephony.TelephonyListenerManager;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class TelephonyActivity_Factory implements Factory<TelephonyActivity> {
    private final Provider<Context> contextProvider;
    private final Provider<TelephonyListenerManager> telephonyListenerManagerProvider;
    private final Provider<TelephonyManager> telephonyManagerProvider;

    public TelephonyActivity_Factory(Provider<Context> provider, Provider<TelephonyManager> provider2, Provider<TelephonyListenerManager> provider3) {
        this.contextProvider = provider;
        this.telephonyManagerProvider = provider2;
        this.telephonyListenerManagerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public TelephonyActivity get() {
        return newInstance(this.contextProvider.get(), DoubleCheck.lazy(this.telephonyManagerProvider), DoubleCheck.lazy(this.telephonyListenerManagerProvider));
    }

    public static TelephonyActivity_Factory create(Provider<Context> provider, Provider<TelephonyManager> provider2, Provider<TelephonyListenerManager> provider3) {
        return new TelephonyActivity_Factory(provider, provider2, provider3);
    }

    public static TelephonyActivity newInstance(Context context, Lazy<TelephonyManager> lazy, Lazy<TelephonyListenerManager> lazy2) {
        return new TelephonyActivity(context, lazy, lazy2);
    }
}
