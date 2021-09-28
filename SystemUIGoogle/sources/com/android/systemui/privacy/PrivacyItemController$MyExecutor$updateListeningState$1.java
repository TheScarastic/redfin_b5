package com.android.systemui.privacy;
/* compiled from: PrivacyItemController.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class PrivacyItemController$MyExecutor$updateListeningState$1 implements Runnable {
    final /* synthetic */ PrivacyItemController this$0;

    /* access modifiers changed from: package-private */
    public PrivacyItemController$MyExecutor$updateListeningState$1(PrivacyItemController privacyItemController) {
        this.this$0 = privacyItemController;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.this$0.setListeningState();
    }
}
