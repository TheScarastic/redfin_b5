package com.google.android.systemui.columbus.actions;

import android.app.IActivityManager;
import android.content.Context;
import com.google.android.systemui.columbus.gates.SilenceAlertsDisabled;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SnoozeAlarm_Factory implements Factory<SnoozeAlarm> {
    private final Provider<IActivityManager> activityManagerServiceProvider;
    private final Provider<Context> contextProvider;
    private final Provider<SilenceAlertsDisabled> silenceAlertsDisabledProvider;

    public SnoozeAlarm_Factory(Provider<Context> provider, Provider<SilenceAlertsDisabled> provider2, Provider<IActivityManager> provider3) {
        this.contextProvider = provider;
        this.silenceAlertsDisabledProvider = provider2;
        this.activityManagerServiceProvider = provider3;
    }

    @Override // javax.inject.Provider
    public SnoozeAlarm get() {
        return newInstance(this.contextProvider.get(), this.silenceAlertsDisabledProvider.get(), this.activityManagerServiceProvider.get());
    }

    public static SnoozeAlarm_Factory create(Provider<Context> provider, Provider<SilenceAlertsDisabled> provider2, Provider<IActivityManager> provider3) {
        return new SnoozeAlarm_Factory(provider, provider2, provider3);
    }

    public static SnoozeAlarm newInstance(Context context, SilenceAlertsDisabled silenceAlertsDisabled, IActivityManager iActivityManager) {
        return new SnoozeAlarm(context, silenceAlertsDisabled, iActivityManager);
    }
}
