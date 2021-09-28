package com.android.systemui.demomode;

import android.content.Context;
/* compiled from: DemoModeController.kt */
/* loaded from: classes.dex */
public final class DemoModeController$tracker$1 extends DemoModeAvailabilityTracker {
    final /* synthetic */ DemoModeController this$0;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public DemoModeController$tracker$1(DemoModeController demoModeController, Context context) {
        super(context);
        this.this$0 = demoModeController;
    }

    @Override // com.android.systemui.demomode.DemoModeAvailabilityTracker
    public void onDemoModeAvailabilityChanged() {
        this.this$0.setIsDemoModeAllowed(isDemoModeAvailable());
    }

    @Override // com.android.systemui.demomode.DemoModeAvailabilityTracker
    public void onDemoModeStarted() {
        if (this.this$0.isInDemoMode() != isInDemoMode()) {
            this.this$0.enterDemoMode();
        }
    }

    @Override // com.android.systemui.demomode.DemoModeAvailabilityTracker
    public void onDemoModeFinished() {
        if (this.this$0.isInDemoMode() != isInDemoMode()) {
            this.this$0.exitDemoMode();
        }
    }
}
