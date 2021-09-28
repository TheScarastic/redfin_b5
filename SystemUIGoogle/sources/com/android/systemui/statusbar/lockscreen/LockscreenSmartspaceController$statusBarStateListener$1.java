package com.android.systemui.statusbar.lockscreen;

import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: LockscreenSmartspaceController.kt */
/* loaded from: classes.dex */
public final class LockscreenSmartspaceController$statusBarStateListener$1 implements StatusBarStateController.StateListener {
    final /* synthetic */ LockscreenSmartspaceController this$0;

    /* access modifiers changed from: package-private */
    public LockscreenSmartspaceController$statusBarStateListener$1(LockscreenSmartspaceController lockscreenSmartspaceController) {
        this.this$0 = lockscreenSmartspaceController;
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onDozeAmountChanged(float f, float f2) {
        this.this$0.execution.assertIsMainThread();
        BcSmartspaceDataPlugin.SmartspaceView smartspaceView = this.this$0.smartspaceView;
        if (smartspaceView != null) {
            smartspaceView.setDozeAmount(f2);
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("smartspaceView");
            throw null;
        }
    }
}
