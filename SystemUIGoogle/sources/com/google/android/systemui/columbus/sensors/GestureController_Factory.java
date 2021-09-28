package com.google.android.systemui.columbus.sensors;

import com.android.internal.logging.UiEventLogger;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class GestureController_Factory implements Factory<GestureController> {
    private final Provider<GestureSensor> gestureSensorProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;

    public GestureController_Factory(Provider<GestureSensor> provider, Provider<UiEventLogger> provider2) {
        this.gestureSensorProvider = provider;
        this.uiEventLoggerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public GestureController get() {
        return newInstance(this.gestureSensorProvider.get(), this.uiEventLoggerProvider.get());
    }

    public static GestureController_Factory create(Provider<GestureSensor> provider, Provider<UiEventLogger> provider2) {
        return new GestureController_Factory(provider, provider2);
    }

    public static GestureController newInstance(GestureSensor gestureSensor, UiEventLogger uiEventLogger) {
        return new GestureController(gestureSensor, uiEventLogger);
    }
}
