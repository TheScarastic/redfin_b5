package com.google.android.systemui.power;

import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.power.PowerUI;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class PowerModuleGoogle_ProvideWarningsUiFactory implements Factory<PowerUI.WarningsUI> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<Context> contextProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;

    public PowerModuleGoogle_ProvideWarningsUiFactory(Provider<Context> provider, Provider<ActivityStarter> provider2, Provider<UiEventLogger> provider3) {
        this.contextProvider = provider;
        this.activityStarterProvider = provider2;
        this.uiEventLoggerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public PowerUI.WarningsUI get() {
        return provideWarningsUi(this.contextProvider.get(), this.activityStarterProvider.get(), this.uiEventLoggerProvider.get());
    }

    public static PowerModuleGoogle_ProvideWarningsUiFactory create(Provider<Context> provider, Provider<ActivityStarter> provider2, Provider<UiEventLogger> provider3) {
        return new PowerModuleGoogle_ProvideWarningsUiFactory(provider, provider2, provider3);
    }

    public static PowerUI.WarningsUI provideWarningsUi(Context context, ActivityStarter activityStarter, UiEventLogger uiEventLogger) {
        return (PowerUI.WarningsUI) Preconditions.checkNotNullFromProvides(PowerModuleGoogle.provideWarningsUi(context, activityStarter, uiEventLogger));
    }
}
