package com.android.systemui.wallet.controller;

import android.service.quickaccesswallet.QuickAccessWalletClient;
import com.android.systemui.wallet.controller.QuickAccessWalletController;
/* loaded from: classes2.dex */
public final /* synthetic */ class QuickAccessWalletController$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ QuickAccessWalletController.AnonymousClass1 f$0;
    public final /* synthetic */ QuickAccessWalletClient.OnWalletCardsRetrievedCallback f$1;

    public /* synthetic */ QuickAccessWalletController$1$$ExternalSyntheticLambda0(QuickAccessWalletController.AnonymousClass1 r1, QuickAccessWalletClient.OnWalletCardsRetrievedCallback onWalletCardsRetrievedCallback) {
        this.f$0 = r1;
        this.f$1 = onWalletCardsRetrievedCallback;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onChange$0(this.f$1);
    }
}
