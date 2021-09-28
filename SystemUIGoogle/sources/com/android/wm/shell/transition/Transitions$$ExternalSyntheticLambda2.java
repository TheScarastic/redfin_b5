package com.android.wm.shell.transition;

import com.android.wm.shell.transition.Transitions;
import java.util.function.Function;
/* loaded from: classes2.dex */
public final /* synthetic */ class Transitions$$ExternalSyntheticLambda2 implements Function {
    public static final /* synthetic */ Transitions$$ExternalSyntheticLambda2 INSTANCE = new Transitions$$ExternalSyntheticLambda2();

    private /* synthetic */ Transitions$$ExternalSyntheticLambda2() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return ((Transitions.ActiveTransition) obj).mToken;
    }
}
