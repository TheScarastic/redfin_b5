package com.google.android.systemui;

import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.Handler;
import android.os.UserManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationClickNotifier;
import com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.google.android.systemui.smartspace.SmartSpaceController;
import dagger.Lazy;
/* loaded from: classes2.dex */
public class NotificationLockscreenUserManagerGoogle extends NotificationLockscreenUserManagerImpl {
    private final Lazy<KeyguardBypassController> mKeyguardBypassControllerLazy;
    private final KeyguardStateController mKeyguardStateController;
    public final KeyguardStateController.Callback mKeyguardVisibilityCallback;
    private final SmartSpaceController mSmartSpaceController;

    public NotificationLockscreenUserManagerGoogle(Context context, BroadcastDispatcher broadcastDispatcher, DevicePolicyManager devicePolicyManager, UserManager userManager, NotificationClickNotifier notificationClickNotifier, KeyguardManager keyguardManager, StatusBarStateController statusBarStateController, Handler handler, DeviceProvisionedController deviceProvisionedController, KeyguardStateController keyguardStateController, Lazy<KeyguardBypassController> lazy, SmartSpaceController smartSpaceController) {
        super(context, broadcastDispatcher, devicePolicyManager, userManager, notificationClickNotifier, keyguardManager, statusBarStateController, handler, deviceProvisionedController, keyguardStateController);
        AnonymousClass1 r1 = new KeyguardStateController.Callback() { // from class: com.google.android.systemui.NotificationLockscreenUserManagerGoogle.1
            @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
            public void onKeyguardShowingChanged() {
                NotificationLockscreenUserManagerGoogle.this.updateSmartSpaceVisibilitySettings();
            }
        };
        this.mKeyguardVisibilityCallback = r1;
        this.mKeyguardBypassControllerLazy = lazy;
        this.mSmartSpaceController = smartSpaceController;
        this.mKeyguardStateController = keyguardStateController;
        keyguardStateController.addCallback(r1);
    }

    public void updateSmartSpaceVisibilitySettings() {
        boolean z = false;
        boolean z2 = !userAllowsPrivateNotificationsInPublic(this.mCurrentUserId) && (isAnyProfilePublicMode() || !this.mKeyguardStateController.isShowing());
        boolean z3 = !allowsManagedPrivateNotificationsInPublic();
        if (!this.mKeyguardBypassControllerLazy.get().getBypassEnabled()) {
            if (z3 && (isAnyManagedProfilePublicMode() || !this.mKeyguardStateController.isShowing())) {
                z = true;
            }
            z3 = z;
        }
        this.mSmartSpaceController.setHideSensitiveData(z2, z3);
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl, com.android.systemui.statusbar.NotificationLockscreenUserManager
    public void updatePublicMode() {
        super.updatePublicMode();
        updateSmartSpaceVisibilitySettings();
    }
}
