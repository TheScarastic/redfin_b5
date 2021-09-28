package com.android.systemui.wmshell;

import android.util.ArraySet;
import com.android.systemui.wmshell.BubblesManager;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class BubblesManager$5$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ BubblesManager.AnonymousClass5 f$0;
    public final /* synthetic */ ArraySet f$1;
    public final /* synthetic */ Consumer f$2;

    public /* synthetic */ BubblesManager$5$$ExternalSyntheticLambda0(BubblesManager.AnonymousClass5 r1, ArraySet arraySet, Consumer consumer) {
        this.f$0 = r1;
        this.f$1 = arraySet;
        this.f$2 = consumer;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$getShouldRestoredEntries$2(this.f$1, this.f$2);
    }
}
