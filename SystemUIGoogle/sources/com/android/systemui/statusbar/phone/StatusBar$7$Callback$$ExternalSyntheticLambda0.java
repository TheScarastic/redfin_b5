package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.StatusBar;
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBar$7$Callback$$ExternalSyntheticLambda0 implements NotificationShadeWindowController.OtherwisedCollapsedListener {
    public final /* synthetic */ StatusBar.AnonymousClass7.Callback f$0;

    public /* synthetic */ StatusBar$7$Callback$$ExternalSyntheticLambda0(StatusBar.AnonymousClass7.Callback callback) {
        this.f$0 = callback;
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController.OtherwisedCollapsedListener
    public final void setWouldOtherwiseCollapse(boolean z) {
        this.f$0.lambda$onHoldStatusBarOpenChange$1(z);
    }
}
