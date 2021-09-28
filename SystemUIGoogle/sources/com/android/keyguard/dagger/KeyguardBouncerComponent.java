package com.android.keyguard.dagger;

import com.android.keyguard.KeyguardHostViewController;
import com.android.keyguard.KeyguardRootViewController;
/* loaded from: classes.dex */
public interface KeyguardBouncerComponent {

    /* loaded from: classes.dex */
    public interface Factory {
        KeyguardBouncerComponent create();
    }

    KeyguardHostViewController getKeyguardHostViewController();

    KeyguardRootViewController getKeyguardRootViewController();
}
