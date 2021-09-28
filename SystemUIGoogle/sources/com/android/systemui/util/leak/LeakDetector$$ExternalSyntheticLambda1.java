package com.android.systemui.util.leak;

import java.util.Collection;
import java.util.function.Predicate;
/* loaded from: classes2.dex */
public final /* synthetic */ class LeakDetector$$ExternalSyntheticLambda1 implements Predicate {
    public static final /* synthetic */ LeakDetector$$ExternalSyntheticLambda1 INSTANCE = new LeakDetector$$ExternalSyntheticLambda1();

    private /* synthetic */ LeakDetector$$ExternalSyntheticLambda1() {
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        return TrackedObjects.isTrackedObject((Collection) obj);
    }
}
