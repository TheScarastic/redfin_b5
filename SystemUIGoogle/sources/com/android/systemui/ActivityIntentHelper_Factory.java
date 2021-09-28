package com.android.systemui;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ActivityIntentHelper_Factory implements Factory<ActivityIntentHelper> {
    private final Provider<Context> contextProvider;

    public ActivityIntentHelper_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public ActivityIntentHelper get() {
        return newInstance(this.contextProvider.get());
    }

    public static ActivityIntentHelper_Factory create(Provider<Context> provider) {
        return new ActivityIntentHelper_Factory(provider);
    }

    public static ActivityIntentHelper newInstance(Context context) {
        return new ActivityIntentHelper(context);
    }
}
