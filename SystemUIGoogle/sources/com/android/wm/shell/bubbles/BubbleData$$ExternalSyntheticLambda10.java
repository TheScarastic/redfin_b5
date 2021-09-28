package com.android.wm.shell.bubbles;

import java.util.function.ToLongFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class BubbleData$$ExternalSyntheticLambda10 implements ToLongFunction {
    public static final /* synthetic */ BubbleData$$ExternalSyntheticLambda10 INSTANCE = new BubbleData$$ExternalSyntheticLambda10();

    private /* synthetic */ BubbleData$$ExternalSyntheticLambda10() {
    }

    @Override // java.util.function.ToLongFunction
    public final long applyAsLong(Object obj) {
        return ((Bubble) obj).getLastActivity();
    }
}
