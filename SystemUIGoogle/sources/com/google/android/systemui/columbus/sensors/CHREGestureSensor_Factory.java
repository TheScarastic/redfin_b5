package com.google.android.systemui.columbus.sensors;

import android.content.Context;
import android.os.Handler;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.google.android.systemui.columbus.sensors.config.GestureConfiguration;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class CHREGestureSensor_Factory implements Factory<CHREGestureSensor> {
    private final Provider<Handler> bgHandlerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<GestureConfiguration> gestureConfigurationProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;
    private final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public CHREGestureSensor_Factory(Provider<Context> provider, Provider<UiEventLogger> provider2, Provider<GestureConfiguration> provider3, Provider<StatusBarStateController> provider4, Provider<WakefulnessLifecycle> provider5, Provider<Handler> provider6) {
        this.contextProvider = provider;
        this.uiEventLoggerProvider = provider2;
        this.gestureConfigurationProvider = provider3;
        this.statusBarStateControllerProvider = provider4;
        this.wakefulnessLifecycleProvider = provider5;
        this.bgHandlerProvider = provider6;
    }

    @Override // javax.inject.Provider
    public CHREGestureSensor get() {
        return newInstance(this.contextProvider.get(), this.uiEventLoggerProvider.get(), this.gestureConfigurationProvider.get(), this.statusBarStateControllerProvider.get(), this.wakefulnessLifecycleProvider.get(), this.bgHandlerProvider.get());
    }

    public static CHREGestureSensor_Factory create(Provider<Context> provider, Provider<UiEventLogger> provider2, Provider<GestureConfiguration> provider3, Provider<StatusBarStateController> provider4, Provider<WakefulnessLifecycle> provider5, Provider<Handler> provider6) {
        return new CHREGestureSensor_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static CHREGestureSensor newInstance(Context context, UiEventLogger uiEventLogger, GestureConfiguration gestureConfiguration, StatusBarStateController statusBarStateController, WakefulnessLifecycle wakefulnessLifecycle, Handler handler) {
        return new CHREGestureSensor(context, uiEventLogger, gestureConfiguration, statusBarStateController, wakefulnessLifecycle, handler);
    }
}
