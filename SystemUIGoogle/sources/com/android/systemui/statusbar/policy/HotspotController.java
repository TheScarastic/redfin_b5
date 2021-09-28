package com.android.systemui.statusbar.policy;

import com.android.systemui.Dumpable;
/* loaded from: classes.dex */
public interface HotspotController extends CallbackController<Callback>, Dumpable {

    /* loaded from: classes.dex */
    public interface Callback {
        default void onHotspotAvailabilityChanged(boolean z) {
        }

        void onHotspotChanged(boolean z, int i);
    }

    int getNumConnectedDevices();

    boolean isHotspotEnabled();

    boolean isHotspotSupported();

    boolean isHotspotTransient();

    void setHotspotEnabled(boolean z);
}
