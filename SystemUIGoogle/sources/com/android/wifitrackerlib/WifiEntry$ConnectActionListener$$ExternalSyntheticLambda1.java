package com.android.wifitrackerlib;

import com.android.wifitrackerlib.WifiEntry;
/* loaded from: classes2.dex */
public final /* synthetic */ class WifiEntry$ConnectActionListener$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ WifiEntry.ConnectActionListener f$0;

    public /* synthetic */ WifiEntry$ConnectActionListener$$ExternalSyntheticLambda1(WifiEntry.ConnectActionListener connectActionListener) {
        this.f$0 = connectActionListener;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onFailure$1();
    }
}
