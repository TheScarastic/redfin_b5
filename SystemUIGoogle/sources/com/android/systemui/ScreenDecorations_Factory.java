package com.android.systemui;

import android.content.Context;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.events.PrivacyDotViewController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.concurrency.ThreadFactory;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ScreenDecorations_Factory implements Factory<ScreenDecorations> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Context> contextProvider;
    private final Provider<PrivacyDotViewController> dotViewControllerProvider;
    private final Provider<Executor> mainExecutorProvider;
    private final Provider<SecureSettings> secureSettingsProvider;
    private final Provider<ThreadFactory> threadFactoryProvider;
    private final Provider<TunerService> tunerServiceProvider;
    private final Provider<UserTracker> userTrackerProvider;

    public ScreenDecorations_Factory(Provider<Context> provider, Provider<Executor> provider2, Provider<SecureSettings> provider3, Provider<BroadcastDispatcher> provider4, Provider<TunerService> provider5, Provider<UserTracker> provider6, Provider<PrivacyDotViewController> provider7, Provider<ThreadFactory> provider8) {
        this.contextProvider = provider;
        this.mainExecutorProvider = provider2;
        this.secureSettingsProvider = provider3;
        this.broadcastDispatcherProvider = provider4;
        this.tunerServiceProvider = provider5;
        this.userTrackerProvider = provider6;
        this.dotViewControllerProvider = provider7;
        this.threadFactoryProvider = provider8;
    }

    @Override // javax.inject.Provider
    public ScreenDecorations get() {
        return newInstance(this.contextProvider.get(), this.mainExecutorProvider.get(), this.secureSettingsProvider.get(), this.broadcastDispatcherProvider.get(), this.tunerServiceProvider.get(), this.userTrackerProvider.get(), this.dotViewControllerProvider.get(), this.threadFactoryProvider.get());
    }

    public static ScreenDecorations_Factory create(Provider<Context> provider, Provider<Executor> provider2, Provider<SecureSettings> provider3, Provider<BroadcastDispatcher> provider4, Provider<TunerService> provider5, Provider<UserTracker> provider6, Provider<PrivacyDotViewController> provider7, Provider<ThreadFactory> provider8) {
        return new ScreenDecorations_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8);
    }

    public static ScreenDecorations newInstance(Context context, Executor executor, SecureSettings secureSettings, BroadcastDispatcher broadcastDispatcher, TunerService tunerService, UserTracker userTracker, PrivacyDotViewController privacyDotViewController, ThreadFactory threadFactory) {
        return new ScreenDecorations(context, executor, secureSettings, broadcastDispatcher, tunerService, userTracker, privacyDotViewController, threadFactory);
    }
}
