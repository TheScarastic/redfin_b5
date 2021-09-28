package com.android.systemui.doze;

import android.content.Context;
import android.hardware.display.AmbientDisplayConfiguration;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dock.DockManager;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.sensors.AsyncSensorManager;
import com.android.systemui.util.sensors.ProximitySensor;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.util.wakelock.WakeLock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeTriggers_Factory implements Factory<DozeTriggers> {
    private final Provider<AuthController> authControllerProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<AmbientDisplayConfiguration> configProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DockManager> dockManagerProvider;
    private final Provider<DozeHost> dozeHostProvider;
    private final Provider<DozeLog> dozeLogProvider;
    private final Provider<DozeParameters> dozeParametersProvider;
    private final Provider<DelayableExecutor> mainExecutorProvider;
    private final Provider<ProximitySensor.ProximityCheck> proxCheckProvider;
    private final Provider<ProximitySensor> proximitySensorProvider;
    private final Provider<SecureSettings> secureSettingsProvider;
    private final Provider<AsyncSensorManager> sensorManagerProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;
    private final Provider<WakeLock> wakeLockProvider;

    public DozeTriggers_Factory(Provider<Context> provider, Provider<DozeHost> provider2, Provider<AmbientDisplayConfiguration> provider3, Provider<DozeParameters> provider4, Provider<AsyncSensorManager> provider5, Provider<WakeLock> provider6, Provider<DockManager> provider7, Provider<ProximitySensor> provider8, Provider<ProximitySensor.ProximityCheck> provider9, Provider<DozeLog> provider10, Provider<BroadcastDispatcher> provider11, Provider<SecureSettings> provider12, Provider<AuthController> provider13, Provider<DelayableExecutor> provider14, Provider<UiEventLogger> provider15) {
        this.contextProvider = provider;
        this.dozeHostProvider = provider2;
        this.configProvider = provider3;
        this.dozeParametersProvider = provider4;
        this.sensorManagerProvider = provider5;
        this.wakeLockProvider = provider6;
        this.dockManagerProvider = provider7;
        this.proximitySensorProvider = provider8;
        this.proxCheckProvider = provider9;
        this.dozeLogProvider = provider10;
        this.broadcastDispatcherProvider = provider11;
        this.secureSettingsProvider = provider12;
        this.authControllerProvider = provider13;
        this.mainExecutorProvider = provider14;
        this.uiEventLoggerProvider = provider15;
    }

    @Override // javax.inject.Provider
    public DozeTriggers get() {
        return newInstance(this.contextProvider.get(), this.dozeHostProvider.get(), this.configProvider.get(), this.dozeParametersProvider.get(), this.sensorManagerProvider.get(), this.wakeLockProvider.get(), this.dockManagerProvider.get(), this.proximitySensorProvider.get(), this.proxCheckProvider.get(), this.dozeLogProvider.get(), this.broadcastDispatcherProvider.get(), this.secureSettingsProvider.get(), this.authControllerProvider.get(), this.mainExecutorProvider.get(), this.uiEventLoggerProvider.get());
    }

    public static DozeTriggers_Factory create(Provider<Context> provider, Provider<DozeHost> provider2, Provider<AmbientDisplayConfiguration> provider3, Provider<DozeParameters> provider4, Provider<AsyncSensorManager> provider5, Provider<WakeLock> provider6, Provider<DockManager> provider7, Provider<ProximitySensor> provider8, Provider<ProximitySensor.ProximityCheck> provider9, Provider<DozeLog> provider10, Provider<BroadcastDispatcher> provider11, Provider<SecureSettings> provider12, Provider<AuthController> provider13, Provider<DelayableExecutor> provider14, Provider<UiEventLogger> provider15) {
        return new DozeTriggers_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15);
    }

    public static DozeTriggers newInstance(Context context, DozeHost dozeHost, AmbientDisplayConfiguration ambientDisplayConfiguration, DozeParameters dozeParameters, AsyncSensorManager asyncSensorManager, WakeLock wakeLock, DockManager dockManager, ProximitySensor proximitySensor, ProximitySensor.ProximityCheck proximityCheck, DozeLog dozeLog, BroadcastDispatcher broadcastDispatcher, SecureSettings secureSettings, AuthController authController, DelayableExecutor delayableExecutor, UiEventLogger uiEventLogger) {
        return new DozeTriggers(context, dozeHost, ambientDisplayConfiguration, dozeParameters, asyncSensorManager, wakeLock, dockManager, proximitySensor, proximityCheck, dozeLog, broadcastDispatcher, secureSettings, authController, delayableExecutor, uiEventLogger);
    }
}
