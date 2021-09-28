package com.android.systemui.util.wakelock;

import android.content.Context;
import com.android.systemui.util.wakelock.DelayedWakeLock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class DelayedWakeLock_Builder_Factory implements Factory<DelayedWakeLock.Builder> {
    private final Provider<Context> contextProvider;

    public DelayedWakeLock_Builder_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public DelayedWakeLock.Builder get() {
        return newInstance(this.contextProvider.get());
    }

    public static DelayedWakeLock_Builder_Factory create(Provider<Context> provider) {
        return new DelayedWakeLock_Builder_Factory(provider);
    }

    public static DelayedWakeLock.Builder newInstance(Context context) {
        return new DelayedWakeLock.Builder(context);
    }
}
