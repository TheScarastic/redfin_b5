package com.android.wm.shell.common;

import android.view.SurfaceControl;
import com.android.wm.shell.common.SyncTransactionQueue;
/* loaded from: classes2.dex */
public final /* synthetic */ class SyncTransactionQueue$SyncCallback$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ SyncTransactionQueue.SyncCallback f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ SurfaceControl.Transaction f$2;

    public /* synthetic */ SyncTransactionQueue$SyncCallback$$ExternalSyntheticLambda0(SyncTransactionQueue.SyncCallback syncCallback, int i, SurfaceControl.Transaction transaction) {
        this.f$0 = syncCallback;
        this.f$1 = i;
        this.f$2 = transaction;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onTransactionReady$0(this.f$1, this.f$2);
    }
}
