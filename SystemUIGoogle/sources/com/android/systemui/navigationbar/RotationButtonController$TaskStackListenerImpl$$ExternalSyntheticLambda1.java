package com.android.systemui.navigationbar;

import com.android.systemui.shared.system.ActivityManagerWrapper;
import java.util.function.Function;
/* loaded from: classes.dex */
public final /* synthetic */ class RotationButtonController$TaskStackListenerImpl$$ExternalSyntheticLambda1 implements Function {
    public static final /* synthetic */ RotationButtonController$TaskStackListenerImpl$$ExternalSyntheticLambda1 INSTANCE = new RotationButtonController$TaskStackListenerImpl$$ExternalSyntheticLambda1();

    private /* synthetic */ RotationButtonController$TaskStackListenerImpl$$ExternalSyntheticLambda1() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return ((ActivityManagerWrapper) obj).getRunningTask();
    }
}
