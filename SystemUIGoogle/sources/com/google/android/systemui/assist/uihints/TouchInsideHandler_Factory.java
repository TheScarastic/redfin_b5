package com.google.android.systemui.assist.uihints;

import com.android.systemui.assist.AssistLogger;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.navigationbar.NavigationModeController;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class TouchInsideHandler_Factory implements Factory<TouchInsideHandler> {
    private final Provider<AssistLogger> assistLoggerProvider;
    private final Provider<AssistManager> assistManagerProvider;
    private final Provider<NavigationModeController> navigationModeControllerProvider;

    public TouchInsideHandler_Factory(Provider<AssistManager> provider, Provider<NavigationModeController> provider2, Provider<AssistLogger> provider3) {
        this.assistManagerProvider = provider;
        this.navigationModeControllerProvider = provider2;
        this.assistLoggerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public TouchInsideHandler get() {
        return newInstance(DoubleCheck.lazy(this.assistManagerProvider), this.navigationModeControllerProvider.get(), this.assistLoggerProvider.get());
    }

    public static TouchInsideHandler_Factory create(Provider<AssistManager> provider, Provider<NavigationModeController> provider2, Provider<AssistLogger> provider3) {
        return new TouchInsideHandler_Factory(provider, provider2, provider3);
    }

    public static TouchInsideHandler newInstance(Lazy<AssistManager> lazy, NavigationModeController navigationModeController, AssistLogger assistLogger) {
        return new TouchInsideHandler(lazy, navigationModeController, assistLogger);
    }
}
