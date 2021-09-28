package com.android.systemui.classifier;

import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class BrightLineFalsingManager$1$$ExternalSyntheticLambda0 implements Consumer {
    public static final /* synthetic */ BrightLineFalsingManager$1$$ExternalSyntheticLambda0 INSTANCE = new BrightLineFalsingManager$1$$ExternalSyntheticLambda0();

    private /* synthetic */ BrightLineFalsingManager$1$$ExternalSyntheticLambda0() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((FalsingClassifier) obj).onSessionEnded();
    }
}
