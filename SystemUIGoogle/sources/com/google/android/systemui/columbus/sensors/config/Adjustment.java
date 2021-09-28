package com.google.android.systemui.columbus.sensors.config;

import android.content.Context;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Adjustment.kt */
/* loaded from: classes2.dex */
public abstract class Adjustment {
    private Function1<? super Adjustment, Unit> callback;
    private final Context context;

    public abstract float adjustSensitivity(float f);

    public Adjustment(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
    }

    public final void setCallback(Function1<? super Adjustment, Unit> function1) {
        this.callback = function1;
    }

    /* access modifiers changed from: protected */
    public final void onSensitivityChanged() {
        Function1<? super Adjustment, Unit> function1 = this.callback;
        if (function1 != null) {
            function1.invoke(this);
        }
    }
}
