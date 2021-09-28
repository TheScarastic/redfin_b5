package com.android.systemui.controls.controller;

import android.os.IBinder;
import android.service.controls.Control;
import android.service.controls.IControlsSubscription;
import com.android.systemui.controls.controller.ControlsBindingControllerImpl;
import java.util.ArrayList;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsBindingControllerImpl.kt */
/* loaded from: classes.dex */
final class ControlsBindingControllerImpl$LoadSubscriber$onNext$1 implements Runnable {
    final /* synthetic */ Control $c;
    final /* synthetic */ IBinder $token;
    final /* synthetic */ ControlsBindingControllerImpl.LoadSubscriber this$0;
    final /* synthetic */ ControlsBindingControllerImpl this$1;

    /* access modifiers changed from: package-private */
    public ControlsBindingControllerImpl$LoadSubscriber$onNext$1(ControlsBindingControllerImpl.LoadSubscriber loadSubscriber, Control control, ControlsBindingControllerImpl controlsBindingControllerImpl, IBinder iBinder) {
        this.this$0 = loadSubscriber;
        this.$c = control;
        this.this$1 = controlsBindingControllerImpl;
        this.$token = iBinder;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (!ControlsBindingControllerImpl.LoadSubscriber.access$isTerminated$p(this.this$0).get()) {
            this.this$0.getLoadedControls().add(this.$c);
            if (((long) this.this$0.getLoadedControls().size()) >= this.this$0.getRequestLimit()) {
                ControlsBindingControllerImpl.LoadSubscriber loadSubscriber = this.this$0;
                ControlsBindingControllerImpl controlsBindingControllerImpl = this.this$1;
                IBinder iBinder = this.$token;
                ArrayList<Control> loadedControls = loadSubscriber.getLoadedControls();
                IControlsSubscription access$getSubscription$p = ControlsBindingControllerImpl.LoadSubscriber.access$getSubscription$p(this.this$0);
                if (access$getSubscription$p != null) {
                    ControlsBindingControllerImpl.LoadSubscriber.access$maybeTerminateAndRun(loadSubscriber, new ControlsBindingControllerImpl.OnCancelAndLoadRunnable(controlsBindingControllerImpl, iBinder, loadedControls, access$getSubscription$p, this.this$0.getCallback()));
                } else {
                    Intrinsics.throwUninitializedPropertyAccessException("subscription");
                    throw null;
                }
            }
        }
    }
}
