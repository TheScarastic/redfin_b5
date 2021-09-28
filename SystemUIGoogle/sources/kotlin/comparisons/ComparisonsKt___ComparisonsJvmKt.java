package kotlin.comparisons;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: _ComparisonsJvm.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class ComparisonsKt___ComparisonsJvmKt extends ComparisonsKt__ComparisonsKt {
    public static float maxOf(float f, float... fArr) {
        Intrinsics.checkNotNullParameter(fArr, "other");
        for (float f2 : fArr) {
            f = Math.max(f, f2);
        }
        return f;
    }
}
