package com.android.keyguard.dagger;

import com.android.systemui.statusbar.phone.UserAvatarView;
import com.android.systemui.statusbar.policy.KeyguardQsUserSwitchController;
/* loaded from: classes.dex */
public interface KeyguardQsUserSwitchComponent {

    /* loaded from: classes.dex */
    public interface Factory {
        KeyguardQsUserSwitchComponent build(UserAvatarView userAvatarView);
    }

    KeyguardQsUserSwitchController getKeyguardQsUserSwitchController();
}
