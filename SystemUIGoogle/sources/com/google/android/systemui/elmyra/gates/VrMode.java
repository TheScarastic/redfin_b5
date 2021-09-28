package com.google.android.systemui.elmyra.gates;

import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.service.vr.IVrManager;
import android.service.vr.IVrStateCallbacks;
import android.util.Log;
/* loaded from: classes2.dex */
public class VrMode extends Gate {
    private boolean mInVrMode;
    private final IVrStateCallbacks mVrStateCallbacks = new IVrStateCallbacks.Stub() { // from class: com.google.android.systemui.elmyra.gates.VrMode.1
        public void onVrStateChanged(boolean z) {
            if (z != VrMode.this.mInVrMode) {
                VrMode.this.mInVrMode = z;
                VrMode.this.notifyListener();
            }
        }
    };
    private final IVrManager mVrManager = IVrManager.Stub.asInterface(ServiceManager.getService("vrmanager"));

    public VrMode(Context context) {
        super(context);
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    protected void onActivate() {
        IVrManager iVrManager = this.mVrManager;
        if (iVrManager != null) {
            try {
                this.mInVrMode = iVrManager.getVrModeState();
                this.mVrManager.registerListener(this.mVrStateCallbacks);
            } catch (RemoteException e) {
                Log.e("Elmyra/VrMode", "Could not register IVrManager listener", e);
                this.mInVrMode = false;
            }
        }
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    protected void onDeactivate() {
        IVrManager iVrManager = this.mVrManager;
        if (iVrManager != null) {
            try {
                iVrManager.unregisterListener(this.mVrStateCallbacks);
            } catch (RemoteException e) {
                Log.e("Elmyra/VrMode", "Could not unregister IVrManager listener", e);
                this.mInVrMode = false;
            }
        }
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    protected boolean isBlocked() {
        return this.mInVrMode;
    }
}
