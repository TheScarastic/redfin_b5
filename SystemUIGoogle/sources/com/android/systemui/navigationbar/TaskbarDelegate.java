package com.android.systemui.navigationbar;

import android.os.IBinder;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.statusbar.CommandQueue;
/* loaded from: classes.dex */
public class TaskbarDelegate implements CommandQueue.Callbacks {
    private final OverviewProxyService mOverviewProxyService;

    public TaskbarDelegate(OverviewProxyService overviewProxyService) {
        this.mOverviewProxyService = overviewProxyService;
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void setImeWindowStatus(int i, IBinder iBinder, int i2, int i3, boolean z) {
        this.mOverviewProxyService.notifyImeWindowStatus(i, iBinder, i2, i3, z);
    }
}
