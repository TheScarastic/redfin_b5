package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.phone.NotificationPanelViewController;
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationPanelViewController$OnOverscrollTopChangedListener$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ NotificationPanelViewController.OnOverscrollTopChangedListener f$0;

    public /* synthetic */ NotificationPanelViewController$OnOverscrollTopChangedListener$$ExternalSyntheticLambda0(NotificationPanelViewController.OnOverscrollTopChangedListener onOverscrollTopChangedListener) {
        this.f$0 = onOverscrollTopChangedListener;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$flingTopOverscroll$0();
    }
}
