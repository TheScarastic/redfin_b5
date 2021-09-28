package com.android.systemui.wallet.ui;

import android.app.PendingIntent;
import android.graphics.drawable.Drawable;
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public interface WalletCardViewInfo {
    Drawable getCardDrawable();

    String getCardId();

    CharSequence getContentDescription();

    Drawable getIcon();

    CharSequence getLabel();

    PendingIntent getPendingIntent();

    default boolean isUiEquivalent(WalletCardViewInfo walletCardViewInfo) {
        if (walletCardViewInfo == null) {
            return false;
        }
        return getCardId().equals(walletCardViewInfo.getCardId());
    }
}
