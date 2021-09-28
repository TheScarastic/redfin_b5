package com.google.android.systemui.columbus;

import com.google.android.systemui.columbus.actions.Action;
import com.google.android.systemui.columbus.actions.UnpinNotifications;
import com.google.android.systemui.columbus.actions.UserSelectedAction;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.List;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ColumbusModule_ProvideColumbusActionsFactory implements Factory<List<Action>> {
    private final Provider<List<Action>> fullscreenActionsProvider;
    private final Provider<UnpinNotifications> unpinNotificationsProvider;
    private final Provider<UserSelectedAction> userSelectedActionProvider;

    public ColumbusModule_ProvideColumbusActionsFactory(Provider<List<Action>> provider, Provider<UnpinNotifications> provider2, Provider<UserSelectedAction> provider3) {
        this.fullscreenActionsProvider = provider;
        this.unpinNotificationsProvider = provider2;
        this.userSelectedActionProvider = provider3;
    }

    @Override // javax.inject.Provider
    public List<Action> get() {
        return provideColumbusActions(this.fullscreenActionsProvider.get(), this.unpinNotificationsProvider.get(), this.userSelectedActionProvider.get());
    }

    public static ColumbusModule_ProvideColumbusActionsFactory create(Provider<List<Action>> provider, Provider<UnpinNotifications> provider2, Provider<UserSelectedAction> provider3) {
        return new ColumbusModule_ProvideColumbusActionsFactory(provider, provider2, provider3);
    }

    public static List<Action> provideColumbusActions(List<Action> list, UnpinNotifications unpinNotifications, UserSelectedAction userSelectedAction) {
        return (List) Preconditions.checkNotNullFromProvides(ColumbusModule.provideColumbusActions(list, unpinNotifications, userSelectedAction));
    }
}
