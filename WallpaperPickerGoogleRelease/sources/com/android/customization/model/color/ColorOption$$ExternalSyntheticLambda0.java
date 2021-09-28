package com.android.customization.model.color;

import java.util.Map;
import java.util.function.Function;
/* loaded from: classes.dex */
public final /* synthetic */ class ColorOption$$ExternalSyntheticLambda0 implements Function {
    public static final /* synthetic */ ColorOption$$ExternalSyntheticLambda0 INSTANCE = new ColorOption$$ExternalSyntheticLambda0();

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return (String) ((Map.Entry) obj).getKey();
    }
}
