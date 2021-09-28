package com.android.systemui.wmshell;

import com.android.systemui.wmshell.BubblesManager;
/* loaded from: classes2.dex */
public final /* synthetic */ class BubblesManager$5$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ BubblesManager.AnonymousClass5 f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ BubblesManager$5$$ExternalSyntheticLambda5(BubblesManager.AnonymousClass5 r1, String str) {
        this.f$0 = r1;
        this.f$1 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$updateNotificationSuppression$10(this.f$1);
    }
}
