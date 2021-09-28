package com.google.android.systemui.columbus.actions;

import android.content.Context;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.google.android.systemui.columbus.gates.SilenceAlertsDisabled;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SilenceCall_Factory implements Factory<SilenceCall> {
    private final Provider<Context> contextProvider;
    private final Provider<SilenceAlertsDisabled> silenceAlertsDisabledProvider;
    private final Provider<TelecomManager> telecomManagerProvider;
    private final Provider<TelephonyListenerManager> telephonyListenerManagerProvider;
    private final Provider<TelephonyManager> telephonyManagerProvider;

    public SilenceCall_Factory(Provider<Context> provider, Provider<SilenceAlertsDisabled> provider2, Provider<TelecomManager> provider3, Provider<TelephonyManager> provider4, Provider<TelephonyListenerManager> provider5) {
        this.contextProvider = provider;
        this.silenceAlertsDisabledProvider = provider2;
        this.telecomManagerProvider = provider3;
        this.telephonyManagerProvider = provider4;
        this.telephonyListenerManagerProvider = provider5;
    }

    @Override // javax.inject.Provider
    public SilenceCall get() {
        return newInstance(this.contextProvider.get(), this.silenceAlertsDisabledProvider.get(), DoubleCheck.lazy(this.telecomManagerProvider), DoubleCheck.lazy(this.telephonyManagerProvider), DoubleCheck.lazy(this.telephonyListenerManagerProvider));
    }

    public static SilenceCall_Factory create(Provider<Context> provider, Provider<SilenceAlertsDisabled> provider2, Provider<TelecomManager> provider3, Provider<TelephonyManager> provider4, Provider<TelephonyListenerManager> provider5) {
        return new SilenceCall_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static SilenceCall newInstance(Context context, SilenceAlertsDisabled silenceAlertsDisabled, Lazy<TelecomManager> lazy, Lazy<TelephonyManager> lazy2, Lazy<TelephonyListenerManager> lazy3) {
        return new SilenceCall(context, silenceAlertsDisabled, lazy, lazy2, lazy3);
    }
}
