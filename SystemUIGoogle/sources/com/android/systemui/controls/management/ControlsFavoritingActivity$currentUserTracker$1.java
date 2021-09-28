package com.android.systemui.controls.management;

import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.CurrentUserTracker;
/* compiled from: ControlsFavoritingActivity.kt */
/* loaded from: classes.dex */
public final class ControlsFavoritingActivity$currentUserTracker$1 extends CurrentUserTracker {
    private final int startingUser;
    final /* synthetic */ ControlsFavoritingActivity this$0;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public ControlsFavoritingActivity$currentUserTracker$1(ControlsFavoritingActivity controlsFavoritingActivity, BroadcastDispatcher broadcastDispatcher) {
        super(broadcastDispatcher);
        this.this$0 = controlsFavoritingActivity;
        this.startingUser = controlsFavoritingActivity.controller.getCurrentUserId();
    }

    @Override // com.android.systemui.settings.CurrentUserTracker
    public void onUserSwitched(int i) {
        if (i != this.startingUser) {
            stopTracking();
            this.this$0.finish();
        }
    }
}
