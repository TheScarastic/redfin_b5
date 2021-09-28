package com.android.systemui.statusbar.policy;

import android.app.NotificationManager;
import android.content.Context;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class BatteryStateNotifier_Factory implements Factory<BatteryStateNotifier> {
    private final Provider<Context> contextProvider;
    private final Provider<BatteryController> controllerProvider;
    private final Provider<DelayableExecutor> delayableExecutorProvider;
    private final Provider<NotificationManager> noManProvider;

    public BatteryStateNotifier_Factory(Provider<BatteryController> provider, Provider<NotificationManager> provider2, Provider<DelayableExecutor> provider3, Provider<Context> provider4) {
        this.controllerProvider = provider;
        this.noManProvider = provider2;
        this.delayableExecutorProvider = provider3;
        this.contextProvider = provider4;
    }

    @Override // javax.inject.Provider
    public BatteryStateNotifier get() {
        return newInstance(this.controllerProvider.get(), this.noManProvider.get(), this.delayableExecutorProvider.get(), this.contextProvider.get());
    }

    public static BatteryStateNotifier_Factory create(Provider<BatteryController> provider, Provider<NotificationManager> provider2, Provider<DelayableExecutor> provider3, Provider<Context> provider4) {
        return new BatteryStateNotifier_Factory(provider, provider2, provider3, provider4);
    }

    public static BatteryStateNotifier newInstance(BatteryController batteryController, NotificationManager notificationManager, DelayableExecutor delayableExecutor, Context context) {
        return new BatteryStateNotifier(batteryController, notificationManager, delayableExecutor, context);
    }
}
