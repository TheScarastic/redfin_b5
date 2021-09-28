package com.google.android.systemui.columbus;

import android.app.SynchronousUserSwitchObserver;
import android.os.RemoteException;
/* compiled from: ColumbusContentObserver.kt */
/* loaded from: classes2.dex */
public final class ColumbusContentObserver$userSwitchCallback$1 extends SynchronousUserSwitchObserver {
    final /* synthetic */ ColumbusContentObserver this$0;

    /* access modifiers changed from: package-private */
    public ColumbusContentObserver$userSwitchCallback$1(ColumbusContentObserver columbusContentObserver) {
        this.this$0 = columbusContentObserver;
    }

    public void onUserSwitching(int i) throws RemoteException {
        ColumbusContentObserver.access$updateContentObserver(this.this$0);
        ColumbusContentObserver.access$getCallback$p(this.this$0).invoke(ColumbusContentObserver.access$getSettingsUri$p(this.this$0));
    }
}
