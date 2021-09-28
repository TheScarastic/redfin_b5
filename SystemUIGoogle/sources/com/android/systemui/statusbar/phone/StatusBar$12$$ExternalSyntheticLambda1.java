package com.android.systemui.statusbar.phone;
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBar$12$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ StatusBarKeyguardViewManager f$0;

    public /* synthetic */ StatusBar$12$$ExternalSyntheticLambda1(StatusBarKeyguardViewManager statusBarKeyguardViewManager) {
        this.f$0 = statusBarKeyguardViewManager;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.readyForKeyguardDone();
    }
}
