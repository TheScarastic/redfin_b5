package com.android.systemui.util.leak;

import android.content.Context;
import android.os.Looper;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class GarbageMonitor_Factory implements Factory<GarbageMonitor> {
    private final Provider<Looper> bgLooperProvider;
    private final Provider<Context> contextProvider;
    private final Provider<LeakDetector> leakDetectorProvider;
    private final Provider<LeakReporter> leakReporterProvider;

    public GarbageMonitor_Factory(Provider<Context> provider, Provider<Looper> provider2, Provider<LeakDetector> provider3, Provider<LeakReporter> provider4) {
        this.contextProvider = provider;
        this.bgLooperProvider = provider2;
        this.leakDetectorProvider = provider3;
        this.leakReporterProvider = provider4;
    }

    @Override // javax.inject.Provider
    public GarbageMonitor get() {
        return newInstance(this.contextProvider.get(), this.bgLooperProvider.get(), this.leakDetectorProvider.get(), this.leakReporterProvider.get());
    }

    public static GarbageMonitor_Factory create(Provider<Context> provider, Provider<Looper> provider2, Provider<LeakDetector> provider3, Provider<LeakReporter> provider4) {
        return new GarbageMonitor_Factory(provider, provider2, provider3, provider4);
    }

    public static GarbageMonitor newInstance(Context context, Looper looper, LeakDetector leakDetector, LeakReporter leakReporter) {
        return new GarbageMonitor(context, looper, leakDetector, leakReporter);
    }
}
