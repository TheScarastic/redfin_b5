package com.android.systemui.statusbar.notification.collection;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.NotificationInteractionTracker;
import com.android.systemui.statusbar.notification.collection.listbuilder.ShadeListBuilderLogger;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ShadeListBuilder_Factory implements Factory<ShadeListBuilder> {
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<NotificationInteractionTracker> interactionTrackerProvider;
    private final Provider<ShadeListBuilderLogger> loggerProvider;
    private final Provider<SystemClock> systemClockProvider;

    public ShadeListBuilder_Factory(Provider<SystemClock> provider, Provider<ShadeListBuilderLogger> provider2, Provider<DumpManager> provider3, Provider<NotificationInteractionTracker> provider4) {
        this.systemClockProvider = provider;
        this.loggerProvider = provider2;
        this.dumpManagerProvider = provider3;
        this.interactionTrackerProvider = provider4;
    }

    @Override // javax.inject.Provider
    public ShadeListBuilder get() {
        return newInstance(this.systemClockProvider.get(), this.loggerProvider.get(), this.dumpManagerProvider.get(), this.interactionTrackerProvider.get());
    }

    public static ShadeListBuilder_Factory create(Provider<SystemClock> provider, Provider<ShadeListBuilderLogger> provider2, Provider<DumpManager> provider3, Provider<NotificationInteractionTracker> provider4) {
        return new ShadeListBuilder_Factory(provider, provider2, provider3, provider4);
    }

    public static ShadeListBuilder newInstance(SystemClock systemClock, ShadeListBuilderLogger shadeListBuilderLogger, DumpManager dumpManager, NotificationInteractionTracker notificationInteractionTracker) {
        return new ShadeListBuilder(systemClock, shadeListBuilderLogger, dumpManager, notificationInteractionTracker);
    }
}
