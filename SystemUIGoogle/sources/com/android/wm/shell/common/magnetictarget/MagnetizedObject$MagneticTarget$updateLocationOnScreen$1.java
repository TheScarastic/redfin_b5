package com.android.wm.shell.common.magnetictarget;

import com.android.wm.shell.common.magnetictarget.MagnetizedObject;
/* compiled from: MagnetizedObject.kt */
/* loaded from: classes2.dex */
final class MagnetizedObject$MagneticTarget$updateLocationOnScreen$1 implements Runnable {
    final /* synthetic */ MagnetizedObject.MagneticTarget this$0;

    /* access modifiers changed from: package-private */
    public MagnetizedObject$MagneticTarget$updateLocationOnScreen$1(MagnetizedObject.MagneticTarget magneticTarget) {
        this.this$0 = magneticTarget;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.this$0.getTargetView().getLocationOnScreen(MagnetizedObject.MagneticTarget.access$getTempLoc$p(this.this$0));
        this.this$0.getCenterOnScreen().set((((float) MagnetizedObject.MagneticTarget.access$getTempLoc$p(this.this$0)[0]) + (((float) this.this$0.getTargetView().getWidth()) / 2.0f)) - this.this$0.getTargetView().getTranslationX(), (((float) MagnetizedObject.MagneticTarget.access$getTempLoc$p(this.this$0)[1]) + (((float) this.this$0.getTargetView().getHeight()) / 2.0f)) - this.this$0.getTargetView().getTranslationY());
    }
}
