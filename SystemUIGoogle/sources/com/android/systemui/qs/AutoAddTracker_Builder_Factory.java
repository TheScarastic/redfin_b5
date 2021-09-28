package com.android.systemui.qs;

import android.content.Context;
import com.android.systemui.qs.AutoAddTracker;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AutoAddTracker_Builder_Factory implements Factory<AutoAddTracker.Builder> {
    private final Provider<Context> contextProvider;

    public AutoAddTracker_Builder_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public AutoAddTracker.Builder get() {
        return newInstance(this.contextProvider.get());
    }

    public static AutoAddTracker_Builder_Factory create(Provider<Context> provider) {
        return new AutoAddTracker_Builder_Factory(provider);
    }

    public static AutoAddTracker.Builder newInstance(Context context) {
        return new AutoAddTracker.Builder(context);
    }
}
