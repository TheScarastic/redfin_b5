package com.google.android.systemui.columbus;

import com.google.android.systemui.columbus.actions.Action;
import com.google.android.systemui.columbus.actions.DismissTimer;
import com.google.android.systemui.columbus.actions.SettingsAction;
import com.google.android.systemui.columbus.actions.SilenceCall;
import com.google.android.systemui.columbus.actions.SnoozeAlarm;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.List;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ColumbusModule_ProvideFullscreenActionsFactory implements Factory<List<Action>> {
    private final Provider<DismissTimer> dismissTimerProvider;
    private final Provider<SettingsAction> settingsActionProvider;
    private final Provider<SilenceCall> silenceCallProvider;
    private final Provider<SnoozeAlarm> snoozeAlarmProvider;

    public ColumbusModule_ProvideFullscreenActionsFactory(Provider<DismissTimer> provider, Provider<SnoozeAlarm> provider2, Provider<SilenceCall> provider3, Provider<SettingsAction> provider4) {
        this.dismissTimerProvider = provider;
        this.snoozeAlarmProvider = provider2;
        this.silenceCallProvider = provider3;
        this.settingsActionProvider = provider4;
    }

    @Override // javax.inject.Provider
    public List<Action> get() {
        return provideFullscreenActions(this.dismissTimerProvider.get(), this.snoozeAlarmProvider.get(), this.silenceCallProvider.get(), this.settingsActionProvider.get());
    }

    public static ColumbusModule_ProvideFullscreenActionsFactory create(Provider<DismissTimer> provider, Provider<SnoozeAlarm> provider2, Provider<SilenceCall> provider3, Provider<SettingsAction> provider4) {
        return new ColumbusModule_ProvideFullscreenActionsFactory(provider, provider2, provider3, provider4);
    }

    public static List<Action> provideFullscreenActions(DismissTimer dismissTimer, SnoozeAlarm snoozeAlarm, SilenceCall silenceCall, SettingsAction settingsAction) {
        return (List) Preconditions.checkNotNullFromProvides(ColumbusModule.provideFullscreenActions(dismissTimer, snoozeAlarm, silenceCall, settingsAction));
    }
}
