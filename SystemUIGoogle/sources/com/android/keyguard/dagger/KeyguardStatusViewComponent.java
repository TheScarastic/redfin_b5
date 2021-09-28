package com.android.keyguard.dagger;

import com.android.keyguard.KeyguardClockSwitchController;
import com.android.keyguard.KeyguardStatusView;
import com.android.keyguard.KeyguardStatusViewController;
/* loaded from: classes.dex */
public interface KeyguardStatusViewComponent {

    /* loaded from: classes.dex */
    public interface Factory {
        KeyguardStatusViewComponent build(KeyguardStatusView keyguardStatusView);
    }

    KeyguardClockSwitchController getKeyguardClockSwitchController();

    KeyguardStatusViewController getKeyguardStatusViewController();
}
