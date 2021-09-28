package com.google.android.systemui.dagger;

import com.android.systemui.dagger.SysUIComponent;
import com.google.android.systemui.smartspace.KeyguardSmartspaceController;
/* loaded from: classes2.dex */
public interface SysUIGoogleSysUIComponent extends SysUIComponent {

    /* loaded from: classes2.dex */
    public interface Builder extends SysUIComponent.Builder {
    }

    KeyguardSmartspaceController createKeyguardSmartspaceController();
}
