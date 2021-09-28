package com.android.systemui.statusbar.policy.dagger;

import android.os.UserManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.AccessPointControllerImpl;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public interface StatusBarPolicyModule {
    static AccessPointControllerImpl provideAccessPointControllerImpl(UserManager userManager, UserTracker userTracker, Executor executor, AccessPointControllerImpl.WifiPickerTrackerFactory wifiPickerTrackerFactory) {
        AccessPointControllerImpl accessPointControllerImpl = new AccessPointControllerImpl(userManager, userTracker, executor, wifiPickerTrackerFactory);
        accessPointControllerImpl.init();
        return accessPointControllerImpl;
    }
}
