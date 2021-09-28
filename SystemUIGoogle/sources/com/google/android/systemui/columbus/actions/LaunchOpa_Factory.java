package com.google.android.systemui.columbus.actions;

import android.app.KeyguardManager;
import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.tuner.TunerService;
import com.google.android.systemui.columbus.ColumbusContentObserver;
import com.google.android.systemui.columbus.feedback.FeedbackEffect;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class LaunchOpa_Factory implements Factory<LaunchOpa> {
    private final Provider<AssistManager> assistManagerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<Set<FeedbackEffect>> feedbackEffectsProvider;
    private final Provider<KeyguardManager> keyguardManagerProvider;
    private final Provider<ColumbusContentObserver.Factory> settingsObserverFactoryProvider;
    private final Provider<StatusBar> statusBarProvider;
    private final Provider<TunerService> tunerServiceProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;

    public LaunchOpa_Factory(Provider<Context> provider, Provider<StatusBar> provider2, Provider<Set<FeedbackEffect>> provider3, Provider<AssistManager> provider4, Provider<KeyguardManager> provider5, Provider<TunerService> provider6, Provider<ColumbusContentObserver.Factory> provider7, Provider<UiEventLogger> provider8) {
        this.contextProvider = provider;
        this.statusBarProvider = provider2;
        this.feedbackEffectsProvider = provider3;
        this.assistManagerProvider = provider4;
        this.keyguardManagerProvider = provider5;
        this.tunerServiceProvider = provider6;
        this.settingsObserverFactoryProvider = provider7;
        this.uiEventLoggerProvider = provider8;
    }

    @Override // javax.inject.Provider
    public LaunchOpa get() {
        return newInstance(this.contextProvider.get(), this.statusBarProvider.get(), this.feedbackEffectsProvider.get(), this.assistManagerProvider.get(), DoubleCheck.lazy(this.keyguardManagerProvider), this.tunerServiceProvider.get(), this.settingsObserverFactoryProvider.get(), this.uiEventLoggerProvider.get());
    }

    public static LaunchOpa_Factory create(Provider<Context> provider, Provider<StatusBar> provider2, Provider<Set<FeedbackEffect>> provider3, Provider<AssistManager> provider4, Provider<KeyguardManager> provider5, Provider<TunerService> provider6, Provider<ColumbusContentObserver.Factory> provider7, Provider<UiEventLogger> provider8) {
        return new LaunchOpa_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8);
    }

    public static LaunchOpa newInstance(Context context, StatusBar statusBar, Set<FeedbackEffect> set, AssistManager assistManager, Lazy<KeyguardManager> lazy, TunerService tunerService, ColumbusContentObserver.Factory factory, UiEventLogger uiEventLogger) {
        return new LaunchOpa(context, statusBar, set, assistManager, lazy, tunerService, factory, uiEventLogger);
    }
}
