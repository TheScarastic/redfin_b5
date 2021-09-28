package com.android.systemui.statusbar.phone;

import com.android.systemui.plugins.OverlayPlugin;
import com.android.systemui.statusbar.phone.StatusBar;
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBar$7$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ StatusBar.AnonymousClass7 f$0;
    public final /* synthetic */ OverlayPlugin f$1;

    public /* synthetic */ StatusBar$7$$ExternalSyntheticLambda1(StatusBar.AnonymousClass7 r1, OverlayPlugin overlayPlugin) {
        this.f$0 = r1;
        this.f$1 = overlayPlugin;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onPluginDisconnected$1(this.f$1);
    }
}
