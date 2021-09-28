package com.android.systemui.shared.system;

import android.window.IRemoteTransitionFinishedCallback;
import com.android.systemui.shared.system.RemoteTransitionCompat;
/* loaded from: classes.dex */
public final /* synthetic */ class RemoteTransitionCompat$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ IRemoteTransitionFinishedCallback f$0;

    public /* synthetic */ RemoteTransitionCompat$1$$ExternalSyntheticLambda0(IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback, int i) {
        this.$r8$classId = i;
        this.f$0 = iRemoteTransitionFinishedCallback;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                RemoteTransitionCompat.AnonymousClass1.m6$r8$lambda$jwJx2V1LAnejviZsZXR1iMLhUc(this.f$0);
                return;
            default:
                RemoteTransitionCompat.AnonymousClass1.m5$r8$lambda$4bGfLVj4G1PZG6H5jtGCYBezCc(this.f$0);
                return;
        }
    }
}
