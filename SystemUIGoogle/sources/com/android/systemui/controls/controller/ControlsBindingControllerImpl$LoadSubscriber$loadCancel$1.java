package com.android.systemui.controls.controller;

import android.util.Log;
import com.android.systemui.controls.controller.ControlsBindingControllerImpl;
import kotlin.jvm.functions.Function0;
/* compiled from: ControlsBindingControllerImpl.kt */
/* loaded from: classes.dex */
final class ControlsBindingControllerImpl$LoadSubscriber$loadCancel$1 implements Runnable {
    final /* synthetic */ ControlsBindingControllerImpl.LoadSubscriber this$0;

    /* access modifiers changed from: package-private */
    public ControlsBindingControllerImpl$LoadSubscriber$loadCancel$1(ControlsBindingControllerImpl.LoadSubscriber loadSubscriber) {
        this.this$0 = loadSubscriber;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Function0 access$get_loadCancelInternal$p = ControlsBindingControllerImpl.LoadSubscriber.access$get_loadCancelInternal$p(this.this$0);
        if (access$get_loadCancelInternal$p != null) {
            Log.d("ControlsBindingControllerImpl", "Canceling loadSubscribtion");
            access$get_loadCancelInternal$p.invoke();
        }
    }
}
