package com.android.systemui.controls.ui;

import android.content.DialogInterface;
/* compiled from: StatusBehavior.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class StatusBehavior$showNotFoundDialog$builder$1$2 implements DialogInterface.OnClickListener {
    public static final StatusBehavior$showNotFoundDialog$builder$1$2 INSTANCE = new StatusBehavior$showNotFoundDialog$builder$1$2();

    StatusBehavior$showNotFoundDialog$builder$1$2() {
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.cancel();
    }
}
