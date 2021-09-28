package com.android.systemui.biometrics;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.StatusBar;
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class UdfpsBpViewController extends UdfpsAnimationViewController<UdfpsBpView> {
    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController
    String getTag() {
        return "UdfpsBpViewController";
    }

    /* access modifiers changed from: protected */
    public UdfpsBpViewController(UdfpsBpView udfpsBpView, StatusBarStateController statusBarStateController, StatusBar statusBar, DumpManager dumpManager) {
        super(udfpsBpView, statusBarStateController, statusBar, dumpManager);
    }
}
