package com.android.systemui.biometrics;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.StatusBar;
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class UdfpsFpmOtherViewController extends UdfpsAnimationViewController<UdfpsFpmOtherView> {
    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController
    String getTag() {
        return "UdfpsFpmOtherViewController";
    }

    /* access modifiers changed from: protected */
    public UdfpsFpmOtherViewController(UdfpsFpmOtherView udfpsFpmOtherView, StatusBarStateController statusBarStateController, StatusBar statusBar, DumpManager dumpManager) {
        super(udfpsFpmOtherView, statusBarStateController, statusBar, dumpManager);
    }
}
