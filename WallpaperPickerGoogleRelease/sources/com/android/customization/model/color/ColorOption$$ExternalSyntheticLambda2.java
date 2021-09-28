package com.android.customization.model.color;

import java.util.Map;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final /* synthetic */ class ColorOption$$ExternalSyntheticLambda2 implements Predicate {
    public static final /* synthetic */ ColorOption$$ExternalSyntheticLambda2 INSTANCE = new ColorOption$$ExternalSyntheticLambda2();

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        return ((Map.Entry) obj).getValue() != null;
    }
}
