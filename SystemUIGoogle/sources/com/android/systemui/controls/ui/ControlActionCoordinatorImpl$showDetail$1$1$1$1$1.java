package com.android.systemui.controls.ui;

import android.content.DialogInterface;
/* compiled from: ControlActionCoordinatorImpl.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ControlActionCoordinatorImpl$showDetail$1$1$1$1$1 implements DialogInterface.OnDismissListener {
    final /* synthetic */ ControlActionCoordinatorImpl this$0;

    /* access modifiers changed from: package-private */
    public ControlActionCoordinatorImpl$showDetail$1$1$1$1$1(ControlActionCoordinatorImpl controlActionCoordinatorImpl) {
        this.this$0 = controlActionCoordinatorImpl;
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        this.this$0.dialog = null;
    }
}
