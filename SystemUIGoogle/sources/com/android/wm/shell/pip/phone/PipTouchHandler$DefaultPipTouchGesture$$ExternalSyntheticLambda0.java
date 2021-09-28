package com.android.wm.shell.pip.phone;

import com.android.wm.shell.pip.phone.PipTouchHandler;
/* loaded from: classes2.dex */
public final /* synthetic */ class PipTouchHandler$DefaultPipTouchGesture$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ PipTouchHandler.DefaultPipTouchGesture f$0;

    public /* synthetic */ PipTouchHandler$DefaultPipTouchGesture$$ExternalSyntheticLambda0(PipTouchHandler.DefaultPipTouchGesture defaultPipTouchGesture) {
        this.f$0 = defaultPipTouchGesture;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.flingEndAction();
    }
}
