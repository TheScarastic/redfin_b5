package com.android.systemui.classifier;

import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class BrightLineFalsingManager$1$$ExternalSyntheticLambda1 implements Consumer {
    public static final /* synthetic */ BrightLineFalsingManager$1$$ExternalSyntheticLambda1 INSTANCE = new BrightLineFalsingManager$1$$ExternalSyntheticLambda1();

    private /* synthetic */ BrightLineFalsingManager$1$$ExternalSyntheticLambda1() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((FalsingClassifier) obj).onSessionStarted();
    }
}
