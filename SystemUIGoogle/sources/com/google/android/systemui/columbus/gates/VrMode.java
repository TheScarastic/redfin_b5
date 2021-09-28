package com.google.android.systemui.columbus.gates;

import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.service.vr.IVrManager;
import android.util.Log;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: VrMode.kt */
/* loaded from: classes2.dex */
public final class VrMode extends Gate {
    public static final Companion Companion = new Companion(null);
    private boolean inVrMode;
    private final IVrManager vrManager = IVrManager.Stub.asInterface(ServiceManager.getService("vrmanager"));
    private final VrMode$vrStateCallbacks$1 vrStateCallbacks = new VrMode$vrStateCallbacks$1(this);

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public VrMode(Context context) {
        super(context, null, 2, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    protected void onActivate() {
        IVrManager iVrManager = this.vrManager;
        if (iVrManager != null) {
            try {
                boolean z = true;
                if (!iVrManager.getVrModeState()) {
                    z = false;
                }
                this.inVrMode = z;
                iVrManager.registerListener(this.vrStateCallbacks);
            } catch (RemoteException e) {
                Log.e("Columbus/VrMode", "Could not register IVrManager listener", e);
                this.inVrMode = false;
            }
        }
        updateBlocking();
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    protected void onDeactivate() {
        try {
            IVrManager iVrManager = this.vrManager;
            if (iVrManager != null) {
                iVrManager.unregisterListener(this.vrStateCallbacks);
            }
        } catch (RemoteException e) {
            Log.e("Columbus/VrMode", "Could not unregister IVrManager listener", e);
        }
        this.inVrMode = false;
    }

    /* access modifiers changed from: private */
    public final void updateBlocking() {
        setBlocking(this.inVrMode);
    }

    /* compiled from: VrMode.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
