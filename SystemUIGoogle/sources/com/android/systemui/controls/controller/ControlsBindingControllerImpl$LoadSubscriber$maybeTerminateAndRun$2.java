package com.android.systemui.controls.controller;

import com.android.systemui.controls.controller.ControlsBindingControllerImpl;
/* compiled from: ControlsBindingControllerImpl.kt */
/* loaded from: classes.dex */
final class ControlsBindingControllerImpl$LoadSubscriber$maybeTerminateAndRun$2 implements Runnable {
    final /* synthetic */ Runnable $postTerminateFn;
    final /* synthetic */ ControlsBindingControllerImpl.LoadSubscriber this$0;

    /* access modifiers changed from: package-private */
    public ControlsBindingControllerImpl$LoadSubscriber$maybeTerminateAndRun$2(ControlsBindingControllerImpl.LoadSubscriber loadSubscriber, Runnable runnable) {
        this.this$0 = loadSubscriber;
        this.$postTerminateFn = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ControlsBindingControllerImpl.LoadSubscriber.access$isTerminated$p(this.this$0).compareAndSet(false, true);
        this.$postTerminateFn.run();
    }
}
