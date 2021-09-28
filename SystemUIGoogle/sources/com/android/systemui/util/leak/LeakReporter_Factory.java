package com.android.systemui.util.leak;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class LeakReporter_Factory implements Factory<LeakReporter> {
    private final Provider<Context> contextProvider;
    private final Provider<LeakDetector> leakDetectorProvider;
    private final Provider<String> leakReportEmailProvider;

    public LeakReporter_Factory(Provider<Context> provider, Provider<LeakDetector> provider2, Provider<String> provider3) {
        this.contextProvider = provider;
        this.leakDetectorProvider = provider2;
        this.leakReportEmailProvider = provider3;
    }

    @Override // javax.inject.Provider
    public LeakReporter get() {
        return newInstance(this.contextProvider.get(), this.leakDetectorProvider.get(), this.leakReportEmailProvider.get());
    }

    public static LeakReporter_Factory create(Provider<Context> provider, Provider<LeakDetector> provider2, Provider<String> provider3) {
        return new LeakReporter_Factory(provider, provider2, provider3);
    }

    public static LeakReporter newInstance(Context context, LeakDetector leakDetector, String str) {
        return new LeakReporter(context, leakDetector, str);
    }
}
