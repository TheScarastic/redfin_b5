package com.android.systemui.statusbar.phone.dagger;

import com.android.systemui.statusbar.phone.NotificationPanelView;
import com.android.systemui.statusbar.phone.TapAgainView;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarViewModule_GetTapAgainViewFactory implements Factory<TapAgainView> {
    private final Provider<NotificationPanelView> npvProvider;

    public StatusBarViewModule_GetTapAgainViewFactory(Provider<NotificationPanelView> provider) {
        this.npvProvider = provider;
    }

    @Override // javax.inject.Provider
    public TapAgainView get() {
        return getTapAgainView(this.npvProvider.get());
    }

    public static StatusBarViewModule_GetTapAgainViewFactory create(Provider<NotificationPanelView> provider) {
        return new StatusBarViewModule_GetTapAgainViewFactory(provider);
    }

    public static TapAgainView getTapAgainView(NotificationPanelView notificationPanelView) {
        return (TapAgainView) Preconditions.checkNotNullFromProvides(StatusBarViewModule.getTapAgainView(notificationPanelView));
    }
}
