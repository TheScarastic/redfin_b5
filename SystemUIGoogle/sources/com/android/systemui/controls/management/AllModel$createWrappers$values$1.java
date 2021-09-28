package com.android.systemui.controls.management;

import com.android.systemui.controls.ControlStatus;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: AllModel.kt */
/* loaded from: classes.dex */
final class AllModel$createWrappers$values$1 extends Lambda implements Function1<ControlStatus, ControlStatusWrapper> {
    public static final AllModel$createWrappers$values$1 INSTANCE = new AllModel$createWrappers$values$1();

    AllModel$createWrappers$values$1() {
        super(1);
    }

    public final ControlStatusWrapper invoke(ControlStatus controlStatus) {
        Intrinsics.checkNotNullParameter(controlStatus, "it");
        return new ControlStatusWrapper(controlStatus);
    }
}
