package com.android.keyguard.dagger;

import com.android.systemui.statusbar.policy.KeyguardUserSwitcherController;
import com.android.systemui.statusbar.policy.KeyguardUserSwitcherView;
/* loaded from: classes.dex */
public interface KeyguardUserSwitcherComponent {

    /* loaded from: classes.dex */
    public interface Factory {
        KeyguardUserSwitcherComponent build(KeyguardUserSwitcherView keyguardUserSwitcherView);
    }

    KeyguardUserSwitcherController getKeyguardUserSwitcherController();
}
