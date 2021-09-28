package com.android.systemui.wmshell;

import com.android.systemui.wmshell.BubblesManager;
/* loaded from: classes2.dex */
public final /* synthetic */ class BubblesManager$5$$ExternalSyntheticLambda12 implements Runnable {
    public final /* synthetic */ BubblesManager.AnonymousClass5 f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ String f$2;

    public /* synthetic */ BubblesManager$5$$ExternalSyntheticLambda12(BubblesManager.AnonymousClass5 r1, boolean z, String str) {
        this.f$0 = r1;
        this.f$1 = z;
        this.f$2 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$requestNotificationShadeTopUi$4(this.f$1, this.f$2);
    }
}
