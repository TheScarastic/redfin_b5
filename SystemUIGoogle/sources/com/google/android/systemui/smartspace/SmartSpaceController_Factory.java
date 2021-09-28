package com.google.android.systemui.smartspace;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Handler;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.dump.DumpManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SmartSpaceController_Factory implements Factory<SmartSpaceController> {
    private final Provider<AlarmManager> alarmManagerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;

    public SmartSpaceController_Factory(Provider<Context> provider, Provider<KeyguardUpdateMonitor> provider2, Provider<Handler> provider3, Provider<AlarmManager> provider4, Provider<DumpManager> provider5) {
        this.contextProvider = provider;
        this.keyguardUpdateMonitorProvider = provider2;
        this.handlerProvider = provider3;
        this.alarmManagerProvider = provider4;
        this.dumpManagerProvider = provider5;
    }

    @Override // javax.inject.Provider
    public SmartSpaceController get() {
        return newInstance(this.contextProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.handlerProvider.get(), this.alarmManagerProvider.get(), this.dumpManagerProvider.get());
    }

    public static SmartSpaceController_Factory create(Provider<Context> provider, Provider<KeyguardUpdateMonitor> provider2, Provider<Handler> provider3, Provider<AlarmManager> provider4, Provider<DumpManager> provider5) {
        return new SmartSpaceController_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static SmartSpaceController newInstance(Context context, KeyguardUpdateMonitor keyguardUpdateMonitor, Handler handler, AlarmManager alarmManager, DumpManager dumpManager) {
        return new SmartSpaceController(context, keyguardUpdateMonitor, handler, alarmManager, dumpManager);
    }
}
