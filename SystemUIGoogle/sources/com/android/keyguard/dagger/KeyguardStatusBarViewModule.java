package com.android.keyguard.dagger;

import com.android.keyguard.CarrierText;
import com.android.systemui.R$id;
import com.android.systemui.statusbar.phone.KeyguardStatusBarView;
/* loaded from: classes.dex */
public abstract class KeyguardStatusBarViewModule {
    /* access modifiers changed from: package-private */
    public static CarrierText getCarrierText(KeyguardStatusBarView keyguardStatusBarView) {
        return (CarrierText) keyguardStatusBarView.findViewById(R$id.keyguard_carrier_text);
    }
}
