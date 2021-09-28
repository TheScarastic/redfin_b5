package com.android.systemui.statusbar.phone;
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBar$12$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ShadeController f$0;

    public /* synthetic */ StatusBar$12$$ExternalSyntheticLambda0(ShadeController shadeController) {
        this.f$0 = shadeController;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.runPostCollapseRunnables();
    }
}
