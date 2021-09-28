package com.google.android.systemui.columbus.actions;

import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
/* compiled from: UnpinNotifications.kt */
/* loaded from: classes2.dex */
public final class UnpinNotifications$headsUpChangedListener$1 implements OnHeadsUpChangedListener {
    final /* synthetic */ UnpinNotifications this$0;

    /* access modifiers changed from: package-private */
    public UnpinNotifications$headsUpChangedListener$1(UnpinNotifications unpinNotifications) {
        this.this$0 = unpinNotifications;
    }

    @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
    public void onHeadsUpPinnedModeChanged(boolean z) {
        this.this$0.hasPinnedHeadsUp = z;
        this.this$0.updateAvailable();
    }
}
