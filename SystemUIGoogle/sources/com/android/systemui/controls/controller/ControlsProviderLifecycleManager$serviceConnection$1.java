package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.service.controls.IControlsProvider;
import android.util.Log;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsProviderLifecycleManager.kt */
/* loaded from: classes.dex */
public final class ControlsProviderLifecycleManager$serviceConnection$1 implements ServiceConnection {
    final /* synthetic */ ControlsProviderLifecycleManager this$0;

    /* access modifiers changed from: package-private */
    public ControlsProviderLifecycleManager$serviceConnection$1(ControlsProviderLifecycleManager controlsProviderLifecycleManager) {
        this.this$0 = controlsProviderLifecycleManager;
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Intrinsics.checkNotNullParameter(componentName, "name");
        Intrinsics.checkNotNullParameter(iBinder, "service");
        Log.d(this.this$0.TAG, Intrinsics.stringPlus("onServiceConnected ", componentName));
        this.this$0.bindTryCount = 0;
        ControlsProviderLifecycleManager controlsProviderLifecycleManager = this.this$0;
        IControlsProvider asInterface = IControlsProvider.Stub.asInterface(iBinder);
        Intrinsics.checkNotNullExpressionValue(asInterface, "asInterface(service)");
        controlsProviderLifecycleManager.wrapper = new ServiceWrapper(asInterface);
        try {
            iBinder.linkToDeath(this.this$0, 0);
        } catch (RemoteException unused) {
        }
        this.this$0.handlePendingServiceMethods();
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName componentName) {
        Log.d(this.this$0.TAG, Intrinsics.stringPlus("onServiceDisconnected ", componentName));
        this.this$0.wrapper = null;
        this.this$0.bindService(false);
    }
}
