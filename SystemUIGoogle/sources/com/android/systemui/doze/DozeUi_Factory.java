package com.android.systemui.doze;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Handler;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.wakelock.WakeLock;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeUi_Factory implements Factory<DozeUi> {
    private final Provider<AlarmManager> alarmManagerProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DozeLog> dozeLogProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<DozeHost> hostProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<DozeParameters> paramsProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<TunerService> tunerServiceProvider;
    private final Provider<WakeLock> wakeLockProvider;

    public DozeUi_Factory(Provider<Context> provider, Provider<AlarmManager> provider2, Provider<WakeLock> provider3, Provider<DozeHost> provider4, Provider<Handler> provider5, Provider<DozeParameters> provider6, Provider<KeyguardUpdateMonitor> provider7, Provider<DozeLog> provider8, Provider<TunerService> provider9, Provider<StatusBarStateController> provider10, Provider<ConfigurationController> provider11) {
        this.contextProvider = provider;
        this.alarmManagerProvider = provider2;
        this.wakeLockProvider = provider3;
        this.hostProvider = provider4;
        this.handlerProvider = provider5;
        this.paramsProvider = provider6;
        this.keyguardUpdateMonitorProvider = provider7;
        this.dozeLogProvider = provider8;
        this.tunerServiceProvider = provider9;
        this.statusBarStateControllerProvider = provider10;
        this.configurationControllerProvider = provider11;
    }

    @Override // javax.inject.Provider
    public DozeUi get() {
        return newInstance(this.contextProvider.get(), this.alarmManagerProvider.get(), this.wakeLockProvider.get(), this.hostProvider.get(), this.handlerProvider.get(), this.paramsProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.dozeLogProvider.get(), this.tunerServiceProvider.get(), DoubleCheck.lazy(this.statusBarStateControllerProvider), this.configurationControllerProvider.get());
    }

    public static DozeUi_Factory create(Provider<Context> provider, Provider<AlarmManager> provider2, Provider<WakeLock> provider3, Provider<DozeHost> provider4, Provider<Handler> provider5, Provider<DozeParameters> provider6, Provider<KeyguardUpdateMonitor> provider7, Provider<DozeLog> provider8, Provider<TunerService> provider9, Provider<StatusBarStateController> provider10, Provider<ConfigurationController> provider11) {
        return new DozeUi_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    public static DozeUi newInstance(Context context, AlarmManager alarmManager, WakeLock wakeLock, DozeHost dozeHost, Handler handler, DozeParameters dozeParameters, KeyguardUpdateMonitor keyguardUpdateMonitor, DozeLog dozeLog, TunerService tunerService, Lazy<StatusBarStateController> lazy, ConfigurationController configurationController) {
        return new DozeUi(context, alarmManager, wakeLock, dozeHost, handler, dozeParameters, keyguardUpdateMonitor, dozeLog, tunerService, lazy, configurationController);
    }
}
