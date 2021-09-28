package com.google.android.systemui.dreamliner;

import android.util.Log;
import com.google.android.systemui.dreamliner.WirelessCharger;
/* loaded from: classes2.dex */
public class DockAlignmentController {
    private static final boolean DEBUG = Log.isLoggable("DockAlignmentController", 3);
    private int mAlignmentState = 0;
    private final DockObserver mDockObserver;
    private final WirelessCharger mWirelessCharger;

    public DockAlignmentController(WirelessCharger wirelessCharger, DockObserver dockObserver) {
        this.mWirelessCharger = wirelessCharger;
        this.mDockObserver = dockObserver;
    }

    /* access modifiers changed from: package-private */
    public void registerAlignInfoListener() {
        WirelessCharger wirelessCharger = this.mWirelessCharger;
        if (wirelessCharger == null) {
            Log.w("DockAlignmentController", "wirelessCharger is null");
        } else {
            wirelessCharger.registerAlignInfo(new RegisterAlignInfoListener());
        }
    }

    /* access modifiers changed from: private */
    public void onAlignInfoCallBack(DockAlignInfo dockAlignInfo) {
        int i = this.mAlignmentState;
        int alignmentState = getAlignmentState(dockAlignInfo);
        this.mAlignmentState = alignmentState;
        if (i != alignmentState) {
            this.mDockObserver.onAlignStateChanged(alignmentState);
            if (DEBUG) {
                Log.d("DockAlignmentController", "onAlignStateChanged, state: " + this.mAlignmentState);
            }
        }
        this.mDockObserver.onFanLevelChange();
    }

    private int getAlignmentState(DockAlignInfo dockAlignInfo) {
        if (DEBUG) {
            Log.d("DockAlignmentController", "onAlignInfo, state: " + dockAlignInfo.getAlignState() + ", alignPct: " + dockAlignInfo.getAlignPct());
        }
        int i = this.mAlignmentState;
        int alignState = dockAlignInfo.getAlignState();
        if (alignState == 0) {
            return i;
        }
        if (alignState == 1) {
            return 2;
        }
        if (alignState == 2) {
            int alignPct = dockAlignInfo.getAlignPct();
            if (alignPct >= 0) {
                if (alignPct < 100) {
                    return 1;
                }
                return 0;
            }
        } else if (alignState != 3) {
            Log.w("DockAlignmentController", "Unexpected state: " + dockAlignInfo.getAlignState());
        }
        return -1;
    }

    /* loaded from: classes2.dex */
    private final class RegisterAlignInfoListener implements WirelessCharger.AlignInfoListener {
        private RegisterAlignInfoListener() {
        }

        @Override // com.google.android.systemui.dreamliner.WirelessCharger.AlignInfoListener
        public void onAlignInfoChanged(DockAlignInfo dockAlignInfo) {
            DockAlignmentController.this.onAlignInfoCallBack(dockAlignInfo);
        }
    }
}
