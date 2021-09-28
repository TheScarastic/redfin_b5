package com.android.systemui.statusbar.tv.notifications;

import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TvNotificationPanelActivity_Factory implements Factory<TvNotificationPanelActivity> {
    private final Provider<TvNotificationHandler> tvNotificationHandlerProvider;

    public TvNotificationPanelActivity_Factory(Provider<TvNotificationHandler> provider) {
        this.tvNotificationHandlerProvider = provider;
    }

    @Override // javax.inject.Provider
    public TvNotificationPanelActivity get() {
        return newInstance(this.tvNotificationHandlerProvider.get());
    }

    public static TvNotificationPanelActivity_Factory create(Provider<TvNotificationHandler> provider) {
        return new TvNotificationPanelActivity_Factory(provider);
    }

    public static TvNotificationPanelActivity newInstance(TvNotificationHandler tvNotificationHandler) {
        return new TvNotificationPanelActivity(tvNotificationHandler);
    }
}
