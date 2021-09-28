package com.android.systemui.appops;

import android.content.Context;
import android.media.AudioManager;
import android.os.Looper;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AppOpsControllerImpl_Factory implements Factory<AppOpsControllerImpl> {
    private final Provider<AudioManager> audioManagerProvider;
    private final Provider<Looper> bgLooperProvider;
    private final Provider<SystemClock> clockProvider;
    private final Provider<Context> contextProvider;
    private final Provider<BroadcastDispatcher> dispatcherProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<IndividualSensorPrivacyController> sensorPrivacyControllerProvider;

    public AppOpsControllerImpl_Factory(Provider<Context> provider, Provider<Looper> provider2, Provider<DumpManager> provider3, Provider<AudioManager> provider4, Provider<IndividualSensorPrivacyController> provider5, Provider<BroadcastDispatcher> provider6, Provider<SystemClock> provider7) {
        this.contextProvider = provider;
        this.bgLooperProvider = provider2;
        this.dumpManagerProvider = provider3;
        this.audioManagerProvider = provider4;
        this.sensorPrivacyControllerProvider = provider5;
        this.dispatcherProvider = provider6;
        this.clockProvider = provider7;
    }

    @Override // javax.inject.Provider
    public AppOpsControllerImpl get() {
        return newInstance(this.contextProvider.get(), this.bgLooperProvider.get(), this.dumpManagerProvider.get(), this.audioManagerProvider.get(), this.sensorPrivacyControllerProvider.get(), this.dispatcherProvider.get(), this.clockProvider.get());
    }

    public static AppOpsControllerImpl_Factory create(Provider<Context> provider, Provider<Looper> provider2, Provider<DumpManager> provider3, Provider<AudioManager> provider4, Provider<IndividualSensorPrivacyController> provider5, Provider<BroadcastDispatcher> provider6, Provider<SystemClock> provider7) {
        return new AppOpsControllerImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static AppOpsControllerImpl newInstance(Context context, Looper looper, DumpManager dumpManager, AudioManager audioManager, IndividualSensorPrivacyController individualSensorPrivacyController, BroadcastDispatcher broadcastDispatcher, SystemClock systemClock) {
        return new AppOpsControllerImpl(context, looper, dumpManager, audioManager, individualSensorPrivacyController, broadcastDispatcher, systemClock);
    }
}
