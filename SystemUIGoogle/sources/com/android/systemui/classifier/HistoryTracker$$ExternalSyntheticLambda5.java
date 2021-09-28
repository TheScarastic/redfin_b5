package com.android.systemui.classifier;

import com.android.systemui.classifier.HistoryTracker;
import java.util.function.Function;
/* loaded from: classes.dex */
public final /* synthetic */ class HistoryTracker$$ExternalSyntheticLambda5 implements Function {
    public static final /* synthetic */ HistoryTracker$$ExternalSyntheticLambda5 INSTANCE = new HistoryTracker$$ExternalSyntheticLambda5();

    private /* synthetic */ HistoryTracker$$ExternalSyntheticLambda5() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return Double.valueOf(((HistoryTracker.CombinedResult) obj).getScore());
    }
}
