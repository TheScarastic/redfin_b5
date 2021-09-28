package com.android.systemui.controls.controller;

import android.service.controls.Control;
import com.android.systemui.controls.controller.ControlsBindingController;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsBindingControllerImpl.kt */
/* loaded from: classes.dex */
public final class ControlsBindingControllerImpl$Companion$emptyCallback$1 implements ControlsBindingController.LoadCallback {
    public void accept(List<Control> list) {
        Intrinsics.checkNotNullParameter(list, "controls");
    }

    @Override // com.android.systemui.controls.controller.ControlsBindingController.LoadCallback
    public void error(String str) {
        Intrinsics.checkNotNullParameter(str, "message");
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
    @Override // java.util.function.Consumer
    public /* bridge */ /* synthetic */ void accept(List<? extends Control> list) {
        accept((List<Control>) list);
    }
}
