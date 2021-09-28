package com.google.android.systemui.assist;

import android.content.Context;
import android.os.UserManager;
import android.view.View;
import com.android.systemui.statusbar.phone.StatusBar;
import dagger.Lazy;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public class OpaEnabledDispatcher implements OpaEnabledListener {
    private final Lazy<StatusBar> mStatusBarLazy;

    public OpaEnabledDispatcher(Lazy<StatusBar> lazy) {
        this.mStatusBarLazy = lazy;
    }

    @Override // com.google.android.systemui.assist.OpaEnabledListener
    public void onOpaEnabledReceived(Context context, boolean z, boolean z2, boolean z3, boolean z4) {
        dispatchUnchecked((z4 && z && z2) || UserManager.isDeviceInDemoMode(context));
    }

    private void dispatchUnchecked(boolean z) {
        StatusBar statusBar = this.mStatusBarLazy.get();
        if (statusBar.getNavigationBarView() != null) {
            ArrayList<View> views = statusBar.getNavigationBarView().getHomeButton().getViews();
            for (int i = 0; i < views.size(); i++) {
                ((OpaLayout) views.get(i)).setOpaEnabled(z);
            }
        }
    }
}
