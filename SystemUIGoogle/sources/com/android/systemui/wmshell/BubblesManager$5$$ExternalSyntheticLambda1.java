package com.android.systemui.wmshell;

import com.android.systemui.model.SysUiState;
import com.android.systemui.wmshell.BubblesManager;
/* loaded from: classes2.dex */
public final /* synthetic */ class BubblesManager$5$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ BubblesManager.AnonymousClass5 f$0;
    public final /* synthetic */ SysUiState f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ BubblesManager$5$$ExternalSyntheticLambda1(BubblesManager.AnonymousClass5 r1, SysUiState sysUiState, boolean z) {
        this.f$0 = r1;
        this.f$1 = sysUiState;
        this.f$2 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onStackExpandChanged$11(this.f$1, this.f$2);
    }
}
