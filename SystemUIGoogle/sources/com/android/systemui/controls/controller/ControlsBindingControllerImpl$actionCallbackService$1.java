package com.android.systemui.controls.controller;

import android.os.IBinder;
import android.service.controls.IControlsActionCallback;
import com.android.systemui.controls.controller.ControlsBindingControllerImpl;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsBindingControllerImpl.kt */
/* loaded from: classes.dex */
public final class ControlsBindingControllerImpl$actionCallbackService$1 extends IControlsActionCallback.Stub {
    final /* synthetic */ ControlsBindingControllerImpl this$0;

    /* access modifiers changed from: package-private */
    public ControlsBindingControllerImpl$actionCallbackService$1(ControlsBindingControllerImpl controlsBindingControllerImpl) {
        this.this$0 = controlsBindingControllerImpl;
    }

    public void accept(IBinder iBinder, String str, int i) {
        Intrinsics.checkNotNullParameter(iBinder, "token");
        Intrinsics.checkNotNullParameter(str, "controlId");
        ControlsBindingControllerImpl.access$getBackgroundExecutor$p(this.this$0).execute(new ControlsBindingControllerImpl.OnActionResponseRunnable(this.this$0, iBinder, str, i));
    }
}
