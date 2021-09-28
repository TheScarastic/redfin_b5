package com.android.systemui.qs.tiles;

import android.os.Handler;
import android.os.Looper;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MicrophoneToggleTile_Factory implements Factory<MicrophoneToggleTile> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<Looper> backgroundLooperProvider;
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<QSHost> hostProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<MetricsLogger> metricsLoggerProvider;
    private final Provider<QSLogger> qsLoggerProvider;
    private final Provider<IndividualSensorPrivacyController> sensorPrivacyControllerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public MicrophoneToggleTile_Factory(Provider<QSHost> provider, Provider<Looper> provider2, Provider<Handler> provider3, Provider<MetricsLogger> provider4, Provider<FalsingManager> provider5, Provider<StatusBarStateController> provider6, Provider<ActivityStarter> provider7, Provider<QSLogger> provider8, Provider<IndividualSensorPrivacyController> provider9, Provider<KeyguardStateController> provider10) {
        this.hostProvider = provider;
        this.backgroundLooperProvider = provider2;
        this.mainHandlerProvider = provider3;
        this.metricsLoggerProvider = provider4;
        this.falsingManagerProvider = provider5;
        this.statusBarStateControllerProvider = provider6;
        this.activityStarterProvider = provider7;
        this.qsLoggerProvider = provider8;
        this.sensorPrivacyControllerProvider = provider9;
        this.keyguardStateControllerProvider = provider10;
    }

    @Override // javax.inject.Provider
    public MicrophoneToggleTile get() {
        return newInstance(this.hostProvider.get(), this.backgroundLooperProvider.get(), this.mainHandlerProvider.get(), this.metricsLoggerProvider.get(), this.falsingManagerProvider.get(), this.statusBarStateControllerProvider.get(), this.activityStarterProvider.get(), this.qsLoggerProvider.get(), this.sensorPrivacyControllerProvider.get(), this.keyguardStateControllerProvider.get());
    }

    public static MicrophoneToggleTile_Factory create(Provider<QSHost> provider, Provider<Looper> provider2, Provider<Handler> provider3, Provider<MetricsLogger> provider4, Provider<FalsingManager> provider5, Provider<StatusBarStateController> provider6, Provider<ActivityStarter> provider7, Provider<QSLogger> provider8, Provider<IndividualSensorPrivacyController> provider9, Provider<KeyguardStateController> provider10) {
        return new MicrophoneToggleTile_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static MicrophoneToggleTile newInstance(QSHost qSHost, Looper looper, Handler handler, MetricsLogger metricsLogger, FalsingManager falsingManager, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, IndividualSensorPrivacyController individualSensorPrivacyController, KeyguardStateController keyguardStateController) {
        return new MicrophoneToggleTile(qSHost, looper, handler, metricsLogger, falsingManager, statusBarStateController, activityStarter, qSLogger, individualSensorPrivacyController, keyguardStateController);
    }
}
