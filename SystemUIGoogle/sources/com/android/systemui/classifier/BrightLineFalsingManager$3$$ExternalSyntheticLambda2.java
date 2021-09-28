package com.android.systemui.classifier;

import com.android.systemui.classifier.FalsingClassifier;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final /* synthetic */ class BrightLineFalsingManager$3$$ExternalSyntheticLambda2 implements Predicate {
    public static final /* synthetic */ BrightLineFalsingManager$3$$ExternalSyntheticLambda2 INSTANCE = new BrightLineFalsingManager$3$$ExternalSyntheticLambda2();

    private /* synthetic */ BrightLineFalsingManager$3$$ExternalSyntheticLambda2() {
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        return ((FalsingClassifier.Result) obj).isFalse();
    }
}
