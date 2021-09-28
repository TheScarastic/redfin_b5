package com.android.systemui.statusbar.phone.dagger;

import com.android.systemui.biometrics.AuthRippleView;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarViewModule_GetAuthRippleViewFactory implements Factory<AuthRippleView> {
    private final Provider<NotificationShadeWindowView> notificationShadeWindowViewProvider;

    public StatusBarViewModule_GetAuthRippleViewFactory(Provider<NotificationShadeWindowView> provider) {
        this.notificationShadeWindowViewProvider = provider;
    }

    @Override // javax.inject.Provider
    public AuthRippleView get() {
        return getAuthRippleView(this.notificationShadeWindowViewProvider.get());
    }

    public static StatusBarViewModule_GetAuthRippleViewFactory create(Provider<NotificationShadeWindowView> provider) {
        return new StatusBarViewModule_GetAuthRippleViewFactory(provider);
    }

    public static AuthRippleView getAuthRippleView(NotificationShadeWindowView notificationShadeWindowView) {
        return StatusBarViewModule.getAuthRippleView(notificationShadeWindowView);
    }
}
