package com.android.systemui.classifier;

import java.util.function.BinaryOperator;
/* loaded from: classes.dex */
public final /* synthetic */ class HistoryTracker$$ExternalSyntheticLambda1 implements BinaryOperator {
    public static final /* synthetic */ HistoryTracker$$ExternalSyntheticLambda1 INSTANCE = new HistoryTracker$$ExternalSyntheticLambda1();

    private /* synthetic */ HistoryTracker$$ExternalSyntheticLambda1() {
    }

    @Override // java.util.function.BiFunction
    public final Object apply(Object obj, Object obj2) {
        return Double.valueOf(Double.sum(((Double) obj).doubleValue(), ((Double) obj2).doubleValue()));
    }
}
