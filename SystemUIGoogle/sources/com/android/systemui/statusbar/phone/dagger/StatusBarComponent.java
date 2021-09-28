package com.android.systemui.statusbar.phone.dagger;

import com.android.keyguard.LockIconViewController;
import com.android.systemui.biometrics.AuthRippleController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import com.android.systemui.statusbar.phone.NotificationShadeWindowViewController;
import com.android.systemui.statusbar.phone.StatusBarWindowController;
/* loaded from: classes.dex */
public interface StatusBarComponent {

    /* loaded from: classes.dex */
    public interface Builder {
        StatusBarComponent build();

        Builder statusBarWindowView(NotificationShadeWindowView notificationShadeWindowView);
    }

    AuthRippleController getAuthRippleController();

    LockIconViewController getLockIconViewController();

    NotificationPanelViewController getNotificationPanelViewController();

    NotificationShadeWindowViewController getNotificationShadeWindowViewController();

    StatusBarWindowController getStatusBarWindowController();
}
