package com.android.systemui.wallet.dagger;

import android.content.Context;
import android.service.quickaccesswallet.QuickAccessWalletClient;
/* loaded from: classes2.dex */
public abstract class WalletModule {
    public static QuickAccessWalletClient provideQuickAccessWalletClient(Context context) {
        return QuickAccessWalletClient.create(context);
    }
}
