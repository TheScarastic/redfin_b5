package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.phone.StatusBar;
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBar$7$Callback$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ StatusBar.AnonymousClass7.Callback f$0;

    public /* synthetic */ StatusBar$7$Callback$$ExternalSyntheticLambda1(StatusBar.AnonymousClass7.Callback callback) {
        this.f$0 = callback;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onHoldStatusBarOpenChange$2();
    }
}
