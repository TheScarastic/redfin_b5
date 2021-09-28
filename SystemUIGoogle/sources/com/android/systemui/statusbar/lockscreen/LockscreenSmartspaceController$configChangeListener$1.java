package com.android.systemui.statusbar.lockscreen;

import com.android.systemui.statusbar.policy.ConfigurationController;
/* compiled from: LockscreenSmartspaceController.kt */
/* loaded from: classes.dex */
public final class LockscreenSmartspaceController$configChangeListener$1 implements ConfigurationController.ConfigurationListener {
    final /* synthetic */ LockscreenSmartspaceController this$0;

    /* access modifiers changed from: package-private */
    public LockscreenSmartspaceController$configChangeListener$1(LockscreenSmartspaceController lockscreenSmartspaceController) {
        this.this$0 = lockscreenSmartspaceController;
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onThemeChanged() {
        this.this$0.execution.assertIsMainThread();
        this.this$0.updateTextColorFromWallpaper();
    }
}
