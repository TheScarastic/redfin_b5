package com.android.systemui.statusbar.phone;

import com.android.keyguard.CarrierTextController;
import com.android.systemui.util.ViewController;
/* loaded from: classes.dex */
public class KeyguardStatusBarViewController extends ViewController<KeyguardStatusBarView> {
    private final CarrierTextController mCarrierTextController;

    @Override // com.android.systemui.util.ViewController
    protected void onViewAttached() {
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewDetached() {
    }

    public KeyguardStatusBarViewController(KeyguardStatusBarView keyguardStatusBarView, CarrierTextController carrierTextController) {
        super(keyguardStatusBarView);
        this.mCarrierTextController = carrierTextController;
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.util.ViewController
    public void onInit() {
        super.onInit();
        this.mCarrierTextController.init();
    }
}
