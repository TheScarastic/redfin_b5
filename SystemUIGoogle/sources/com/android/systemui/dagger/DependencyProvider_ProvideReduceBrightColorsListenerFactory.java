package com.android.systemui.dagger;

import android.hardware.display.ColorDisplayManager;
import android.os.Handler;
import com.android.systemui.qs.ReduceBrightColorsController;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvideReduceBrightColorsListenerFactory implements Factory<ReduceBrightColorsController> {
    private final Provider<Handler> bgHandlerProvider;
    private final Provider<ColorDisplayManager> colorDisplayManagerProvider;
    private final DependencyProvider module;
    private final Provider<SecureSettings> secureSettingsProvider;
    private final Provider<UserTracker> userTrackerProvider;

    public DependencyProvider_ProvideReduceBrightColorsListenerFactory(DependencyProvider dependencyProvider, Provider<Handler> provider, Provider<UserTracker> provider2, Provider<ColorDisplayManager> provider3, Provider<SecureSettings> provider4) {
        this.module = dependencyProvider;
        this.bgHandlerProvider = provider;
        this.userTrackerProvider = provider2;
        this.colorDisplayManagerProvider = provider3;
        this.secureSettingsProvider = provider4;
    }

    @Override // javax.inject.Provider
    public ReduceBrightColorsController get() {
        return provideReduceBrightColorsListener(this.module, this.bgHandlerProvider.get(), this.userTrackerProvider.get(), this.colorDisplayManagerProvider.get(), this.secureSettingsProvider.get());
    }

    public static DependencyProvider_ProvideReduceBrightColorsListenerFactory create(DependencyProvider dependencyProvider, Provider<Handler> provider, Provider<UserTracker> provider2, Provider<ColorDisplayManager> provider3, Provider<SecureSettings> provider4) {
        return new DependencyProvider_ProvideReduceBrightColorsListenerFactory(dependencyProvider, provider, provider2, provider3, provider4);
    }

    public static ReduceBrightColorsController provideReduceBrightColorsListener(DependencyProvider dependencyProvider, Handler handler, UserTracker userTracker, ColorDisplayManager colorDisplayManager, SecureSettings secureSettings) {
        return (ReduceBrightColorsController) Preconditions.checkNotNullFromProvides(dependencyProvider.provideReduceBrightColorsListener(handler, userTracker, colorDisplayManager, secureSettings));
    }
}
