package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.phone.NotificationPanelViewController;
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationPanelViewController$KeyguardAffordanceHelperCallback$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ NotificationPanelViewController.KeyguardAffordanceHelperCallback f$0;

    public /* synthetic */ NotificationPanelViewController$KeyguardAffordanceHelperCallback$$ExternalSyntheticLambda0(NotificationPanelViewController.KeyguardAffordanceHelperCallback keyguardAffordanceHelperCallback) {
        this.f$0 = keyguardAffordanceHelperCallback;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onIconClicked$2();
    }
}
