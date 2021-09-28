package com.android.systemui.controls.management;

import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.CurrentUserTracker;
/* compiled from: ControlsProviderSelectorActivity.kt */
/* loaded from: classes.dex */
public final class ControlsProviderSelectorActivity$currentUserTracker$1 extends CurrentUserTracker {
    private final int startingUser;
    final /* synthetic */ ControlsProviderSelectorActivity this$0;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public ControlsProviderSelectorActivity$currentUserTracker$1(ControlsProviderSelectorActivity controlsProviderSelectorActivity, BroadcastDispatcher broadcastDispatcher) {
        super(broadcastDispatcher);
        this.this$0 = controlsProviderSelectorActivity;
        this.startingUser = ControlsProviderSelectorActivity.access$getListingController$p(controlsProviderSelectorActivity).getCurrentUserId();
    }

    @Override // com.android.systemui.settings.CurrentUserTracker
    public void onUserSwitched(int i) {
        if (i != this.startingUser) {
            stopTracking();
            this.this$0.finish();
        }
    }
}
