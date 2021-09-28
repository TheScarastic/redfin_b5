package com.android.systemui.wallet.ui;

import android.view.View;
import com.android.systemui.wallet.ui.WalletCardCarousel;
/* loaded from: classes2.dex */
public final /* synthetic */ class WalletCardCarousel$WalletCardCarouselAdapter$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ WalletCardCarousel.WalletCardCarouselAdapter f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ WalletCardViewInfo f$2;

    public /* synthetic */ WalletCardCarousel$WalletCardCarouselAdapter$$ExternalSyntheticLambda0(WalletCardCarousel.WalletCardCarouselAdapter walletCardCarouselAdapter, int i, WalletCardViewInfo walletCardViewInfo) {
        this.f$0 = walletCardCarouselAdapter;
        this.f$1 = i;
        this.f$2 = walletCardViewInfo;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.lambda$onBindViewHolder$0(this.f$1, this.f$2, view);
    }
}
