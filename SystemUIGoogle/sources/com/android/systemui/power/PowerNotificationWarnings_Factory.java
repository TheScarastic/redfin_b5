package com.android.systemui.power;

import android.content.Context;
import com.android.systemui.plugins.ActivityStarter;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PowerNotificationWarnings_Factory implements Factory<PowerNotificationWarnings> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<Context> contextProvider;

    public PowerNotificationWarnings_Factory(Provider<Context> provider, Provider<ActivityStarter> provider2) {
        this.contextProvider = provider;
        this.activityStarterProvider = provider2;
    }

    @Override // javax.inject.Provider
    public PowerNotificationWarnings get() {
        return newInstance(this.contextProvider.get(), this.activityStarterProvider.get());
    }

    public static PowerNotificationWarnings_Factory create(Provider<Context> provider, Provider<ActivityStarter> provider2) {
        return new PowerNotificationWarnings_Factory(provider, provider2);
    }

    public static PowerNotificationWarnings newInstance(Context context, ActivityStarter activityStarter) {
        return new PowerNotificationWarnings(context, activityStarter);
    }
}
