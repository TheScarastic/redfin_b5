package com.android.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.ExtensionControllerImpl;
import java.util.function.ToIntFunction;
/* loaded from: classes.dex */
public final /* synthetic */ class ExtensionControllerImpl$ExtensionBuilder$$ExternalSyntheticLambda0 implements ToIntFunction {
    public static final /* synthetic */ ExtensionControllerImpl$ExtensionBuilder$$ExternalSyntheticLambda0 INSTANCE = new ExtensionControllerImpl$ExtensionBuilder$$ExternalSyntheticLambda0();

    private /* synthetic */ ExtensionControllerImpl$ExtensionBuilder$$ExternalSyntheticLambda0() {
    }

    @Override // java.util.function.ToIntFunction
    public final int applyAsInt(Object obj) {
        return ((ExtensionControllerImpl.Item) obj).sortOrder();
    }
}
