package com.google.android.systemui.columbus.actions;

import android.content.Context;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.google.android.systemui.columbus.gates.SilenceAlertsDisabled;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class UnpinNotifications_Factory implements Factory<UnpinNotifications> {
    private final Provider<Context> contextProvider;
    private final Provider<Optional<HeadsUpManager>> headsUpManagerOptionalProvider;
    private final Provider<SilenceAlertsDisabled> silenceAlertsDisabledProvider;

    public UnpinNotifications_Factory(Provider<Context> provider, Provider<SilenceAlertsDisabled> provider2, Provider<Optional<HeadsUpManager>> provider3) {
        this.contextProvider = provider;
        this.silenceAlertsDisabledProvider = provider2;
        this.headsUpManagerOptionalProvider = provider3;
    }

    @Override // javax.inject.Provider
    public UnpinNotifications get() {
        return newInstance(this.contextProvider.get(), this.silenceAlertsDisabledProvider.get(), this.headsUpManagerOptionalProvider.get());
    }

    public static UnpinNotifications_Factory create(Provider<Context> provider, Provider<SilenceAlertsDisabled> provider2, Provider<Optional<HeadsUpManager>> provider3) {
        return new UnpinNotifications_Factory(provider, provider2, provider3);
    }

    public static UnpinNotifications newInstance(Context context, SilenceAlertsDisabled silenceAlertsDisabled, Optional<HeadsUpManager> optional) {
        return new UnpinNotifications(context, silenceAlertsDisabled, optional);
    }
}
