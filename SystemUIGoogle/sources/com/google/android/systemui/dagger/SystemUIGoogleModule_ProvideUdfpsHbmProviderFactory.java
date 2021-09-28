package com.google.android.systemui.dagger;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.UdfpsHbmProvider;
import com.google.android.systemui.fingerprint.UdfpsGhbmProvider;
import com.google.android.systemui.fingerprint.UdfpsLhbmProvider;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SystemUIGoogleModule_ProvideUdfpsHbmProviderFactory implements Factory<UdfpsHbmProvider> {
    private final Provider<AuthController> authControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DisplayManager> displayManagerProvider;
    private final Provider<UdfpsGhbmProvider> ghbmProvider;
    private final Provider<UdfpsLhbmProvider> lhbmProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<Executor> uiBgExecutorProvider;

    public SystemUIGoogleModule_ProvideUdfpsHbmProviderFactory(Provider<Context> provider, Provider<Handler> provider2, Provider<Executor> provider3, Provider<UdfpsGhbmProvider> provider4, Provider<UdfpsLhbmProvider> provider5, Provider<AuthController> provider6, Provider<DisplayManager> provider7) {
        this.contextProvider = provider;
        this.mainHandlerProvider = provider2;
        this.uiBgExecutorProvider = provider3;
        this.ghbmProvider = provider4;
        this.lhbmProvider = provider5;
        this.authControllerProvider = provider6;
        this.displayManagerProvider = provider7;
    }

    @Override // javax.inject.Provider
    public UdfpsHbmProvider get() {
        return provideUdfpsHbmProvider(this.contextProvider.get(), this.mainHandlerProvider.get(), this.uiBgExecutorProvider.get(), this.ghbmProvider.get(), this.lhbmProvider.get(), this.authControllerProvider.get(), this.displayManagerProvider.get());
    }

    public static SystemUIGoogleModule_ProvideUdfpsHbmProviderFactory create(Provider<Context> provider, Provider<Handler> provider2, Provider<Executor> provider3, Provider<UdfpsGhbmProvider> provider4, Provider<UdfpsLhbmProvider> provider5, Provider<AuthController> provider6, Provider<DisplayManager> provider7) {
        return new SystemUIGoogleModule_ProvideUdfpsHbmProviderFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static UdfpsHbmProvider provideUdfpsHbmProvider(Context context, Handler handler, Executor executor, UdfpsGhbmProvider udfpsGhbmProvider, UdfpsLhbmProvider udfpsLhbmProvider, AuthController authController, DisplayManager displayManager) {
        return (UdfpsHbmProvider) Preconditions.checkNotNullFromProvides(SystemUIGoogleModule.provideUdfpsHbmProvider(context, handler, executor, udfpsGhbmProvider, udfpsLhbmProvider, authController, displayManager));
    }
}
