package com.google.android.systemui.smartspace;

import android.app.smartspace.SmartspaceTarget;
import java.util.function.Function;
/* loaded from: classes2.dex */
public final /* synthetic */ class BcSmartspaceView$$ExternalSyntheticLambda0 implements Function {
    public static final /* synthetic */ BcSmartspaceView$$ExternalSyntheticLambda0 INSTANCE = new BcSmartspaceView$$ExternalSyntheticLambda0();

    private /* synthetic */ BcSmartspaceView$$ExternalSyntheticLambda0() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return ((SmartspaceTarget) obj).getSmartspaceTargetId();
    }
}
